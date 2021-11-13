package com.axios.core.connection;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Connection {

	private final URL url;
	private final Proxy proxy;
	private HttpURLConnection conn;

	public Connection(URL url, Proxy proxy) {
		this.url = url;
		this.proxy = proxy;

		// 初始化Http连接
		initConn();
	}

	public Connection initConn() {
		try {
			this.conn = openHttp();
		} catch (IOException e) {
			throw new Exception();
		}

		// 默认读取响应内容
		this.conn.setDoInput(true);

		return this;
	}

	private HttpURLConnection openHttp() throws IOException {
		final URLConnection conn = openConnection();
		if (false == conn instanceof HttpURLConnection) {
			// 防止其它协议造成的转换异常
			throw new Exception();
		}

		return (HttpURLConnection) conn;
	}

	private URLConnection openConnection() throws IOException {
		return (null == this.proxy) ? url.openConnection() : url.openConnection(this.proxy);
	}

}
