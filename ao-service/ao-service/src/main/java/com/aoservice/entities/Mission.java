package com.aoservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.util.Set;

@Entity
public class Mission {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String usernamePrestataire;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mission")
    private Set<UrlContract> urlsContrat;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private AppelOffre appelOffre;

//    @JsonBackReference
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Prestataire prestataire;
        public Mission() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UrlContract> getUrlsContrat() {
        return urlsContrat;
    }

    public void setUrlsContrat(Set<UrlContract> urlsContrat) {
        this.urlsContrat = urlsContrat;
    }

    public AppelOffre getAppelOffre() {
        return appelOffre;
    }

    public void setAppelOffre(AppelOffre appelOffre) {
        this.appelOffre = appelOffre;
    }

    public String getUsernamePrestataire() {
        return usernamePrestataire;
    }

    public void setUsernamePrestataire(String usernamePrestataire) {
        this.usernamePrestataire = usernamePrestataire;
    }
//    public Prestataire getPrestataire() {
//        return prestataire;
//    }
//
//    public void setPrestataire(Prestataire prestataire) {
//        this.prestataire = prestataire;
//    }
}
