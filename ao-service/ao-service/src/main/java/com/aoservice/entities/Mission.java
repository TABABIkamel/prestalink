package com.aoservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import java.util.Set;

@Entity
public class Mission {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mission")
    private Set<UrlContract> urlsContrat;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private AppelOffre appelOffre;
    @OneToOne
    private Contrat contrat;
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

    public Contrat getContrat() {
        return contrat;
    }

    public void setContrat(Contrat contrat) {
        this.contrat = contrat;
    }
}
