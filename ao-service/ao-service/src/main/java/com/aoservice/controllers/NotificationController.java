package com.aoservice.controllers;

import com.aoservice.configurationMapper.NotificationMapper;
import com.aoservice.dto.NotificationDto;
import com.aoservice.repositories.NotificationRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping(value = "/api/ao")
@RestController
public class NotificationController {

    @Autowired
    NotificationRepository notificationRepository;
    private NotificationMapper mapperNotification = Mappers.getMapper(NotificationMapper.class);
    @GetMapping("/getAllNotificationByUsername/{username}")
    public ResponseEntity<List<NotificationDto>> getListEducation(@PathVariable("username")String username){
        List<Optional<NotificationDto>> notificationDtos= notificationRepository.findByUsernameReceiver(username)
                                                            .stream()
                                                            .map(notification -> Optional.ofNullable(mapperNotification.notificationtoNotificationDto(notification))).collect(Collectors.toList());
        if(notificationDtos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else
            return new ResponseEntity(notificationDtos,HttpStatus.OK);
    }
}
