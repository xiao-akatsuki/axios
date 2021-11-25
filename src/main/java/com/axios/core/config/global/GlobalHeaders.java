package com.axios.core.config.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.axios.core.tool.UrlTool;
import com.axios.core.tool.http.HttpTool;
import com.axios.header.RequestHeader;

/**
 * [全局头部信息](Global header information)
 * @description zh - 全局头部信息
 * @description en - Global header information
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-25 19:14:37
 */
public enum GlobalHeaders {

	INSTANCE;

	/** Storage header information */
	public Map<String, List<String>> headers = new HashMap<>();

	GlobalHeaders() {
		putDefault(false);
	}

	/**
	 * [加入默认的头部信息](Add default header information)
	 * @description zh - 加入默认的头部信息
	 * @description en - Add default header information
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:18:24
	 * @param isReset 是否重置所有头部信息
	 * @return com.axios.core.config.global.GlobalHeaders
	 */
	public GlobalHeaders putDefault(boolean isReset) {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		if (isReset) {
			this.headers.clear();
		}
		header(RequestHeader.ACCEPT, "text/html,application/json,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8", true);
		header(RequestHeader.ACCEPT_ENCODING, "gzip, deflate", true);
		header(RequestHeader.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8", true);
		header(RequestHeader.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36 Hutool", true);
		return this;
	}

	/**
	 * [根据name获取头信息](Get header information according to name)
	 * @description zh - 根据name获取头信息
	 * @description en - Get header information according to name
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:28:28
	 * @param name header名字
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
	 * @since 2021-11-25 19:27:31
	 * @param name header名字
	 * @return java.util.List<java.lang.String>
	 */
	public List<String> headerList(String name) {
		if (UrlTool.isBlank(name)) {
			return null;
		}

		return headers.get(name.trim());
	}

	/**
	 * [根据name获取头信息](Get header information according to name)
	 * @description zh - 根据name获取头信息
	 * @description en - Get header information according to name
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:26:42
	 * @param name 请求头
	 * @return java.lang.String
	 */
	public String header(RequestHeader name) {
		if (null == name) {
			return null;
		}
		return header(name.toString());
	}

	/**
	 * [设置一个header](Set a header)
	 * @description zh - 设置一个header
	 * @description en - Set a header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:25:24
	 * @param name header名字
	 * @param value header值
	 * @param isOverride 是否覆盖
	 * @return com.axios.core.config.global.GlobalHeaders
	 */
	public GlobalHeaders header(RequestHeader name, String value, boolean isOverride) {
		return header(name.toString(), value, isOverride);
	}

	/**
	 * [设置一个header](Set a header)
	 * @description zh - 设置一个header
	 * @description en - Set a header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:24:59
	 * @param name header名字
	 * @param value header值
	 * @return com.axios.core.config.global.GlobalHeaders
	 */
	public GlobalHeaders header(RequestHeader name, String value) {
		return header(name.toString(), value, true);
	}

	/**
	 * [设置一个header](Set a header)
	 * @description zh - 设置一个header
	 * @description en - Set a header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:23:57
	 * @param name header名字
	 * @param value header值
	 * @return com.axios.core.config.global.GlobalHeaders
	 */
	public GlobalHeaders header(String name, String value) {
		return header(name, value, true);
	}

	/**
	 * [设置请求头](Set request header)
	 * @description zh - 设置请求头
	 * @description en - Set request header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:23:15
	 * @param headers 请求头
	 * @return com.axios.core.config.global.GlobalHeaders
	 */
	public GlobalHeaders header(Map<String, List<String>> headers) {
		if (HttpTool.isEmpty(headers)) {
			return this;
		}
		String name;
		for (Entry<String, List<String>> entry : headers.entrySet()) {
			name = entry.getKey();
			for (String value : entry.getValue()) {
				this.header(name, UrlTool.nullToEmpty(value), false);
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
	 * @since 2021-11-25 19:26:10
	 * @param name header名字
	 * @param value header值
	 * @param isOverride 是否覆盖
	 * @return com.axios.core.config.global.GlobalHeaders
	 */
	public GlobalHeaders header(String name, String value, boolean isOverride) {
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
		return this;
	}

	/**
	 * [移除一个头信息](Remove a header)
	 * @description zh - 移除一个头信息
	 * @description en - Remove a header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:21:49
	 * @param name headerName
	 * @return com.axios.core.config.global.GlobalHeaders
	 */
	public GlobalHeaders removeHeader(String name) {
		if (name != null) {
			headers.remove(name.trim());
		}
		return this;
	}

	/**
	 * [移除一个头信息](Remove a header)
	 * @description zh - 移除一个头信息
	 * @description en - Remove a header
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:21:07
	 * @param name headerName
	 * @return com.axios.core.config.global.GlobalHeaders
	 */
	public GlobalHeaders removeHeader(RequestHeader name) {
		return removeHeader(name.toString());
	}

	/**
	 * [获取headers](Get headers)
	 * @description zh - 获取headers
	 * @description en - Get headers
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:20:17
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
	 * @since 2021-11-25 19:19:41
	 * @return com.axios.core.config.global.GlobalHeaders
	 */
	public GlobalHeaders clearHeaders() {
		this.headers.clear();
		return this;
	}
}
