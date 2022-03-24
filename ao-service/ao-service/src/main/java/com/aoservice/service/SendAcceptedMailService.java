package com.aoservice.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class SendAcceptedMailService implements JavaDelegate {
    public void execute(DelegateExecution execution) {
        System.out.println("Sending accepted mail to the owner of accepted candidature.");
    }
}
