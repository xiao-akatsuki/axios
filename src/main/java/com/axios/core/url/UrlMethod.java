package com.axios.core.url;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.function.Supplier;

import com.axios.exception.UrlException;

/**
 * [url 的一些操作](Some operations of URL)
 * @description zh - url 的一些操作
 * @description en - Some operations of URL
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-13 20:44:04
 */
public class UrlMethod {

	/**
	 * [将URL字符串转换为URL对象，并做必要验证](Convert the URL string into a URL object and verify it as necessary)
	 * @description zh - 将URL字符串转换为URL对象，并做必要验证
	 * @description en - Convert the URL string into a URL object and verify it as necessary
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:43:39
	 * @param url url
	 * @return java.net.URL
	 */
	public static URL toUrlForHttp(String url) {
		return toUrlForHttp(url, null);
	}

	/**
	 * [将URL字符串转换为URL对象，并做必要验证](Convert the URL string into a URL object and verify it as necessary)
	 * @description zh - 将URL字符串转换为URL对象，并做必要验证
	 * @description en - Convert the URL string into a URL object and verify it as necessary
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:41:45
	 * @param url URL
	 * @param handler URLStreamHandler
	 * @return java.net.URL
	 */
	public static URL toUrlForHttp(String url, URLStreamHandler handler) {
		notBlank(url, "Url is blank !");
		// 编码空白符，防止空格引起的请求异常
		url = encodeBlank(url);
		try {
			return new URL(null, url, handler);
		} catch (MalformedURLException e) {
			throw new UrlException(e);
		}
	}

	/**
	 * [单独编码URL中的空白符，空白符编码为%20](The whitespace in the URL is encoded separately, and the whitespace is encoded as% 20)
	 * @description zh - 单独编码URL中的空白符，空白符编码为%20
	 * @description en - The whitespace in the URL is encoded separately, and the whitespace is encoded as% 20
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:41:23
	 * @param url URL
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
			if (isBlankChar(c)) {
				sb.append("%20");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * [检查给定字符串是否为空白](Checks whether the given string is blank)
	 * @description zh - 检查给定字符串是否为空白
	 * @description en - Checks whether the given string is blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:38:18
	 * @param text 文本
	 * @throws java.lang.IllegalArgumentException
	 * @return T
	 */
	public static <T extends CharSequence> T notBlank(T text) throws IllegalArgumentException {
		return notBlank(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	/**
	 * [检查给定字符串是否为空白](Checks whether the given string is blank)
	 * @description zh - 检查给定字符串是否为空白
	 * @description en - Checks whether the given string is blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:39:30
	 * @param text 文本
	 * @param errorMessage 错误信息
	 * @throws java.lang.IllegalArgumentException
	 * @return T
	 */
	public static <T extends CharSequence> T notBlank(T text, String errorMessage) throws IllegalArgumentException {
		return notBlank(text, () -> new IllegalArgumentException(errorMessage));
	}

	/**
	 * [检查给定字符串是否为空白](Checks whether the given string is blank)
	 * @description zh - 检查给定字符串是否为空白
	 * @description en - Checks whether the given string is blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:40:12
	 * @param text 文本
	 * @param errorMessage 错误信息
	 * @throws java.lang.IllegalArgumentException
	 * @return T
	 */
	public static <T extends CharSequence, X extends Throwable> T notBlank(T text, Supplier<X> errorMessage) throws X {
		if (isBlank(text)) {
			throw errorMessage.get();
		}
		return text;
	}

	/**
	 * [字符串是否为空白](Is the string blank)
	 * @description zh - 字符串是否为空白
	 * @description en - Is the string blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:37:23
	 * @param value 字符串
	 * @return boolean
	 */
	public static boolean isBlank(CharSequence value) {
		int length;

		if ((value == null) || ((length = value.length()) == 0)) {
			return true;
		}

		for (int i = 0; i < length; i++) {
			if (false == isBlankChar(value.charAt(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * [是否空白符](Is it blank)
	 * @description zh - 是否空白符
	 * @description en - Is it blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:36:05
	 * @param value 字符
	 * @return boolean
	 */
	public static boolean isBlankChar(char value) {
		return isBlankChar((int) value);
	}

	/**
	 * [是否空白符](Is it blank)
	 * @description zh - 是否空白符
	 * @description en - Is it blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:36:48
	 * @param value 字符
	 * @return boolean
	 */
	public static boolean isBlankChar(int value) {
		return Character.isWhitespace(value)
				|| Character.isSpaceChar(value)
				|| value == '\ufeff'
				|| value == '\u202a'
				|| value == '\u0000';
	}

}
