package br.com.paulinobruno.codechallenge.exception;

public class Exceptions {

    public static NotFoundException notFound() {
        return new NotFoundException();
    }

    public static UnprocessableEntityException unprocessableEntity() {
        return new UnprocessableEntityException();
    }

}
