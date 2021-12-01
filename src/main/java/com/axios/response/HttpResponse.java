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

import com.axios.core.connection.Connection;
import com.axios.core.http.HttpBase;
import com.axios.core.tool.UrlTool;
import com.axios.core.tool.http.HttpTool;
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

	public long contentLength() {
		long contentLength = HttpTool.toLong(header(RequestHeader.CONTENT_LENGTH), -1L);
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

}
