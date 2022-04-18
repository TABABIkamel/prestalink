package com.aoservice.controllers;

import com.aoservice.beans.AppelOffreBean;
import com.aoservice.dto.ContratDto;
import com.aoservice.entities.*;
import com.aoservice.repositories.*;
import com.aoservice.service.AppelOffreService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.aoservice.configurationMapper.AppelOffreMapper;
import com.aoservice.dto.AppelOffreDto;
import com.aoservice.exceptions.coreExceptionClasses.ErrorMessages;
import com.aoservice.exceptions.exceptionClasses.AppelOffreNotFoundException;
import lombok.Data;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestControllerAdvice
@RequestMapping("/api/ao")
public class AppelOffreController {
    @Autowired
    private AppellOffreRepository appellOffreRepository;
    @Autowired
    private EsnRepository esnRepository;
    @Autowired
    private CandidatureFinishedRepository candidatureFinishedRepository;
    @Autowired
    PrestataireRepository prestataireRepository;
    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private UrlContractRepository urlContractRepository;
    @Autowired
    private AppelOffreService appelOffreService;
    @Autowired
    private AppelOffreBean appelOffreBean;
    private final AppelOffreMapper mapper = Mappers.getMapper(AppelOffreMapper.class);

    @PostMapping(value = "/generateContrat")
    @ResponseBody
    public ResponseEntity<Object> generateContrat(@RequestBody ContratDto contrat, HttpServletRequest request) throws IOException, JRException {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext = principal.getKeycloakSecurityContext();
        try {
        Optional<CandidatureFinished> candidatureFinished = Optional.ofNullable(candidatureFinishedRepository.findByIdTask(contrat.getIdCandidature()));
        Optional<AppelOffre> appelOffre = Optional.ofNullable(appellOffreRepository.findByRefAo(contrat.getRefAo()));
        JasperPrint jasper=appelOffreBean.generateContract(contrat,appelOffre);
        appelOffreBean.storeContratSousFs(keycloakSecurityContext,jasper,candidatureFinished,contrat);
            String contratUrl=appelOffreBean.uploadFileToCloudinary(keycloakSecurityContext,contrat);
            appelOffreBean.sendMailToInternautes(contratUrl,candidatureFinished,appelOffre);

            Optional<Mission> mission = Optional.ofNullable(missionRepository.getMissionByIdAppelOffre(appelOffre.get().getId()));
            appelOffreBean.createOrUpdateMission(mission,contratUrl,appelOffre);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            return new ResponseEntity<>(ex.getCause(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/allAo")
    public ResponseEntity<List<AppelOffreDto>> getAllAo() {
        List<AppelOffre> allAppelOffre = appellOffreRepository.findAll();
        List<Optional<AppelOffre>> allAo = allAppelOffre.stream().map(o -> Optional.of(o)).collect(Collectors.toList());
        if (allAo.isEmpty()) {
            throw new AppelOffreNotFoundException(ErrorMessages.NO_APPELOFFRE_FOUND.getErrorMessage());
        }
        List<AppelOffreDto> appelOffresDto = allAppelOffre.stream()
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
                .collect(Collectors.toList());
        return new ResponseEntity<>(appelOffresDto, HttpStatus.OK);
    }

    @GetMapping(value = "/getProfileAo")
    public ProfileLikedin[] getProfile() {
        ProfileLikedin[] pageProfiles = keycloakRestTemplate.getForObject("http://127.0.0.1:8085/api/profilelinkedin/allProfiles", ProfileLikedin[].class);
        return pageProfiles;
    }

    @ResponseBody
    @PostMapping(value = "/createAo")
    public ResponseEntity<AppelOffreDto> createAo(@RequestBody AppelOffreDto appelOffreDto, HttpServletRequest request) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext = principal.getKeycloakSecurityContext();
        Esn esn = esnRepository.findByEsnUsernameRepresentant(keycloakSecurityContext.getToken().getPreferredUsername());
        Optional<AppelOffre> appelOffre = Optional.of(mapper.appelOffreDTOtoAppelOffre(appelOffreDto));
        if (appelOffre.isPresent()) {
            appelOffre.get().setEsn(esn);
            AppelOffre appelOffreSaved = appellOffreRepository.save(appelOffre.get());
            appelOffreSaved.setRefAo("AO_" + appelOffreSaved.getId());
            appellOffreRepository.save(appelOffreSaved);
            return new ResponseEntity<AppelOffreDto>(mapper.appelOffreToAppelOffreDTO(appelOffreSaved), HttpStatus.CREATED);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/getAllAoByUsernameEsn/{username}")
    public ResponseEntity<List<AppelOffreDto>> getAllAoByUsernameEsn(@PathVariable("username") String username){
        List<Optional<AppelOffre>> appelOffresByUsernameEsn = appelOffreService.getAoByUsernameEsn(username);
        if(appelOffreService.getAoByUsernameEsn(username).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            List<AppelOffreDto> appelOffresDtoByUsernameEsn=appelOffresByUsernameEsn.stream().map(ao->mapper.appelOffreToAppelOffreDTO(ao.get())).collect(Collectors.toList());
            return new ResponseEntity(appelOffresByUsernameEsn,HttpStatus.OK);
        }

    }
}

@Data
class ProfileLikedin {
    private String idProfile;
    private String profileTitle;
    private String location;
    private int nbrConnexion;
    private String nomProfile;
    private HashMap<String, String> experience;
    private HashMap<String, String> education;
}
