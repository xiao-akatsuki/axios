package com.axios.core.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import com.axios.core.config.global.GlobalCookieManager;
import com.axios.core.config.global.HttpGlobalConfig;
import com.axios.core.connection.Connection;
import com.axios.core.http.url.UrlBuilder;
import com.axios.core.requestMethod.RequestMethod;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLStreamHandler;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * [http请求类](HTTP request class)
 * @description zh - http请求类
 * @description en - HTTP request class
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-20 09:25:33
 */
public class HttpRequest extends HttpBase<HttpRequest> {

	/** URL */
	private UrlBuilder url;

	/** header */
	private URLStreamHandler urlHandler;

	/** method */
	private RequestMethod method = RequestMethod.GET;

	/**
	 * 请求前的拦截器，用于在请求前重新编辑请求
	 */
	private final HttpInterceptor.Interceptor interceptors = new HttpInterceptor.Interceptor();

	/**
	 * 默认连接超时
	 */
	private int connectionTimeout = HttpGlobalConfig.timeout;

	/**
	 * 默认读取超时
	 */
	private int readTimeout = HttpGlobalConfig.timeout;

	/**
	 * 存储表单数据
	 */
	private Map<String, Object> form;

	/**
	 * 是否为Multipart表单
	 */
	private boolean isMultiPart;

	/**
	 * Cookie
	 */
	private String cookie;

	/**
	 * 连接对象
	 */
	private Connection httpConnection;
	/**
	 * 是否禁用缓存
	 */
	private boolean isDisableCache;
	/**
	 * 是否是REST请求模式
	 */
	private boolean isRest;
	/**
	 * 重定向次数计数器，内部使用
	 */
	private int redirectCount;
	/**
	 * 最大重定向次数
	 */
	private int maxRedirectCount;
	/**
	 * Chuncked块大小，0或小于0表示不设置Chuncked模式
	 */
	private int blockSize;
	/**
	 * 代理
	 */
	private Proxy proxy;

	/**
	 * HostnameVerifier，用于HTTPS安全连接
	 */
	private HostnameVerifier hostnameVerifier;
	/**
	 * SSLSocketFactory，用于HTTPS安全连接
	 */
	private SSLSocketFactory ssf;

	public HttpRequest(String url) {
		this(UrlBuilder.ofHttp(url));
	}

	public HttpRequest(UrlBuilder url) {
		this.url = url;
		this.header(GlobalHeaders.INSTANCE.headers);
	}

	/**
	 * [设置全局默认的连接和读取超时时长](Set the global default connection and read timeout)
	 * @description zh - 设置全局默认的连接和读取超时时长
	 * @description en - Set the global default connection and read timeout
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-20 09:24:56
	 * @param customTimeout 超时时长
	 */
	public static void setGlobalTimeout(int customTimeout) {
		HttpGlobalConfig.setTimeout(customTimeout);
	}

	/**
	 * [获取Cookie管理器](Get cookie Manager)
	 * @description zh - 获取Cookie管理器
	 * @description en - Get cookie Manager
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-20 09:26:12
	 * @return java.net.CookieManager
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
	 * @since 2021-11-20 09:26:58
	 * @param customCookieManager 自定义的 CookieManager
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
	 * @since 2021-11-20 09:27:46
	 */
	public static void closeCookie() {
		GlobalCookieManager.setCookieManager(null);
	}



}
