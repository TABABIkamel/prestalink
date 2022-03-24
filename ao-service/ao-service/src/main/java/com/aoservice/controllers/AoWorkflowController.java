package com.aoservice.controllers;

import java.util.List;

import com.aoservice.entities.*;
import com.aoservice.repositories.AppellOffreRepository;
import com.aoservice.repositories.EducationRepository;
import com.aoservice.repositories.ExperienceRepository;
import com.aoservice.repositories.PrestataireRepository;
import com.aoservice.service.ArticleWorkflowService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@RequestMapping(value = "/api/ao")
@RestController
public class AoWorkflowController {
    @Autowired
    ArticleWorkflowService service;
    @Autowired
    PrestataireRepository prestataireRepository;
    @Autowired
    EducationRepository educationRepository;
    @Autowired
    ExperienceRepository experienceRepository;

//    @Autowired
//    AppellOffreRepository appellOffreRepository;
//    @GetMapping("/getAoById/{idAo}")
//    public AppelOffre getEsnByIdAo(@PathVariable("idAo")Long idAo){
//        return appellOffreRepository.getAoById(idAo);
//    }
    @GetMapping("/getPrestataire/{username}")
    public Prestataire getPrestataireByUsername(@PathVariable("username")String username){
        return prestataireRepository.findByPrestataireUsername(username);
    }

    @GetMapping("/getEducation/{username}")
    public List<Education> getListEducation(@PathVariable("username")String username){
        return educationRepository.getEducationPrestataire(username);
    }
    @GetMapping("/getExperience/{username}")
    public List<Experience> getListExperience(@PathVariable("username")String username){
        return experienceRepository.getExperiencePrestataire(username);
    }
    @PostMapping("/submit")
    public void submit(@RequestBody Candidature candidature, HttpServletRequest request) {
//        KeycloakAuthenticationToken token=(KeycloakAuthenticationToken) request.getUserPrincipal();
//        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
//        KeycloakSecurityContext keycloakSecurityContext=principal.getKeycloakSecurityContext();
//        keycloakSecurityContext.getToken().getId();
          service.startProcess(candidature);
    }
    @GetMapping("/tasks")
    public List<Candidature> getTasks(@RequestParam String assignee) {
        return service.getTasks(assignee);
    }
    @GetMapping("/statusTasks")
    public List<Object> getStatusTasks(@RequestParam String assignee) {
        return service.getStatusOfTasks(assignee);
    }
    @PostMapping("/review")
    public void review(@RequestBody Approval approval) {
        service.submitReview(approval);
    }
}