package com.aoservice.service;

import com.aoservice.configurationMapper.NotificationMapper;
import com.aoservice.dto.NotificationDto;
import com.aoservice.repositories.NotificationRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    private NotificationMapper mapperNotification = Mappers.getMapper(NotificationMapper.class);

    public ResponseEntity<List<NotificationDto>> getAllNotificationByUsername(String username){
        List<Optional<NotificationDto>> notificationDtos= notificationRepository.findByUsernameReceiver(username)
                .stream()
                .map(notification -> Optional.ofNullable(mapperNotification.notificationtoNotificationDto(notification))).collect(Collectors.toList());
        if(notificationDtos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else
            return new ResponseEntity(notificationDtos,HttpStatus.OK);
    }
}
