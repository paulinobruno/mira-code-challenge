package br.com.paulinobruno.codechallenge.service;

import br.com.paulinobruno.codechallenge.domain.Person;
import br.com.paulinobruno.codechallenge.domain.PersonSearch;
import br.com.paulinobruno.codechallenge.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.paulinobruno.codechallenge.exception.Exceptions.notFound;
import static br.com.paulinobruno.codechallenge.repository.PersonSpecifications.isActive;
import static br.com.paulinobruno.codechallenge.repository.PersonSpecifications.with;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class PersonService {

    private PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public Person saveNewPerson(Person person) {
        person.setActive(true);
        return savePerson(person);
    }

    public Person savePerson(Person person) {
        if (person.getEmails() != null) {
            person.getEmails().forEach(email -> email.setPerson(person));
        }

        if (person.getPhones() != null) {
            person.getPhones().forEach(phone -> phone.setPerson(person));
        }

        return repository.save(person);
    }

    public void deletePerson(Person person) {
        person.setActive(false);
        savePerson(person);
    }

    public Person findPerson(Integer id) {
        return repository.findByIdAndActiveTrue(id).orElseThrow(notFound());
    }

    public List<Person> findAllPeople() {
        return repository.findAllByActiveTrue();
    }

    public List<Person> searchPeople(PersonSearch search) {
        return repository.findAll(
            where(isActive())
                .and(
                    where(with("givenName", search.getGivenName()))
                        .or(with("familyName", search.getFamilyName()))
                        .or(with("cpf", search.getCpf()))
                )
        );
    }
}
