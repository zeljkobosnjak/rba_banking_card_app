package com.banking.app.service;

import com.banking.app.exception.ActiveFileAlreadyExistsException;
import com.banking.app.exception.FileException;
import com.banking.app.exception.PersonNotFoundException;
import com.banking.app.model.ByteArrayResourceWithFileName;
import com.banking.app.model.FileStatus;
import com.banking.app.model.Person;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BankService {

    private static final Logger logger = LoggerFactory.getLogger(BankService.class);

    private static final String WORKING_DIR = System.getProperty("user.dir") + File.separator + "files";
    final static DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    private final PersonService personService;

    public Person registerPerson(Person person) {

        Optional<Person> existingPersonOpt = personService.findByOIB(person.getOIB());

        Person savedPerson;

        if (existingPersonOpt.isEmpty()) {
            LocalDateTime cardIssueDate = LocalDateTime.now();
            person.setCardRequestTimestamp(cardIssueDate);
            person.setFilePath(WORKING_DIR);
            person.setFileName(replaceIllegalCharacters(person.getOIB() + "_" + cardIssueDate.format(ISO_FORMATTER) + "_" + FileStatus.AKTIVAN.toString().toLowerCase() + ".txt"));

            savedPerson = personService.save(person);

            writeFile(savedPerson);
        } else {
            throw new ActiveFileAlreadyExistsException(person.getOIB());
        }

        return savedPerson;
    }

    public Person findByOIB(final String oib) {
        return personService.findByOIB(oib)
                .orElseThrow(() -> new PersonNotFoundException("Trazena osoba s OIB-om " + oib + " nije pronadjena u bazi podataka."));
    }

    public ByteArrayResourceWithFileName exportFile(String oib) {

        Optional<Person> personOpt = personService.findByOIB(oib);

        if (personOpt.isEmpty()) {
            throw new PersonNotFoundException("Trazena osoba s OIB-om " + oib + " nije pronadjena u bazi podataka.");
        }

        Person person = personOpt.get();

        ByteArrayResourceWithFileName byteArrayResourceWithFileName = new ByteArrayResourceWithFileName();

        try {
            Path path = Paths.get(person.getFilePath() + File.separator + person.getFileName());
            byte[] fileContent = Files.readAllBytes(path);
            ByteArrayResource byteArrayResource = new ByteArrayResource(fileContent);
            byteArrayResourceWithFileName.setByteArrayResource(byteArrayResource);
            byteArrayResourceWithFileName.setFileName(person.getFileName());
        } catch (IOException e) {
            logger.error("Pogreska pri citanju txt datoteke iz sustava.", e);
            throw new FileException(e.getMessage());
        }

        return byteArrayResourceWithFileName;
    }

    public Person updatePerson(Person person) {
        Person updatedPerson = personService.update(person);
        updateFile(updatedPerson);
        return updatedPerson;
    }

    public void deletePerson(String oib) {

        Optional<Person> personOpt = personService.findByOIB(oib);

        if (personOpt.isEmpty()) {
            throw new PersonNotFoundException("Trazena osoba s OIB-om " + oib + " nije pronadjena u bazi podataka.");
        }

        Person person = personOpt.get();


        String filePath = person.getFilePath() + File.separator + person.getFileName();

        File oldFile = new File(filePath);

        if (oldFile.exists()) {
            String newFilePath = person.getFilePath() + File.separator + person.getOIB() + "_" + person.getCardRequestTimestamp().format(ISO_FORMATTER) + "_" + FileStatus.NEAKTIVAN.toString().toLowerCase() + ".txt";

            File newFile = new File(newFilePath);

            oldFile.renameTo(newFile);
        }

        personService.deleteByOIB(person.getOIB());
    }

    private static void writeFile(Person person) {
        File directory = new File(person.getFilePath());
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = person.getFilePath() + File.separator + person.getFileName();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("OIB: " + person.getOIB());
            writer.newLine();
            writer.write("IME: " + person.getIme());
            writer.newLine();
            writer.write("PREZIME: " + person.getPrezime());
            writer.newLine();
            writer.write("Status: " + person.getStatus());
            writer.newLine();
        } catch (IOException e) {
            logger.error("Pogreska pri zapisivanju txt datoteke u sustav.", e);
            throw new FileException(e.getMessage());
        }
    }

    private static void updateFile(Person person) {
        String filePath = person.getFilePath() + File.separator + person.getFileName();

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write("OIB: " + person.getOIB());
            writer.newLine();
            writer.write("IME: " + person.getIme());
            writer.newLine();
            writer.write("PREZIME: " + person.getPrezime());
            writer.newLine();
            writer.write("Status: " + person.getStatus());
            writer.newLine();
        } catch (IOException e) {
            logger.error("Pogreska pri azuriranju txt datoteke.", e);
            throw new FileException(e.getMessage());
        }
    }

    private static String replaceIllegalCharacters(String fileName) {
        String illegalCharsPattern = "[\\\\/:*?\"<>|]";
        String replacement = "-";

        String sanitizedFileName = fileName.replaceAll(illegalCharsPattern, replacement);

        return sanitizedFileName;
    }

}
