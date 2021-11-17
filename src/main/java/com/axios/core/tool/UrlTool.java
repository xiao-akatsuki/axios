package com.axios.core.tool;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.axios.core.assertion.Assert;
import com.axios.core.tool.mutable.MutableObj;
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

	/** Content-Type */
	public static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

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

	/**
	 * [检查对象是否为null](Check whether the object is null)
	 * @description zh - 检查对象是否为null
	 * @description en - Check whether the object is null
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:49:42
	 * @param obj 检查的对象
	 * @return boolean
	 */
	public static boolean isNull(Object obj) {
		return null == obj || obj.equals(null);
	}

	/**
	 * [数组是否为非空](Is the array non empty)
	 * @description zh - 数组是否为非空
	 * @description en - Is the array non empty
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:30:53
	 * @param array 数组
	 * @return boolean
	 */
	public static <T> boolean isNotEmpty(T[] array) {
		return (null != array && array.length != 0);
	}

	/**
	 * [如果给定对象为 null 返回默认值](Returns the default value if the given object is null)
	 * @description zh - 如果给定对象为 null 返回默认值
	 * @description en - Returns the default value if the given object is null
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:50:20
	 * @param object 检查的对象
	 * @param defaultValue 默认值
	 * @return T
	 */
	public static <T> T defaultIfNull(final T object, final T defaultValue) {
		return isNull(object) ? defaultValue : object;
	}

	/**
	 * [比较两个字符串](Compare two strings)
	 * @description zh - 比较两个字符串
	 * @description en - Compare two strings
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:55:03
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @return boolean
	 */
	public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
		return equals(str1, str2, true);
	}

	/**
	 * [比较两个字符串是否相等。](Compares whether two strings are equal.)
	 * @description zh - 比较两个字符串是否相等。
	 * @description en - Compares whether two strings are equal.
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-16 18:55:51
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @param ignoreCase 是否忽略大小写
	 * @return boolean
	 */
	public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
		if (null == str1) {
			return str2 == null;
		}
		if (null == str2) {
			return false;
		}

		if (ignoreCase) {
			return str1.toString().equalsIgnoreCase(str2.toString());
		} else {
			return str1.toString().contentEquals(str2);
		}
	}

	/**
	 * [字符串是否为非空白](Is the string non blank)
	 * @description zh - 字符串是否为非空白
	 * @description en - Is the string non blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:43:09
	 * @param str 字符串
	 * @return boolean
	 */
	public static boolean isNotBlank(CharSequence str) {
		return false == isBlank(str);
	}

	/**
	 * [字符串是否为空白](Is the string blank)
	 * @description zh - 字符串是否为空白
	 * @description en - Is the string blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:45:26
	 */
	public static boolean isBlank(CharSequence str) {
		int length;
		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}
		for (int i = 0; i < length; i++) {
			if (false == Assert.isBlankChar(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * [从Http连接的头信息中获得字符集](Get the character set from the header information of the HTTP connection)
	 * @description zh - 从Http连接的头信息中获得字符集
	 * @description en - Get the character set from the header information of the HTTP connection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:23:46
	 * @param conn HttpURLConnection
	 * @return java.lang.String
	 */
	public static String getCharset(HttpURLConnection conn) {
		return conn == null ? null : getCharset(conn.getContentType());
	}

	/**
	 * [从Http连接的头信息中获得字符集](Get the character set from the header information of the HTTP connection)
	 * @description zh - 从Http连接的头信息中获得字符集
	 * @description en - Get the character set from the header information of the HTTP connection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:26:01
	 * @param contentType Content-Type
	 * @return java.lang.String
	 */
	public static String getCharset(String contentType) {
		return isBlank(contentType) ? null :
			get(CHARSET_PATTERN, contentType, 1);
	}

	/**
	 * [在给定字符串中查找给定规则的字符](Finds the character of the given rule in the given string)
	 * @description zh - 在给定字符串中查找给定规则的字符
	 * @description en - Finds the character of the given rule in the given string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:34:47
	 * @param pattern 匹配的正则
	 * @param content 被匹配的内容
	 * @param groupIndex 匹配正则的分组序号
	 * @return java.lang.String
	 */
	public static String get(Pattern pattern, CharSequence content, int groupIndex) {
		if (null == content || null == pattern) {
			return null;
		}
		final MutableObj<String> result = new MutableObj<>();
		get(pattern, content, matcher -> result.set(matcher.group(groupIndex)));
		return result.get();
	}

	/**
	 * [在给定字符串中查找给定规则的字符](Finds the character of the given rule in the given string)
	 * @description zh - 在给定字符串中查找给定规则的字符
	 * @description en - Finds the character of the given rule in the given string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:35:48
	 * @param pattern 匹配的正则
	 * @param content 被匹配的内容
	 * @param consumer 匹配到的内容处理器
	 */
	public static void get(Pattern pattern, CharSequence content, Consumer<Matcher> consumer) {
		if (null == content || null == pattern || null == consumer) {
			return;
		}
		final Matcher m = pattern.matcher(content);
		if (m.find()) {
			consumer.accept(m);
		}
	}

}
