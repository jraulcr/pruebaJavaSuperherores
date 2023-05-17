package com.crud.exception;

import java.util.NoSuchElementException;

public class RegistroDuplicadoException extends NoSuchElementException {

	private static final long serialVersionUID = 1L;

	public RegistroDuplicadoException(Long id) {
        super("El Universo con id " + id + " ya existe en tabla.");
    }
}
