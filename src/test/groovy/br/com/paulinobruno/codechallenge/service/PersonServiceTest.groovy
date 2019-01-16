package br.com.paulinobruno.codechallenge.service

import br.com.paulinobruno.codechallenge.domain.Person
import br.com.paulinobruno.codechallenge.repository.PersonRepository
import spock.lang.Specification
import spock.lang.Unroll

class PersonServiceTest extends Specification {

    private PersonService service
    private PersonRepository repositoryMock

    void setup() {
        repositoryMock = Mock(PersonRepository)
        service = new PersonService(repositoryMock)
    }

    @Unroll
    void 'given new person should be persisted as active'() {
        given:
            Person person = new Person(active: flag)

        when:
            person = service.savePerson(person)

        then:
            1 * repositoryMock.save(person) >> person
            person.active

        where:
            flag << [null, false, true]
    }

    void 'when delete person it should be set inactive instead'() {
        given:
            Person person = new Person(active: true)

        when:
            service.deletePerson(person)

        then:
            1 * repositoryMock.save(person)
            !person.active
    }

}
