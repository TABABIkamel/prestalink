package com.aoservice.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CandidatureFinished {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String idTask;
    private String titreAo;
    private String username;
    private String status;

    public CandidatureFinished() {
    }

    public CandidatureFinished(Long id, String idTask, String titreAo, String username) {
        this.id = id;
        this.idTask = idTask;
        this.titreAo = titreAo;
        this.username = username;
    }

    public CandidatureFinished(String idTask, String titreAo, String username) {
        this.idTask=idTask;
        this.titreAo = titreAo;
        this.username = username;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitreAo() {
        return titreAo;
    }

    public void setTitreAo(String titreAo) {
        this.titreAo = titreAo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIdTask() {
        return idTask;
    }

    public void setIdTask(String idTask) {
        this.idTask = idTask;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
