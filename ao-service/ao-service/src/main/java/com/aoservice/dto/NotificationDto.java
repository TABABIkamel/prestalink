package com.aoservice.dto;

public class NotificationDto {
    private Long idDto;
    private String contentDto;
    private String usernameSenderDto;
    private String usernameReceiverDto;
    private String urlImageReceiverDto;
    public NotificationDto() {
    }

    public NotificationDto(Long idDto, String contentDto, String usernameSenderDto, String usernameReceiverDto, String urlImageReceiverDto) {
        this.idDto = idDto;
        this.contentDto = contentDto;
        this.usernameSenderDto = usernameSenderDto;
        this.usernameReceiverDto = usernameReceiverDto;
        this.urlImageReceiverDto = urlImageReceiverDto;
    }

    public Long getIdDto() {
        return idDto;
    }

    public void setIdDto(Long idDto) {
        this.idDto = idDto;
    }

    public String getContentDto() {
        return contentDto;
    }

    public void setContentDto(String contentDto) {
        this.contentDto = contentDto;
    }

    public String getUsernameSenderDto() {
        return usernameSenderDto;
    }

    public void setUsernameSenderDto(String usernameSenderDto) {
        this.usernameSenderDto = usernameSenderDto;
    }

    public String getUsernameReceiverDto() {
        return usernameReceiverDto;
    }

    public void setUsernameReceiverDto(String usernameReceiverDto) {
        this.usernameReceiverDto = usernameReceiverDto;
    }

    public String getUrlImageReceiverDto() {
        return urlImageReceiverDto;
    }

    public void setUrlImageReceiverDto(String urlImageReceiverDto) {
        this.urlImageReceiverDto = urlImageReceiverDto;
    }
}
