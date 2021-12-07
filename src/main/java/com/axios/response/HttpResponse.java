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
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map.Entry;

import com.axios.core.assertion.Assert;
import com.axios.core.config.global.GlobalCookieManager;
import com.axios.core.connection.Connection;
import com.axios.core.http.HttpBase;
import com.axios.core.strem.HttpInputStream;
import com.axios.core.tool.UrlTool;
import com.axios.core.tool.file.FileTool;
import com.axios.core.tool.http.HttpTool;
import com.axios.core.tool.io.FastByteArrayOutputStream;
import com.axios.core.tool.io.IoTool;
import com.axios.exception.HttpException;
import com.axios.exception.IORuntimeException;
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
	public Connection httpConnection;
	/**
	 * Http请求原始流
	 */
	public InputStream in;
	/**
	 * 是否异步，异步下只持有流，否则将在初始化时直接读取body内容
	 */
	private volatile boolean isAsync;
	/**
	 * 响应状态码
	 */
	public int status;
	/**
	 * 是否忽略读取Http响应体
	 */
	private final boolean ignoreBody;
	/**
	 * 从响应中获取的编码
	 */
	private Charset charsetFromResponse;

	/** ---------------- http response ---------------- */

	public HttpResponse(Connection httpConnection, Charset charset, boolean isAsync, boolean isIgnoreBody) {
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

	/**
	 * [将响应内容写出到OutputStream](Write out the response content to the OutputStream)
	 * @description zh - 将响应内容写出到OutputStream
	 * @description en - Write out the response content to the OutputStream
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:22:34
	 * @param out OutputStream
	 * @param isCloseOut 是否关闭输出流
	 * @return long
	 */
	public long writeBody(OutputStream out, boolean isCloseOut) {
		Assert.notNull(out, "[out] must be not null!");
		final long contentLength = contentLength();
		try {
			return copyBody(bodyStream(), out, contentLength);
		} finally {
			IoTool.close(this);
			if (isCloseOut) {
				IoTool.close(out);
			}
		}
	}

	/**
	 * [将响应内容写出到文件](Write out the response content to the file)
	 * @description zh - 将响应内容写出到文件
	 * @description en - Write out the response content to the file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:24:55
	 * @param targetFileOrDir
	 * @return long
	 */
	public long writeBody(File targetFileOrDir) {
		Assert.notNull(targetFileOrDir, "[targetFileOrDir] must be not null!");
		final File outFile = completeFileNameFromHeader(targetFileOrDir);
		return writeBody(FileTool.getOutputStream(outFile), true);
	}

	/**
	 * [将响应内容写出到文件](Write out the response content to the file)
	 * @description zh - 将响应内容写出到文件
	 * @description en - Write out the response content to the file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:27:36
	 * @param targetFileOrDir 写出到的文件或目录
	 * @param tempFileSuffix 临时文件后缀
	 * @return long
	 */
	public long writeBody(File targetFileOrDir, String tempFileSuffix) {
		Assert.notNull(targetFileOrDir, "[targetFileOrDir] must be not null!");
		File outFile = completeFileNameFromHeader(targetFileOrDir);
		tempFileSuffix = UrlTool.isBlank(tempFileSuffix) ? ".temp" : UrlTool.prependIfMissing(tempFileSuffix, ".", false, ".");
		final String fileName = outFile.getName();
		final String tempFileName = fileName + tempFileSuffix;
		outFile = new File(outFile.getParentFile(), tempFileName);
		long length;
		try {
			length = writeBody(outFile);
			FileTool.rename(outFile, fileName, true);
		} catch (Throwable e) {
			FileTool.del(outFile);
			throw new HttpException(e);
		}
		return length;
	}

	/**
	 * [将响应内容写出到文件](Write out the response content to the file)
	 * @description zh - 将响应内容写出到文件
	 * @description en - Write out the response content to the file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:28:42
	 * @param targetFileOrDir 写出到的文件或目录
	 * @return java.io.File
	 */
	public File writeBodyForFile(File targetFileOrDir) {
		Assert.notNull(targetFileOrDir, "[targetFileOrDir] must be not null!");
		final File outFile = completeFileNameFromHeader(targetFileOrDir);
		writeBody(FileTool.getOutputStream(outFile), true);
		return outFile;
	}

	/**
	 * [将响应内容写出到文件](Write out the response content to the file)
	 * @description zh - 将响应内容写出到文件
	 * @description en - Write out the response content to the file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:30:43
	 * @param targetFileOrDir 写出到的文件或目录
	 * @return long
	 */
	public long writeBody(String targetFileOrDir) {
		return writeBody(FileTool.file(targetFileOrDir));
	}

	/** ---------------- Override ---------------- */

	@Override
	public void close() {
		IoTool.close(this.in);
		this.in = null;
		// 关闭连接
		this.httpConnection.disconnectQuietly();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Response Headers: ").append("\r\n");
		for (Entry<String, List<String>> entry : this.headers.entrySet()) {
			sb.append("    ").append(entry).append("\r\n");
		}
		sb.append("Response Body: ").append("\r\n");
		sb.append("    ").append(this.body()).append("\r\n");
		return sb.toString();
	}

	/**
	 * [从响应头补全下载文件名](Complete the download file name from the response header)
	 * @description zh - 从响应头补全下载文件名
	 * @description en - Complete the download file name from the response header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:36:22
	 * @param targetFileOrDir 写出到的文件或目录
	 * @return java.io.File
	 */
	public File completeFileNameFromHeader(File targetFileOrDir) {
		if (false == targetFileOrDir.isDirectory()) {
			return targetFileOrDir;
		}
		// 从头信息中获取文件名
		String fileName = getFileNameFromDisposition();
		if (UrlTool.isBlank(fileName)) {
			final String path = httpConnection.getUrl().getPath();
			// 从路径中获取文件名
			fileName = UrlTool.subSuf(path, path.lastIndexOf('/') + 1);
			if (UrlTool.isBlank(fileName)) {
				// 编码后的路径做为文件名
				fileName = UrlTool.encodeQuery(path, StandardCharsets.UTF_8);
			}
		}
		return FileTool.file(targetFileOrDir, fileName);
	}

	/** ---------------- private ---------------- */

	/**
	 * [初始化Http响应，并在报错时关闭连接。](Initialize the HTTP response and close the connection when an error is reported.)
	 * @description zh - 初始化Http响应，并在报错时关闭连接。
	 * @description en - Initialize the HTTP response and close the connection when an error is reported.
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:38:55
	 * @throws com.axios.exception.HttpException
	 * @return com.axios.response.HttpResponse
	 */
	private HttpResponse initWithDisconnect() throws HttpException {
		try {
			init();
		} catch (HttpException e) {
			this.httpConnection.disconnectQuietly();
			throw e;
		}
		return this;
	}

	/**
	 * [初始化Http响应](Initialize HTTP response)
	 * @description zh - 初始化Http响应
	 * @description en - Initialize HTTP response
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:39:47
	 * @throws com.axios.exception.HttpException
	 * @return com.axios.response.HttpResponse
	 */
	private HttpResponse init() throws HttpException {
		// 获取响应状态码
		try {
			this.status = httpConnection.responseCode();
		} catch (IOException e) {
			if (false == (e instanceof FileNotFoundException)) {
				throw new HttpException(e);
			}
			// 服务器无返回内容，忽略之
		}
		// 读取响应头信息
		try {
			this.headers = httpConnection.headers();
		} catch (IllegalArgumentException e) {
			// ignore
			// StaticLog.warn(e, e.getMessage());
		}
		// 存储服务端设置的Cookie信息
		GlobalCookieManager.store(httpConnection);
		// 获取响应编码
		final Charset charset = httpConnection.getCharset();
		this.charsetFromResponse = charset;
		if (null != charset) {
			this.charset = charset;
		}
		// 获取响应内容流
		this.in = new HttpInputStream(this);
		// 同步情况下强制同步
		return this.isAsync ? this : forceSync();
	}

	/**
	 * [强制同步，用于初始化](Force synchronization for initialization)
	 * @description zh - 强制同步，用于初始化
	 * @description en - Force synchronization for initialization
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:41:56
	 * @return com.axios.response.HttpResponse
	 */
	private HttpResponse forceSync() {
		// 非同步状态转为同步状态
		try {
			this.readBody(this.in);
		} catch (IORuntimeException e) {
			//noinspection StatementWithEmptyBody
			if (e.getCause() instanceof FileNotFoundException) {
				// 服务器无返回内容，忽略之
			} else {
				throw new HttpException(e);
			}
		} finally {
			if (this.isAsync) {
				this.isAsync = false;
			}
			this.close();
		}
		return this;
	}

	/**
	 * [读取主体](Read body)
	 * @description zh - 读取主体
	 * @description en - Read body
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:42:42
	 * @param in InputStream
	 * @throws com.axios.exception.IORuntimeException
	 */
	private void readBody(InputStream in) throws IORuntimeException {
		if (ignoreBody) {
			return;
		}
		final long contentLength = contentLength();
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream((int) contentLength);
		copyBody(in, out, contentLength, null);
		this.bodyBytes = out.toByteArray();
	}

	/**
	 * [将响应内容写出到OutputStream](Write out the response content to the OutputStream)
	 * @description zh - 将响应内容写出到OutputStream
	 * @description en - Write out the response content to the OutputStream
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:44:21
	 * @param in InputStream
	 * @param out OutputStream
	 * @param contentLength 总长度
	 * @return long
	 */
	private static long copyBody(InputStream in, OutputStream out, long contentLength) {
		if (null == out) {
			throw new NullPointerException("[out] is null!");
		}
		long copyLength = -1;
		try {
			copyLength = IoTool.copy(in, out, 2 << 12, contentLength);
		} catch (IORuntimeException e) {
			//noinspection StatementWithEmptyBody
			if (e.getCause() instanceof EOFException || UrlTool.containsIgnoreCase(e.getMessage(), "Premature EOF")) {
				// 忽略读取HTTP流中的EOF错误
			} else {
				throw e;
			}
		}
		return copyLength;
	}

	/**
	 * [从Content-Disposition头中获取文件名](Get the file name from the content disposition header)
	 * @description zh - 从Content-Disposition头中获取文件名
	 * @description en - Get the file name from the content disposition header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-02 20:45:31
	 * @return java.lang.String
	 */
	private String getFileNameFromDisposition() {
		String fileName = null;
		final String disposition = header(RequestHeader.CONTENT_DISPOSITION);
		if (UrlTool.isNotBlank(disposition)) {
			fileName = ReUtil.get("filename=\"(.*?)\"", disposition, 1);
			if (UrlTool.isBlank(fileName)) {
				fileName = UrlTool.subAfter(disposition, "filename=", true);
			}
		}
		return fileName;
	}
}
