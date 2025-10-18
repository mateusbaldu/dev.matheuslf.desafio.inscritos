package dev.matheuslf.desafio.inscritos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundExce extends RuntimeException {
    public ResourceNotFoundExce(String message) {
        super(message);
    }
}
