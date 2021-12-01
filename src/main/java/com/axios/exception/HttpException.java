package com.axios.exception;

/**
 * [http 异常](http exception)
 * @description zh - http 异常
 * @description en - http exception
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-01 22:12:28
 */
public class HttpException extends RuntimeException {
	private static final long serialVersionUID = 8247610319171014183L;

	public HttpException(Throwable e) {
		super(e.getMessage(), e);
	}

	public HttpException(String message) {
		super(message);
	}

}
