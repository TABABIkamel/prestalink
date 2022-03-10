package com.aoservice.controllers;

import com.aoservice.entities.AppelOffre;
import com.aoservice.repositories.AppellOffreRepository;
import lombok.Data;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/ao")
public class AppelOffreController {
    @Autowired
    private AppellOffreRepository appellOffreRepository;
    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;
    @GetMapping(value = "/allAo")
    public List<AppelOffre> getAllAo(){
        return appellOffreRepository.findAll();
    }
    @GetMapping(value = "/getProfileAo")
    public ProfileLikedin[] getProfile(){
        ProfileLikedin[] pageProfiles =keycloakRestTemplate.getForObject("http://127.0.0.1:8085/api/profilelinkedin/allProfiles",ProfileLikedin[].class);
        return pageProfiles;
    }

}
@Data
class ProfileLikedin{
    private String idProfile;
    private String profileTitle;
    private String location;
    private int nbrConnexion;
    private String nomProfile;
    private HashMap<String,String> experience;
    private HashMap<String,String> education;
}
