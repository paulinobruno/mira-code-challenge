package br.com.paulinobruno.codechallenge.controller;

import br.com.paulinobruno.codechallenge.domain.Person;
import br.com.paulinobruno.codechallenge.domain.PersonSearch;
import br.com.paulinobruno.codechallenge.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static br.com.paulinobruno.codechallenge.exception.Exceptions.unprocessableEntity;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.created;

@Slf4j
@Validated
@RestController
@RequestMapping("/")
public class PersonController {

    private PersonService service;

    public PersonController(PersonService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        log.info("create person {}", person);

        person = service.saveNewPerson(person);

        try {
            String locationUri = format("/%d", person.getId());
            return created(new URI(locationUri)).body(person);
        } catch (URISyntaxException e) {
            log.warn("should not happend since we know the expected URI syntax", e);
            throw new InternalError("should not happen", e);
        }
    }

    @ResponseStatus(OK)
    @PostMapping("/bulk")
    public List<Person> createMany(@Valid @RequestBody List<Person> persons) {
        log.info("bulk create persons, {}", persons);

        if (persons.isEmpty()) {
            log.info("for empty persons list, there is nothing to be done.");
            throw unprocessableEntity();
        }

        return persons.stream()
            .map(service::saveNewPerson)
            .collect(toList());
    }

    @GetMapping
    public List<Person> listAll() {
        return service.findAllPeople();
    }

    @GetMapping("/search")
    public List<Person> searchPeople(PersonSearch search) {
        log.info("running search query for criterias={}", search);
        return service.searchPeople(search);
    }

    @GetMapping("/{id}")
    public Person getPerson(@PathVariable Integer id) {
        log.info("trying to fetch person for id={}", id);
        return service.findPerson(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(ACCEPTED)
    public void update(@PathVariable Integer id, @Valid @RequestBody Person person) {
        log.info("updating person for id={} with data={}", id, person);
        service.findPerson(id);

        person.setId(id);
        service.savePerson(person);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        log.info("trying to delete person with id={}", id);

        Person person = service.findPerson(id);
        service.deletePerson(person);

        log.info("person with id={} masked as deleted", id);
    }

}
