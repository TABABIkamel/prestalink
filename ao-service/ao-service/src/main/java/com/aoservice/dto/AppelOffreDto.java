package com.aoservice.dto;


import com.aoservice.entities.Modalite;
import lombok.*;

import javax.persistence.Id;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
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
    public Modalite modaliteAoDto;

}
