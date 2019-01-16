package br.com.paulinobruno.codechallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Email {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Person person;
    private String alias;
    private String emailAddress;

}
