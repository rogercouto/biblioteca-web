package br.com.uabrestingaseca.biblioteca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;
import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private List<String> errors;

    public ValidationException(String message, String ...errors) {
        super(message);
        this.errors = Arrays.asList(errors);
    }

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}
