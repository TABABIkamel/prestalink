package com.aoservice.controllers;

import com.aoservice.entities.*;
import com.aoservice.repositories.*;
import com.aoservice.service.AoWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import java.util.List;


@RequestMapping(value = "/api/ao")
@RestController
public class AoWorkflowController {
    @Autowired
    AoWorkflowService aoWorkflowService;
    @Autowired
    PrestataireRepository prestataireRepository;
    @Autowired
    CandidatureFinishedRepository candidatureFinishedRepository;
    @Autowired
    private AppellOffreRepository appellOffreRepository;

    @GetMapping("/getPrestataire/{username}")
    public Prestataire getPrestataireByUsername(@PathVariable("username") String username) {
        return prestataireRepository.findByPrestataireUsername(username);
    }

    //    @GetMapping("/getEducation/{username}")
//    public List<Education> getListEducation(@PathVariable("username")String username){
//        return educationRepository.getEducationPrestataire(username);
//    }
//    @GetMapping("/getExperience/{username}")
//    public List<Experience> getListExperience(@PathVariable("username")String username){
//        return experienceRepository.getExperiencePrestataire(username);
//    }
    @PostMapping("/submit")
    @ResponseBody
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<String> submit(@RequestBody Candidature candidature, HttpServletRequest request) {
        return aoWorkflowService.postuler(candidature);

    }

    @GetMapping("/tasks/{assignee}")
    public ResponseEntity<List<Candidature>> getTasks(@PathVariable("assignee") String assignee) {
        return aoWorkflowService.getTasks(assignee);
    }

    @GetMapping("/finishedTasks/{assignee}")
    public ResponseEntity<List<Candidature>> getFinishedTask(@PathVariable("assignee") String assignee) {
        return aoWorkflowService.getFinishedTask(assignee);
    }

    //    @GetMapping("/getTaskById/{idTask}")
//    public List<Task> getTaskById(@PathVariable("idTask") String idTask) {
//        return aoWorkflowService.getTaskById(idTask);
//    }
    @PostMapping("/review")
    public ResponseEntity<String> review(@RequestBody Approval approval) {
        return aoWorkflowService.submitReview(approval);
    }

    @GetMapping("/getCandidatureByUsernameCandidate/{username}")
    public ResponseEntity<List<CandidatureFinished>> getCandidatureByUsernameCandidate(@PathVariable("username") String username) {
        List<CandidatureFinished> candidatureFinishedList = candidatureFinishedRepository.findByUsername(username);
        if (candidatureFinishedList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(candidatureFinishedList, HttpStatus.OK);
    }


}