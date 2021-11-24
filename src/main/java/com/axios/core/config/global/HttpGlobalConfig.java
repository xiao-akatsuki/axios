package com.axios.core.config.global;

import java.io.Serializable;
import java.net.CookieManager;

/**
 * [HTTP 全局参数配置](HTTP global parameter configuration)
 * @description zh - HTTP 全局参数配置
 * @description en - HTTP global parameter configuration
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-18 14:04:59
 */
public class HttpGlobalConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	public static int timeout = -1;

	/**
	 * [获取全局默认的超时时长](Gets the global default timeout length)
	 * @description zh - 获取全局默认的超时时长
	 * @description en - Gets the global default timeout length
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:01:40
	 * @return int
	 */
	public static int getTimeout() {
		return timeout;
	}

	/**
	 * [设置默认的连接和读取超时时长](Set the default connection and read timeout)
	 * @description zh - 设置默认的连接和读取超时时长
	 * @description en - Set the default connection and read timeout
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:02:21
	 * @param customTimeout 超时时长
	 */
	synchronized public static void setTimeout(int customTimeout) {
		timeout = customTimeout;
	}

	/**
	 * [获取Cookie管理器](Get cookie Manager)
	 * @description zh - 获取Cookie管理器
	 * @description en - Get cookie Manager
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:03:01
	 */
	public static CookieManager getCookieManager() {
		return GlobalCookieManager.getCookieManager();
	}

	/**
	 * [自定义 CookieManager](Custom cookiemanager)
	 * @description zh - 自定义 CookieManager
	 * @description en - Custom cookiemanager
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:03:28
	 * @param customCookieManager CookieManager
	 */
	public static void setCookieManager(CookieManager customCookieManager) {
		GlobalCookieManager.setCookieManager(customCookieManager);
	}

	/**
	 * [关闭Cookie](Close cookies)
	 * @description zh - 关闭Cookie
	 * @description en - Close cookies
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:04:11
	 */
	public static void closeCookie() {
		GlobalCookieManager.setCookieManager(null);
	}

}
