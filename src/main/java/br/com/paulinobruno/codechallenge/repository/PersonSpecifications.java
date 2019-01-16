package br.com.paulinobruno.codechallenge.repository;

import br.com.paulinobruno.codechallenge.domain.Person;
import org.springframework.data.jpa.domain.Specification;

public class PersonSpecifications {

    public static Specification<Person> with(String fieldName, String fieldValue) {
        if (fieldValue == null) {
            return null;
        }

        return (root, query, builder) -> builder.like(
            builder.lower(root.get(fieldName)),
            "%" + fieldValue.toLowerCase() + "%"
        );
    }

    public static Specification<Person> isActive() {
        return (root, query, builder) -> builder.isTrue(root.get("active"));
    }

}
