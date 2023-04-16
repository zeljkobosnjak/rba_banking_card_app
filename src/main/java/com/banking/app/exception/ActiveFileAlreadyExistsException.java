package com.banking.app.exception;

public class ActiveFileAlreadyExistsException extends RuntimeException{

    public ActiveFileAlreadyExistsException(String oib){
        super("Aktivna datoteka za osobu s OIB-om " + oib +" vec postoji.");
    }
}
