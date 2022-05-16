package com.aoservice.beans;

import com.aoservice.dto.ContratDto;
import com.aoservice.entities.*;
import com.aoservice.repositories.*;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
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
import org.apache.log4j.Logger;

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
    public static final Logger LOGGER = Logger.getLogger(AppelOffreBean.class);

    public void sendMail(String emailReceiver, Map<String, Object> model,String objet,String templateName/*String name, String nameInternaute, String titreAo, String urlContract*/) {
        var message = sender.createMimeMessage();
        MimeMessageHelper helper =null;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
        } catch (MessagingException e) {
            LOGGER.info( e.getMessage());
            /*e.printStackTrace();*/
        }
//        Map<String, Object> model = new HashMap<>();
//        model.put("name",name);
//        model.put("nameInternaute",nameInternaute);
//        model.put("titreAo",titreAo);
//        model.put("urlContract",urlContract);

        var context = new Context();
        context.setVariables(model);
        String html = templateEngine.process(templateName/*"send-contract-template"*/, context);
        try {
            helper.setTo(emailReceiver);
            helper.setSubject(objet/*"CONTRAT"*/);
            helper.setText(html,true);
        } catch (javax.mail.MessagingException e) {
            /*e.printStackTrace();*/
            LOGGER.info( e.getMessage());
        }
        sender.send(message);

    }

    public JasperPrint generateContract(ContratDto contrat, Optional<AppelOffre> appelOffre) throws FileNotFoundException, JRException {
        var jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Arrays.asList(new Approval(false)));
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
        var dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        var now = LocalDateTime.now();
        map.put("dateGenerationContrat", dtf.format(now));
        if (appelOffre.isPresent()) {
            map.put("dateDebut", appelOffre.get().getDateDebutAo().toString());
            map.put("dateFin", appelOffre.get().getDateFinAo().toString());
           /* System.out.println(appelOffre.get().getDateFinAo().getTime() - appelOffre.get().getDateDebutAo().getTime());*/
            long resultat = appelOffre.get().getDateFinAo().getTime() - appelOffre.get().getDateDebutAo().getTime();
            String duree = resultat + " jours";
            map.put("duree", duree);
        }
        return JasperFillManager.fillReport(compile, map, jrBeanCollectionDataSource);


    }
    public void storeContratSousFs(String givenName,JasperPrint jasper,Optional<CandidatureFinished> candidatureFinished,ContratDto contrat) throws JRException {
        var path="C:/prestalink/";
        var path1="/contract_";
        var outDir = new File(path + givenName);
        outDir.mkdirs();
            JasperExportManager.exportReportToPdfFile(jasper,
                    path+ givenName + path1 + contrat.getRefAo() + "_" + contrat.getNomPrestataire() + "_" + contrat.getPrenomPrestataire() + ".pdf");
            var contratFile = FileUtils.getFile(path + givenName + path1 + contrat.getRefAo() + "_" + contrat.getNomPrestataire() + "_" + contrat.getPrenomPrestataire() + ".pdf");

            /*System.out.println("Done!");*/
            if(candidatureFinished.isPresent()){
                candidatureFinished.get().setHasContract(true);
                candidatureFinishedRepository.save(candidatureFinished.get());
            }
    }

    public String uploadFileToCloudinary(String givenName,ContratDto contrat) throws IOException {
        var path="C:/prestalink/";
        var path1="/contract_";
        var contratFile = FileUtils.getFile(path + givenName + path1 + contrat.getRefAo() + "_" + contrat.getNomPrestataire() + "_" + contrat.getPrenomPrestataire() + ".pdf");
        var cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dhum7apjy",
                "api_key", "265837847724928",
                "api_secret", "CVKzJr7cldr0au9oFSh6t3mGqzw"));
        Map uploadResult = cloudinary.uploader().upload(contratFile, ObjectUtils.emptyMap());
       /* System.out.println(uploadResult.get("url"));*/
        return uploadResult.get("url").toString();
    }
    public void sendMailToInternautes(String contratUrl,Optional<CandidatureFinished> candidatureFinished,Optional<AppelOffre> appelOffre){
        if(candidatureFinished.isPresent()) {
            Optional<Prestataire> prestataire = Optional.ofNullable(prestataireRepository.findByPrestataireUsername(candidatureFinished.get().getUsername()));
            if (prestataire.isPresent()&&appelOffre.isPresent()) {
                Map<String, Object> modelForPrestataire = new HashMap<>();
                modelForPrestataire.put("name",prestataire.get().getPrestataireNom());
                modelForPrestataire.put("nameInternaute",appelOffre.get().getEsn().getEsnnom());
                modelForPrestataire.put("titreAo",appelOffre.get().getTitreAo());
                modelForPrestataire.put("urlContract",contratUrl);
                this.sendMail(prestataire.get().getPrestataireEmail(),modelForPrestataire /*prestataire.get().getPrestataireNom(), appelOffre.get().getEsn().getEsnnom(), appelOffre.get().getTitreAo(), contratUrl*/,"CONTRAT","send-contract-template");
                Map<String, Object> modelForEsn = new HashMap<>();
                modelForEsn.put("name",appelOffre.get().getEsn().getEsnnom());
                modelForEsn.put("nameInternaute",prestataire.get().getPrestataireNom());
                modelForEsn.put("titreAo",appelOffre.get().getTitreAo());
                modelForEsn.put("urlContract",contratUrl);
                this.sendMail(appelOffre.get().getEsn().getEsnEmail(),modelForEsn/* appelOffre.get().getEsn().getEsnnom(), prestataire.get().getPrestataireNom(), appelOffre.get().getTitreAo(), contratUrl*/,"CONTRAT","send-contract-template");

            } else {
                Optional<Esn> esn = Optional.ofNullable(esnRepository.findByEsnUsernameRepresentant(candidatureFinished.get().getUsername()));
                if(esn.isPresent() && appelOffre.isPresent()) {
                    Map<String, Object> modelForEsnAsPrestataire = new HashMap<>();
                    modelForEsnAsPrestataire.put("name",esn.get().getEsnnom());
                    modelForEsnAsPrestataire.put("nameInternaute",appelOffre.get().getEsn().getEsnnom());
                    modelForEsnAsPrestataire.put("titreAo",appelOffre.get().getTitreAo());
                    modelForEsnAsPrestataire.put("urlContract",contratUrl);
                    this.sendMail(esn.get().getEsnEmail(),modelForEsnAsPrestataire/* esn.get().getEsnnom(), appelOffre.get().getEsn().getEsnnom(), appelOffre.get().getTitreAo(), contratUrl*/,"CONTRAT","send-contract-template");
                    Map<String, Object> modelForEsn2 = new HashMap<>();
                    modelForEsn2.put("name",appelOffre.get().getEsn().getEsnnom());
                    modelForEsn2.put("nameInternaute",esn.get().getEsnnom());
                    modelForEsn2.put("titreAo",appelOffre.get().getTitreAo());
                    modelForEsn2.put("urlContract",contratUrl);
                    this.sendMail(appelOffre.get().getEsn().getEsnEmail(),modelForEsn2 /*appelOffre.get().getEsn().getEsnnom(), esn.get().getEsnnom(), appelOffre.get().getTitreAo(), contratUrl*/,"CONTRAT","send-contract-template");
                }
            }
        }
    }
    public void createMission(String contratUrl,Optional<AppelOffre> appelOffre,Optional<Prestataire> prestataire,Optional<Esn> esn){
//        if (mission.isPresent()) {
//            UrlContract urlContract = new UrlContract();
//            urlContract.setUrlContrat(contratUrl);
//            urlContract.setMission(mission.get());
//            urlContractRepository.save(urlContract);
//        } else {

                var newMission = new Mission();
                var newMissionSaved = missionRepository.save(newMission);
            if(appelOffre.isPresent() && prestataire.isPresent()) {
                newMissionSaved.setAppelOffre(appelOffre.get());
                newMissionSaved.setUsernamePrestataire(prestataire.get().getPrestataireUsername());
            }
        if(appelOffre.isPresent() && esn.isPresent()) {
            newMissionSaved.setAppelOffre(appelOffre.get());
            newMissionSaved.setUsernamePrestataire(esn.get().getEsnUsernameRepresentant());
        }
            missionRepository.save(newMissionSaved);
            var urlContract = new UrlContract();
            urlContract.setUrlContrat(contratUrl);
            urlContract.setMission(newMissionSaved);
            urlContractRepository.save(urlContract);
       // }
    }
}
