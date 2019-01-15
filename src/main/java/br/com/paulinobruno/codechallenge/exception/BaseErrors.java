package br.com.paulinobruno.codechallenge.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;

public class BaseErrors implements Iterable<BaseError> {

    private List<BaseError> errors;

    public BaseErrors() {
        this.errors = new ArrayList<>();
    }

    public BaseErrors(String logref, String message) {
        this(new BaseError(logref, message));
    }

    public BaseErrors(BaseError error, BaseError... errors) {
        this.errors = new ArrayList<>(errors.length + 1);
        this.errors.add(error);
        this.errors.addAll(asList(errors));
    }

    @JsonCreator
    public BaseErrors(List<BaseError> errors) {
        this.errors = errors;
    }

    public BaseErrors add(BaseError error) {
        this.errors.add(error);
        return this;
    }

    @Override
    public Iterator<BaseError> iterator() {
        return this.errors.iterator();
    }

    @JsonValue
    public List<BaseError> getErrors() {
        return errors;
    }

}
