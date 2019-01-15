package br.com.paulinobruno.codechallenge.domain;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class Address {

    private String streetName;
    private String district;
    private String location;
    private String region;
    private String country;

}
