package com.aoservice.dto;

import java.util.Date;

public class ExperienceDto {
    private String nomSociete;
    private String titrePoste;
    private Date dateDebut;
    private Date dateFin;
    private String descriptionPoste;

    public ExperienceDto() {
    }

    public ExperienceDto(String nomSociete, String titrePoste, Date dateDebut, Date dateFin, String descriptionPoste) {
        this.nomSociete = nomSociete;
        this.titrePoste = titrePoste;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.descriptionPoste = descriptionPoste;
    }

    public String getNomSociete() {
        return nomSociete;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public String getTitrePoste() {
        return titrePoste;
    }

    public void setTitrePoste(String titrePoste) {
        this.titrePoste = titrePoste;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getDescriptionPoste() {
        return descriptionPoste;
    }

    public void setDescriptionPoste(String descriptionPoste) {
        this.descriptionPoste = descriptionPoste;
    }
}
