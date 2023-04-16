package com.banking.app.exception;

public class PersonAlreadyExistsException extends RuntimeException {

    public PersonAlreadyExistsException(String oib) {
        super("Osoba s OIB-om " + oib + " vec postoji.");
    }
}
