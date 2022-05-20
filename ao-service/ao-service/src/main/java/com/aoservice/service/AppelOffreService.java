package com.aoservice.service;

import com.aoservice.beans.AppelOffreBean;
import com.aoservice.configurationMapper.AppelOffreMapper;
import com.aoservice.configurationMapper.PrestataireMapper;
import com.aoservice.dto.AppelOffreDto;
import com.aoservice.dto.ContratDto;
import com.aoservice.entities.*;
import com.aoservice.repositories.*;
import com.aoservice.response.ContratResponse;
import net.sf.jasperreports.engine.JasperPrint;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppelOffreService {
    @Autowired
    AppellOffreRepository appellOffreRepository;
    @Autowired
    private CandidatureFinishedRepository candidatureFinishedRepository;
    @Autowired
    private AppelOffreBean appelOffreBean;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private EsnRepository esnRepository;
    @Autowired
    private PrestataireRepository prestataireRepository;
    private PrestataireMapper mapperPrestataire = Mappers.getMapper(PrestataireMapper.class);
    private final AppelOffreMapper mapper = Mappers.getMapper(AppelOffreMapper.class);

    public ResponseEntity<List<AppelOffreDto>> getAoByUsernameEsn(String username) {
        List<Optional<AppelOffre>> appelOffres=appellOffreRepository.getAoByUsernameEsn(username).stream().map(Optional::ofNullable).collect(Collectors.toList());
        if(appelOffres.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(appelOffres.stream()
                    .map(appelOffre -> {
                        if(appelOffre.isPresent()) {
                            var a = mapper.appelOffreToAppelOffreDTO(appelOffre.get());
                            a.setEsnImage(appelOffre.get().getEsn().getLocationImage());
                            a.setEsnNom(appelOffre.get().getEsn().getEsnnom());
                            return a;
                        }else return null;
                    })
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    public ResponseEntity<String> generateContrat(ContratDto contrat, String givenName) {
        try {
            Optional<CandidatureFinished> candidatureFinished = Optional.ofNullable(candidatureFinishedRepository.findByIdTask(contrat.getIdCandidature()));
            Optional<AppelOffre> appelOffre = Optional.ofNullable(appellOffreRepository.findByRefAo(contrat.getRefAo()));
            JasperPrint jasper = appelOffreBean.generateContract(contrat, appelOffre);
            String contratUrl = appelOffreBean.uploadFileToCloudinary(jasper,givenName, contrat);
            appelOffreBean.sendMailToInternautes(contratUrl, candidatureFinished, appelOffre);
            Optional<Prestataire> optionalPrestataire=Optional.ofNullable(new Prestataire());
            if(candidatureFinished.isPresent()){
                optionalPrestataire = Optional.ofNullable(prestataireRepository.findByPrestataireUsername(candidatureFinished.get().getUsername()));
                candidatureFinished.get().setHasContract(true);
                candidatureFinishedRepository.save(candidatureFinished.get());
            }
            if(appelOffre.isPresent() && optionalPrestataire.isPresent()) {
                appelOffreBean.createMission(contratUrl, appelOffre,optionalPrestataire, Optional.empty());
            }else if(appelOffre.isPresent() && candidatureFinished.isPresent()){
                Optional<Esn> esnOptional=Optional.ofNullable(esnRepository.findByEsnUsernameRepresentant(candidatureFinished.get().getUsername()));
                appelOffreBean.createMission(contratUrl, appelOffre, Optional.empty(),esnOptional);
            }
            return new ResponseEntity<>("Done", HttpStatus.OK);
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            return new ResponseEntity<>("Error", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<AppelOffreDto>> getAllAo() {
        List<AppelOffre> allAppelOffre = appellOffreRepository.findAll();
        List<Optional<AppelOffre>> allAo = allAppelOffre.stream().map(Optional::ofNullable).collect(Collectors.toList());
        if (allAo.isEmpty()) {
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(allAppelOffre.stream()
                    .map(ao -> {
                        var appelOffreDto = mapper.appelOffreToAppelOffreDTO(ao);
                        appelOffreDto.setEsnImage(ao.getEsn().getLocationImage());
                        appelOffreDto.setEsnNom(ao.getEsn().getEsnnom());
                        appelOffreDto.setEsnUsernameRepresentant(ao.getEsn().getEsnUsernameRepresentant());
                        Set<String> prestataireUsernames = new TreeSet<>();
                        Set<String> esnUsernames = new TreeSet<>();
                        //ao.getPrestataires().stream().map(prestataire -> prestataireUsernames.add(prestataire.getPrestataireUsername()));
                        for (Prestataire prestataire : ao.getPrestataires()) {
                            prestataireUsernames.add(prestataire.getPrestataireUsername());
                        }
                        for (Esn esn : ao.getEsns()) {
                            esnUsernames.add(esn.getEsnUsernameRepresentant());
                        }
                        appelOffreDto.setUsernamePrestataires(prestataireUsernames);
                        appelOffreDto.setUsernameEsns(esnUsernames);
                        return appelOffreDto;
                    })
                    .collect(Collectors.toList()),HttpStatus.OK);


    }

    public ResponseEntity<AppelOffreDto> createAo(AppelOffreDto appelOffreDto, String username) {
        var esn = esnRepository.findByEsnUsernameRepresentant(username);
        Optional<AppelOffre> appelOffre = Optional.ofNullable(mapper.appelOffreDTOtoAppelOffre(appelOffreDto));
        if (appelOffre.isPresent()) {
            appelOffre.get().setEsn(esn);
            var appelOffreSaved = appellOffreRepository.save(appelOffre.get());
            appelOffreSaved.setRefAo("AO_" + appelOffreSaved.getId());
            appellOffreRepository.save(appelOffreSaved);
            return new ResponseEntity<>(mapper.appelOffreToAppelOffreDTO(appelOffreSaved),HttpStatus.CREATED) ;
        } else
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<AppelOffreDto> editAo(AppelOffreDto appelOffreDto, String username) {
        var esn = esnRepository.findByEsnUsernameRepresentant(username);
        Optional<AppelOffre> appelOffre = Optional.ofNullable(mapper.appelOffreDTOtoAppelOffre(appelOffreDto));
        if (appelOffre.isPresent()) {
            appelOffre.get().setEsn(esn);
            var appelOffreSaved = appellOffreRepository.save(appelOffre.get());
            return new ResponseEntity<>(mapper.appelOffreToAppelOffreDTO(appelOffreSaved),HttpStatus.CREATED) ;
        } else
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> deleteAo(Long id){

        if(id!=null){
            appellOffreRepository.deleteById(id);
            return new ResponseEntity<>("deleted",HttpStatus.OK);
        }else
            return new ResponseEntity<>("not deleted",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<AppelOffreDto> getAoById(Long id){
        Optional<AppelOffre> appelOffre = Optional.ofNullable(appellOffreRepository.getAoById(id));
        if(appelOffre.isPresent()){
            return new ResponseEntity<>(mapper.appelOffreToAppelOffreDTO(appelOffre.get()),HttpStatus.OK);
        }else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<List<ContratResponse>> getAllContratUrlByUsernameForPrestataire(String username){
        List<ContratResponse> contratResponses=new ArrayList<>();
        Optional<Prestataire> prestataire=Optional.ofNullable(prestataireRepository.findByPrestataireUsername(username));
        if (prestataire.isPresent()){
            Set<AppelOffre> appelOffres = prestataire.get().getAppelOffres();
            List<Mission> missions=new ArrayList<>();
            for (AppelOffre appelOffre:appelOffres) {
                for (Mission mission:appelOffre.getMissions()) {
                    if(mission.getUsernamePrestataire().equals(prestataire.get().getPrestataireUsername())){
                        missions.add(mission);
                    }
                }
               // missions.addAll(appelOffre.getMissions());
            }
            List<UrlContract> urlContracts=new ArrayList<>();
            for (Mission mission:missions
                 ) {
                urlContracts.addAll(mission.getUrlsContrat());
            }

            for (UrlContract urlContrat:urlContracts
                 ) {
//                String url=urlContrat.getUrlContrat();
//                System.out.println(urlContrat.getUrlContrat());
                contratResponses.add(new ContratResponse(urlContrat.getUrlContrat(),urlContrat.getMission().getAppelOffre().getTitreAo(),urlContrat.getMission().getAppelOffre().getEsn().getEsnnom()));
            }
//            prestataire.get().getAppelOffres().stream()
//                    .map(appelOffre -> appelOffre.getMissions()
//                            .stream().map(mission -> mission.getUrlsContrat()
//                                    .stream().map(urlContract ->{
//                                        String url=urlContract.getUrlContrat();
//                                        urlsContrat.add(urlContract.getUrlContrat());
//                                        System.out.println(url);
//                                    return urlContract.getUrlContrat();
//
//                                    })));
           return new ResponseEntity<>(contratResponses,HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<HashMap<String, List<ContratResponse>>> getAllContratUrlByUsernameForEsn(String username){
        //List<String> urlsContrat=new ArrayList<>();
        HashMap<String, List<ContratResponse>> responseContrats = new HashMap<>();
        Optional<Esn> esn=Optional.ofNullable(esnRepository.findByEsnUsernameRepresentant(username));
        if (esn.isPresent()){
            Set<AppelOffre> appelOffres = esn.get().getAppelOffresPostulated();

            List<Mission> missions=new ArrayList<>();
            for (AppelOffre appelOffre:appelOffres) {
                for (Mission mission:appelOffre.getMissions()) {
                    if(mission.getUsernamePrestataire().equals(esn.get().getEsnUsernameRepresentant())){
                        missions.add(mission);
                    }
                }
                // missions.addAll(appelOffre.getMissions());
            }
            List<UrlContract> urlContracts=new ArrayList<>();
            for (Mission mission:missions
            ) {
                urlContracts.addAll(mission.getUrlsContrat());
            }

            responseContrats.put("prestataire",urlContracts.stream().map(urlContrat -> new ContratResponse(urlContrat.getUrlContrat(),urlContrat.getMission().getAppelOffre().getTitreAo(),urlContrat.getMission().getAppelOffre().getEsn().getEsnnom())).collect(Collectors.toList()));
//            for (UrlContract urlContrat:urlContracts
//            ) {
////                String url=urlContrat.getUrlContrat();
////                System.out.println(urlContrat.getUrlContrat());
//                urlsContrat.put("prestataire",urlContrat.getUrlContrat());
//            }

            //get url contrat for esn appels offre
            List<UrlContract> urlContractsProprietaire=new ArrayList<>();
            Set<AppelOffre> esnAppelOffres = esn.get().getAppelOffres();
            List<Mission> missionsProprietaire=new ArrayList<>();
            for (AppelOffre appelOffre:esnAppelOffres) {
                missionsProprietaire.addAll(appelOffre.getMissions());
//                for (Mission mission:appelOffre.getMissions()) {
//                    missionsProprietaire.add(mission);
//                }
                // missions.addAll(appelOffre.getMissions());
            }
            // // // // HashMap<String,String> urlswithNomInternaute
            for (Mission mission:missionsProprietaire
            ) {
                urlContractsProprietaire.addAll(mission.getUrlsContrat());
            }
            responseContrats.put("esn",urlContractsProprietaire.stream()
                    .map(urlContract -> {
                        Optional<Prestataire> prestataire=Optional.ofNullable(prestataireRepository.findByPrestataireUsername(urlContract.getMission().getUsernamePrestataire()));
                        if(prestataire.isPresent())
                            return new ContratResponse(urlContract.getUrlContrat(),urlContract.getMission().getAppelOffre().getTitreAo(),prestataire.get().getPrestataireNom()+" "+prestataire.get().getPrestatairePrenom());
                        else {
                            Optional<Esn> esnOptional = Optional.ofNullable(esnRepository.findByEsnUsernameRepresentant(urlContract.getMission().getUsernamePrestataire()));
                            return new ContratResponse(urlContract.getUrlContrat(),urlContract.getMission().getAppelOffre().getTitreAo(),esnOptional.get().getEsnnom());
                        }
                    }).collect(Collectors.toList()));
            return new ResponseEntity<>(responseContrats,HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

//    public ResponseEntity<AppelOffreDto> editAo(AppelOffreDto appelOffreDto){
//        Optional<AppelOffre> appelOffre = Optional.ofNullable(mapper.appelOffreDTOtoAppelOffre(appelOffreDto));
//        if(appelOffre.isPresent()){
//            AppelOffre appelOffreEdited= appellOffreRepository.save(appelOffre.get());
//            return new ResponseEntity<>(mapper.appelOffreToAppelOffreDTO(appelOffreEdited),HttpStatus.OK);
//        }else
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//    }
}
