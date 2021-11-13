package com.axios.core.config;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.HttpURLConnection;

import com.axios.core.manager.GlobalCookieManager;
import com.axios.exception.UrlException;

/**
 * [发送请求的配置类](Configuration class to send the request)
 * @description zh - 发送请求的配置类
 * @description en - Configuration class to send the request
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-13 20:55:37
 */
public class AjaxGlobalConfig implements Serializable{

	private static final long serialVersionUID = 1L;

	protected static int timeout = -1;
	private static boolean isAllowPatch = false;

	/**
	 * [获取全局默认的超时时长](Gets the global default timeout length)
	 * @description zh - 获取全局默认的超时时长
	 * @description en - Gets the global default timeout length
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 20:56:24
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
	 * @since 2021-11-13 20:56:45
	 */
	synchronized public static void setTimeout(int customTimeout) {
		timeout = customTimeout;
	}

	/**
	 * [获取cookie](Get cookie)
	 * @description zh - 获取cookie
	 * @description en - Get cookie
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:01:21
	 */
	public static CookieManager getCookieManager() {
		return GlobalCookieManager.getCookieManager();
	}

	/**
	 * [自定义cookie](Custom cookie)
	 * @description zh - 自定义cookie
	 * @description en - Custom cookie
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:01:01
	 */
	public static void setCookieManager(CookieManager customCookieManager) {
		GlobalCookieManager.setCookieManager(customCookieManager);
	}

	/**
	 * [关闭cookie](Close cookies)
	 * @description zh - 关闭cookie
	 * @description en - Close cookies
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-13 21:03:04
	 */
	public static void closeCookie() {
		GlobalCookieManager.setCookieManager(null);
	}

	synchronized public static void allowPatch() {
		if(isAllowPatch){
			return;
		}
		final Field methodsField = ReflectUtil.getField(HttpURLConnection.class, "methods");
		if (null == methodsField) {
			throw new UrlException("None static field [methods] with Java version: [" + System.getProperty("java.version") + "]");
		}

		// 去除final修饰
		ReflectUtil.setFieldValue(methodsField, "modifiers", methodsField.getModifiers() & ~Modifier.FINAL);
		final String[] methods = {
				"GET", "POST", "HEAD", "OPTIONS", "PUT", "DELETE", "TRACE", "PATCH"
		};
		ReflectUtil.setFieldValue(null, methodsField, methods);

		// 检查注入是否成功
		final Object staticFieldValue = ReflectUtil.getStaticFieldValue(methodsField);
		if(false == ArrayUtil.equals(methods, staticFieldValue)){
			throw new UrlException("Inject value to field [methods] failed!");
		}

		isAllowPatch = true;
	}
}
