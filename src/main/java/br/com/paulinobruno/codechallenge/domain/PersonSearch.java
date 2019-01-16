package br.com.paulinobruno.codechallenge.domain;

import lombok.Data;

@Data
public class PersonSearch {

    private String givenName;
    private String familyName;
    private String cpf;

}
