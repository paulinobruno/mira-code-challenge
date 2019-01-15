package br.com.paulinobruno.codechallenge.fixture

import br.com.paulinobruno.codechallenge.domain.Address
import br.com.paulinobruno.codechallenge.domain.Email
import br.com.paulinobruno.codechallenge.domain.Person
import br.com.paulinobruno.codechallenge.domain.Phone

import static java.time.LocalDate.now

class PersonFixture {

    static aPerson(String name = 'BRUNO') {
        new Person(
            givenName: name,
            familyName: 'Paulino',
            cpf: '11122233344',
            birthDate: now(),
            active: true,
            address: new Address(
                streetName: 'Av Paulista, 197',
                district: 'Jardins',
                location: 'São Paulo',
                region: 'SP',
                country: 'BRA'
            ),
            phones: [
                new Phone(
                    alias: 'mobile',
                    phoneNumber: '11 98877-6655'
                ),
                new Phone(
                    alias: 'home',
                    phoneNumber: '11 3344-5566'
                )
            ],
            emails: [
                new Email(
                    alias: 'home',
                    emailAddress: 'paulino.bruno@gmail.com'
                )
            ]
        )
    }

}
