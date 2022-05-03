package com.aoservice.configurationMapper;

import com.aoservice.dto.EducationDto;
import com.aoservice.dto.EsnDto;
import com.aoservice.dto.ExperienceDto;
import com.aoservice.dto.PrestataireDto;
import com.aoservice.entities.Education;
import com.aoservice.entities.Esn;
import com.aoservice.entities.Experience;
import com.aoservice.entities.Prestataire;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface PrestataireMapper {
    @Mappings({
            @Mapping(target="id", source="prestataire.prestataireId"),
            @Mapping(target="usernamePrestataire", source="prestataire.prestataireUsername"),
            @Mapping(target="nom", source="prestataire.prestataireNom"),
            @Mapping(target="prenom", source="prestataire.prestatairePrenom"),
            @Mapping(target="profession", source="prestataire.prestataireProfession"),
            @Mapping(target="rib", source="prestataire.prestataireRib"),
            @Mapping(target="iban", source="prestataire.prestataireIban"),
            @Mapping(target="lieu", source="prestataire.prestataireLieu"),
            @Mapping(target="email", source="prestataire.prestataireEmail"),
            @Mapping(target="completed", source="prestataire.prestataireIsCompleted"),
            @Mapping(target="imageLocation", source="prestataire.locationImage"),
//            @Mapping(target="education", source="prestataire.prestataireEducation"),
//            @Mapping(target="experience", source="prestataire.prestataireExperience"),
    })
    PrestataireDto prestataireToPrestataireDTO(Prestataire prestataire);
    @Mappings({
            @Mapping(target="prestataireId", source="prestataireDto.id"),
            @Mapping(target="prestataireUsername", source="prestataireDto.usernamePrestataire"),
            @Mapping(target="prestataireNom", source="prestataireDto.nom"),
            @Mapping(target="prestatairePrenom", source="prestataireDto.prenom"),
            @Mapping(target="prestataireProfession", source="prestataireDto.profession"),
            @Mapping(target="prestataireRib", source="prestataireDto.rib"),
            @Mapping(target="prestataireIban", source="prestataireDto.iban"),
            @Mapping(target="prestataireLieu", source="prestataireDto.lieu"),
            @Mapping(target="prestataireEmail", source="prestataireDto.email"),
            @Mapping(target="prestataireIsCompleted", source="prestataireDto.completed"),
            @Mapping(target="locationImage", source="prestataireDto.imageLocation"),
    })
    Prestataire prestataireDTOToPrestataire(PrestataireDto prestataireDto);


    @Mappings({})
    EducationDto educationToEducationDTO(Education education);
    @Mappings({})
    Education educationDtoToEducation(EducationDto educationDto);
    @Mappings({})
    ExperienceDto experienceToExperienceDTO(Experience experience);
    @Mappings({})
    Experience experienceDtoToExperience(ExperienceDto experienceDto);
}
