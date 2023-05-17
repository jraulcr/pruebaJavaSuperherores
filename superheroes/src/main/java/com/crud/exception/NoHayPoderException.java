package com.crud.exception;

public class NoHayPoderException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	private static final String NO_EXISTE_PODER_ID = "** No existe poder con este id :: ";

    public NoHayPoderException(Long id) {
        super(NO_EXISTE_PODER_ID + id);
    }
}
