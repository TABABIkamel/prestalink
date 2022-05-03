package com.aoservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

import java.util.Date;
import java.util.Set;


@Entity
@EqualsAndHashCode
public class AppelOffre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String refAo;
    public String titreAo;
    public Date dateDebutAo;
    public Date dateFinAo;
    public String descriptionAo;
    public Float tjmAo;
    private Boolean isDeleted=false;

    @Enumerated(EnumType.STRING)
    public Modalite modaliteAo;

    private String lieu;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Esn esn;
    //@JsonBackReference
    @ManyToMany//(cascade = CascadeType.ALL)
    private Set<Prestataire> prestataires;
    //@JsonBackReference
    @ManyToMany//(cascade = CascadeType.ALL)
    private Set<Esn> esns;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "appelOffre")
    private Set<Mission> missions;

    public AppelOffre() {
    }

    public AppelOffre(
            Long id,
            String refAo,
            String titreAo,
            Date dateDebutAo,
            Date dateFinAo,
            String descriptionAo,
            Float tjmAo,
            Modalite modaliteAo,
            String lieu) {
        this.id = id;
        this.refAo = refAo;
        this.titreAo = titreAo;
        this.dateDebutAo = dateDebutAo;
        this.dateFinAo = dateFinAo;
        this.descriptionAo = descriptionAo;
        this.tjmAo = tjmAo;
        this.modaliteAo = modaliteAo;
        this.lieu = lieu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefAo() {
        return refAo;
    }

    public void setRefAo(String refAo) {
        this.refAo = refAo;
    }

    public String getTitreAo() {
        return titreAo;
    }

    public void setTitreAo(String titreAo) {
        this.titreAo = titreAo;
    }

    public Date getDateDebutAo() {
        return dateDebutAo;
    }

    public void setDateDebutAo(Date dateDebutAo) {
        this.dateDebutAo = dateDebutAo;
    }

    public Date getDateFinAo() {
        return dateFinAo;
    }

    public void setDateFinAo(Date dateFinAo) {
        this.dateFinAo = dateFinAo;
    }

    public String getDescriptionAo() {
        return descriptionAo;
    }

    public void setDescriptionAo(String descriptionAo) {
        this.descriptionAo = descriptionAo;
    }

    public Float getTjmAo() {
        return tjmAo;
    }

    public void setTjmAo(Float tjmAo) {
        this.tjmAo = tjmAo;
    }

    public Modalite getModaliteAo() {
        return modaliteAo;
    }

    public void setModaliteAo(Modalite modaliteAo) {
        this.modaliteAo = modaliteAo;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Esn getEsn() {
        return esn;
    }

    public void setEsn(Esn esn) {
        this.esn = esn;
    }

    public Set<Prestataire> getPrestataires() {
        return prestataires;
    }

    public void setPrestataires(Set<Prestataire> prestataires) {
        this.prestataires = prestataires;
    }

    public Set<Esn> getEsns() {
        return esns;
    }

    public void setEsns(Set<Esn> esns) {
        this.esns = esns;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public Set<Mission> getMissions() {
        return missions;
    }

    public void setMissions(Set<Mission> missions) {
        this.missions = missions;
    }
}
