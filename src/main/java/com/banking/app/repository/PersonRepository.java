package com.banking.app.repository;

import com.banking.app.model.Person;
import com.banking.app.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByOIB(String oib);

    List<Person> findByStatus(Status status);

    void deleteByOIB(String oib);
}
