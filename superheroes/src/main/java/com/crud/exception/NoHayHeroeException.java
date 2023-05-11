package com.crud.exception;

public class NoHayHeroeException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private static final String NO_EXISTE_HEROE_ID = "** No existe superhéroe con este id :: ";

    public NoHayHeroeException(Long id) {
        super(NO_EXISTE_HEROE_ID + id);
    }
}
