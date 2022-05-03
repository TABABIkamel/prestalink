package com.aoservice.entities;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Entity(name = "prestataire")
public class Prestataire {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long prestataireId;
    private String prestataireUsername;
    private String prestataireNom;
    private String prestatairePrenom;
    private String prestataireProfession;
    private String prestataireRib;
    private String prestataireIban;
    private String prestataireLieu;
    private String locationImage;
    private String prestataireEmail;
    private boolean prestataireIsCompleted;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.ALL,mappedBy="prestataire")
    //@JsonBackReference
    //cause error
    private List<Education> prestataireEducation;
    @LazyCollection(LazyCollectionOption.FALSE)
    //@JsonBackReference
    //cause error
    @OneToMany(cascade = CascadeType.ALL,mappedBy="prestataire")
    private List<Experience> prestataireExperience;
    @ManyToMany(mappedBy="prestataires",cascade = CascadeType.ALL)
    private Set<AppelOffre> appelOffres;
//    @LazyCollection(LazyCollectionOption.FALSE)
//    @OneToMany(cascade = CascadeType.ALL,mappedBy="prestataire")
//    private Set<Mission> prestataireMissions;
    public Prestataire() {
    }

    public Prestataire(Long prestataireId, String prestataireUsername, String prestataireNom, String prestatairePrenom, String prestataireProfession, String prestataireRib, String prestataireIban, String prestataireLieu, String prestataireEmail, boolean prestataireIsCompleted) {
        this.prestataireId = prestataireId;
        this.prestataireUsername = prestataireUsername;
        this.prestataireNom = prestataireNom;
        this.prestatairePrenom = prestatairePrenom;
        this.prestataireProfession = prestataireProfession;
        this.prestataireRib = prestataireRib;
        this.prestataireIban = prestataireIban;
        this.prestataireLieu = prestataireLieu;
        this.prestataireEmail = prestataireEmail;
        this.prestataireIsCompleted = prestataireIsCompleted;

    }

    public Long getPrestataireId() {
        return prestataireId;
    }

    public void setPrestataireId(Long prestataireId) {
        this.prestataireId = prestataireId;
    }

    public String getPrestataireUsername() {
        return prestataireUsername;
    }

    public void setPrestataireUsername(String prestataireUsername) {
        this.prestataireUsername = prestataireUsername;
    }

    public String getPrestataireNom() {
        return prestataireNom;
    }

    public void setPrestataireNom(String prestataireNom) {
        this.prestataireNom = prestataireNom;
    }

    public String getPrestatairePrenom() {
        return prestatairePrenom;
    }

    public void setPrestatairePrenom(String prestatairePrenom) {
        this.prestatairePrenom = prestatairePrenom;
    }

    public String getPrestataireProfession() {
        return prestataireProfession;
    }

    public void setPrestataireProfession(String prestataireProfession) {
        this.prestataireProfession = prestataireProfession;
    }

    public String getPrestataireRib() {
        return prestataireRib;
    }

    public void setPrestataireRib(String prestataireRib) {
        this.prestataireRib = prestataireRib;
    }

    public String getPrestataireIban() {
        return prestataireIban;
    }

    public void setPrestataireIban(String prestataireIban) {
        this.prestataireIban = prestataireIban;
    }

    public String getPrestataireLieu() {
        return prestataireLieu;
    }

    public void setPrestataireLieu(String prestataireLieu) {
        this.prestataireLieu = prestataireLieu;
    }

    public String getPrestataireEmail() {
        return prestataireEmail;
    }

    public void setPrestataireEmail(String prestataireEmail) {
        this.prestataireEmail = prestataireEmail;
    }

    public String getLocationImage() {
        return locationImage;
    }

    public void setLocationImage(String locationImage) {
        this.locationImage = locationImage;
    }

    public boolean isPrestataireIsCompleted() {
        return prestataireIsCompleted;
    }

    public void setPrestataireIsCompleted(boolean prestataireIsCompleted) {
        this.prestataireIsCompleted = prestataireIsCompleted;
    }

    public List<Education> getPrestataireEducation() {
        return prestataireEducation;
    }

    public void setPrestataireEducation(List<Education> prestataireEducation) {
        this.prestataireEducation = prestataireEducation;
    }

    public List<Experience> getPrestataireExperience() {
        return prestataireExperience;
    }

    public void setPrestataireExperience(List<Experience> prestataireExperience) {
        this.prestataireExperience = prestataireExperience;
    }

    public Set<AppelOffre> getAppelOffres() {
        return appelOffres;
    }

    public void setAppelOffres(Set<AppelOffre> appelOffres) {
        this.appelOffres = appelOffres;
    }

//    public Set<Mission> getPrestataireMissions() {
//        return prestataireMissions;
//    }
//
//    public void setPrestataireMissions(Set<Mission> prestataireMissions) {
//        this.prestataireMissions = prestataireMissions;
//    }
}
