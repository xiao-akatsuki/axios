package com.axios.core.tool;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
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

	/**
	 * 用于建立十六进制字符的输出的小写字符数组
	 */
	private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	/**
	 * 用于建立十六进制字符的输出的大写字符数组
	 */
	private static final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

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

	/**
	 * [转URL为URI](Convert URL to URI)
	 * @description zh - 转URL为URI
	 * @description en - Convert URL to URI
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:18:56
	 * @param url URL
	 * @throws com.axios.exception.ConnException
	 * @return java.net.URI
	 */
	public static URI toURI(URL url) throws ConnException {
		return toURI(url);
	}

	/**
	 * [转URL为URI](Convert URL to URI)
	 * @description zh - 转URL为URI
	 * @description en - Convert URL to URI
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:19:36
	 * @param location URL
	 * @throws com.axios.exception.ConnException
	 * @return java.net.URI
	 */
	public static URI toURI(String location) throws ConnException {
		try {
			return new URI(trim(location));
		} catch (URISyntaxException e) {
			throw new ConnException(e);
		}
	}

	/**
	 * [除去字符串头尾部的断言为真的字符](Remove the character whose assertion is true at the beginning and end of the string)
	 * @description zh - 除去字符串头尾部的断言为真的字符
	 * @description en - Remove the character whose assertion is true at the beginning and end of the string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:15:30
	 * @param str 要处理的字符串
	 * @return java.lang.String
	 */
	public static String trim(CharSequence str) {
		return (null == str) ? null : trim(str, 0);
	}

	/**
	 * [除去字符串头尾部的断言为真的字符](Remove the character whose assertion is true at the beginning and end of the string)
	 * @description zh - 除去字符串头尾部的断言为真的字符
	 * @description en - Remove the character whose assertion is true at the beginning and end of the string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:14:30
	 * @param str 要处理的字符串
	 * @param mode trimStart
	 * @return java.lang.String
	 */
	public static String trim(CharSequence str, int mode) {
		return trim(str, mode, Assert::isBlankChar);
	}

	/**
	 * [除去字符串头尾部的断言为真的字符](Remove the character whose assertion is true at the beginning and end of the string)
	 * @description zh - 除去字符串头尾部的断言为真的字符
	 * @description en - Remove the character whose assertion is true at the beginning and end of the string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:13:31
	 * @param str 要处理的字符串
	 * @param mode trimStart
	 * @param predicate 是否过掉字符
	 * @return java.lang.String
	 */
	public static String trim(CharSequence str, int mode, Predicate<Character> predicate) {
		String result;
		if (str == null) {
			result = null;
		} else {
			int length = str.length();
			int start = 0;
			int end = length;
			if (mode <= 0) {
				while ((start < end) && (predicate.test(str.charAt(start)))) {
					start++;
				}
			}
			if (mode >= 0) {
				while ((start < end) && (predicate.test(str.charAt(end - 1)))) {
					end--;
				}
			}
			if ((start > 0) || (end < length)) {
				result = str.toString().substring(start, end);
			} else {
				result = str.toString();
			}
		}

		return result;
	}

	/**
	 * [字符串是否为空](Is the string empty)
	 * @description zh - 字符串是否为空
	 * @description en - Is the string empty
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 21:06:22
	 * @param str 字符串
	 * @return boolean
	 */
	public static boolean isEmpty(CharSequence str) {
		return str == null || str.length() == 0;
	}

	/**
	 * [字符串是否为非空白](Is the string non blank)
	 * @description zh - 字符串是否为非空白
	 * @description en - Is the string non blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 21:06:57
	 * @param str 字符串
	 * @return boolean
	 */
	public static boolean isNotEmpty(CharSequence str) {
		return false == isEmpty(str);
	}

	/**
	 * [字符串是否以给定字符结尾](Whether the string ends with the given character)
	 * @description zh - 字符串是否以给定字符结尾
	 * @description en - Whether the string ends with the given character
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 21:07:59
	 * @param str 字符串
	 * @param c 字符
	 * @return boolean
	 */
	public static boolean endWith(CharSequence str, char c) {
		if (isEmpty(str)) {
			return false;
		}
		return c == str.charAt(str.length() - 1);
	}

	/**
	 * [字符串是否以给定字符结尾](Whether the string ends with the given character)
	 * @description zh - 字符串是否以给定字符结尾
	 * @description en - Whether the string ends with the given character
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 21:09:23
	 * @param str 字符串
	 * @param suffix 前缀
	 * @return boolean
	 */
	public static boolean endWith(CharSequence str, CharSequence suffix) {
		return endWith(str, suffix, false);
	}

	/**
	 * [字符串是否以给定字符结尾](Whether the string ends with the given character)
	 * @description zh - 字符串是否以给定字符结尾
	 * @description en - Whether the string ends with the given character
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 21:09:58
	 * @param str 字符串
	 * @param suffix 前缀
	 * @param isIgnoreCase 是否忽略大小写
	 * @return boolean
	 */
	public static boolean endWith(CharSequence str, CharSequence suffix, boolean isIgnoreCase) {
		if (null == str || null == suffix) {
			return null == str && null == suffix;
		}

		if (isIgnoreCase) {
			return str.toString().toLowerCase().endsWith(suffix.toString().toLowerCase());
		} else {
			return str.toString().endsWith(suffix.toString());
		}
	}

	/**
	 * [如果字符串是 null 或者&quot;&quot;，则返回指定默认字符串，否则返回字符串本身。](If the string is null or & quote& quot;， Returns the specified default string, otherwise returns the string itself.)
	 * @description zh - 如果字符串是 null 或者&quot;&quot;，则返回指定默认字符串，否则返回字符串本身。
	 * @description en - If the string is null or & quote& quot;， Returns the specified default string, otherwise returns the string itself.
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:31:45
	 * @param str 判断的文本
	 * @param defaultStr 默认的文本
	 * @return java.lang.String
	 */
	public static String emptyToDefault(CharSequence str, String defaultStr) {
		return isEmpty(str) ? defaultStr : str.toString();
	}

	/**
	 * [如果字符串是 null 或者&quot;&quot;，则返回指定默认字符串，否则返回字符串本身。](If the string is null or & quote& quot;， Returns the specified default string, otherwise returns the string itself.)
	 * @description zh - 如果字符串是 null 或者&quot;&quot;，则返回指定默认字符串，否则返回字符串本身。
	 * @description en - If the string is null or & quote& quot;， Returns the specified default string, otherwise returns the string itself.
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:55:26
	 * @param str 判断的文本
	 * @param defaultStr 默认的文本
	 * @return java.lang.String
	 */
	public static String blankToDefault(CharSequence str, String defaultStr) {
		return isBlank(str) ? defaultStr : str.toString();
	}

	public static String toString(CharSequence cs) {
		return null == cs ? null : cs.toString();
	}

	/**
	 * [去掉指定前缀](Remove the specified prefix)
	 * @description zh - 去掉指定前缀
	 * @description en - Remove the specified prefix
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:21:35
	 * @param str 字符串
	 * @param prefix 前缀
	 * @return java.lang.String
	 */
	public static String removePrefix(CharSequence str, CharSequence prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str.toString();
		}
		final String str2 = str.toString();
		if (str2.startsWith(prefix.toString())) {
			return str2.substring(prefix.length(), str2.length());
		}
		return str2;
	}

	/**
	 * [去掉指定后缀](Remove the specified suffix)
	 * @description zh - 去掉指定后缀
	 * @description en - Remove the specified suffix
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:24:54
	 * @param str 字符串
	 * @param suffix 后缀
	 * @return java.lang.String
	 */
	public static String removeSuffix(CharSequence str, CharSequence suffix) {
		if (isEmpty(str) || isEmpty(suffix)) {
			return str.toString();
		}
		final String str2 = str.toString();
		if (str2.endsWith(suffix.toString())) {
			return str2.substring(0,str2.length() - suffix.length());
		}
		return str2;
	}

	/**
	 * [数组是否为空](Is the array empty)
	 * @description zh - 数组是否为空
	 * @description en - Is the array empty
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:26:18
	 * @param collection 集合
	 * @return boolean
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * [是否包含特定字符](Does it contain specific characters)
	 * @description zh - 是否包含特定字符
	 * @description en - Does it contain specific characters
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-05 09:29:15
	 * @param str 被检测字符串
	 * @param testStr 被测试是否包含的字符串
	 * @return boolean
	 */
	public static boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
		return null == str ? null == testStr : str.toString().toLowerCase().contains(testStr.toString().toLowerCase());
	}

	/**
	 * [如果给定字符串不是以prefix开头的，在开头补充 prefix](If the given string does not start with prefix, prefix is supplemented at the beginning)
	 * @description zh - 如果给定字符串不是以prefix开头的，在开头补充 prefix
	 * @description en - If the given string does not start with prefix, prefix is supplemented at the beginning
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-06 21:27:41
	 * @param str 被监测字符串
	 * @param prefix 前缀
	 * @param ignoreCase 是否忽略大小写
	 * @param prefixes 前缀
	 * @return java.lang.String
	 */
	public static String prependIfMissing(CharSequence str, CharSequence prefix, boolean ignoreCase, CharSequence... prefixes) {
		if (str == null || isEmpty(prefix) || startWith(str, prefix, ignoreCase, false)) {
			return null == str ? null : str.toString();
		}
		if (prefixes != null && prefixes.length > 0) {
			for (final CharSequence s : prefixes) {
				if (startWith(str, s, ignoreCase, false)) {
					return str.toString();
				}
			}
		}
		return prefix.toString().concat(str.toString());
	}

	/**
	 * [是否以指定字符串开头](Whether to start with the specified string)
	 * @description zh - 是否以指定字符串开头
	 * @description en - Whether to start with the specified string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-06 21:26:34
	 * @param str 被监测字符串
	 * @param prefix 开头字符串
	 * @param ignoreCase 是否忽略大小写
	 * @param ignoreEquals 是否忽略字符串相等的情况
	 * @return boolean
	 */
	public static boolean startWith(CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
		if (null == str || null == prefix) {
			if (false == ignoreEquals) {
				return false;
			}
			return null == str && null == prefix;
		}

		boolean isStartWith;
		if (ignoreCase) {
			isStartWith = str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
		} else {
			isStartWith = str.toString().startsWith(prefix.toString());
		}

		if (isStartWith) {
			return (false == ignoreEquals) || (false == equals(str, prefix, ignoreCase));
		}
		return false;
	}

	public static String subSuf(CharSequence string, int fromIndex) {
		if (isEmpty(string)) {
			return null;
		}
		return sub(string, fromIndex, string.length());
	}

	public static String sub(CharSequence str, int fromIndexInclude, int toIndexExclude) {
		if (isEmpty(str)) {
			return null == str ? null : str.toString();
		}
		int len = str.length();

		if (fromIndexInclude < 0) {
			fromIndexInclude = len + fromIndexInclude;
			if (fromIndexInclude < 0) {
				fromIndexInclude = 0;
			}
		} else if (fromIndexInclude > len) {
			fromIndexInclude = len;
		}

		if (toIndexExclude < 0) {
			toIndexExclude = len + toIndexExclude;
			if (toIndexExclude < 0) {
				toIndexExclude = len;
			}
		} else if (toIndexExclude > len) {
			toIndexExclude = len;
		}

		if (toIndexExclude < fromIndexInclude) {
			int tmp = fromIndexInclude;
			fromIndexInclude = toIndexExclude;
			toIndexExclude = tmp;
		}

		if (fromIndexInclude == toIndexExclude) {
			return "";
		}

		return str.toString().substring(fromIndexInclude, toIndexExclude);
	}

	public static String subAfter(CharSequence string, CharSequence separator, boolean isLastSeparator) {
		if (isEmpty(string)) {
			return null == string ? null : "";
		}
		if (separator == null) {
			return "";
		}
		final String str = string.toString();
		final String sep = separator.toString();
		final int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
		if (-1 == pos || (string.length() - 1) == pos) {
			return "";
		}
		return str.substring(pos + separator.length());
	}

	public static void appendHex(StringBuilder builder, byte b, boolean toLowerCase) {
		final char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;

		int high = (b & 0xf0) >>> 4;
		int low = b & 0x0f;
		builder.append(toDigits[high]);
		builder.append(toDigits[low]);
	}

	public static boolean contains(CharSequence str, char searchChar) {
		return indexOf(str, searchChar, 0, -1) > -1;
	}

	public static int indexOf(final CharSequence str, char searchChar, int start, int end) {
		if (isEmpty(str)) {
			return -1;
		}
		final int len = str.length();
		if (start < 0 || start > len) {
			start = 0;
		}
		if (end > len || end < 0) {
			end = len;
		}
		for (int i = start; i < end; i++) {
			if (str.charAt(i) == searchChar) {
				return i;
			}
		}
		return -1;
	}

}
