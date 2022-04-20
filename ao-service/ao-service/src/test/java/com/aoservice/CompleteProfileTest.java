package com.aoservice;

import com.aoservice.dto.EsnDto;
import com.aoservice.dto.PrestataireDto;
import com.aoservice.service.CompleteProfileService;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class CompleteProfileTest {
    @Autowired
    CompleteProfileService completeProfileService;

    @Test
    @Transactional
    public void completeProfileEsnTest(){
        EsnDto esnDto=new EsnDto(1L,"LG","1111","TN4523","tunis","lg@lg.com","",false,false);
        Assert.assertEquals(HttpStatus.CREATED,completeProfileService.completeProfileEsn(esnDto,"lg").getStatusCode());
        Assert.assertNotEquals(HttpStatus.BAD_REQUEST,completeProfileService.completeProfileEsn(esnDto,"lg").getStatusCode());
    }
    @Test
    @Transactional
    public void setPhotoToEsnTest() throws IOException {
        File file = new File("src/main/resources/photo.png");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "image/png", IOUtils.toByteArray(input));
        Assert.assertEquals(HttpStatus.OK,completeProfileService.setPhotoToEsn(multipartFile,"appel").getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST,completeProfileService.setPhotoToEsn(multipartFile,"FauxUsername").getStatusCode());
    }
    @Test
    @Transactional
    public void setPhotoToPrestataireTest() throws IOException {
        File file = new File("src/main/resources/photo.png");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "image/png", IOUtils.toByteArray(input));
        Assert.assertEquals(HttpStatus.OK,completeProfileService.setPhotoToPrestataire(multipartFile,"kamel").getStatusCode());
        Assert.assertEquals(HttpStatus.BAD_REQUEST,completeProfileService.setPhotoToPrestataire(multipartFile,"FauxUsername").getStatusCode());
    }
    @Test
    @Transactional
    public void checkIfProfileEsnCompletedTest(){
        Assert.assertEquals(HttpStatus.OK,completeProfileService.checkIfProfileEsnCompleted("inetum").getStatusCode());
        Assert.assertEquals(HttpStatus.NOT_FOUND,completeProfileService.checkIfProfileEsnCompleted("FauxUsername").getStatusCode());
    }
    @Test
    @Transactional
    public void checkIfProfilePrestataireCompletedTest(){
        Assert.assertEquals(HttpStatus.OK,completeProfileService.checkIfProfilePrestataireCompleted("kamel").getStatusCode());
        Assert.assertEquals(HttpStatus.NOT_FOUND,completeProfileService.checkIfProfilePrestataireCompleted("FauxUsername").getStatusCode());
    }
    @Test
    @Transactional
    public void CompleterProfilPrestataireTest(){
        PrestataireDto prestataireDto=new PrestataireDto();
        Assert.assertEquals(HttpStatus.CREATED,completeProfileService.CompleterProfilPrestataire(prestataireDto).getStatusCode());

    }
}
