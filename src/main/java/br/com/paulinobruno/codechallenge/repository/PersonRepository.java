package br.com.paulinobruno.codechallenge.repository;

import br.com.paulinobruno.codechallenge.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    Optional<Person> findByIdAndActiveTrue(Integer id);

    List<Person> findAllByActiveTrue();

}
