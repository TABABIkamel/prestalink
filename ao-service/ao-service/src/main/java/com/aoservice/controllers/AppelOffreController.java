package com.aoservice.controllers;

import com.aoservice.entities.Esn;
import com.aoservice.entities.Prestataire;
import com.aoservice.repositories.EsnRepository;
import com.aoservice.repositories.PrestataireRepository;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.aoservice.configurationMapper.AppelOffreMapper;
import com.aoservice.dto.AppelOffreDto;
import com.aoservice.entities.AppelOffre;
import com.aoservice.entities.Approval;
import com.aoservice.entities.Contrat;
import com.aoservice.exceptions.coreExceptionClasses.ErrorMessages;
import com.aoservice.exceptions.exceptionClasses.AppelOffreNotFoundException;
import com.aoservice.repositories.AppellOffreRepository;
import lombok.Data;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import javax.servlet.http.HttpServletRequest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private KeycloakRestTemplate keycloakRestTemplate;
    private AppelOffreMapper mapper = Mappers.getMapper(AppelOffreMapper.class);

    @PostMapping(value = "/generateContrat")
    @ResponseBody
    public ResponseEntity<byte[]> generateContrat(@RequestBody Contrat contrat) throws FileNotFoundException, JRException {

        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Arrays.asList(new Approval(false)));
        JasperReport compile = JasperCompileManager.compileReport(new FileInputStream("src/main/resources/jaspertest.jrxml"));
        Map<String, Object> map = new HashMap<>();
        map.put("title", "jasper test wiw");
        map.put("minSalary", 1000);
        map.put("condition", "condition");
        map.put("nom", contrat.getNomSocieteClient());
        map.put("lieu", contrat.getLieuSiegeClient());
        map.put("capital", contrat.getCapitaleSocieteClient());
        map.put("nomRepresentantSociete", contrat.getNomRepresentantSocieteClient());
        map.put("numeroRegitre", contrat.getNumeroRegitreCommerceClient());
        map.put("nomPrestataire", contrat.getNomPrestataire());
        map.put("prenomPrestataire", contrat.getPrenomPrestataire());
        map.put("cin", contrat.getCin());
        map.put("lieuPrestataire", contrat.getLieuPrestataire());
        map.put("prixTotaleMission", contrat.getPrixTotaleMission());
        map.put("preambule", contrat.getPreambule());
        map.put("penalisationParJour", contrat.getPenalisationParJour());
        map.put("dateGenerationContrat", contrat.getDateGenerationContrat());
        JasperPrint jasper = JasperFillManager.fillReport(compile, map, jrBeanCollectionDataSource);
        //    	JasperExportManager.exportReportToPdfFile(jasper,"facture.pdf");
        byte data[] = JasperExportManager.exportReportToPdf(jasper);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=citiesreport.pdf");


        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);

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
            return new ResponseEntity<AppelOffreDto>(mapper.appelOffreToAppelOffreDTO(appelOffreSaved), HttpStatus.CREATED);
        } else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
