package com.axios.response;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;

import com.axios.core.config.global.GlobalCookieManager;
import com.axios.core.connection.Connection;
import com.axios.core.http.HttpBase;
import com.axios.core.tool.UrlTool;
import com.axios.core.tool.http.HttpTool;
import com.axios.exception.HttpException;
import com.axios.header.RequestHeader;

/**
 * [Http响应类](HTTP response class)
 * @description zh - Http响应类
 * @description en - HTTP response class
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-30 21:26:16
 */
public class HttpResponse extends HttpBase<HttpResponse> implements Closeable {

	/**
	 * 持有连接对象
	 */
	protected Connection httpConnection;
	/**
	 * Http请求原始流
	 */
	protected InputStream in;
	/**
	 * 是否异步，异步下只持有流，否则将在初始化时直接读取body内容
	 */
	private volatile boolean isAsync;
	/**
	 * 响应状态码
	 */
	protected int status;
	/**
	 * 是否忽略读取Http响应体
	 */
	private final boolean ignoreBody;
	/**
	 * 从响应中获取的编码
	 */
	private Charset charsetFromResponse;

	/** ---------------- http response ---------------- */

	protected HttpResponse(Connection httpConnection, Charset charset, boolean isAsync, boolean isIgnoreBody) {
		this.httpConnection = httpConnection;
		this.charset = charset;
		this.isAsync = isAsync;
		this.ignoreBody = isIgnoreBody;
		initWithDisconnect();
	}

	/**
	 * [获取状态码](Get status code)
	 * @description zh - 获取状态码
	 * @description en - Get status code
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-30 21:31:58
	 * @return int
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * [请求是否成功](Is the request successful)
	 * @description zh - 请求是否成功
	 * @description en - Is the request successful
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-30 21:31:31
	 * @return boolean
	 */
	public boolean isOk() {
		return this.status >= 200 && this.status < 300;
	}

	/**
	 * [同步操作](Synchronous operation)
	 * @description zh - 同步操作
	 * @description en - Synchronous operation
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-30 21:31:02
	 * @return com.axios.response.HttpResponse
	 */
	public HttpResponse sync() {
		return this.isAsync ? forceSync() : this;
	}

	/** ---------------- response head ---------------- */

	/**
	 * [获取内容编码](Get content encoding)
	 * @description zh - 获取内容编码
	 * @description en - Get content encoding
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-30 21:33:17
	 * @return java.lang.String
	 */
	public String contentEncoding() {
		return header(RequestHeader.CONTENT_ENCODING);
	}

	/**
	 * [获取内容编码](Get content encoding)
	 * @description zh - 获取内容编码
	 * @description en - Get content encoding
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-01 22:07:19
	 * @return long
	 */
	public long contentLength() {
		long contentLength = Long.valueOf(header(RequestHeader.CONTENT_LENGTH));
		if (contentLength > 0 && (isChunked() || UrlTool.isNotBlank(contentEncoding()))) {
			contentLength = -1;
		}
		return contentLength;
	}

	/**
	 * [是否为gzip压缩过的内容](Is it gzip compressed content)
	 * @description zh - 是否为gzip压缩过的内容
	 * @description en - Is it gzip compressed content
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-01 21:28:12
	 * @return boolean
	 */
	public boolean isGzip() {
		final String contentEncoding = contentEncoding();
		return "gzip".equalsIgnoreCase(contentEncoding);
	}

	/**
	 * [是否为zlib(Deflate)压缩过的内容](Is it zlib (deflate) compressed content)
	 * @description zh - 是否为zlib(Deflate)压缩过的内容
	 * @description en - Is it zlib (deflate) compressed content
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-01 21:57:44
	 * @return boolean
	 */
	public boolean isDeflate() {
		final String contentEncoding = contentEncoding();
		return "deflate".equalsIgnoreCase(contentEncoding);
	}

	/**
	 * [是否为Transfer-Encoding:Chunked的内容](Is it the content of transfer encoding: chunked)
	 * @description zh - 是否为Transfer-Encoding:Chunked的内容
	 * @description en - Is it the content of transfer encoding: chunked
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-01 21:59:34
	 * @return boolean
	 */
	public boolean isChunked() {
		final String transferEncoding = header(RequestHeader.TRANSFER_ENCODING);
		return "Chunked".equalsIgnoreCase(transferEncoding);
	}

	/**
	 * [获取本次请求服务器返回的Cookie信息](Obtain the cookie information returned by the server for this request)
	 * @description zh - 获取本次请求服务器返回的Cookie信息
	 * @description en - Obtain the cookie information returned by the server for this request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-01 22:00:10
	 * @return java.lang.String
	 */
	public String getCookieStr() {
		return header(RequestHeader.SET_COOKIE);
	}

	/**
	 * [获取Cookie](get Cookie)
	 * @description zh - 获取Cookie
	 * @description en - get Cookie
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-01 22:00:56
	 */
	public List<HttpCookie> getCookies() {
		return GlobalCookieManager.getCookies(this.httpConnection);
	}

	/**
	 * [获取Cookie](get Cookie)
	 * @description zh - 获取Cookie
	 * @description en - get Cookie
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-01 22:03:37
	 * @param name cookie名字
	 * @return java.net.HttpCookie
	 */
	public HttpCookie getCookie(String name) {
		List<HttpCookie> cookie = getCookies();
		if (null != cookie) {
			for (HttpCookie httpCookie : cookie) {
				if (httpCookie.getName().equals(name)) {
					return httpCookie;
				}
			}
		}
		return null;
	}

	/**
	 * [获取Cookie值](get cookie value)
	 * @description zh - 获取Cookie值
	 * @description en - get cookie value
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-01 22:04:34
	 * @param name cookie名字
	 * @return java.lang.String
	 */
	public String getCookieValue(String name) {
		HttpCookie cookie = getCookie(name);
		return (null == cookie) ? null : cookie.getValue();
	}

	/** ---------------- response body ---------------- */

	/**
	 * [获得服务区响应流](Get service area response flow)
	 * @description zh - 获得服务区响应流
	 * @description en - Get service area response flow
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-01 22:09:17
	 * @return java.io.InputStream
	 */
	public InputStream bodyStream() {
		return isAsync ?
			this.in :
			new ByteArrayInputStream(this.bodyBytes);
	}

	/**
	 * [获取响应流字节码](Get response stream bytecode)
	 * @description zh - 获取响应流字节码
	 * @description en - Get response stream bytecode
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-01 22:10:08
	 * @return byte[]
	 */
	public byte[] bodyBytes() {
		sync();
		return this.bodyBytes;
	}

	/**
	 * [获取响应主体](Get response body)
	 * @description zh - 获取响应主体
	 * @description en - Get response body
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-01 22:13:04
	 * @throws com.axios.exception.HttpException
	 * @return java.lang.String
	 */
	public String body() throws HttpException {
		return HttpTool.getString(bodyBytes(), this.charset, null == this.charsetFromResponse);
	}



}
