package com.aoservice.dto;

public class EsnDto {

    private Long id;
    private String nonEsn;
    private String rib;
    private String iban;
    private String lieu;
    private String email;
    private String usernameRepresentant;
    private boolean isCompleted;
    private boolean isPrestataire;

    public EsnDto() {
    }

    public EsnDto(Long id, String nonEsn, String rib, String iban, String lieu, String email, String usernameRepresentant, boolean isCompleted, boolean isPrestataire) {
        this.id = id;
        this.nonEsn = nonEsn;
        this.rib = rib;
        this.iban = iban;
        this.lieu = lieu;
        this.email = email;
        this.usernameRepresentant = usernameRepresentant;
        this.isCompleted = isCompleted;
        this.isPrestataire = isPrestataire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNonEsn() {
        return nonEsn;
    }

    public void setNonEsn(String nonEsn) {
        this.nonEsn = nonEsn;
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

    public String getUsernameRepresentant() {
        return usernameRepresentant;
    }

    public void setUsernameRepresentant(String usernameRepresentant) {
        this.usernameRepresentant = usernameRepresentant;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isPrestataire() {
        return isPrestataire;
    }

    public void setPrestataire(boolean prestataire) {
        isPrestataire = prestataire;
    }
}
