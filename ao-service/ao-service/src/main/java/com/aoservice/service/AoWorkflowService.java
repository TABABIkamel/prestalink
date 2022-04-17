package com.aoservice.service;

import java.util.*;
import java.util.stream.Collectors;

import com.aoservice.entities.*;
import com.aoservice.repositories.*;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.context.event.EventListener;
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
    NotificationRepository notificationRepository;

    @Autowired
    CandidatureFinishedRepository candidatureFinishedRepository;
    //start notification system
    private final SimpMessagingTemplate template;

    private Set<String> listeners = new HashSet<>();

    public AoWorkflowService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void add(String sessionId) {
        listeners.add(sessionId);
    }

    public void remove(String sessionId) {
        listeners.remove(sessionId);
    }
    //end
    @Transactional
    public void startProcess(Candidature candidature) {
        AppelOffre appelOffre = appellOffreRepository.getAoById(candidature.getIdPost());
        Prestataire prestataire = prestataireRepository.findByPrestataireUsername(candidature.getUsername());
        Notification notification=new Notification();
        candidature.setTitreAo(appelOffre.getTitreAo());
        candidature.setRefAo(appelOffre.getRefAo());
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("esn", appelOffre.getEsn().getEsnUsernameRepresentant());
        variables.put("idAo", candidature.getIdPost());
        variables.put("titrAo", candidature.getTitreAo());
        variables.put("refAo", candidature.getRefAo());
        variables.put("username", candidature.getUsername());
        variables.put("Nom Candidat", candidature.getName());
        //        if(prestataire!=null){
//            AppelOffre appelOffre=appellOffreRepository.findById(candidature.getIdPost()).get();
//            appelOffre.getPrestataires().add(prestataire);
//        }

        if (prestataire != null) {
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
            notification.setUrlImageReceiver(prestataire.getLocationImage());

        } else {
            Esn esn = esnRepository.findByEsnUsernameRepresentant(candidature.getUsername());
            candidature.setEmail(esn.getEsnEmail());
            candidature.setLieu(esn.getEsnLieu());
            candidature.setName(esn.getEsnnom());
            variables.put("Nom Candidat", candidature.getName());
            variables.put("lieu", candidature.getLieu());
            variables.put("email", candidature.getEmail());
            runtimeService.startProcessInstanceByKey("candidatureReview", variables);
            appelOffre.getEsns().add(esn);
            appellOffreRepository.save(appelOffre);
            notification.setUrlImageReceiver(esn.getLocationImage());
        }
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(appelOffre.getEsn().getEsnUsernameRepresentant())
                .list();
        tasks.stream()
                .map(task -> {
                    Map<String, Object> newVariables = taskService.getVariables(task.getId());
                    if (candidatureFinishedRepository.findByIdTask(task.getId()) == null) {
                        candidatureFinishedRepository.save(new CandidatureFinished(task.getId(), (String) newVariables.get("titrAo"), (String) newVariables.get("refAo"), (String) newVariables.get("username")));
                    }
                    return null;
                })
                .collect(Collectors.toList());
        String payload = candidature.getName()+" a postulé à votre appel offre "+ appelOffre.getTitreAo();
        for (String listener : listeners) {
           // LOGGER.info("Sending notification to " + listener);

            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(listener);
            headerAccessor.setLeaveMutable(true);

            //int value = (int) Math.round(Math.random() * 100d);
            template.convertAndSendToUser(
                    listener,
                    "/notification/item"+appelOffre.getEsn().getEsnUsernameRepresentant(),
                    payload,
                    headerAccessor.getMessageHeaders());
        }
        //store notification in database
        notification.setContent(candidature.getName()+" a postulé à votre appel offre "+ appelOffre.getTitreAo());
        notification.setUsernameSender(candidature.getUsername());
        notification.setUsernameReceiver(appelOffre.getEsn().getEsnUsernameRepresentant());
        notificationRepository.save(notification);
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
                    Candidature candidature = new Candidature(
                            task.getId(), (Long) variables.get("idAo"), (String) variables.get("titrAo"), (String) variables.get("refAo"), (String) variables.get("Nom Candidat"), (String) variables.get("lieu"), (List<Education>) variables.get("education"), (List<Experience>) variables.get("experience")
                            , (String) variables.get("email"));
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
        Notification notification=new Notification();
        variables.put("approved", approval.isStatus());
        taskService.complete(approval.getId(), variables);
        Optional<CandidatureFinished> candidatureFinished = Optional.ofNullable(candidatureFinishedRepository.findByIdTask(approval.getId()));
        if(candidatureFinished.isPresent()){
        if ( approval.isStatus()) {
            candidatureFinished.get().setStatus("ACCEPTED");
        } else
            candidatureFinished.get().setStatus("REJECTED");
        }
        // start this code is for send notification
        if(candidatureFinished.isPresent()){
           Optional<AppelOffre> appelOffre= Optional.ofNullable(appellOffreRepository.findByRefAo(candidatureFinished.get().getRefAo()));
            for (String listener : listeners) {
                // LOGGER.info("Sending notification to " + listener);

                SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
                headerAccessor.setSessionId(listener);
                headerAccessor.setLeaveMutable(true);

                //int value = (int) Math.round(Math.random() * 100d);
                template.convertAndSendToUser(
                        listener,
                        "/notification/item"+candidatureFinished.get().getUsername(),
                        appelOffre.get().getEsn().getEsnnom()+" a examiné votre candidature pour l appel offre "+appelOffre.get().getTitreAo(),
                        headerAccessor.getMessageHeaders());
            }
            notification.setContent(appelOffre.get().getEsn().getEsnnom()+" a examiné votre candidature pour l appel offre "+appelOffre.get().getTitreAo());
            notification.setUsernameSender(appelOffre.get().getEsn().getEsnnom());
            notification.setUsernameReceiver(candidatureFinished.get().getUsername());
            notification.setUrlImageReceiver(appelOffre.get().getEsn().getLocationImage());
            notificationRepository.save(notification);


        }
        // end

    }

    public List<Candidature> getFinishedTask(String assigne) {
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .finished()
                // .taskDeleteReasonLike("%invalid%")
                .taskAssignee(assigne).list();
        List<String> listIds = tasks.stream().map(task -> task.getId()).collect(Collectors.toList());
        List<Candidature> candidatures = new ArrayList<>();
        listIds.stream().map(idTask ->
        {
            CandidatureFinished candidatureFinished = candidatureFinishedRepository.findByIdTask(idTask);

            if (candidatureFinished != null) {
                Prestataire prestataire = prestataireRepository.findByPrestataireUsername(candidatureFinished.getUsername());
                if (prestataire != null) {

                    Candidature candidature = new Candidature();
                    candidature.setId(idTask);
                    candidature.setHasContract(candidatureFinished.getHasContract());
                    candidature.setTitreAo(candidatureFinished.getTitreAo());
                    candidature.setRefAo(candidatureFinished.getRefAo());
                    candidature.setName(prestataire.getPrestataireNom());
                    candidature.setEmail(prestataire.getPrestataireEmail());
                    candidature.setLieu(prestataire.getPrestataireLieu());
                    candidature.setEducations(prestataire.getPrestataireEducation());
                    candidature.setExperiences(prestataire.getPrestataireExperience());
                    candidature.setStatus(candidatureFinished.getStatus());
                    candidatures.add(candidature);
                } else {
                    Esn esn = esnRepository.findByEsnUsernameRepresentant(candidatureFinished.getUsername());
                    Candidature candidature = new Candidature();
                    candidature.setId(idTask);
                    candidature.setHasContract(candidatureFinished.getHasContract());
                    candidature.setTitreAo(candidatureFinished.getTitreAo());
                    candidature.setRefAo(candidatureFinished.getRefAo());
                    candidature.setName(esn.getEsnnom());
                    candidature.setEmail(esn.getEsnEmail());
                    candidature.setLieu(esn.getEsnLieu());
                    candidature.setStatus(candidatureFinished.getStatus());
                    candidatures.add(candidature);
                }
            }
            return candidatures;
        }).collect(Collectors.toList());
        return candidatures;


    }
    public List<Task> getTaskById(String idTask) {
        List<String> listIds = new ArrayList<>();
        listIds.add(idTask);
        return taskService.createTaskQuery().taskAssigneeIds(listIds).list();

    }

//    @Scheduled(fixedDelay = 2000)
//    public void dispatch() {
//        for (String listener : listeners) {
//            //LOGGER.info("Sending notification to " + listener);
//
//            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
//            headerAccessor.setSessionId(listener);
//            headerAccessor.setLeaveMutable(true);
//
//            int value = (int) Math.round(Math.random() * 100d);
//            template.convertAndSendToUser(
//                    listener,
//                    "/notification/item"+1,
//                    "salem",
//                    headerAccessor.getMessageHeaders());
//        }
//    }
    @EventListener
    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        //LOGGER.info("Disconnecting " + sessionId + "!");
        remove(sessionId);
    }
}