package com.aoservice.entities;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
public class Esn  {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long esnid;
    private String esnnom;
    private String esnrib;
    private String esnIban;
    private String esnLieu;
    private String esnEmail;
    private String esnUsernameRepresentant;
    private boolean esnIsCompleted;
    private String locationImage;
    private boolean esnIsPrestataire;
    @OneToMany(cascade = CascadeType.ALL,mappedBy="esn")
    private Set<AppelOffre> appelOffres;
    @ManyToMany(cascade = CascadeType.ALL,mappedBy="esns")
    private Set<AppelOffre> appelOffress;

    public Esn() {
    }

    public Esn(Long esnid, String esnnom, String esnrib, String esnIban, String esnLieu, String esnEmail, String esnUsernameRepresentant, boolean esnIsCompleted, boolean esnIsPrestataire) {
        this.esnid = esnid;
        this.esnnom = esnnom;
        this.esnrib = esnrib;
        this.esnIban = esnIban;
        this.esnLieu = esnLieu;
        this.esnEmail = esnEmail;
        this.esnUsernameRepresentant = esnUsernameRepresentant;
        this.esnIsCompleted = esnIsCompleted;
        this.esnIsPrestataire = esnIsPrestataire;
    }

    public Long getEsnid() {
        return esnid;
    }

    public void setEsnid(Long esnid) {
        this.esnid = esnid;
    }

    public String getEsnnom() {
        return esnnom;
    }

    public void setEsnnom(String esnnom) {
        this.esnnom = esnnom;
    }

    public String getEsnrib() {
        return esnrib;
    }

    public void setEsnrib(String esnrib) {
        this.esnrib = esnrib;
    }

    public String getEsnIban() {
        return esnIban;
    }

    public void setEsnIban(String esnIban) {
        this.esnIban = esnIban;
    }

    public String getEsnLieu() {
        return esnLieu;
    }

    public void setEsnLieu(String esnLieu) {
        this.esnLieu = esnLieu;
    }

    public String getEsnEmail() {
        return esnEmail;
    }

    public void setEsnEmail(String esnEmail) {
        this.esnEmail = esnEmail;
    }

    public String getEsnUsernameRepresentant() {
        return esnUsernameRepresentant;
    }

    public void setEsnUsernameRepresentant(String esnUsernameRepresentant) {
        this.esnUsernameRepresentant = esnUsernameRepresentant;
    }

    public boolean isEsnIsCompleted() {
        return esnIsCompleted;
    }

    public void setEsnIsCompleted(boolean esnIsCompleted) {
        this.esnIsCompleted = esnIsCompleted;
    }

    public boolean isEsnIsPrestataire() {
        return esnIsPrestataire;
    }

    public void setEsnIsPrestataire(boolean esnIsPrestataire) {
        this.esnIsPrestataire = esnIsPrestataire;
    }

    public String getLocationImage() {
        return locationImage;
    }

    public void setLocationImage(String locationImage) {
        this.locationImage = locationImage;
    }

    public Set<AppelOffre> getAppelOffres() {
        return appelOffres;
    }

    public void setAppelOffres(Set<AppelOffre> appelOffres) {
        this.appelOffres = appelOffres;
    }

    public Set<AppelOffre> getAppelOffress() {
        return appelOffress;
    }

    public void setAppelOffress(Set<AppelOffre> appelOffress) {
        this.appelOffress = appelOffress;
    }
}
