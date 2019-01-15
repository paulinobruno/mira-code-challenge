package br.com.paulinobruno.codechallenge.controller

import br.com.paulinobruno.codechallenge.AbstractTests
import br.com.paulinobruno.codechallenge.domain.Person
import br.com.paulinobruno.codechallenge.repository.PersonRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.ResultActions

import static br.com.paulinobruno.codechallenge.fixture.PersonFixture.aPerson
import static org.hamcrest.Matchers.hasSize
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

class PersonControllerIT extends AbstractTests {

    @Autowired
    PersonRepository repository
    @Autowired
    ObjectMapper mapper

    private Person sample

    void setup() {
        mvc = webAppContextSetup(context).build()

        sample = aPerson()
        repository.save(sample)
    }

    void cleanup() {
        repository.deleteAll()
    }

    void 'GET / should list all active users'() {
        setup:
            repository.save(aPerson())

        when:
            ResultActions result = mvc.perform(
                get('/')
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(2)))
    }

    void 'GET / should list empty when no active users'() {
        setup:
            sample.active = false
            repository.save(sample)


        when:
            ResultActions result = mvc.perform(
                get('/')
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isOk())
                .andExpect(jsonPath('$', hasSize(0)))
    }

    void 'GET /:existing should be OK'() {
        when:
            ResultActions result = mvc.perform(
                get("/${sample.id}")
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isOk())
                .andExpect(jsonPath('$.givenName').value(sample.givenName))
    }

    void 'GET /:inactive should be NOT_FOUND'() {
        setup:
            sample.active = false
            repository.save(sample)

        when:
            ResultActions result = mvc.perform(
                get("/${sample.id}")
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isNotFound())
    }

    void 'GET /:xpto should be NOT_FOUND'() {
        when:
            ResultActions result = mvc.perform(
                get('/0')
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isNotFound())
    }

    void 'DELETE /:xpto should be NOT_FOUND'() {
        when:
            ResultActions result = mvc.perform(
                delete('/0')
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isNotFound())
    }

    void 'DELETE /:existing should be NO_CONTENT'() {
        when:
            ResultActions result = mvc.perform(
                delete("/${sample.id}")
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isNoContent())

        and:
            Optional<Person> persisted = repository.findById(sample.id)

        then:
            persisted.isPresent()
            !persisted.get().active
    }

    void 'POST / with empty body should be BAD_REQUEST'() {
        when:
            ResultActions result = mvc.perform(
                post('/')
                    .content('{}')
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isBadRequest())
    }

    void 'POST / with new object should be CREATED'() {
        given:
            Person newPerson = aPerson('Almeida')

        when:
            ResultActions result = mvc.perform(
                post('/')
                    .content(mapper.writeValueAsString(newPerson))
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isCreated())
                .andExpect(header().exists('Location'))
                .andExpect(jsonPath('$.active').value(true))
            String locationHeader = result.andReturn().response.getHeader('Location')
            String generatedId = locationHeader.replaceAll('^/(.+)$', '$1')
            generatedId

        and:
            ResultActions resultGet = mvc.perform(
                get("/$generatedId")
                    .contentType(APPLICATION_JSON)
            )

        then:
            resultGet.andExpect(status().isOk())
                .andExpect(jsonPath('$.givenName').value(newPerson.givenName))
    }

    void 'PUT /:xpto should be NOT_FOUND'() {
        when:
            ResultActions result = mvc.perform(
                put('/0')
                    .content(mapper.writeValueAsString(aPerson('Almeida')))
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isNotFound())
    }

    void 'PUT /:existing with empty body should be BAD_REQUEST'() {
        when:
            ResultActions result = mvc.perform(
                put("/${sample.id}")
                    .content('{}')
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isBadRequest())
    }

    void 'PUT /:existing should be ACCEPTED'() {
        when:
            ResultActions result = mvc.perform(
                put("/${sample.id}")
                    .content(mapper.writeValueAsString(aPerson('Almeida')))
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isAccepted())

        and:
            ResultActions resultGet = mvc.perform(
                get("/${sample.id}")
                    .contentType(APPLICATION_JSON)
            )

        then:
            resultGet.andExpect(status().isOk())
                .andExpect(jsonPath('$.givenName').value('Almeida'))
    }

    void 'POST /bulk with empty list should be UNPROCESSABLE_ENTITY'() {
        when:
            ResultActions result = mvc.perform(
                post('/bulk')
                    .content('[]')
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isUnprocessableEntity())
    }

    void 'POST /bulk with at least one invalid object in list should be BAD_REQUEST'() {
        when:
            ResultActions result = mvc.perform(
                post('/bulk')
                    .content('[{ "givenName": "BRUNO" }, {}]')
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isBadRequest())
    }

    void 'POST /bulk with all valid objects in list should be OK'() {
        given:
            Person p1 = aPerson('Johnny')
            Person p2 = aPerson('Rivers')

        when:
            ResultActions result = mvc.perform(
                post('/bulk')
                    .content(mapper.writeValueAsString([p1, p2]))
                    .contentType(APPLICATION_JSON)
            )

        then:
            result.andExpect(status().isOk())
                .andExpect(jsonPath('$[:2].active').value([true, true]))
                .andExpect(jsonPath('$[:2].givenName').value(['Johnny', 'Rivers']))

        and:
            List<String> peopleNames = repository.findAll().collect() { it.givenName }

        then:
            peopleNames.size() == 3
            peopleNames == ['BRUNO', 'Johnny', 'Rivers']
    }

}
