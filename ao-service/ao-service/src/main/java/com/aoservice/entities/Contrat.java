package com.aoservice.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Contrat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nomSocieteClient;
    private String formeJuriqiqueClient;
    private Double capitaleSocieteClient;
    private String lieuSiegeClient;
    private Long numeroRegitreCommerceClient;
    private String nomRepresentantSocieteClient;
    private String nomPrestataire;
    private String prenomPrestataire;
    private String lieuPrestataire;
    private Long cin;
    private String preambule;
    private Float prixTotaleMission;
    private Float penalisationParJour;
    @Column(nullable = false, updatable = false)
    private Timestamp dateGenerationContrat;
    @OneToOne(mappedBy = "contrat",cascade = CascadeType.REMOVE)
    private Mission mission;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNomSocieteClient() {
		return nomSocieteClient;
	}
	public void setNomSocieteClient(String nomSocieteClient) {
		this.nomSocieteClient = nomSocieteClient;
	}
	public String getFormeJuriqiqueClient() {
		return formeJuriqiqueClient;
	}
	public void setFormeJuriqiqueClient(String formeJuriqiqueClient) {
		this.formeJuriqiqueClient = formeJuriqiqueClient;
	}
	public Double getCapitaleSocieteClient() {
		return capitaleSocieteClient;
	}
	public void setCapitaleSocieteClient(Double capitaleSocieteClient) {
		this.capitaleSocieteClient = capitaleSocieteClient;
	}
	public String getLieuSiegeClient() {
		return lieuSiegeClient;
	}
	public void setLieuSiegeClient(String lieuSiegeClient) {
		this.lieuSiegeClient = lieuSiegeClient;
	}
	public Long getNumeroRegitreCommerceClient() {
		return numeroRegitreCommerceClient;
	}
	public void setNumeroRegitreCommerceClient(Long numeroRegitreCommerceClient) {
		this.numeroRegitreCommerceClient = numeroRegitreCommerceClient;
	}
	public String getNomRepresentantSocieteClient() {
		return nomRepresentantSocieteClient;
	}
	public void setNomRepresentantSocieteClient(String nomRepresentantSocieteClient) {
		this.nomRepresentantSocieteClient = nomRepresentantSocieteClient;
	}

	public String getNomPrestataire() {
		return nomPrestataire;
	}

	public void setNomPrestataire(String nomPrestataire) {
		this.nomPrestataire = nomPrestataire;
	}

	public String getPrenomPrestataire() {
		return prenomPrestataire;
	}

	public void setPrenomPrestataire(String prenomPrestataire) {
		this.prenomPrestataire = prenomPrestataire;
	}

	public String getLieuPrestataire() {
		return lieuPrestataire;
	}
	public void setLieuPrestataire(String lieuPrestataire) {
		this.lieuPrestataire = lieuPrestataire;
	}
	public Long getCin() {
		return cin;
	}
	public void setCin(Long cin) {
		this.cin = cin;
	}
	public String getPreambule() {
		return preambule;
	}
	public void setPreambule(String preambule) {
		this.preambule = preambule;
	}
	public Float getPrixTotaleMission() {
		return prixTotaleMission;
	}
	public void setPrixTotaleMission(Float prixTotaleMission) {
		this.prixTotaleMission = prixTotaleMission;
	}
	public Float getPenalisationParJour() {
		return penalisationParJour;
	}
	public void setPenalisationParJour(Float penalisationParJour) {
		this.penalisationParJour = penalisationParJour;
	}
	
	public Timestamp getDateGenerationContrat() {
		return dateGenerationContrat;
	}
	public void setDateGenerationContrat(Timestamp dateGenerationContrat) {
		this.dateGenerationContrat = dateGenerationContrat;
	}
	public Mission getMission() {
		return mission;
	}
	public void setMission(Mission mission) {
		this.mission = mission;
	}

}
