package com.aoservice.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@Getter
@Setter
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private String usernameSender;
    private String usernameReceiver;
    private String urlImageReceiver;

    public Notification() {
    }

    public Notification(String content, String usernameSender, String usernameReceiver) {
        this.content = content;
        this.usernameSender = usernameSender;
        this.usernameReceiver = usernameReceiver;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsernameSender() {
        return usernameSender;
    }

    public void setUsernameSender(String usernameSender) {
        this.usernameSender = usernameSender;
    }

    public String getUsernameReceiver() {
        return usernameReceiver;
    }

    public void setUsernameReceiver(String usernameReceiver) {
        this.usernameReceiver = usernameReceiver;
    }

    public String getUrlImageReceiver() {
        return urlImageReceiver;
    }

    public void setUrlImageReceiver(String urlImageReceiver) {
        this.urlImageReceiver = urlImageReceiver;
    }
}
