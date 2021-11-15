package com.axios.core.connection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import com.axios.core.urlTool.UrlTool;
import com.axios.exception.ConnException;

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

	public static Connection create(String url,Proxy proxy){
		return new Connection(url, proxy);
	}

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

}
