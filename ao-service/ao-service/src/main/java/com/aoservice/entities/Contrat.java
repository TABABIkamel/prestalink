package com.aoservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    private String NomPrestataire;
    private String PrenomPrestataire;
    private String lieuPrestataire;
    private Long cin;
    private String preambule;
    private Float prixTotaleMission;
    private Float penalisationParJour;
    private Langue langueContrat;
    @Column(nullable = false, updatable = false)
    private Timestamp dateGenerationContrat;
    @OneToOne(mappedBy = "contrat",cascade = CascadeType.REMOVE)
    private Mission mission;

}
