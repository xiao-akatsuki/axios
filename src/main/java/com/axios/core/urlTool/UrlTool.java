package com.axios.core.urlTool;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Map;

import com.axios.core.assertion.Assert;
import com.axios.exception.ConnException;

/**
 * [整合的工具类](Integrated tools)
 * @description zh - 整合的工具类
 * @description en - Integrated tools
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-16 18:35:23
 */
public class UrlTool {

	/**
	 * [将URL字符串转换为URL对象，并做必要验证](Convert the URL string into a URL object and verify it as necessary)
	 * @description zh - 将URL字符串转换为URL对象，并做必要验证
	 * @description en - Convert the URL string into a URL object and verify it as necessary
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 20:54:07
	 * @param text url文本
	 * @return java.net.URL
	 */
	public static URL toUrlForHttp(String text) {
		return toUrlForHttp(text, null);
	}

	/**
	 * [将URL字符串转换为URL对象，并做必要验证](Convert the URL string into a URL object and verify it as necessary)
	 * @description zh - 将URL字符串转换为URL对象，并做必要验证
	 * @description en - Convert the URL string into a URL object and verify it as necessary
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 20:55:30
	 * @param text URL文本
	 * @param handler URLStreamHandler
	 * @return java.net.URL
	 */
	public static URL toUrlForHttp(String text, URLStreamHandler handler) {
		Assert.notBlank(text, "Url is blank !");
		// 编码空白符，防止空格引起的请求异常
		text = encodeBlank(text);
		try {
			return new URL(null, text, handler);
		} catch (MalformedURLException e) {
			throw new ConnException(e);
		}
	}

	/**
	 * [单独编码URL中的空白符，空白符编码为%20](The whitespace in the URL is encoded separately, and the whitespace is encoded as% 20)
	 * @description zh - 单独编码URL中的空白符，空白符编码为%20
	 * @description en - The whitespace in the URL is encoded separately, and the whitespace is encoded as% 20
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 20:56:37
	 * @param url URL文本
	 * @return java.lang.String
	 */
	public static String encodeBlank(CharSequence url) {
		if (url == null) {
			return null;
		}

		int len = url.length();
		final StringBuilder sb = new StringBuilder(len);
		char c;
		for (int i = 0; i < len; i++) {
			c = url.charAt(i);
			if (Assert.isBlankChar(c)) {
				sb.append("%20");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * [判断该集合是否为空](Judge whether the collection is empty)
	 * @description zh - 判断该集合是否为空
	 * @description en - Judge whether the collection is empty
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:34:34
	 * @param map 集合
	 * @return boolean
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return null != map && false == map.isEmpty();
	}

	/**
	 * [当给定字符串为null时，转换为“”](Converts to '' when the given string is null)
	 * @description zh - 当给定字符串为null时，转换为“”
	 * @description en - Converts to '' when the given string is null
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:36:26
	 */
	public static String nullToEmpty(CharSequence str) {
		return (str == null) ? "" : str.toString();
	}



}
