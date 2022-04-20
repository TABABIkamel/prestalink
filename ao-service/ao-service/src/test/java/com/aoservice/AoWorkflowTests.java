package com.aoservice;

import com.aoservice.entities.Approval;
import com.aoservice.entities.Candidature;
import com.aoservice.service.AoWorkflowService;
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
public class AoWorkflowTests {
    @Autowired
    AoWorkflowService aoWorkflowService;

    @Test
    @Transactional
    public void postulerTest(){
        Candidature candidature=new Candidature();
        candidature.setIdPost(8L);
        candidature.setUsername("kamel");
        Assert.assertEquals(HttpStatus.OK,aoWorkflowService.postuler(candidature).getStatusCode());
    }

    @Test
    @Transactional
    public void getTasksTest(){
        Assert.assertEquals(HttpStatus.OK,aoWorkflowService.getTasks("inetum").getStatusCode());
    }

    @Test
    @Transactional
    public void submitReviewTest(){
        Approval approval=new Approval("b68a863e-bfb5-11ec-9619-588a5a210df0",true);
        Assert.assertEquals(HttpStatus.OK,aoWorkflowService.submitReview(approval).getStatusCode());
    }

    @Test
    @Transactional
    public void getFinishedTaskTest(){
        Assert.assertEquals(HttpStatus.OK,aoWorkflowService.getFinishedTask("inetum").getStatusCode());
    }
}
