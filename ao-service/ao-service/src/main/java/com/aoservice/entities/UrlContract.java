package com.aoservice.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class UrlContract {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String urlContrat;
    @ManyToOne
    private Mission mission;

    public UrlContract(Long id, String urlContrat) {
        this.id = id;
        this.urlContrat = urlContrat;
    }

    public UrlContract() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrlContrat() {
        return urlContrat;
    }

    public void setUrlContrat(String urlContrat) {
        this.urlContrat = urlContrat;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UrlContract that = (UrlContract) o;
        return Objects.equals(id, that.id) && Objects.equals(urlContrat, that.urlContrat) && Objects.equals(mission, that.mission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, urlContrat, mission);
    }
}
