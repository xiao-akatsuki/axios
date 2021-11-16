package com.axios.core.connection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import com.axios.core.requestMethod.RequestMethod;
import com.axios.core.urlTool.UrlTool;
import com.axios.exception.ConnException;
import com.axios.header.RequestHeader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * [连接池](Connection pool)
 * @description zh - 连接池
 * @description en - Connection pool
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-15 20:31:04
 */
public class Connection {

	/** Connection URL */
	private final URL url;
	/** agent */
	private final Proxy proxy;
	/** Connection object */
	private HttpURLConnection conn;

	/** structure ------------------- 构造 */

	/**
	 * [有参](Have reference)
	 * @description zh - 有参
	 * @description en - Have reference
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 20:33:11
	 * @param url URL
	 * @param proxy 代理
	 */
	public Connection(URL url, Proxy proxy) {
		this.url = url;
		this.proxy = proxy;

		// Initialize HTTP connection
		initConn();
	}

	/**
	 * [有参](Have reference)
	 * @description zh - 有参
	 * @description en - Have reference
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 20:33:11
	 * @param url URL文本
	 * @param proxy 代理
	 */
	public Connection(String url,Proxy proxy){
		this.url = UrlTool.toUrlForHttp(url);
		this.proxy = proxy;

		// Initialize HTTP connection
		initConn();
	}

	/** create ------------------- 创建 */

	/**
	 * [创建HttpConnection](create HttpConnection)
	 * @description zh - 创建HttpConnection
	 * @description en - create HttpConnection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 20:35:52
	 * @param url URL
	 * @param proxy 代理对象
	 * @return com.axios.core.connection.Connection
	 */
	public static Connection create(URL url, Proxy proxy) {
		return new Connection(url, proxy);
	}

	/**
	 * [创建HttpConnection](create HttpConnection)
	 * @description zh - 创建HttpConnection
	 * @description en - create HttpConnection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 21:03:23
	 * @param url URL文本
	 * @param proxy 代理对象
	 * @return com.axios.core.connection.Connection
	 */
	public static Connection create(String url,Proxy proxy){
		return new Connection(url, proxy);
	}

	/** init ------------------- 初始化 */

	/**
	 * [初始化连接相关信息](Initialization connection related information)
	 * @description zh - 初始化连接相关信息
	 * @description en - Initialization connection related information
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 20:37:36
	 * @return com.axios.core.connection.Connection
	 */
	public Connection initConn() {
		try {
			this.conn = openHttp();
		} catch (IOException e) {
			throw new ConnException(e);
		}
		// default read response content
		this.conn.setDoInput(true);
		return this;
	}

	/** getter and setter ------------------- 获取和设置 */

	/**
	 * [获取请求方法](Get request method)
	 * @description zh - 获取请求方法
	 * @description en - Get request method
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 21:05:48
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
	 * @since 2021-11-15 21:09:57
	 * @param method 请求方法
	 * @return com.axios.core.connection.Connection
	 */
	public Connection setMethod(RequestMethod method) {
		if (RequestMethod.POST.equals(method)
				|| RequestMethod.PUT.equals(method)
				|| RequestMethod.PATCH.equals(method)
				|| RequestMethod.DELETE.equals(method)) {
			this.conn.setUseCaches(false);
			// add patch method support
			if (RequestMethod.PATCH.equals(method)) {
				HttpGlobalConfig.allowPatch();
			}
		}
		// method
		try {
			this.conn.setRequestMethod(method.toString());
		} catch (ProtocolException e) {
			throw new ConnException(e);
		}
		return this;
	}

	/**
	 * [获取URL](Get URL)
	 * @description zh - 获取URL
	 * @description en - Get URL
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 21:11:15
	 * @return java.net.URL
	 */
	public URL getUrl() {
		return this.url;
	}

	/**
	 * [获取代理](get proxy)
	 * @description zh - 获取代理
	 * @description en - get proxy
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 21:11:46
	 * @return java.net.Proxy
	 */
	public Proxy getProxy() {
		return this.proxy;
	}

