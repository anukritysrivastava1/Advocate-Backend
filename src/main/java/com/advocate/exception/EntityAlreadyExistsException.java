package com.advocate.exception;

public class EntityAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityAlreadyExistsException() {

		super();

	}

	public EntityAlreadyExistsException(String message) {

		super(message);

	}

	public EntityAlreadyExistsException(String message, Throwable cause) {

		super(message, cause);

	}

	public EntityAlreadyExistsException(Throwable cause) {

		super(cause);

	}

}