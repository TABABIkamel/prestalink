package com.aoservice.beans;

import com.aoservice.dto.ContratDto;
import com.aoservice.entities.*;
import com.aoservice.repositories.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.FileUtils;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class AppelOffreBean {
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private CandidatureFinishedRepository candidatureFinishedRepository;
    @Autowired
    PrestataireRepository prestataireRepository;
    @Autowired
    private EsnRepository esnRepository;
    @Autowired
    private MissionRepository missionRepository;
    @Autowired
    private UrlContractRepository urlContractRepository;
    public AppelOffreBean() {
    }

    public void sendMail(String emailReceiver,String name,String nameInternaute,String titreAo,String urlContract) {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("name",name);
        model.put("nameInternaute",nameInternaute);
        model.put("titreAo",titreAo);
        model.put("urlContract",urlContract);

        Context context = new Context();
        context.setVariables(model);
        String html = templateEngine.process("send-contract-template", context);
        try {
            helper.setTo(emailReceiver);
            helper.setSubject("CONTRAT");
            helper.setText(html,true);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
        }
        sender.send(message);

    }

    public JasperPrint generateContract(ContratDto contrat, Optional<AppelOffre> appelOffre) throws FileNotFoundException, JRException {
        JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Arrays.asList(new Approval(false)));
        JasperReport compile = JasperCompileManager.compileReport(new FileInputStream("src/main/resources/jaspertest.jrxml"));
        Map<String, Object> map = new HashMap<>();
        map.put("title", "jasper test wiw");
        map.put("minSalary", 1000);
        map.put("condition", "condition");
        map.put("nom", contrat.getNomSocieteClient());
        map.put("lieu", contrat.getLieuSiegeClient());
        map.put("capital", contrat.getCapitaleSocieteClient());
        map.put("nomRepresentantSociete", contrat.getNomRepresentantSocieteClient());
        map.put("numeroRegitre", contrat.getNumeroRegitreCommerceClient());
        map.put("nomPrestataire", contrat.getNomPrestataire());
        map.put("prenomPrestataire", contrat.getPrenomPrestataire());
        map.put("cin", contrat.getCin());
        map.put("lieuPrestataire", contrat.getLieuPrestataire());
        map.put("prixTotaleMission", contrat.getPrixTotaleMission());
        map.put("preambule", contrat.getPreambule());
        map.put("penalisationParJour", contrat.getPenalisationParJour());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
        map.put("dateGenerationContrat", dtf.format(now));
        if (appelOffre.isPresent()) {
            map.put("dateDebut", appelOffre.get().getDateDebutAo().toString());
            map.put("dateFin", appelOffre.get().getDateFinAo().toString());
            System.out.println(appelOffre.get().getDateFinAo().getTime() - appelOffre.get().getDateDebutAo().getTime());
            long resultat = appelOffre.get().getDateFinAo().getTime() - appelOffre.get().getDateDebutAo().getTime();
            String duree = resultat + " jours";
            map.put("duree", duree);
        }
        JasperPrint jasper = JasperFillManager.fillReport(compile, map, jrBeanCollectionDataSource);
        return jasper;

    }
    public void storeContratSousFs(KeycloakSecurityContext keycloakSecurityContext,JasperPrint jasper,Optional<CandidatureFinished> candidatureFinished,ContratDto contrat) throws JRException {

        File outDir = new File("C:/prestalink/" + keycloakSecurityContext.getToken().getGivenName());
        outDir.mkdirs();
            JasperExportManager.exportReportToPdfFile(jasper,
                    "C:/prestalink/" + keycloakSecurityContext.getToken().getGivenName() + "/contract_" + contrat.getRefAo() + "_" + contrat.getNomPrestataire() + "_" + contrat.getPrenomPrestataire() + ".pdf");
            File contratFile = FileUtils.getFile("C:/prestalink/" + keycloakSecurityContext.getToken().getGivenName() + "/contract_" + contrat.getRefAo() + "_" + contrat.getNomPrestataire() + "_" + contrat.getPrenomPrestataire() + ".pdf");

            System.out.println("Done!");
            if(candidatureFinished.isPresent()){
                candidatureFinished.get().setHasContract(true);
                candidatureFinishedRepository.save(candidatureFinished.get());
            }
    }

    public String uploadFileToCloudinary(KeycloakSecurityContext keycloakSecurityContext,ContratDto contrat) throws IOException {
        File contratFile = FileUtils.getFile("C:/prestalink/" + keycloakSecurityContext.getToken().getGivenName() + "/contract_" + contrat.getRefAo() + "_" + contrat.getNomPrestataire() + "_" + contrat.getPrenomPrestataire() + ".pdf");
        Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dhum7apjy",
                "api_key", "265837847724928",
                "api_secret", "CVKzJr7cldr0au9oFSh6t3mGqzw"));
        Map uploadResult = cloudinary.uploader().upload(contratFile, ObjectUtils.emptyMap());
        System.out.println(uploadResult.get("url"));
        return uploadResult.get("url").toString();
    }
    public void sendMailToInternautes(String contratUrl,Optional<CandidatureFinished> candidatureFinished,Optional<AppelOffre> appelOffre){
        if(candidatureFinished.isPresent()) {
            Optional<Prestataire> prestataire = Optional.ofNullable(prestataireRepository.findByPrestataireUsername(candidatureFinished.get().getUsername()));
            if (prestataire.isPresent()) {
                this.sendMail(prestataire.get().getPrestataireEmail(), prestataire.get().getPrestataireNom(), appelOffre.get().getEsn().getEsnnom(), appelOffre.get().getTitreAo(), contratUrl);
                this.sendMail(appelOffre.get().getEsn().getEsnEmail(), appelOffre.get().getEsn().getEsnnom(), prestataire.get().getPrestataireNom(), appelOffre.get().getTitreAo(), contratUrl);

            } else {
                Optional<Esn> esn = Optional.ofNullable(esnRepository.findByEsnUsernameRepresentant(candidatureFinished.get().getUsername()));
                this.sendMail(esn.get().getEsnEmail(), esn.get().getEsnnom(), appelOffre.get().getEsn().getEsnnom(), appelOffre.get().getTitreAo(), contratUrl);
                this.sendMail(appelOffre.get().getEsn().getEsnEmail(), appelOffre.get().getEsn().getEsnnom(), esn.get().getEsnnom(), appelOffre.get().getTitreAo(), contratUrl);

            }
        }
    }
    public void createOrUpdateMission(Optional<Mission> mission,String contratUrl,Optional<AppelOffre> appelOffre){
        if (mission.isPresent()) {
            UrlContract urlContract = new UrlContract();
            urlContract.setUrlContrat(contratUrl);
            urlContract.setMission(mission.get());
            urlContractRepository.save(urlContract);
        } else {
            Mission newMission = new Mission();
            Mission newMissionSaved = missionRepository.save(newMission);
            newMissionSaved.setAppelOffre(appelOffre.get());
            missionRepository.save(newMissionSaved);
            UrlContract urlContract = new UrlContract();
            urlContract.setUrlContrat(contratUrl);
            urlContract.setMission(newMissionSaved);
            urlContractRepository.save(urlContract);
        }
    }
}
