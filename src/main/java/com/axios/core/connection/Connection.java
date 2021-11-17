package com.axios.core.connection;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import com.axios.core.config.ssl.DefaultSSLInfo;
import com.axios.core.requestMethod.RequestMethod;
import com.axios.core.tool.UrlTool;
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

	/**
	 * [关闭缓存](Close cache)
	 * @description zh - 关闭缓存
	 * @description en - Close cache
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:55:53
	 * @return com.axios.core.connection.Connection
	 */
	public Connection disableCache() {
		this.conn.setUseCaches(false);
		return this;
	}

	/**
	 * [设置连接超时](Set connection timeout)
	 * @description zh - 设置连接超时
	 * @description en - Set connection timeout
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:56:30
	 * @param timeout 超时时间
	 * @return com.axios.core.connection.Connection
	 */
	public Connection setConnectTimeout(int timeout) {
		if (timeout > 0 && null != this.conn) {
			this.conn.setConnectTimeout(timeout);
		}
		return this;
	}

	/**
	 * [设置读取超时](Set read timeout)
	 * @description zh - 设置读取超时
	 * @description en - Set read timeout
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:57:25
	 * @param timeout 超时时间
	 * @return com.axios.core.connection.Connection
	 */
	public Connection setReadTimeout(int timeout) {
		if (timeout > 0 && null != this.conn) {
			this.conn.setReadTimeout(timeout);
		}
		return this;
	}

	/**
	 * [设置连接和读取的超时时间](Set connection and read timeout)
	 * @description zh - 设置连接和读取的超时时间
	 * @description en - Set connection and read timeout
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:58:02
	 * @param timeout 超时时间
	 * @return com.axios.core.connection.Connection
	 */
	public Connection setConnectionAndReadTimeout(int timeout) {
		setConnectTimeout(timeout);
		setReadTimeout(timeout);
		return this;
	}

	/**
	 * [设置Cookie](Set cookies)
	 * @description zh - 设置Cookie
	 * @description en - Set cookies
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:59:41
	 * @param cookie Cookie
	 * @return com.axios.core.connection.Connection
	 */
	public Connection setCookie(String cookie) {
		if (cookie != null) {
			header(RequestHeader.COOKIE, cookie, true);
		}
		return this;
	}

	/**
	 * [采用流方式上传数据，无需本地缓存数据。](Upload data in streaming mode without local cache data.)
	 * @description zh - 采用流方式上传数据，无需本地缓存数据。
	 * @description en - Upload data in streaming mode without local cache data.
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:07:01
	 * @param blockSize 块大小（bytes数）
	 * @return com.axios.core.connection.Connection
	 */
	public Connection setChunkedStreamingMode(int blockSize) {
		if (blockSize > 0) {
			conn.setChunkedStreamingMode(blockSize);
		}
		return this;
	}

	/**
	 * [设置自动HTTP 30X跳转](Set automatic HTTP 30x jump)
	 * @description zh - 设置自动HTTP 30X跳转
	 * @description en - Set automatic HTTP 30x jump
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:08:13
	 * @param isInstanceFollowRedirects 是否自定跳转
	 * @return com.axios.core.connection.Connection
	 */
	public Connection setInstanceFollowRedirects(boolean isInstanceFollowRedirects) {
		conn.setInstanceFollowRedirects(isInstanceFollowRedirects);
		return this;
	}

	/**
	 * [连接](connect)
	 * @description zh - 连接
	 * @description en - connect
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:09:08
	 * @throws java.io.IOException
	 * @return com.axios.core.connection.Connection
	 */
	public Connection connect() throws IOException {
		if (null != this.conn) {
			this.conn.connect();
		}
		return this;
	}

	/**
	 * [静默断开连接。不抛出异常](Silently disconnect. Do not throw exceptions)
	 * @description zh - 静默断开连接。不抛出异常
	 * @description en - Silently disconnect. Do not throw exceptions
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:09:55
	 * @return com.axios.core.connection.Connection
	 */
	public Connection disconnectQuietly() {
		try {
			disconnect();
		} catch (Throwable e) {
			// ignore
		}

		return this;
	}

	/**
	 * [断开连接](Disconnect)
	 * @description zh - 断开连接
	 * @description en - Disconnect
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:10:37
	 * @return com.axios.core.connection.Connection
	 */
	public Connection disconnect() {
		if (null != this.conn) {
			this.conn.disconnect();
		}
		return this;
	}

	/**
	 * [获得输入流对象](Get input stream object)
	 * @description zh - 获得输入流对象
	 * @description en - Get input stream object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:11:21
	 * @throws java.io.IOException
	 * @return java.io.InputStream
	 */
	public InputStream getInputStream() throws IOException {
		if (null != this.conn) {
			return this.conn.getInputStream();
		}
		return null;
	}

	/**
	 * [当返回错误代码时，获得错误内容流](When the error code is returned, the error content stream is obtained)
	 * @description zh - 当返回错误代码时，获得错误内容流
	 * @description en - When the error code is returned, the error content stream is obtained
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:12:09
	 */
	public InputStream getErrorStream() {
		if (null != this.conn) {
			return this.conn.getErrorStream();
		}
		return null;
	}

	/**
	 * [获取输出流对象 输出流对象用于发送数据](Gets the output stream object used to send data)
	 * @description zh - 获取输出流对象 输出流对象用于发送数据
	 * @description en - Gets the output stream object used to send data
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:13:36
	 * @throws java.io.IOException
	 * @return java.io.OutputStream
	 */
	public OutputStream getOutputStream() throws IOException {
		if (null == this.conn) {
			throw new IOException("HttpURLConnection has not been initialized.");
		}
		final RequestMethod method = getMethod();
		this.conn.setDoOutput(true);
		final OutputStream out = this.conn.getOutputStream();
		if(method == RequestMethod.GET && method != getMethod()){
			this.conn.setRequestMethod(RequestMethod.GET.name());
		}
		return out;
	}

	/**
	 * [获取响应码](Get response code)
	 * @description zh - 获取响应码
	 * @description en - Get response code
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:14:44
	 * @throws java.io.IOException
	 * @return int
	 */
	public int responseCode() throws IOException {
		if (null != this.conn) {
			return this.conn.getResponseCode();
		}
		return 0;
	}

	/**
	 * [获得字符集编码](Get character set encoding)
	 * @description zh - 获得字符集编码
	 * @description en - Get character set encoding
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:16:43
	 * @return java.lang.String
	 */
	public String getCharsetName() {
		return UrlTool.getCharset(conn);
	}

	/**
	 * [获取字符集编码](Get character set encoding)
	 * @description zh - 获取字符集编码
	 * @description en - Get character set encoding
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:17:24
	 * @return java.nio.charset.Charset
	 */
	public Charset getCharset() {
		Charset charset = null;
		final String charsetName = getCharsetName();
		if (UrlTool.isNotBlank(charsetName)) {
			try {
				charset = Charset.forName(charsetName);
			} catch (UnsupportedCharsetException e) {
				// ignore
			}
		}
		return charset;
	}

	/**
	 * [初始化http或https请求参数](Initialize HTTP or HTTPS request parameters)
	 * @description zh - 初始化http或https请求参数
	 * @description en - Initialize HTTP or HTTPS request parameters
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:19:47
	 * @throws java.io.IOException
	 * @return java.net.HttpURLConnection
	 */
	private HttpURLConnection openHttp() throws IOException {
		final URLConnection conn = openConnection();
		if (false == conn instanceof HttpURLConnection) {
			// prevent conversion exceptions caused by other protocols
			throw new ConnException("'"+ conn.getClass().getName() +"' of URL [" +  this.url + "] is not a http connection, make sure URL is format for http.");
		}
		return (HttpURLConnection) conn;
	}

	/**
	 * [建立连接](Establish connection)
	 * @description zh - 建立连接
	 * @description en - Establish connection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:20:37
	 * @throws java.io.IOException
	 * @return java.net.URLConnection
	 */
	private URLConnection openConnection() throws IOException {
		return (null == this.proxy) ? url.openConnection() : url.openConnection(this.proxy);
	}
}
