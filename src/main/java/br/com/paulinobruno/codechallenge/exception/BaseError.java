package br.com.paulinobruno.codechallenge.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Data
class BaseError implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty
    private String logref;
    @JsonProperty
    private String message;
    @JsonProperty
    private List<String> arguments = new LinkedList<>();

    public BaseError() {
    }

    public BaseError(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            logref = format("%s", ((FieldError) objectError).getField());
        } else {
            logref = format("request param: %s", objectError.getObjectName());
        }

        message = objectError.getDefaultMessage();
        addArguments(objectError);
    }

    public BaseError(String logref, String message) {
        this.logref = logref;
        this.message = message;
    }

    public BaseError(String logref, String message, List<String> arguments) {
        this.logref = logref;
        this.message = message;
        this.arguments = arguments;
    }

    public BaseError withArguments(Object... argumentsArray) {
        List<String> argumentsAsString = emptyList();

        if (argumentsArray != null) {
            argumentsAsString = stream(argumentsArray)
                .filter(Objects::nonNull)
                .map(Objects::toString)
                .collect(toList());
        }

        return new BaseError(this.logref, this.message, argumentsAsString);
    }

    private void addArguments(ObjectError objectError) {
        Object[] arguments = objectError.getArguments();

        if (arguments != null) {
            for (int i = arguments.length - 1; i >= 0; i--) {
                Object argument = arguments[i];

                if (!(argument instanceof MessageSourceResolvable) && argument != null) {
                    this.arguments.add(argument.toString());
                }
            }
        }
    }
}
