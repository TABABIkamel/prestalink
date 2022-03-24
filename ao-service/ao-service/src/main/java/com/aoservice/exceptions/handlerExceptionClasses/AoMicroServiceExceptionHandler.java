package com.aoservice.exceptions.handlerExceptionClasses;

import com.aoservice.exceptions.coreExceptionClasses.CoreException;
import com.aoservice.exceptions.exceptionClasses.AppelOffreNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.annotation.Priority;
import java.util.Date;

@ControllerAdvice
public class AoMicroServiceExceptionHandler {
    @ExceptionHandler(value = {AppelOffreNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> HandlerUserException(AppelOffreNotFoundException appelOffreNotFoundException, WebRequest webRequest){
        CoreException coreException = new CoreException(new Date(), appelOffreNotFoundException.getMessage());
        return new ResponseEntity<>(coreException,new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<Object> HandlerUnknownException(Exception exception, WebRequest webRequest){
        CoreException coreException = new CoreException(new Date(),exception.getMessage());
        return new ResponseEntity<>(coreException,new HttpHeaders(), HttpStatus.NOT_FOUND);
    }
}
