package com.axios.core.config.global;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.axios.cookie.ThreadLocalCookieStore;
import com.axios.core.connection.Connection;
import com.axios.core.tool.UrlTool;
import com.axios.exception.IORuntimeException;

/**
 * [全局Cookie管理器](Global cookie Manager)
 * @description zh - 全局Cookie管理器
 * @description en - Global cookie Manager
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-18 14:05:46
 */
public class GlobalCookieManager {

	/** Cookie Manager */
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
	 * @since 2021-11-18 14:06:41
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
	 * @since 2021-11-18 14:07:25
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
	 * @since 2021-11-18 14:08:28
	 * @param conn Connection
	 * @return java.util.List<java.net.HttpCookie>
	 */
	public static List<HttpCookie> getCookies(Connection conn){
		return cookieManager.getCookieStore().get(getURI(conn));
	}

	/**
	 * [将本地存储的Cookie信息附带到Http请求中](Attach locally stored cookie information to the HTTP request)
	 * @description zh - 将本地存储的Cookie信息附带到Http请求中
	 * @description en - Attach locally stored cookie information to the HTTP request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:09:40
	 * @param conn Connection
	 */
	public static void add(Connection conn) {
		if(null == cookieManager) {
			// Global cookie manager shutdown
			return;
		}
		Map<String, List<String>> cookieHeader;
		try {
			cookieHeader = cookieManager.get(getURI(conn), new HashMap<>(0));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		// Do not overwrite mode backfill cookie header
		conn.header(cookieHeader, false);
	}

	/**
	 * [存储响应的Cookie信息到本地](Store the cookie information of the response locally)
	 * @description zh - 存储响应的Cookie信息到本地
	 * @description en - Store the cookie information of the response locally
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:10:28
	 * @param conn Connection
	 */
	public static void store(Connection conn) {
		if(null == cookieManager) {
			// Global cookie manager shutdown
			return;
		}
		try {
			cookieManager.put(getURI(conn), conn.headers());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	private static URI getURI(Connection conn){
		return UrlTool.toURI(conn.getUrl());
	}
}
