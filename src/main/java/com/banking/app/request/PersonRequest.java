package com.banking.app.request;

import com.banking.app.model.Person;
import com.banking.app.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequest {
    private String ime;
    private String prezime;
    private String OIB;
    private Status status;

    public static Person toPerson(PersonRequest personRequest) {
        Person person = new Person();
        person.setIme(personRequest.getIme());
        person.setPrezime(personRequest.getPrezime());
        person.setOIB(personRequest.getOIB());
        person.setStatus(personRequest.getStatus());
        return person;
    }
}
