package com.banking.app.response;

import com.banking.app.model.Person;
import com.banking.app.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonResponse {
    private String ime;
    private String prezime;
    private String OIB;
    private Status status;
    private LocalDateTime cardRequestTimestamp;
    private String filePath;
    private String fileName;

    public static PersonResponse toPersonResponse(Person person) {
        PersonResponse personResponse = new PersonResponse();
        personResponse.setIme(person.getIme());
        personResponse.setPrezime(person.getPrezime());
        personResponse.setOIB(person.getOIB());
        personResponse.setStatus(person.getStatus());
        personResponse.setCardRequestTimestamp(person.getCardRequestTimestamp());
        personResponse.setFileName(person.getFileName());
        personResponse.setFilePath(person.getFilePath());
        return personResponse;
    }
}
