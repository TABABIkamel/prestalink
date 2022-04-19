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
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestControllerAdvice
@RequestMapping("/api/ao")
public class AppelOffreController {

    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

    @Autowired
    private AppelOffreService appelOffreService;

    private final AppelOffreMapper mapper = Mappers.getMapper(AppelOffreMapper.class);

    @PostMapping(value = "/generateContrat")
    @ResponseBody
    public ResponseEntity<String> generateContrat(@RequestBody ContratDto contrat, HttpServletRequest request)  {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext = principal.getKeycloakSecurityContext();
        String givenName=keycloakSecurityContext.getToken().getGivenName();
        return appelOffreService.generateContrat(contrat,givenName);
    }

    @GetMapping(value = "/allAo")
    public ResponseEntity<List<AppelOffreDto>> getAllAo() {
        return appelOffreService.getAllAo();
    }

//    @GetMapping(value = "/getProfileAo")
//    public ProfileLikedin[] getProfile() {
//        ProfileLikedin[] pageProfiles = keycloakRestTemplate.getForObject("http://127.0.0.1:8085/api/profilelinkedin/allProfiles", ProfileLikedin[].class);
//        return pageProfiles;
//    }

    @ResponseBody
    @PostMapping(value = "/createAo")
    public ResponseEntity<AppelOffreDto> createAo(@RequestBody AppelOffreDto appelOffreDto, HttpServletRequest request) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext = principal.getKeycloakSecurityContext();
        String username= keycloakSecurityContext.getToken().getPreferredUsername();
        return appelOffreService.createAo(appelOffreDto,username);
    }
    @GetMapping("/getAllAoByUsernameEsn/{username}")
    public ResponseEntity<List<AppelOffreDto>> getAllAoByUsernameEsn(@PathVariable("username") String username){
        return appelOffreService.getAoByUsernameEsn(username);

    }
}

//@Data
//class ProfileLikedin {
//    private String idProfile;
//    private String profileTitle;
//    private String location;
//    private int nbrConnexion;
//    private String nomProfile;
//    private HashMap<String, String> experience;
//    private HashMap<String, String> education;
//}
