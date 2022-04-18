package com.aoservice.service;

import com.aoservice.beans.AppelOffreBean;
import com.aoservice.configurationMapper.AppelOffreMapper;
import com.aoservice.dto.AppelOffreDto;
import com.aoservice.dto.ContratDto;
import com.aoservice.entities.AppelOffre;
import com.aoservice.entities.CandidatureFinished;
import com.aoservice.entities.Mission;
import com.aoservice.repositories.AppellOffreRepository;
import com.aoservice.repositories.CandidatureFinishedRepository;
import com.aoservice.repositories.MissionRepository;
import net.sf.jasperreports.engine.JasperPrint;
import org.keycloak.KeycloakSecurityContext;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppelOffreService {
    @Autowired
    AppellOffreRepository appellOffreRepository;
    @Autowired
    private CandidatureFinishedRepository candidatureFinishedRepository;
    @Autowired
    private AppelOffreBean appelOffreBean;
    @Autowired
    private MissionRepository missionRepository;
    public List<Optional<AppelOffre>> getAoByUsernameEsn(String username){
        return appellOffreRepository.getAoByUsernameEsn(username).stream().map(ao->Optional.ofNullable(ao)).collect(Collectors.toList());

    }
    public String generateContrat(ContratDto contrat, KeycloakSecurityContext keycloakSecurityContext){
        try {
            Optional<CandidatureFinished> candidatureFinished = Optional.ofNullable(candidatureFinishedRepository.findByIdTask(contrat.getIdCandidature()));
            Optional<AppelOffre> appelOffre = Optional.ofNullable(appellOffreRepository.findByRefAo(contrat.getRefAo()));
            JasperPrint jasper=appelOffreBean.generateContract(contrat,appelOffre);
            appelOffreBean.storeContratSousFs(keycloakSecurityContext,jasper,candidatureFinished,contrat);
            String contratUrl=appelOffreBean.uploadFileToCloudinary(keycloakSecurityContext,contrat);
            appelOffreBean.sendMailToInternautes(contratUrl,candidatureFinished,appelOffre);

            Optional<Mission> mission = Optional.ofNullable(missionRepository.getMissionByIdAppelOffre(appelOffre.get().getId()));
            appelOffreBean.createOrUpdateMission(mission,contratUrl,appelOffre);
            return "Done";
        } catch (Exception ex) {
            System.out.println(ex.getCause());
            return "Error";
        }
    }
}
