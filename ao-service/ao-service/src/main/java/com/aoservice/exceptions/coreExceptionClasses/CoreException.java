package com.aoservice.exceptions.coreExceptionClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class CoreException {
    private Date timestamp;
    private String message;

    public CoreException(Date timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }
}
