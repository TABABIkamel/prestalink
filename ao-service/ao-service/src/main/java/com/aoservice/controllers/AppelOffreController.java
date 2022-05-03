package com.aoservice.controllers;

import com.aoservice.dto.ContratDto;
import com.aoservice.response.ContratResponse;
import com.aoservice.service.AppelOffreService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.aoservice.dto.AppelOffreDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestControllerAdvice
@RequestMapping("/api/ao")
public class AppelOffreController {

    @Autowired
    private AppelOffreService appelOffreService;

    @PostMapping(value = "/generateContrat")
    @ResponseBody
    public ResponseEntity<String> generateContrat(@RequestBody ContratDto contrat, HttpServletRequest request) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext = principal.getKeycloakSecurityContext();
        String givenName = keycloakSecurityContext.getToken().getGivenName();
        return appelOffreService.generateContrat(contrat, givenName);
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
        String username = keycloakSecurityContext.getToken().getPreferredUsername();
        return appelOffreService.createAo(appelOffreDto, username);
    }

    @GetMapping("/getAllAoByUsernameEsn/{username}")
    public ResponseEntity<List<AppelOffreDto>> getAllAoByUsernameEsn(@PathVariable("username") String username) {
        return appelOffreService.getAoByUsernameEsn(username);

    }
    @DeleteMapping("/deleteAo/{id}")
    public ResponseEntity<String> deleteAo(@PathVariable("id") Long id) {
        return appelOffreService.deleteAo(id);

    }
    @GetMapping("/getAoById/{id}")
    public ResponseEntity<AppelOffreDto> getAoById(@PathVariable("id") Long id) {
        return appelOffreService.getAoById(id);
    }
    @PutMapping("/editAo")
    public ResponseEntity<AppelOffreDto> editAo(@RequestBody AppelOffreDto appelOffreDto, HttpServletRequest request) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext = principal.getKeycloakSecurityContext();
        String username = keycloakSecurityContext.getToken().getPreferredUsername();
        return appelOffreService.editAo(appelOffreDto, username);
    }

    @GetMapping("/getAllContratUrlByUsernameForPrestataire/{username}")
    public ResponseEntity<List<ContratResponse>> getAllContratUrlByUsernameForPrestataire(@PathVariable("username") String username) {
        return appelOffreService.getAllContratUrlByUsernameForPrestataire(username);
    }

    @GetMapping("/getAllContratUrlByUsernameForEsn/{username}")
    public ResponseEntity<HashMap<String, List<ContratResponse>>> getAllContratUrlByUsernameForEsn(@PathVariable("username") String username) {
        return appelOffreService.getAllContratUrlByUsernameForEsn(username);
    }
}

