package com.aoservice.service;

import com.aoservice.beans.AppelOffreBean;
import com.aoservice.configurationMapper.AppelOffreMapper;
import com.aoservice.dto.AppelOffreDto;
import com.aoservice.dto.ContratDto;
import com.aoservice.entities.*;
import com.aoservice.repositories.AppellOffreRepository;
import com.aoservice.repositories.CandidatureFinishedRepository;
import com.aoservice.repositories.EsnRepository;
import com.aoservice.repositories.MissionRepository;
import net.sf.jasperreports.engine.JasperPrint;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
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
    private final AppelOffreMapper mapper = Mappers.getMapper(AppelOffreMapper.class);

    public ResponseEntity<List<AppelOffreDto>> getAoByUsernameEsn(String username) {
        List<Optional<AppelOffre>> appelOffres=appellOffreRepository.getAoByUsernameEsn(username).stream().map(Optional::ofNullable).collect(Collectors.toList());
        if(appelOffres.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(appelOffres.stream().map(appelOffre -> mapper.appelOffreToAppelOffreDTO(appelOffre.get())).collect(Collectors.toList()), HttpStatus.OK);

    }

    public ResponseEntity<String> generateContrat(ContratDto contrat, String givenName) {
        try {
            Optional<CandidatureFinished> candidatureFinished = Optional.ofNullable(candidatureFinishedRepository.findByIdTask(contrat.getIdCandidature()));
            Optional<AppelOffre> appelOffre = Optional.ofNullable(appellOffreRepository.findByRefAo(contrat.getRefAo()));
            JasperPrint jasper = appelOffreBean.generateContract(contrat, appelOffre);
            appelOffreBean.storeContratSousFs(givenName, jasper, candidatureFinished, contrat);
            String contratUrl = appelOffreBean.uploadFileToCloudinary(givenName, contrat);
            appelOffreBean.sendMailToInternautes(contratUrl, candidatureFinished, appelOffre);

            Optional<Mission> mission = Optional.ofNullable(missionRepository.getMissionByIdAppelOffre(appelOffre.get().getId()));
            appelOffreBean.createOrUpdateMission(mission, contratUrl, appelOffre);
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
                        AppelOffreDto appelOffreDto = mapper.appelOffreToAppelOffreDTO(ao);
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
        Esn esn = esnRepository.findByEsnUsernameRepresentant(username);
        Optional<AppelOffre> appelOffre = Optional.ofNullable(mapper.appelOffreDTOtoAppelOffre(appelOffreDto));
        if (appelOffre.isPresent()) {
            appelOffre.get().setEsn(esn);
            AppelOffre appelOffreSaved = appellOffreRepository.save(appelOffre.get());
            appelOffreSaved.setRefAo("AO_" + appelOffreSaved.getId());
            appellOffreRepository.save(appelOffreSaved);
            return new ResponseEntity<>(mapper.appelOffreToAppelOffreDTO(appelOffreSaved),HttpStatus.CREATED) ;
        } else
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }
}
