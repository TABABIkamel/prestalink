package com.aoservice.dto;

import java.util.List;
public class PrestataireDto {
    private Long id;
    private String usernamePrestataire;
    private String nom;
    private String prenom;
    private String profession;
    private String rib;
    private String iban;
    private String lieu;
    private String email;
    private boolean isCompleted;
    private List<EducationDto> education;
    private List<ExperienceDto> experience;

    public PrestataireDto() {
    }

    public PrestataireDto(Long id, String usernamePrestataire, String nom, String prenom, String profession, String rib, String iban, String lieu, String email, boolean isCompleted) {
        this.id = id;
        this.usernamePrestataire = usernamePrestataire;
        this.nom = nom;
        this.prenom = prenom;
        this.profession = profession;
        this.rib = rib;
        this.iban = iban;
        this.lieu = lieu;
        this.email = email;
        this.isCompleted = isCompleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsernamePrestataire() {
        return usernamePrestataire;
    }

    public void setUsernamePrestataire(String usernamePrestataire) {
        this.usernamePrestataire = usernamePrestataire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
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

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public List<EducationDto> getEducation() {
        return education;
    }

    public void setEducation(List<EducationDto> education) {
        this.education = education;
    }

    public List<ExperienceDto> getExperience() {
        return experience;
    }

    public void setExperience(List<ExperienceDto> experience) {
        this.experience = experience;
    }
}
