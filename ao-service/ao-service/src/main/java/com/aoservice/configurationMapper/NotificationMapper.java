package com.aoservice.configurationMapper;

import com.aoservice.dto.NotificationDto;
import com.aoservice.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
@Mapper
public interface NotificationMapper {

    @Mappings({
            @Mapping(target="idDto", source="notification.id"),
            @Mapping(target="contentDto", source="notification.content"),
            @Mapping(target="usernameSenderDto", source="notification.usernameSender"),
            @Mapping(target="usernameReceiverDto", source="notification.usernameReceiver"),
            @Mapping(target="urlImageReceiverDto", source="notification.urlImageReceiver")
    })
    NotificationDto notificationtoNotificationDto(Notification notification);

    @Mappings({
            @Mapping(target="id", source="notificationDto.idDto"),
            @Mapping(target="content", source="notificationDto.contentDto"),
            @Mapping(target="usernameSender", source="notificationDto.usernameSenderDto"),
            @Mapping(target="usernameReceiver", source="notificationDto.usernameReceiverDto"),
            @Mapping(target="urlImageReceiver", source="notificationDto.urlImageReceiverDto")
    })
    Notification notificationDtoToNotification(NotificationDto notificationDto);
}
