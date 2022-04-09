package com.aoservice.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aoservice.entities.*;
import com.aoservice.repositories.AppellOffreRepository;
import com.aoservice.repositories.CandidatureFinishedRepository;
import com.aoservice.repositories.EsnRepository;
import com.aoservice.repositories.PrestataireRepository;
import liquibase.pro.packaged.S;
import lombok.Builder;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricDetail;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AoWorkflowService {
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    PrestataireRepository prestataireRepository;
    @Autowired
    AppellOffreRepository appellOffreRepository;
    @Autowired
    EsnRepository esnRepository;

    @Autowired
    CandidatureFinishedRepository candidatureFinishedRepository;
    @Transactional
    public void startProcess(Candidature candidature) {
        AppelOffre appelOffre =appellOffreRepository.getAoById(candidature.getIdPost());
        Prestataire prestataire=prestataireRepository.findByPrestataireUsername(candidature.getUsername());
        candidature.setTitreAo(appelOffre.getTitreAo());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("esn", appelOffre.getEsn().getEsnUsernameRepresentant());
        variables.put("idAo", candidature.getIdPost());
        variables.put("titrAo", candidature.getTitreAo());
        variables.put("username", candidature.getUsername());
        variables.put("Nom Candidat", candidature.getName());
        //        if(prestataire!=null){
//            AppelOffre appelOffre=appellOffreRepository.findById(candidature.getIdPost()).get();
//            appelOffre.getPrestataires().add(prestataire);
//        }
        if(prestataire!=null){
            List<Experience> prestataireExperience = prestataire.getPrestataireExperience();
            List<Education> prestataireEducation = prestataire.getPrestataireEducation();
            candidature.setExperiences(prestataireExperience);
            candidature.setEducations(prestataireEducation);
            candidature.setLieu(prestataire.getPrestataireLieu());
            candidature.setEmail(prestataire.getPrestataireEmail());
            variables.put("lieu", candidature.getLieu());
            variables.put("experience", candidature.getExperiences());
            variables.put("education", candidature.getEducations());
            variables.put("email", candidature.getEmail());
            ProcessInstance candidatureReview = runtimeService.startProcessInstanceByKey("candidatureReview", variables);
            appelOffre.getPrestataires().add(prestataire);
            appellOffreRepository.save(appelOffre);
        }else {
            Esn esn =esnRepository.findByEsnUsernameRepresentant(candidature.getUsername());
            candidature.setEmail(esn.getEsnEmail());
            candidature.setLieu(esn.getEsnLieu());
            candidature.setName(esn.getEsnnom());
            variables.put("Nom Candidat", candidature.getName());
            variables.put("lieu", candidature.getLieu());
            variables.put("email", candidature.getEmail());
            runtimeService.startProcessInstanceByKey("candidatureReview", variables);
            appelOffre.getEsns().add(esn);
            appellOffreRepository.save(appelOffre);
        }
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(appelOffre.getEsn().getEsnUsernameRepresentant())
                .list();
        tasks.stream()
                .map(task -> {
                    Map<String, Object> newVariables = taskService.getVariables(task.getId());
                    if(candidatureFinishedRepository.findByIdTask(task.getId())==null){
                        candidatureFinishedRepository.save(new CandidatureFinished(task.getId(),(String) newVariables.get("titrAo"),(String) newVariables.get("username")));
                    }
                    return null;
                })
                .collect(Collectors.toList());

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
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(assignee)
                .list();
        return tasks.stream()
                .map(task -> {
                    Map<String, Object> variables = taskService.getVariables(task.getId());
                    Candidature candidature=new Candidature(
                            task.getId(),(Long) variables.get("idAo"),(String) variables.get("titrAo"),(String) variables.get("Nom Candidat"), (String) variables.get("lieu"),(List<Education>) variables.get("education"),(List<Experience>)variables.get("experience")
                            ,(String) variables.get("email"));
//                    if(candidatureFinishedRepository.findByIdTask(task.getId())==null){
//                        candidatureFinishedRepository.save(new CandidatureFinished(task.getId(),(String) variables.get("titrAo"),(String) variables.get("username")));
//                    }
                    return candidature;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void submitReview(Approval approval) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("approved", approval.isStatus());
        taskService.complete(approval.getId(), variables);
        CandidatureFinished candidatureFinished=candidatureFinishedRepository.findByIdTask(approval.getId());
        if (approval.isStatus()){
            candidatureFinished.setStatus("ACCEPTED");
        }else
            candidatureFinished.setStatus("REJECTED");
    }

    public List<Candidature> getFinishedTask(String assigne){
        List<HistoricTaskInstance> tasks=historyService.createHistoricTaskInstanceQuery()
                .finished()
                // .taskDeleteReasonLike("%invalid%")
                .taskAssignee(assigne).list();
        List<String> listIds= tasks.stream().map(task->task.getId()).collect(Collectors.toList());
        List<Candidature> candidatures=new ArrayList<>();
        listIds.stream().map(idTask->
        {
            CandidatureFinished candidatureFinished=candidatureFinishedRepository.findByIdTask(idTask);

            if(candidatureFinished!=null){
            Prestataire prestataire=prestataireRepository.findByPrestataireUsername(candidatureFinished.getUsername());
            if (prestataire!=null){

                Candidature candidature=new Candidature();
                candidature.setId(idTask);
                candidature.setTitreAo(candidatureFinished.getTitreAo());
                candidature.setName(prestataire.getPrestataireNom());
                candidature.setEmail(prestataire.getPrestataireEmail());
                candidature.setLieu(prestataire.getPrestataireLieu());
                candidature.setEducations(prestataire.getPrestataireEducation());
                candidature.setExperiences(prestataire.getPrestataireExperience());
                candidature.setStatus(candidatureFinished.getStatus());
                candidatures.add(candidature);
            }else{
                Esn esn=esnRepository.findByEsnUsernameRepresentant(candidatureFinished.getUsername());
                Candidature candidature=new Candidature();
                candidature.setId(idTask);
                candidature.setTitreAo(candidatureFinished.getTitreAo());
                candidature.setName(esn.getEsnnom());
                candidature.setEmail(esn.getEsnEmail());
                candidature.setLieu(esn.getEsnLieu());
                candidature.setStatus(candidatureFinished.getStatus());
                candidatures.add(candidature);
            }}
            return candidatures;
        }).collect(Collectors.toList());
        return candidatures;



    }
    public List<Task> getTaskById(String idTask){
        List<String> listIds=new ArrayList<>();
        listIds.add(idTask);
        return taskService.createTaskQuery().taskAssigneeIds(listIds).list();

    }
}