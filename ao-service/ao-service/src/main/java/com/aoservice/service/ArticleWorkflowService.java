package com.aoservice.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aoservice.entities.*;
import com.aoservice.repositories.AppellOffreRepository;
import com.aoservice.repositories.PrestataireRepository;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArticleWorkflowService {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    PrestataireRepository prestataireRepository;
    @Autowired
    AppellOffreRepository appellOffreRepository;
    @Transactional
    public void startProcess(Candidature candidature) {
        AppelOffre appelOffre =appellOffreRepository.getAoById(candidature.getIdPost());
        Prestataire prestataire=prestataireRepository.findByPrestataireUsername(candidature.getUsername());
        List<Experience> prestataireExperience = prestataire.getPrestataireExperience();
        List<Education> prestataireEducation = prestataire.getPrestataireEducation();
        candidature.setExperiences(prestataireExperience);
        candidature.setEducations(prestataireEducation);
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("Nom Candidat", candidature.getName());
        variables.put("experience", candidature.getExperiences());
        variables.put("education", candidature.getEducations());
        variables.put("esn", appelOffre.getEsn().getEsnid());
        runtimeService.startProcessInstanceByKey("candidatureReview", variables);
    }
    @Transactional
    public List<Object> getStatusOfTasks(String assignee) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
        return tasks.stream().map(task -> task.getClaimTime()).collect(Collectors.toList());
    }
    @Transactional
    public List<Candidature> getTasks(String assignee) {
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
        return tasks.stream()
                .map(task -> {
                    Map<String, Object> variables = taskService.getVariables(task.getId());
                    return new Candidature(
                            task.getId(),1L, (String) variables.get("Nom Candidat"),"kamel",(List<Education>) variables.get("education"),(List<Experience>)variables.get("experience"));
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void submitReview(Approval approval) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("approved", approval.isStatus());
        taskService.complete(approval.getId(), variables);

    }
}