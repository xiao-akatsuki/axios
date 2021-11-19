package com.axios.core.http;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.axios.core.map.CaseInsensitiveMap;
import com.axios.core.tool.UrlTool;
import com.axios.core.tool.http.HttpTool;
import com.axios.header.RequestHeader;

/**
 * [HTTP 基类](HTTP base class)
 * @description zh - HTTP 基类
 * @description en - HTTP base class
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-19 16:52:49
 */
@SuppressWarnings("unchecked")
public abstract class HttpBase<T> {

	/**
	 * HTTP/1.0
	 */
	public static final String HTTP_1_0 = "HTTP/1.0";

	/**
	 * HTTP/1.1
	 */
	public static final String HTTP_1_1 = "HTTP/1.1";

	/**
	 * 存储头信息
	 */
	protected Map<String, List<String>> headers = new HashMap<>();

	/**
	 * 编码
	 */
	protected Charset charset = StandardCharsets.UTF_8;

	/**
	 * http版本
	 */
	protected String httpVersion = HTTP_1_1;

	/**
	 * 存储主体
	 */
	protected byte[] bodyBytes;

	/**
	 * [根据name获取头信息](Get header information according to name)
	 * @description zh - 根据name获取头信息
	 * @description en - Get header information according to name
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 16:53:20
	 * @param name Header名
	 * @return java.lang.String
	 */
	public String header(String name) {
		final List<String> values = headerList(name);
		if (HttpTool.isEmpty(values)) {
			return null;
		}
		return values.get(0);
	}

	/**
	 * [根据name获取头信息列表](Get header information list according to name)
	 * @description zh - 根据name获取头信息列表
	 * @description en - Get header information list according to name
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 16:54:00
	 * @param name Header名
	 * @return java.util.List<java.lang.String>
	 */
	public List<String> headerList(String name) {
		if (UrlTool.isBlank(name)) {
			return null;
		}
		final CaseInsensitiveMap<String, List<String>> headersIgnoreCase = new CaseInsensitiveMap<>(this.headers);
		return headersIgnoreCase.get(name.trim());
	}

	/**
	 * [根据name获取头信息](Get header information according to name)
	 * @description zh - 根据name获取头信息
	 * @description en - Get header information according to name
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 17:06:48
	 * @param name Header名
	 * @return java.lang.String
	 */
	public String header(RequestHeader name) {
		return null == name ? null : header(name.toString());
	}

	/**
	 * [设置一个header](Set a header)
	 * @description zh - 设置一个header
	 * @description en - Set a header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:17:05
	 * @param name Header名
	 * @param value Header值
	 * @param isOverride 是否覆盖已有值
	 * @return T
	 */
	public T header(RequestHeader name, String value, boolean isOverride) {
		return header(name.toString(), value, isOverride);
	}

	/**
	 * [设置一个header](Set a header)
	 * @description zh - 设置一个header
	 * @description en - Set a header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:17:52
	 * @param name Header名
	 * @param value Header值
	 * @return T
	 */
	public T header(RequestHeader name, String value) {
		return header(name.toString(), value, true);
	}

	/**
	 * [设置一个header](Set a header)
	 * @description zh - 设置一个header
	 * @description en - Set a header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:18:23
	 * @param name Header名
	 * @param value Header值
	 * @return T
	 */
	public T header(String name, String value) {
		return header(name, value, true);
	}

	/**
	 * [设置一个header](Set a header)
	 * @description zh - 设置一个header
	 * @description en - Set a header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:15:56
	 * @param name Header名
	 * @param value Header值
	 * @param isOverride 是否覆盖已有值
	 * @return T
	 */
	public T header(String name, String value, boolean isOverride) {
		if (null != name && null != value) {
			final List<String> values = headers.get(name.trim());
			if (isOverride || HttpTool.isEmpty(values)) {
				final ArrayList<String> valueList = new ArrayList<>();
				valueList.add(value);
				headers.put(name.trim(), valueList);
			} else {
				values.add(value.trim());
			}
		}
		return (T) this;
	}

	/**
	 * [设置header](Set header)
	 * @description zh - 设置header
	 * @description en - Set header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:19:51
	 * @param headers 请求头
	 * @param isOverride 是否覆盖已有头信息
	 * @return T
	 */
	public T headerMap(Map<String, String> headers, boolean isOverride) {
		if (HttpTool.isEmpty(headers)) {
			return (T) this;
		}
		for (Entry<String, String> entry : headers.entrySet()) {
			this.header(entry.getKey(), UrlTool.nullToEmpty(entry.getValue()), isOverride);
		}
		return (T) this;
	}

