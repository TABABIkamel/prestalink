package com.aoservice.configurationMapper;

import com.aoservice.dto.AppelOffreDto;
import com.aoservice.entities.AppelOffre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
@Mapper
public interface AppelOffreMapper {
        @Mappings({
                @Mapping(target="idDto", source="appelOffre.id"),
                @Mapping(target="refAoDto", source="appelOffre.refAo"),
                @Mapping(target="titreAoDto", source="appelOffre.titreAo"),
                @Mapping(target="dateDebutAoDto", source="appelOffre.dateDebutAo"),
                @Mapping(target="dateFinAoDto", source="appelOffre.dateFinAo"),
                @Mapping(target="descriptionAoDto", source="appelOffre.descriptionAo"),
                @Mapping(target="tjmAoDto", source="appelOffre.tjmAo"),
                @Mapping(target="lieuAoDto", source="appelOffre.lieu"),
                @Mapping(target="modaliteAoDto", source="appelOffre.modaliteAo")
        })
        AppelOffreDto appelOffreToAppelOffreDTO(AppelOffre appelOffre);
        @Mappings({
                @Mapping(target="id", source="appelOffreDto.idDto"),
                @Mapping(target="refAo", source="appelOffreDto.refAoDto"),
                @Mapping(target="titreAo", source="appelOffreDto.titreAoDto"),
                @Mapping(target="dateDebutAo", source="appelOffreDto.dateDebutAoDto"),
                @Mapping(target="dateFinAo", source="appelOffreDto.dateFinAoDto"),
                @Mapping(target="descriptionAo", source="appelOffreDto.descriptionAoDto"),
                @Mapping(target="tjmAo", source="appelOffreDto.tjmAoDto"),
                @Mapping(target="lieu", source="appelOffreDto.lieuAoDto"),
                @Mapping(target="modaliteAo", source="appelOffreDto.modaliteAoDto")
        })
        AppelOffre appelOffreDTOtoAppelOffre(AppelOffreDto appelOffreDto);
    }

