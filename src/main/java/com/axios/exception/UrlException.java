package com.axios.exception;

public class UrlException extends RuntimeException {

	private static final long serialVersionUID = 8247610319171014183L;

	public UrlException(Throwable e) {
		super(e.getMessage(), e);
	}

	public UrlException(String message) {
		super(message);
	}

}
