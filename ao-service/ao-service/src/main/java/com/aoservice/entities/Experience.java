package com.aoservice.entities;

import javax.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonBackReference;
@Entity
public class Experience {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String nomSociete;
    private String titrePoste;
    private Date dateDebut;
    private Date dateFin;
    private String descriptionPoste;
    @JsonBackReference
    @ManyToOne(fetch=FetchType.LAZY)
    private Prestataire prestataire;
    public Experience() {
    }

    public Experience(Long id, String nomSociete, String titrePoste, Date dateDebut, Date dateFin, String descriptionPoste) {
        this.id = id;
        this.nomSociete = nomSociete;
        this.titrePoste = titrePoste;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.descriptionPoste = descriptionPoste;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Prestataire getPrestataire() {
        return prestataire;
    }

    public void setPrestataire(Prestataire prestataire) {
        this.prestataire = prestataire;
    }
}
