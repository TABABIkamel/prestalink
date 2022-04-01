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

    private PrestataireMapper mapperPrestataire = Mappers.getMapper(PrestataireMapper.class);
    private EsnMapper mapperEsn = Mappers.getMapper(EsnMapper.class);

    @PostMapping(value = "/completeProfileEsn")
    @ResponseBody
    public ResponseEntity<EsnDto> CompleterProfilEsn(@RequestBody EsnDto esnDto, HttpServletRequest request) throws Exception {
        KeycloakAuthenticationToken token=(KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext keycloakSecurityContext=principal.getKeycloakSecurityContext();
        //
//        String fileName = esnDto.getFile().getOriginalFilename();
//        String prefix = fileName.substring(fileName.lastIndexOf("."));
//
//        File file1 = null;
//        try {
//
//            file1 = File.createTempFile(fileName, prefix);
//            esnDto.getFile().transferTo(file1);
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
        //
        Optional<Esn> esn = Optional.of(mapperEsn.esnDTOtoEsn(esnDto));
        if(esn.isPresent()){
            esn.get().setEsnUsernameRepresentant(keycloakSecurityContext.getToken().getPreferredUsername());
            esn.get().setEsnIsCompleted(true);
            esn.get().setEsnIsPrestataire(false);
            //esn.get().setLocationImage((String)uploadResult.get("url"));
            return new ResponseEntity<>(mapperEsn.esnToEsnDTO(esnRepository.save(esn.get())), HttpStatus.CREATED) ;
        }else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
    @PostMapping(value = "setPhotoToEsn/{username}")
    public void setPhotoToEsn(MultipartFile file,@PathVariable("username") String username) throws IOException {
        Esn esn=esnRepository.findByEsnUsernameRepresentant(username);


        String fileName = file.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        File file1 = null;
        try {

            file1 = File.createTempFile(fileName, prefix);
            file.transferTo(file1);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // After operating the above files, you need to delete the temporary files generated in the root directory
            File f = new File(file1.toURI());
            // f.delete();
        }
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dhum7apjy",
                "api_key", "265837847724928",
                "api_secret", "CVKzJr7cldr0au9oFSh6t3mGqzw"));
        //File file = new File("img1.png");
        Map uploadResult = cloudinary.uploader().upload(file1, ObjectUtils.emptyMap());
        System.out.println(uploadResult.get("url"));
        if(esn!=null){
            System.out.println("in if");
            esn.setLocationImage((String)uploadResult.get("url"));
            esnRepository.save(esn);
        }
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
