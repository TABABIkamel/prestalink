package com.aoservice.service;

import com.aoservice.entities.Esn;
import com.aoservice.entities.Prestataire;
import com.aoservice.repositories.EsnRepository;
import com.aoservice.repositories.PrestataireRepository;
import org.apache.log4j.Logger;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
@Service("sendAcceptedMailService")
public class SendAcceptedMailService implements JavaDelegate {
    private static PrestataireRepository prestataireRepository;
    private static SpringTemplateEngine templateEngine;

    private static EsnRepository esnRepository;

    private static JavaMailSender sender;
    public static final Logger LOGGER = Logger.getLogger(SendAcceptedMailService.class);
    @Autowired
    public void setPrestataireRepository(PrestataireRepository prestataireRepository) {
        this.prestataireRepository = prestataireRepository;
    }
    @Autowired
    public void setTemplateEngine(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Autowired
    public void setEsnRepository(EsnRepository esnRepository) {
        this.esnRepository = esnRepository;
    }
    @Autowired
    public void setSender(JavaMailSender sender) {
        this.sender = sender;
    }


    public void execute(DelegateExecution execution) {
        String username = (String) execution.getVariable("username");
        String titreAo = (String) execution.getVariable("titrAo");
        String nomCandidat = (String) execution.getVariable("Nom Candidat");
        String esnUsername = (String) execution.getVariable("esn");
        Esn esnHasPostedAo=esnRepository.findByEsnUsernameRepresentant(esnUsername);
        String nameEsnHasPostedAo=esnHasPostedAo.getEsnnom();
        Prestataire prestataire=prestataireRepository.findByPrestataireUsername(username);
        var message = sender.createMimeMessage();
        MimeMessageHelper helper = null;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
        } catch (MessagingException e) {
            LOGGER.info(e.getMessage());
        }

        Map<String, Object> model = new HashMap<>();
        model.put("nameCandidat",nomCandidat);
        model.put("titreAo",titreAo);
        model.put("nameEsnHasPostedAo",nameEsnHasPostedAo);

        var context = new Context();
        context.setVariables(model);
        String html = templateEngine.process("acceptation-email-template", context);

        try {
            if(prestataire!=null){
                helper.setTo(prestataire.getPrestataireEmail());
            }else{
                Esn esn=esnRepository.findByEsnUsernameRepresentant(username);
                helper.setTo(esn.getEsnEmail());
            }

            helper.setText(html,true);
            helper.setSubject("acceptation de candidature");
        } catch (javax.mail.MessagingException e) {
            LOGGER.info(e.getMessage());
        }
        sender.send(message);
    }


}
