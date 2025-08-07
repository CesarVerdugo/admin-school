package com.verdugocode.administrador_escolar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Mapea esta excepción a un 409 Conflict
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s con %s : '%s' ya existe.", resourceName, fieldName, fieldValue));
    }
}