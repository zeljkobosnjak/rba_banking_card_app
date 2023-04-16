package com.banking.app.controller;

import com.banking.app.model.ByteArrayResourceWithFileName;
import com.banking.app.model.Person;
import com.banking.app.model.Status;
import com.banking.app.request.PersonRequest;
import com.banking.app.response.PersonResponse;
import com.banking.app.service.BankService;
import com.banking.app.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bank-api/person")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;
    private final PersonService personService;

    @PostMapping
    public ResponseEntity<PersonResponse> createPerson(@RequestBody PersonRequest personRequest) {
        Person person = PersonRequest.toPerson(personRequest);
        Person registeredPerson = bankService.registerPerson(person);

        return new ResponseEntity<>(
                PersonResponse.toPersonResponse(registeredPerson),
                HttpStatus.CREATED
        );
    }

    @PutMapping
    public ResponseEntity<PersonResponse> updatePerson(@RequestBody PersonRequest personRequest) {
        Person person = PersonRequest.toPerson(personRequest);
        Person updatedPerson = bankService.updatePerson(person);
        return ResponseEntity.ok(PersonResponse.toPersonResponse(updatedPerson));
    }

    @DeleteMapping("/{oib}")
    public ResponseEntity<Void> deletePerson(@PathVariable String oib) {
        bankService.deletePerson(oib);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{oib}")
    public ResponseEntity<PersonResponse> getPersonById(@PathVariable String oib) {
        Person person = bankService.findByOIB(oib);
        return ResponseEntity.ok(PersonResponse.toPersonResponse(person));
    }

    @GetMapping("/{oib}/file")
    public ResponseEntity<ByteArrayResource> getPersonFile(@PathVariable String oib) {
        ByteArrayResourceWithFileName byteArrayResourceWithFileName = bankService.exportFile(oib);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", byteArrayResourceWithFileName.getFileName());
        return ResponseEntity.ok()
                .headers(headers)
                .body(byteArrayResourceWithFileName.getByteArrayResource());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PersonResponse>> getPersonsByStatus(@PathVariable Status status) {
        List<PersonResponse> persons = personService.findByStatus(status).stream()
                .map(PersonResponse::toPersonResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(persons);
    }
}