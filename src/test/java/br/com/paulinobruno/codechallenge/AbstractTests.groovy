package br.com.paulinobruno.codechallenge


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@ActiveProfiles(value = 'test')
@ContextConfiguration(loader = SpringBootContextLoader, classes = CodeChallengeApplication)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class AbstractTests extends Specification {

    @Autowired
    WebApplicationContext context

    MockMvc mvc

}