	/**
	 * [获取HttpURLConnection](Get httpurlconnection)
	 * @description zh - 获取HttpURLConnection
	 * @description en - Get httpurlconnection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 21:12:16
	 * @return java.net.HttpURLConnection
	 */
	public HttpURLConnection getHttpURLConnection() {
		return conn;
	}

	/** header ------------------- 请求头 */

	/**
	 * [设置请求头](Set request header)
	 * @description zh - 设置请求头
	 * @description en - Set request header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:26:15
	 * @param header 请求头
	 * @param value 请求内容
	 * @param isOverride 是否覆盖旧值
	 * @return com.axios.core.connection.Connection
	 */
	public Connection header(String header, String value, boolean isOverride) {
		if (null != this.conn) {
			if (isOverride) {
				this.conn.setRequestProperty(header, value);
			} else {
				this.conn.addRequestProperty(header, value);
			}
		}
		return this;
	}

	/**
	 * [设置请求头](Set request header)
	 * @description zh - 设置请求头
	 * @description en - Set request header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:30:59
	 * @param header 请求头
	 * @param value 请求内容
	 * @param isOverride 是否覆盖旧值
	 * @return com.axios.core.connection.Connection
	 */
	public Connection header(RequestHeader header, String value, boolean isOverride) {
		return header(header.toString(), value, isOverride);
	}

	/**
	 * [设置请求头](Set request header)
	 * @description zh - 设置请求头
	 * @description en - Set request header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:33:36
	 * @param header 请求头
	 * @param isOverride 是否覆盖旧值
	 * @return com.axios.core.connection.Connection
	 */
	public Connection header(Map<String, List<String>> headerMap, boolean isOverride) {
		if (UrlTool.isNotEmpty(headerMap)) {
			String name;
			for (Entry<String, List<String>> entry : headerMap.entrySet()) {
				name = entry.getKey();
				for (String value : entry.getValue()) {
					this.header(name, UrlTool.nullToEmpty(value), isOverride);
				}
			}
		}
		return this;
	}

	/**
	 * [获取请求头](Get request header)
	 * @description zh - 获取请求头
	 * @description en - Get request header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:39:24
	 * @param name 请求头的名字
	 * @return java.lang.String
	 */
	public String header(String name) {
		return this.conn.getHeaderField(name);
	}

	/**
	 * [获取请求头](Get request header)
	 * @description zh - 获取请求头
	 * @description en - Get request header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:40:05
	 * @param name 请求头
	 * @return java.lang.String
	 */
	public String header(RequestHeader name) {
		return header(name.toString());
	}

	/**
	 * [获取所有Http请求头](Get all HTTP request headers)
	 * @description zh - 获取所有Http请求头
	 * @description en - Get all HTTP request headers
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:40:33
	 * @return java.util.Map<java.lang.String, java.util.List<java.lang.String>>
	 */
	public Map<String, List<String>> headers() {
		return this.conn.getHeaderFields();
	}

	/**
	 * [设置https请求参数](Set HTTPS request parameters)
	 * @description zh - 设置https请求参数
	 * @description en - Set HTTPS request parameters
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:46:49
	 * @param hostnameVerifier 域名验证器
	 * @param ssf  SSLSocketFactory
	 * @throws com.axios.exception.ConnException
	 * @return com.axios.core.connection.Connection
	 */
	public Connection setHttpsInfo(HostnameVerifier hostnameVerifier, SSLSocketFactory ssf) throws ConnException {
		final HttpURLConnection conn = this.conn;

		if (conn instanceof HttpsURLConnection) {
			// HTTPS request
			final HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
			// Authentication domain
			httpsConn.setHostnameVerifier(UrlTool.defaultIfNull(hostnameVerifier, DefaultSSLInfo.TRUST_ANY_HOSTNAME_VERIFIER));
			httpsConn.setSSLSocketFactory(UrlTool.defaultIfNull(ssf, DefaultSSLInfo.DEFAULT_SSF));
		}
		return this;
	}

}
