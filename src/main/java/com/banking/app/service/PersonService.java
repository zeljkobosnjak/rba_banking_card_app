package com.banking.app.service;

import com.banking.app.exception.PersonAlreadyExistsException;
import com.banking.app.exception.PersonNotFoundException;
import com.banking.app.model.Person;
import com.banking.app.model.Status;
import com.banking.app.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonService {

    private final PersonRepository personRepository;

    public Person save(final Person person) {

        final Optional<Person> existingPersonOpt = personRepository.findByOIB(person.getOIB());

        existingPersonOpt.ifPresent(existingUser -> {
            throw new PersonAlreadyExistsException(existingPersonOpt.get().getOIB());
        });

        return personRepository.save(person);
    }

    public Person update(final Person person) {

        final Optional<Person> existingPersonOpt = personRepository.findByOIB(person.getOIB());

        if (existingPersonOpt.isPresent()) {
            Person existingPerson = existingPersonOpt.get();
            existingPerson.setIme(person.getIme());
            existingPerson.setPrezime(person.getPrezime());
            existingPerson.setStatus(person.getStatus());
            existingPerson.setOIB(person.getOIB());

            return personRepository.save(existingPerson);
        } else {
            throw new PersonNotFoundException("Ne pronalazi se osoba s OIB-om " + person.getOIB() + " u bazi podataka.");
        }
    }

    public void deleteByOIB(final String oib){
        Optional<Person> personOpt = personRepository.findByOIB(oib);

        if(personOpt.isPresent()){
            personRepository.deleteByOIB(personOpt.get().getOIB());
        } else {
            throw new PersonNotFoundException("Osoba zahtjeva s ID-om " + oib + " nije pronađena u bazi podataka.");
        }
    }

    public Optional<Person> findByOIB(final String oib) {
        return personRepository.findByOIB(oib);
    }

    public List<Person> findByStatus(final Status status) {
        final List<Person> personList = personRepository.findByStatus(status);

        if (personList.isEmpty()) {
            throw new PersonNotFoundException("Nema pronađenih zapisa sa statusom " + status);
        }

        return personList;
    }
}
