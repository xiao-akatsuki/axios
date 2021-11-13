package com.axios.exception;

public class ConnException extends RuntimeException {

	private static final long serialVersionUID = 8247610319171014183L;

	public ConnException(Throwable e) {
		super(e.getMessage(), e);
	}

	public ConnException(String message) {
		super(message);
	}

}
