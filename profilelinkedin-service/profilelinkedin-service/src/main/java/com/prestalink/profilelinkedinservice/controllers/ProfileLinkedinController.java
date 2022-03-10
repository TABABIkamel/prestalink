package com.prestalink.profilelinkedinservice.controllers;


import com.prestalink.profilelinkedinservice.documents.ProfilLinkedin;
import com.prestalink.profilelinkedinservice.repositories.ProfileLinkedinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/profilelinkedin")
public class ProfileLinkedinController {
    @Autowired
    ProfileLinkedinRepository profileLinkedinRepository;
    @PostMapping (value = "/create")
    public ResponseEntity<ProfilLinkedin> createProfileLinkedin(@RequestBody ProfilLinkedin profileLinkedIn){
        return ResponseEntity.ok(profileLinkedinRepository.save(profileLinkedIn));
    }
    @GetMapping(value = "/allProfiles")
    public List<ProfilLinkedin> getAllProfile(){
        return profileLinkedinRepository.findAll();
    }
}
