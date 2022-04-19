package com.aoservice;

import com.aoservice.dto.AppelOffreDto;
import com.aoservice.dto.ContratDto;
import com.aoservice.service.AppelOffreService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class AppelOffreTests {
    @Autowired
    AppelOffreService appelOffreService;

    @Test
    @Transactional
    public void getAllAoTest(){
        Assert.assertEquals(HttpStatus.OK,appelOffreService.getAllAo().getStatusCode());
    }
    @Test
    @Transactional
    public void getAoByUsernameEsnTest(){
        Assert.assertEquals(HttpStatus.OK,appelOffreService.getAoByUsernameEsn("inetum").getStatusCode());
        Assert.assertNotEquals(HttpStatus.OK,appelOffreService.getAoByUsernameEsn("foulen").getStatusCode());
    }
    @Test
    @Transactional
    public void generateContratTest(){
        ContratDto contratDto=new ContratDto();
        Assert.assertNotEquals(HttpStatus.OK,appelOffreService.generateContrat(contratDto,"inetum").getStatusCode());
    }
    @Test
    @Transactional
    public void createAoTest(){
        AppelOffreDto appelOffreDto=new AppelOffreDto();

        Assert.assertEquals(HttpStatus.CREATED,appelOffreService.createAo(appelOffreDto,"inetum").getStatusCode());
        Assert.assertNotEquals(HttpStatus.CREATED,appelOffreService.createAo(null,"inetum").getStatusCode());
    }
}
