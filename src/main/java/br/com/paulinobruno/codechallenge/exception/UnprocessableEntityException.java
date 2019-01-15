package br.com.paulinobruno.codechallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends BaseException {

    public UnprocessableEntityException() {
        super(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase());
    }

    public UnprocessableEntityException(String message) {
        super(message);
    }
}
