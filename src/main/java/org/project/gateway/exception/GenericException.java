package org.project.gateway.exception;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericException extends Exception {

    private final String message;
    private final String description;


    public GenericException(String message, String description) {
        this.message = message;
        this.description = description;
    }
}
