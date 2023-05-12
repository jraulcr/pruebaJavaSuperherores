package com.crud.exception;

public class NoHayUniversoException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private static final String NO_EXISTE_UNIVERSO = "** No existe este Universo :: ";

    public NoHayUniversoException(Long id) {
        super(NO_EXISTE_UNIVERSO + id);
    }
}
