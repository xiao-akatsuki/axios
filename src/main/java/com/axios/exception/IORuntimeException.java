package com.axios.exception;

/**
 * [IO运行时异常](IO runtime exception)
 * @description zh - IO运行时异常
 * @description en - IO runtime exception
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-17 13:27:43
 */
public class IORuntimeException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public IORuntimeException(Throwable e) {
		super(e);
	}

	public IORuntimeException(String message) {
		super(message);
	}
}
