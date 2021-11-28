package com.axios.exception;

public class NoResourceException extends IORuntimeException {

	private static final long serialVersionUID = -623254467603299129L;

	public NoResourceException(Throwable e) {
		super(e);
	}

	public NoResourceException(String message) {
		super(message);
	}

}
