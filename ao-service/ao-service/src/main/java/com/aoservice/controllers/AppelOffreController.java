package com.aoservice.controllers;
import com.aoservice.entities.Esn;
import com.aoservice.entities.Prestataire;
import com.aoservice.repositories.EsnRepository;
import com.aoservice.repositories.PrestataireRepository;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import com.aoservice.configurationMapper.AppelOffreMapper;
import com.aoservice.dto.AppelOffreDto;
import com.aoservice.entities.AppelOffre;
import com.aoservice.exceptions.coreExceptionClasses.ErrorMessages;
import com.aoservice.exceptions.exceptionClasses.AppelOffreNotFoundException;
import com.aoservice.repositories.AppellOffreRepository;
import lombok.Data;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.mapstruct.factory.Mappers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@CrossOrigin(origins = "*")
@RestControllerAdvice
@RequestMapping("/api/ao")
public class AppelOffreController {
    @Autowired
    private AppellOffreRepository appellOffreRepository;
    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;
    private AppelOffreMapper mapper = Mappers.getMapper(AppelOffreMapper.class);

    @GetMapping(value = "/allAo")
    public ResponseEntity<List<AppelOffreDto>> getAllAo(){
        List<AppelOffre> allAppelOffre = appellOffreRepository.findAll();
        List<Optional<AppelOffre>> allAo = allAppelOffre.stream().map(o->Optional.of(o)).collect(Collectors.toList());
        if(allAo.isEmpty()){
            throw new AppelOffreNotFoundException(ErrorMessages.NO_APPELOFFRE_FOUND.getErrorMessage());
        }
        List<AppelOffreDto> appelOffresDto = allAppelOffre.stream().map(ao -> mapper.appelOffreToAppelOffreDTO(ao)).collect(Collectors.toList());
        return new ResponseEntity<>(appelOffresDto, HttpStatus.OK);
    }
    @GetMapping(value = "/getProfileAo")
    public ProfileLikedin[] getProfile(){
        ProfileLikedin[] pageProfiles =keycloakRestTemplate.getForObject("http://127.0.0.1:8085/api/profilelinkedin/allProfiles",ProfileLikedin[].class);
        return pageProfiles;
    }
    @ResponseBody
    @PostMapping(value = "/createAo")
    public ResponseEntity<AppelOffreDto> createAo(@RequestBody AppelOffreDto appelOffreDto) {
        Optional<AppelOffre> appelOffre = Optional.of(mapper.appelOffreDTOtoAppelOffre(appelOffreDto));
        AppelOffre appelOffreSaved = appellOffreRepository.save(appelOffre.get());
        return new ResponseEntity<AppelOffreDto>(mapper.appelOffreToAppelOffreDTO(appelOffreSaved),HttpStatus.CREATED);
    }
}
@Data
class ProfileLikedin {
    private String idProfile;
    private String profileTitle;
    private String location;
    private int nbrConnexion;
    private String nomProfile;
    private HashMap<String, String> experience;
    private HashMap<String, String> education;
}
