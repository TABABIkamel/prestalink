package com.profilsservice.controllers;

import com.profilsservice.documents.ProfilLinkein;
import com.profilsservice.dto.ProfilLinkeinDto;
import com.profilsservice.repositories.ProfileLinkedinRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/profilelinkedin")
public class ProfileLinkedinController {
    @Autowired
    ProfileLinkedinRepository profileLinkedinRepository;
    @PostMapping (value = "/create")
    public ResponseEntity<ProfilLinkein> createProfileLinkedin(@RequestBody ProfilLinkein profileLinkedIn){
        return ResponseEntity.ok(profileLinkedinRepository.save(profileLinkedIn));
    }
    @GetMapping(value = "/allProfiles")
    public List<ProfilLinkein> getAllProfile(){
        return profileLinkedinRepository.findAll();
    }
}
