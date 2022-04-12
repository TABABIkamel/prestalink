package com.aoservice.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
public class Candidature {
    private String id;
    private Long idPost;
    private String titreAo;
    private String refAo;
    private String username;
    private String name;
    private String lieu;
    private List<Education> educations;
    private List<Experience> experiences;
    private String email;
    private String status;
    private Boolean hasContract;

    public Candidature() {
    }

    public Candidature(String id,
                       Long idPost,
                       String name,
                       List<Education> educations,
                       List<Experience> experiences) {
        this.id = id;
        this.idPost = idPost;
        this.name = name;
        this.educations = educations;
        this.experiences = experiences;
    }

    public Candidature(String id, Long idPost, String username, String email, String lieu) {
        this.id = id;
        this.idPost = idPost;
        this.username = username;
        this.email = email;
        this.lieu = lieu;
    }

    public Candidature(String id,
                       Long idPost,
                       String titreAo,
                       String refAo,
                       String name,
                       String lieu,
                       List<Education> educations,
                       List<Experience> experiences,
                       String email) {
        this.id = id;
        this.idPost = idPost;
        this.titreAo=titreAo;
        this.refAo=refAo;
        this.name = name;
        this.lieu = lieu;
        this.educations = educations;
        this.experiences = experiences;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getHasContract() {
        return hasContract;
    }

    public void setHasContract(Boolean hasContract) {
        this.hasContract = hasContract;
    }

    public Long getIdPost() {
        return idPost;
    }

    public void setIdPost(Long idPost) {
        this.idPost = idPost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitreAo() {
        return titreAo;
    }

    public void setTitreAo(String titreAo) {
        this.titreAo = titreAo;
    }

    public String getRefAo() {
        return refAo;
    }

    public void setRefAo(String refAo) {
        this.refAo = refAo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
