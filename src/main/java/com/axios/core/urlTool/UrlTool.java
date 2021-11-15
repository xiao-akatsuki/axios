package com.axios.core.urlTool;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;

import com.axios.core.assertion.Assert;
import com.axios.exception.ConnException;

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

}
