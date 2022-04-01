package com.aoservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
//@ConfigurationProperties("mail-adress")
public class PropertiesConfiguration {
    private String adressMail;

    public String getAdressMail() {
        return adressMail;
    }

    public void setAdressMail(String adressMail) {
        this.adressMail = adressMail;
    }
@Autowired
    public PropertiesConfiguration(@Value("${mail-adress.adressMail}")String adressMail) {
         System.out.println("in constructor");
         this.adressMail = adressMail;
         System.out.println(adressMail);

    }
}