	/**
	 * [设置请求头](Set request header)
	 * @description zh - 设置请求头
	 * @description en - Set request header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:24:04
	 * @param headers 请求头
	 * @return T
	 */
	public T header(Map<String, List<String>> headers) {
		return header(headers, false);
	}

	/**
	 * [设置请求头](Set request header)
	 * @description zh - 设置请求头
	 * @description en - Set request header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:23:18
	 * @param headers 请求头
	 * @param isOverride 是否覆盖已有头信息
	 * @return T
	 */
	public T header(Map<String, List<String>> headers, boolean isOverride) {
		if (HttpTool.isEmpty(headers)) {
			return (T) this;
		}
		String name;
		for (Entry<String, List<String>> entry : headers.entrySet()) {
			name = entry.getKey();
			for (String value : entry.getValue()) {
				this.header(name, UrlTool.nullToEmpty(value), isOverride);
			}
		}
		return (T) this;
	}

	/**
	 * [新增请求头](Add request header)
	 * @description zh - 新增请求头
	 * @description en - Add request header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:24:55
	 * @param headers 请求头
	 * @return T
	 */
	public T addHeaders(Map<String, String> headers) {
		if (HttpTool.isEmpty(headers)) {
			return (T) this;
		}
		for (Entry<String, String> entry : headers.entrySet()) {
			this.header(entry.getKey(), UrlTool.nullToEmpty(entry.getValue()), false);
		}
		return (T) this;
	}

	/**
	 * [移除一个头信息](Remove a header)
	 * @description zh - 移除一个头信息
	 * @description en - Remove a header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:26:28
	 * @param name Header名
	 * @return T
	 */
	public T removeHeader(RequestHeader name) {
		return removeHeader(name.toString());
	}

	/**
	 * [移除一个头信息](Remove a header)
	 * @description zh - 移除一个头信息
	 * @description en - Remove a header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:25:33
	 * @param name Header名
	 * @return T
	 */
	public T removeHeader(String name) {
		if (name != null) {
			headers.remove(name.trim());
		}
		return (T) this;
	}

	/**
	 * [获取headers](Get headers)
	 * @description zh - 获取headers
	 * @description en - Get headers
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:27:14
	 * @return java.util.Map<java.lang.String, java.util.List<java.lang.String>>
	 */
	public Map<String, List<String>> headers() {
		return Collections.unmodifiableMap(headers);
	}

	/**
	 * [清除所有头信息](Clear all headers)
	 * @description zh - 清除所有头信息
	 * @description en - Clear all headers
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:28:05
	 * @return T
	 */
	public T clearHeaders() {
		this.headers.clear();
		return (T) this;
	}

	/**
	 * [HTTP 版本](HTTP version)
	 * @description zh - HTTP 版本
	 * @description en - HTTP version
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:28:43
	 * @return java.lang.String
	 */
	public String httpVersion() {
		return httpVersion;
	}

	/**
	 * [设置 HTTP 版本](Set HTTP version)
	 * @description zh - 设置 HTTP 版本
	 * @description en - Set HTTP version
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:29:25
	 * @param httpVersion http版本
	 * @return T
	 */
	public T httpVersion(String httpVersion) {
		this.httpVersion = httpVersion;
		return (T) this;
	}

	/**
	 * [返回字符集](Return character set)
	 * @description zh - 返回字符集
	 * @description en - Return character set
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:30:16
	 * @return java.lang.String
	 */
	public String charset() {
		return charset.name();
	}

	/**
	 * [设置字符集](Set character set)
	 * @description zh - 设置字符集
	 * @description en - Set character set
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:31:45
	 * @param charset 字符集
	 * @return T
	 */
	public T charset(Charset charset) {
		if (null != charset) {
			this.charset = charset;
		}
		return (T) this;
	}

	/**
	 * [设置字符集](Set character set)
	 * @description zh - 设置字符集
	 * @description en - Set character set
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:30:55
	 * @param charset 字符集
	 * @return T
	 */
	public T charset(String charset) {
		if (UrlTool.isNotBlank(charset)) {
			charset(Charset.forName(charset));
		}
		return (T) this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Request Headers: ").append("\r\n");
		for (Entry<String, List<String>> entry : this.headers.entrySet()) {
			sb.append("    ")
			  .append(entry.getKey())
			  .append(": ")
			  .append(HttpTool.join(entry.getValue(), ","))
			  .append("\r\n");
		}

		sb.append("Request Body: ").append("\r\n");
		sb.append("    ").append(HttpTool.str(this.bodyBytes, this.charset)).append("\r\n");

		return sb.toString();
	}
}
