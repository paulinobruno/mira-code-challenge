package br.com.paulinobruno.codechallenge.service;

import br.com.paulinobruno.codechallenge.domain.Person;
import br.com.paulinobruno.codechallenge.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.paulinobruno.codechallenge.exception.Exceptions.notFound;

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
}
