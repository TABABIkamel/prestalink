package com.aoservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observer;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppelOffre implements Observe {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String titreAo;
    private Date dateDebutAo;
    private Date dateFinAo;
    private String descriptionAo;
    private Float tjmAo;
    @Enumerated(EnumType.STRING)
    private Modalite modaliteAo;
    List<Observer> observers = new ArrayList();
    @Override
    public void addObserver(Observe observe) {

    }

    @Override
    public void supprimerObservateur(Observateur observateur) {

    }

    @Override
    public void notifyObservateur() {

    }
}
