package br.com.paulinobruno.codechallenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RequestMapping(produces = "application/vnd.error")
@ControllerAdvice(annotations = RestController.class)
@ResponseBody
public class RestExceptionHandler {

    private static boolean hasErrors(BaseException e) {
        List<BaseError> errors = e.getErrors();
        return errors != null && !errors.isEmpty();
    }

    private static boolean hasErrors(BaseErrors errors) {
        return errors != null && errors.iterator().hasNext();
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseErrors> handle(BaseException e) {
        ResponseStatus responseStatus = findAnnotation(e.getClass(), ResponseStatus.class);
        HttpStatus status = responseStatus.value();
        BaseErrors errors;

        if (hasErrors(e)) {
            errors = new BaseErrors(e.getErrors());
        } else {
            errors = new BaseErrors(new BaseError(e.getLogref(), e.getMessage()));
        }

        return new ResponseEntity<>(errors, status);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseError> handle(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations()
            .stream()
            .map(Objects::toString)
            .collect(toList());

        return new ResponseEntity<>(
            new BaseError("INVALID_PAYLOAD", "cannot accept payload", errors),
            BAD_REQUEST
        );
    }

}
