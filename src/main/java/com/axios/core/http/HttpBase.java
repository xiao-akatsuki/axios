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
import com.axios.core.tool.ssl.SSLTool;
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
}
