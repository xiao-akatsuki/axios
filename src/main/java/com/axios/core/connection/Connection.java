package com.axios.core.connection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import com.axios.core.config.AjaxGlobalConfig;
import com.axios.core.requestMethod.RequestMethod;
import com.axios.core.url.UrlMethod;
import com.axios.exception.ConnException;
import com.axios.exception.UrlException;

/**
 * [HTTP连接对象](HTTP connection object)
 * @description zh - HTTP连接对象
 * @description en - HTTP connection object
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-13 19:54:52
 */
public class Connection {

	/** url */
	private final URL url;

	/** agent */
	private final Proxy proxy;

	/** connection */
	private HttpURLConnection conn;

	/**
	 * [有参](Have reference)
	 * @description zh - 有参
	 * @description en - Have reference
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 19:42:49
	 * @param url URL
	 * @param proxy 代理
	 */
	public Connection(URL url, Proxy proxy) {
		this.url = url;
		this.proxy = proxy;
		initConn();
	}

	/**
	 * [创建HttpConnection](Create HttpConnection)
	 * @description zh - 创建HttpConnection
	 * @description en - Create HttpConnection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:45:15
	 * @param url URL
	 * @param proxy 代理
	 * @return com.axios.core.connection.Connection
	 */
	public static Connection create(String url, Proxy proxy) {
		return create(UrlMethod.toUrlForHttp(url), proxy);
	}

	/**
	 * [创建HttpConnection](Create HttpConnection)
	 * @description zh - 创建HttpConnection
	 * @description en - Create HttpConnection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:05:21
	 * @param url URL
	 * @param proxy 代理
	 * @return com.axios.core.connection.Connection
	 */
	public static Connection create(URL url, Proxy proxy) {
		return new Connection(url, proxy);
	}

	/**
	 * [初始化连接相关信息](Initialization connection related information)
	 * @description zh - 初始化连接相关信息
	 * @description en - Initialization connection related information
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:04:26
	 * @return com.axios.core.connection.Connection
	 */
	public Connection initConn() {
		try {
			this.conn = openHttp();
		} catch (IOException e) {
			throw new ConnException(e);
		}
		this.conn.setDoInput(true);
		return this;
	}

	/**
	 * [获取请求方法](Get request method)
	 * @description zh - 获取请求方法
	 * @description en - Get request method
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:49:32
	 * @return com.axios.core.requestMethod.RequestMethod
	 */
	public RequestMethod getMethod() {
		return RequestMethod.valueOf(this.conn.getRequestMethod());
	}

	/**
	 * [设置请求方法](Set request method)
	 * @description zh - 设置请求方法
	 * @description en - Set request method
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:52:03
	 * @param method 请求方法
	 * @return com.axios.core.connection.Connection
	 */
	public Connection setMethod(RequestMethod method) {
		if (RequestMethod.POST.equals(method)
				|| RequestMethod.PUT.equals(method)
				|| RequestMethod.PATCH.equals(method)
				|| RequestMethod.DELETE.equals(method)) {
			this.conn.setUseCaches(false);

			// 增加PATCH方法支持
			if (RequestMethod.PATCH.equals(method)) {
				AjaxGlobalConfig.allowPatch();
			}
		}

		// method
		try {
			this.conn.setRequestMethod(method.toString());
		} catch (ProtocolException e) {
			throw new UrlException(e);
		}

		return this;
	}

	/**
	 * [初始化HTTP或HTTPS请求参数](Initialize HTTP or HTTPS request parameters)
	 * @description zh - 初始化HTTP或HTTPS请求参数
	 * @description en - Initialize HTTP or HTTPS request parameters
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:02:15
	 * @throws IOException java.io.IOException
	 * @return java.net.HttpURLConnection
	 */
	private HttpURLConnection openHttp() throws IOException {
		final URLConnection conn = openConnection();
		if (false == conn instanceof HttpURLConnection) {
			throw new ConnException("'" + conn.getClass().getName() +"' of URL [" + this.url + "] is not a http connection, make sure URL is format for http.");
		}
		return (HttpURLConnection) conn;
	}

	/**
	 * [建立连接](Establish connection)
	 * @description zh - 建立连接
	 * @description en - Establish connection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:03:12
	 * @throws IOException java.io.IOException
	 * @return java.net.URLConnection
	 */
	private URLConnection openConnection() throws IOException {
		return (null == this.proxy) ?
			url.openConnection() :
			url.openConnection(this.proxy);
	}

}
