package com.banking.app.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "person")
@Getter
@Setter
public class Person {

    @Id
    @Column(name = "oib", nullable = false)
    private String OIB;

    @Column(name = "ime", nullable = false)
    private String ime;

    @Column(name = "prezime", nullable = false)
    private String prezime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime cardRequestTimestamp;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(OIB, person.OIB) && Objects.equals(ime, person.ime) && Objects.equals(prezime, person.prezime) && status == person.status && Objects.equals(cardRequestTimestamp, person.cardRequestTimestamp) && Objects.equals(filePath, person.filePath) && Objects.equals(fileName, person.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(OIB, ime, prezime, status, cardRequestTimestamp, filePath, fileName);
    }
}
