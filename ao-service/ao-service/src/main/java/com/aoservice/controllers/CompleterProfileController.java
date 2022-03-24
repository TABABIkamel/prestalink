package com.aoservice.controllers;

import com.aoservice.configurationMapper.AppelOffreMapper;
import com.aoservice.configurationMapper.EsnMapper;
import com.aoservice.configurationMapper.PrestataireMapper;
import com.aoservice.dto.EsnDto;
import com.aoservice.dto.PrestataireDto;
import com.aoservice.entities.Education;
import com.aoservice.entities.Esn;
import com.aoservice.entities.Experience;
import com.aoservice.entities.Prestataire;
import com.aoservice.exceptions.coreExceptionClasses.ErrorMessages;
import com.aoservice.repositories.EsnRepository;
import com.aoservice.repositories.PrestataireRepository;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestControllerAdvice
@RequestMapping(value = "/api/ao")
public class CompleterProfileController {
    @Autowired
    private PrestataireRepository prestataireRepository;
    @Autowired
    EsnRepository esnRepository;

    private PrestataireMapper mapperPrestataire = Mappers.getMapper(PrestataireMapper.class);
    private EsnMapper mapperEsn = Mappers.getMapper(EsnMapper.class);

    @PostMapping(value = "/completeProfileEsn")
    @ResponseBody
    public ResponseEntity<EsnDto> CompleterProfilEsn(@RequestBody EsnDto esnDto, HttpServletRequest request) throws Exception {
        KeycloakAuthenticationToken token=(KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext=principal.getKeycloakSecurityContext();
        Optional<Esn> esn = Optional.of(mapperEsn.esnDTOtoEsn(esnDto));
        if(esn.isPresent()){
            esn.get().setEsnUsernameRepresentant(keycloakSecurityContext.getToken().getPreferredUsername());
            esn.get().setEsnIsCompleted(true);
            esn.get().setEsnIsPrestataire(false);
            return new ResponseEntity<>(mapperEsn.esnToEsnDTO(esnRepository.save(esn.get())), HttpStatus.CREATED) ;
        }else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
    @GetMapping(value = "/checkIfProfileEsnCompleted/{username}")
    public ResponseEntity<EsnDto> checkIfProfileEsnCompleted(@PathVariable("username") String usename){
        Esn esn =esnRepository.findByEsnUsernameRepresentant(usename);
        if(esn!=null)
        return new ResponseEntity<>(mapperEsn.esnToEsnDTO(esn),HttpStatus.OK) ;
        else
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);

    }
    @GetMapping(value = "/checkIfProfilePrestataireCompleted/{username}")
    public ResponseEntity<PrestataireDto> checkIfProfilePrestataireCompleted(@PathVariable("username") String usename){
        Prestataire prestataire = prestataireRepository.findByPrestataireUsername(usename);
        if (prestataire!=null)
        return new ResponseEntity<>(mapperPrestataire.prestataireToPrestataireDTO(prestataire),HttpStatus.OK) ;
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/completeProfilePrestataire")
    @ResponseBody
    public ResponseEntity<PrestataireDto> CompleterProfilPrestataire(@RequestBody PrestataireDto prestataireDto) throws Exception {
        Prestataire prestataire = mapperPrestataire.prestataireDTOToPrestataire(prestataireDto);
        List<Education> educations=prestataireDto.getEducation().stream().map(educationDto->mapperPrestataire.educationDtoToEducation(educationDto)).collect(Collectors.toList());
        List<Experience> experiences=prestataireDto.getExperience().stream().map(experienceDto -> mapperPrestataire.experienceDtoToExperience(experienceDto)).collect(Collectors.toList());
        if(prestataire!=null){
            if(prestataireRepository.findByPrestataireUsername(prestataire.getPrestataireUsername())!=null){
                return new ResponseEntity<>(HttpStatus.FOUND);
            }
            prestataire.setPrestataireIsCompleted(true);

            for(Education education:educations)
                education.setPrestataire(prestataire);

            for (Experience experience:experiences)
                experience.setPrestataire(prestataire);
            prestataire.setPrestataireEducation(educations);
            prestataire.setPrestataireExperience(experiences);
            prestataireRepository.save(prestataire);
            return new ResponseEntity<>(prestataireDto, HttpStatus.CREATED) ;
        }else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
