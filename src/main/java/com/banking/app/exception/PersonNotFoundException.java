package com.banking.app.exception;

import com.banking.app.model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PersonNotFoundException extends RuntimeException{

    public PersonNotFoundException(String message){
        super(message);
    }
}
