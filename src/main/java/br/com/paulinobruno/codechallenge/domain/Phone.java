package br.com.paulinobruno.codechallenge.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@ToString(exclude = {"id", "person"})
public class Phone {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Person person;
    private String alias;
    private String phoneNumber;

}
