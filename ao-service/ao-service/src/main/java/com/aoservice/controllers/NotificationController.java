package com.aoservice.controllers;

import com.aoservice.configurationMapper.NotificationMapper;
import com.aoservice.dto.NotificationDto;
import com.aoservice.repositories.NotificationRepository;
import com.aoservice.service.NotificationService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequestMapping(value = "/api/ao")
@RestController
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    private NotificationMapper mapperNotification = Mappers.getMapper(NotificationMapper.class);
    @GetMapping("/getAllNotificationByUsername/{username}")
    public ResponseEntity<List<NotificationDto>> getAllNotificationByUsername(@PathVariable("username")String username){
        return notificationService.getAllNotificationByUsername(username);
    }
}
