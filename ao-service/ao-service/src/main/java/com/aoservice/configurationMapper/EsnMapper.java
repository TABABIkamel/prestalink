package com.aoservice.configurationMapper;

import com.aoservice.dto.AppelOffreDto;
import com.aoservice.dto.EsnDto;
import com.aoservice.entities.AppelOffre;
import com.aoservice.entities.Esn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface EsnMapper {
    @Mappings({
            @Mapping(target="id", source="esn.esnid"),
            @Mapping(target="nonEsn", source="esn.esnnom"),
            @Mapping(target="rib", source="esn.esnrib"),
            @Mapping(target="iban", source="esn.esnIban"),
            @Mapping(target="lieu", source="esn.esnLieu"),
            @Mapping(target="email", source="esn.esnEmail"),
            @Mapping(target="usernameRepresentant", source="esn.esnUsernameRepresentant"),
            @Mapping(target="completed", source="esn.esnIsCompleted"),
            @Mapping(target="prestataire", source="esn.esnIsPrestataire"),
            @Mapping(target="imageLocation", source="esn.locationImage"),
    })
    EsnDto esnToEsnDTO(Esn esn);
    @Mappings({
            @Mapping(target="esnid", source="esnDto.id"),
            @Mapping(target="esnnom", source="esnDto.nonEsn"),
            @Mapping(target="esnrib", source="esnDto.rib"),
            @Mapping(target="esnIban", source="esnDto.iban"),
            @Mapping(target="esnLieu", source="esnDto.lieu"),
            @Mapping(target="esnEmail", source="esnDto.email"),
            @Mapping(target="esnUsernameRepresentant", source="esnDto.usernameRepresentant"),
            @Mapping(target="esnIsCompleted", source="esnDto.completed"),
            @Mapping(target="esnIsPrestataire", source="esnDto.prestataire"),
            @Mapping(target="locationImage", source="esnDto.imageLocation"),
    })
    Esn esnDTOtoEsn(EsnDto esnDto);
}
