package com.aoservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mission {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String titreMission;
    private Date dateDebutMission;
    private Date dateFinMission;
    private String descriptionMission;
    private Float tjmMission;
    @Enumerated(EnumType.STRING)
    private Modalite modaliteMission;
    @OneToOne
    private Contrat contrat;
}
