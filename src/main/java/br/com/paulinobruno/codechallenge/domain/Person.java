package br.com.paulinobruno.codechallenge.domain;

import lombok.Data;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @NotEmpty
    private String givenName;
    private String familyName;
    private String cpf;

    private LocalDate birthDate;
    private Boolean active;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "person", cascade = ALL)
    private List<Phone> phones;

    @OneToMany(mappedBy = "person", cascade = ALL)
    private List<Email> emails;

}
