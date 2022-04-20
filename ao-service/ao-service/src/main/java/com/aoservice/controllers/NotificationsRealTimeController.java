package com.aoservice.controllers;

import com.aoservice.service.AoWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationsRealTimeController {

    //private final NotificationDispatcher dispatcher;
    private final AoWorkflowService aoWorkflowService;

    @Autowired
    public NotificationsRealTimeController(/*NotificationDispatcher dispatcher,*/AoWorkflowService aoWorkflowService) {
        this.aoWorkflowService=aoWorkflowService;
        //this.dispatcher = dispatcher;
    }

    @MessageMapping("/start")
    public void start(StompHeaderAccessor stompHeaderAccessor) {

        aoWorkflowService.add(stompHeaderAccessor.getSessionId());
    }

    @MessageMapping("/stop")
    public void stop(StompHeaderAccessor stompHeaderAccessor) {
        aoWorkflowService.remove(stompHeaderAccessor.getSessionId());
    }
}
