package com.aoservice.entities;

public interface Observe {
    void addObserver(Observe observe);
    void supprimerObservateur(Observateur observateur);
    void notifyObservateur();
}
