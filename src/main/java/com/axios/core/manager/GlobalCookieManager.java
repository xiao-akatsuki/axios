package com.axios.core.manager;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import com.axios.core.connection.Connection;
import com.axios.core.url.UrlMethod;
import com.axios.exception.UrlException;

public class GlobalCookieManager {

	/** Cookie management */
	private static CookieManager cookieManager;

	static {
		cookieManager = new CookieManager(new ThreadLocalCookieStore(), CookiePolicy.ACCEPT_ALL);
	}

	/**
	 * [自定义 CookieManager](Custom cookiemanager)
	 * @description zh - 自定义 CookieManager
	 * @description en - Custom cookiemanager
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:10:41
	 * @param customCookieManager CookieManager
	 */
	public static void setCookieManager(CookieManager customCookieManager) {
		cookieManager = customCookieManager;
	}

	/**
	 * [获取全局 CookieManager](Get global cookiemanager)
	 * @description zh - 获取全局 CookieManager
	 * @description en - Get global cookiemanager
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:12:52
	 * @return java.net.CookieManager
	 */
	public static CookieManager getCookieManager() {
		return cookieManager;
	}

	/**
	 * [获取指定域名下所有Cookie信息](Get all cookie information under the specified domain name)
	 * @description zh - 获取指定域名下所有Cookie信息
	 * @description en - Get all cookie information under the specified domain name
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:14:01
	 * @param conn Connection
	 * @return java.util.List<java.net.HttpCookie>
	 */
	public static List<HttpCookie> getCookies(Connection conn){
		return cookieManager.getCookieStore().get(getURI(conn));
	}

	/**
	 * [将本地存储的Cookie信息附带到Http请求中，不覆盖用户定义好的Cookie](Attach the locally stored cookie information to the HTTP request without overwriting the user-defined cookie)
	 * @description zh - 将本地存储的Cookie信息附带到Http请求中，不覆盖用户定义好的Cookie
	 * @description en - Attach the locally stored cookie information to the HTTP request without overwriting the user-defined cookie
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:16:12
	 * @param conn Connection
	 */
	public static void add(Connection conn) {
		if(null == cookieManager) {
			return;
		}

		Map<String, List<String>> cookieHeader;
		try {
			cookieHeader = cookieManager.get(getURI(conn), new HashMap<>(0));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}


		// 不覆盖模式回填Cookie头，这样用户定义的Cookie将优先
		conn.header(cookieHeader, false);
	}

	/**
	 * [存储响应的Cookie信息到本地](Store the cookie information of the response locally)
	 * @description zh - 存储响应的Cookie信息到本地
	 * @description en - Store the cookie information of the response locally
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:17:49
	 * @param conn Connection
	 */
	public static void store(Connection conn) {
		if(null == cookieManager) {
			// 全局Cookie管理器关闭
			return;
		}

		try {
			cookieManager.put(getURI(conn), conn.headers());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * [转字符串为URI](Convert string to URI)
	 * @description zh - 转字符串为URI
	 * @description en - Convert string to URI
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:24:38
	 * @param location 字符串路径
	 * @param isEncode 是否编码参数中的特殊字符
	 * @throws com.axios.exception.UrlException
	 * @return java.net.URI
	 */
	public static URI toURI(String location, boolean isEncode) throws UrlException {
		if (isEncode) {
			location = encode(location);
		}
		try {
			return new URI(trim(location));
		} catch (URISyntaxException e) {
			throw new UrlException(e);
		}
	}

	/**
	 * [除去字符串头尾部的断言为真的字符](Remove the character whose assertion is true at the beginning and end of the string)
	 * @description zh - 除去字符串头尾部的断言为真的字符
	 * @description en - Remove the character whose assertion is true at the beginning and end of the string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:33:33
	 * @param str 字符
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
	 * @since 2021-11-13 21:32:13
	 * @param str 字符
	 * @param mode -1 表示trimStart， 0 表示trim全部，  1 表示trimEnd
	 * @return java.lang.String
	 */
	public static String trim(CharSequence str, int mode) {
		return trim(str, mode, UrlMethod::isBlankChar);
	}

	/**
	 * [除去字符串头尾部的断言为真的字符](Remove the character whose assertion is true at the beginning and end of the string)
	 * @description zh - 除去字符串头尾部的断言为真的字符
	 * @description en - Remove the character whose assertion is true at the beginning and end of the string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:30:09
	 * @param str 要处理的字符串
	 * @param mode -1 表示trimStart， 0 表示trim全部，  1 表示trimEnd
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
			int end = length;// 扫描字符串头部
			if (mode <= 0) {
				while ((start < end) && (predicate.test(str.charAt(start)))) {
					start++;
				}
			}// 扫描字符串尾部
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
	 * [获取连接的URL中URI信息](Gets the URI information in the URL of the connection)
	 * @description zh - 获取连接的URL中URI信息
	 * @description en - Gets the URI information in the URL of the connection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:19:43
	 * @param conn Connection
	 * @return java.net.URI
	 */
	private static URI getURI(Connection conn){
		return toURI(conn.getUrl());
	}
}
