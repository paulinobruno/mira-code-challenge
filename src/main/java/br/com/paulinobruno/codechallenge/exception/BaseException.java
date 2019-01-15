package br.com.paulinobruno.codechallenge.exception;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Data
public abstract class BaseException extends RuntimeException {

    private String logref;
    private List<BaseError> errors;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BaseException withErrors(List<BaseError> errors) {
        this.errors = errors;
        return this;
    }

    public BaseException withErrors(BaseErrors errors) {
        if (errors != null) {
            this.errors = new ArrayList<>(errors.getErrors());
        }

        return this;
    }

    public BaseException withErrors(BaseError... errors) {
        if (errors != null && errors.length > 0) {
            this.errors = asList(errors);
        }

        return this;
    }
}
