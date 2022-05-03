package com.aoservice.controllers;

import com.aoservice.dto.EsnDto;
import com.aoservice.dto.PrestataireDto;
import com.aoservice.service.CompleteProfileService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestControllerAdvice
@RequestMapping(value = "/api/ao")
public class CompleterProfileController {
    @Autowired
    CompleteProfileService completeProfileService;
    @PostMapping(value = "/completeProfileEsn")
    @ResponseBody
    public ResponseEntity<EsnDto> CompleterProfilEsn(@RequestBody EsnDto esnDto, HttpServletRequest request) throws Exception {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext = principal.getKeycloakSecurityContext();
        String username = keycloakSecurityContext.getToken().getPreferredUsername();
        return completeProfileService.completeProfileEsn(esnDto, username);
    }

    @PostMapping(value = "setPhotoToEsn/{username}")
    public ResponseEntity<String> setPhotoToEsn(MultipartFile file, @PathVariable("username") String username) throws IOException {
        return completeProfileService.setPhotoToEsn(file, username);
    }

    @PostMapping(value = "setPhotoToPrestataire/{username}")
    public ResponseEntity<String> setPhotoToPrestataire(MultipartFile file, @PathVariable("username") String username) throws IOException {
        return completeProfileService.setPhotoToPrestataire(file,username);
    }

    @GetMapping(value = "/checkIfProfileEsnCompleted/{username}")
    public ResponseEntity<EsnDto> checkIfProfileEsnCompleted(@PathVariable("username") String usename) {
            return completeProfileService.checkIfProfileEsnCompleted(usename);
    }

    @GetMapping(value = "/checkIfProfilePrestataireCompleted/{username}")
    public ResponseEntity<PrestataireDto> checkIfProfilePrestataireCompleted(@PathVariable("username") String usename) {
            return completeProfileService.checkIfProfilePrestataireCompleted(usename);
    }

    @PostMapping(value = "/completeProfilePrestataire")
    @ResponseBody
    public ResponseEntity<PrestataireDto> CompleterProfilPrestataire(@RequestBody PrestataireDto prestataireDto) {
        return completeProfileService.CompleterProfilPrestataire(prestataireDto);
    }
    @PutMapping(value = "/modifCvPrestataire")
    @ResponseBody
    public ResponseEntity<PrestataireDto> modifCvPrestataire(@RequestBody PrestataireDto prestataireDto) {
        return completeProfileService.modifierCvPrestataire(prestataireDto);
    }

    @GetMapping(value = "/getPrestataireByUsername/{username}")
    public ResponseEntity<PrestataireDto> getPrestataireByUsername(@PathVariable("username") String usename) {
        return completeProfileService.getPrestataireByUsername(usename);
    }
    @GetMapping(value = "/getPrestataireWithHisCvByUsername/{username}")
    public ResponseEntity<PrestataireDto> getPrestataireWithHisCvByUsername(@PathVariable("username") String usename) {
        return completeProfileService.getPrestataireWithHisCvByUsername(usename);
    }
    @GetMapping(value = "/getEsnByUsername/{username}")
    public ResponseEntity<EsnDto> getEsnByUsername(@PathVariable("username") String usename) {
        return completeProfileService.getEsnByUsername(usename);
    }

    @PutMapping(value = "/modifierProfilPrestataire")
    @ResponseBody
    public ResponseEntity<String> modifierProfilPrestataire(@RequestBody PrestataireDto prestataireDto) {
        return completeProfileService.modifierPrestataireProfile(prestataireDto);
    }

    @PutMapping(value = "/modifierProfilEsn")
    @ResponseBody
    public ResponseEntity<String> modifierProfilEsn(@RequestBody EsnDto esnDto) {
        return completeProfileService.modifierESnProfile(esnDto);
    }

}
