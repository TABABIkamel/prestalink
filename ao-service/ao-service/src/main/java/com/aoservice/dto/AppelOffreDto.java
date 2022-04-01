package com.aoservice.dto;


import com.aoservice.entities.Modalite;
import lombok.*;

import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class AppelOffreDto {
    public Long idDto;
    public String titreAoDto;
    public Date dateDebutAoDto;
    public Date dateFinAoDto;
    public String descriptionAoDto;
    public Float tjmAoDto;
    public String lieuAoDto;
    public Modalite modaliteAoDto;
    public String esnImage;
    public String esnNom;

    public AppelOffreDto() {
    }

    public AppelOffreDto(Long idDto, String titreAoDto, Date dateDebutAoDto, Date dateFinAoDto, String descriptionAoDto, Float tjmAoDto, Modalite modaliteAoDto, String esnImage) {
        this.idDto = idDto;
        this.titreAoDto = titreAoDto;
        this.dateDebutAoDto = dateDebutAoDto;
        this.dateFinAoDto = dateFinAoDto;
        this.descriptionAoDto = descriptionAoDto;
        this.tjmAoDto = tjmAoDto;
        this.modaliteAoDto = modaliteAoDto;
        this.esnImage = esnImage;
    }

    public Long getIdDto() {
        return idDto;
    }

    public void setIdDto(Long idDto) {
        this.idDto = idDto;
    }

    public String getTitreAoDto() {
        return titreAoDto;
    }

    public void setTitreAoDto(String titreAoDto) {
        this.titreAoDto = titreAoDto;
    }

    public Date getDateDebutAoDto() {
        return dateDebutAoDto;
    }

    public void setDateDebutAoDto(Date dateDebutAoDto) {
        this.dateDebutAoDto = dateDebutAoDto;
    }

    public Date getDateFinAoDto() {
        return dateFinAoDto;
    }

    public void setDateFinAoDto(Date dateFinAoDto) {
        this.dateFinAoDto = dateFinAoDto;
    }

    public String getDescriptionAoDto() {
        return descriptionAoDto;
    }

    public void setDescriptionAoDto(String descriptionAoDto) {
        this.descriptionAoDto = descriptionAoDto;
    }

    public Float getTjmAoDto() {
        return tjmAoDto;
    }

    public void setTjmAoDto(Float tjmAoDto) {
        this.tjmAoDto = tjmAoDto;
    }

    public Modalite getModaliteAoDto() {
        return modaliteAoDto;
    }

    public void setModaliteAoDto(Modalite modaliteAoDto) {
        this.modaliteAoDto = modaliteAoDto;
    }

    public String getLieuAoDto() {
        return lieuAoDto;
    }

    public void setLieuAoDto(String lieuAoDto) {
        this.lieuAoDto = lieuAoDto;
    }

    public String getEsnImage() {
        return esnImage;
    }

    public void setEsnImage(String esnImage) {
        this.esnImage = esnImage;
    }

    public String getEsnNom() {
        return esnNom;
    }

    public void setEsnNom(String esnNom) {
        this.esnNom = esnNom;
    }
}
