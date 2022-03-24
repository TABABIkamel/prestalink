package com.aoservice.entities;

import java.util.List;

public class Candidature {
    private String id;
    private Long idPost;
    private String username;
    private String name;
    private List<Education> educations;
    private List<Experience> experiences;

    public Candidature() {
    }

    public Candidature(String id, Long idPost, String username, String name, List<Education> educations, List<Experience> experiences) {
        this.id = id;
        this.idPost = idPost;
        this.username = username;
        this.name = name;
        this.educations = educations;
        this.experiences = experiences;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
