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
import com.aoservice.service.CompleteProfileService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequestMapping(value = "/api/ao")
public class CompleterProfileController {
    @Autowired
    private PrestataireRepository prestataireRepository;
    @Autowired
    EsnRepository esnRepository;
    @Autowired
    CompleteProfileService completeProfileService;

    private PrestataireMapper mapperPrestataire = Mappers.getMapper(PrestataireMapper.class);
    private EsnMapper mapperEsn = Mappers.getMapper(EsnMapper.class);

    @PostMapping(value = "/completeProfileEsn")
    @ResponseBody
    public ResponseEntity<EsnDto> CompleterProfilEsn(@RequestBody EsnDto esnDto, HttpServletRequest request) throws Exception {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext = principal.getKeycloakSecurityContext();
        String username = keycloakSecurityContext.getToken().getPreferredUsername();
        return completeProfileService.completeProfileEsn(esnDto, username);
    }

    @PostMapping(value = "setPhotoToEsn/{username}")
    public ResponseEntity<String> setPhotoToEsn(MultipartFile file, @PathVariable("username") String username) throws IOException {
        return completeProfileService.setPhotoToEsn(file, username);
//        Esn esn=esnRepository.findByEsnUsernameRepresentant(username);
//        String fileName = file.getOriginalFilename();
//        String prefix = fileName.substring(fileName.lastIndexOf("."));
//
//        File file1 = null;
//        try {
//
//            file1 = File.createTempFile(fileName, prefix);
//            file.transferTo(file1);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//            File f = new File(file1.toURI());
//        }
//        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", "dhum7apjy",
//                "api_key", "265837847724928",
//                "api_secret", "CVKzJr7cldr0au9oFSh6t3mGqzw"));
//        //File file = new File("img1.png");
//        Map uploadResult = cloudinary.uploader().upload(file1, ObjectUtils.emptyMap());
//        System.out.println(uploadResult.get("url"));
//        if(esn!=null){
//            System.out.println("in if");
//            esn.setLocationImage((String)uploadResult.get("url"));
//            esnRepository.save(esn);
//        }
    }

    @PostMapping(value = "setPhotoToPrestataire/{username}")
    public ResponseEntity<String> setPhotoToPrestataire(MultipartFile file, @PathVariable("username") String username) throws IOException {
        return completeProfileService.setPhotoToPrestataire(file,username);
//        Prestataire prestataire = prestataireRepository.findByPrestataireUsername(username);
//        String fileName = file.getOriginalFilename();
//        String prefix = fileName.substring(fileName.lastIndexOf("."));
//
//        File file1 = null;
//        try {
//
//            file1 = File.createTempFile(fileName, prefix);
//            file.transferTo(file1);
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        } finally {
//            // After operating the above files, you need to delete the temporary files generated in the root directory
//            File f = new File(file1.toURI());
//            // f.delete();
//        }
//        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
//                "cloud_name", "dhum7apjy",
//                "api_key", "265837847724928",
//                "api_secret", "CVKzJr7cldr0au9oFSh6t3mGqzw"));
//        //File file = new File("img1.png");
//        Map uploadResult = cloudinary.uploader().upload(file1, ObjectUtils.emptyMap());
//        System.out.println(uploadResult.get("url"));
//        if (prestataire != null) {
//            System.out.println("in if");
//            prestataire.setLocationImage((String) uploadResult.get("url"));
//            prestataireRepository.save(prestataire);
//        }
    }

    @GetMapping(value = "/checkIfProfileEsnCompleted/{username}")
    public ResponseEntity<EsnDto> checkIfProfileEsnCompleted(@PathVariable("username") String usename) {
            return completeProfileService.checkIfProfileEsnCompleted(usename);
//        Esn esn = esnRepository.findByEsnUsernameRepresentant(usename);
//        if (esn != null)
//            return new ResponseEntity<>(mapperEsn.esnToEsnDTO(esn), HttpStatus.OK);
//        else
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }

    @GetMapping(value = "/checkIfProfilePrestataireCompleted/{username}")
    public ResponseEntity<PrestataireDto> checkIfProfilePrestataireCompleted(@PathVariable("username") String usename) {
            return completeProfileService.checkIfProfilePrestataireCompleted(usename);
//        Prestataire prestataire = prestataireRepository.findByPrestataireUsername(usename);
//        if (prestataire != null)
//            return new ResponseEntity<>(mapperPrestataire.prestataireToPrestataireDTO(prestataire), HttpStatus.OK);
//        else
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/completeProfilePrestataire")
    @ResponseBody
    public ResponseEntity<PrestataireDto> CompleterProfilPrestataire(@RequestBody PrestataireDto prestataireDto) {
        return completeProfileService.CompleterProfilPrestataire(prestataireDto);
    }
}
