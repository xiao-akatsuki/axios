package com.axios.core.http;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import com.axios.core.config.global.GlobalCookieManager;
import com.axios.core.config.global.GlobalHeaders;
import com.axios.core.config.global.HttpGlobalConfig;
import com.axios.core.connection.Connection;
import com.axios.core.http.url.UrlBuilder;
import com.axios.core.http.url.UrlQuery;
import com.axios.core.requestMethod.RequestMethod;
import com.axios.core.resource.BytesResource;
import com.axios.core.resource.FileResource;
import com.axios.core.resource.MultiFileResource;
import com.axios.core.resource.Resource;
import com.axios.core.tool.UrlTool;
import com.axios.core.tool.http.HttpTool;
import com.axios.core.tool.io.IoTool;
import com.axios.core.tool.ssl.SSLTool;
import com.axios.core.type.ContentType;
import com.axios.exception.HttpException;
import com.axios.exception.IORuntimeException;
import com.axios.header.RequestHeader;
import com.axios.request.Request;
import com.axios.response.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
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

	/** --------------- http request --------------- */

	/**
	 * [POST请求](Post request)
	 * @description zh - POST请求
	 * @description en - Post request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:33:04
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest post(String url) {
		return new HttpRequest(url).method(RequestMethod.POST);
	}

	/**
	 * [GET请求](GET request)
	 * @description zh - GET请求
	 * @description en - GET request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-25 19:33:51
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest get(String url) {
		return new HttpRequest(url).method(RequestMethod.GET);
	}

	/**
	 * [HEAD 请求](HEAD request)
	 * @description zh - HEAD 请求
	 * @description zh - HEAD request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:39:38
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest head(String url) {
		return new HttpRequest(url).method(RequestMethod.HEAD);
	}

	/**
	 * [OPTIONS 请求](OPTIONS request)
	 * @description zh - OPTIONS 请求
	 * @description en - OPTIONS request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:41:21
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest options(String url) {
		return new HttpRequest(url).method(RequestMethod.OPTIONS);
	}

	/**
	 * [PUT 请求](PUT request)
	 * @description zh - PUT 请求
	 * @description en - PUT request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:43:24
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest put(String url) {
		return new HttpRequest(url).method(RequestMethod.PUT);
	}

	/**
	 * [PATCH 请求](PATCH request)
	 * @description zh - PATCH 请求
	 * @description zh - PATCH request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:44:46
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest patch(String url) {
		return new HttpRequest(url).method(RequestMethod.PATCH);
	}

	/**
	 * [DELETE 请求](DELETE request)
	 * @description zh - DELETE 请求
	 * @description en - DELETE request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:45:56
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest delete(String url) {
		return new HttpRequest(url).method(RequestMethod.DELETE);
	}

	/** --------------- getter and setter --------------- */

	/**
	 * [获取请求URL](Get request URL)
	 * @description zh - 获取请求URL
	 * @description en - Get request URL
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:48:08
	 * @return java.lang.String
	 */
	public String getUrl() {
		return url.toString();
	}

	/**
	 * [设置请求的URL](Set the requested URL)
	 * @description zh - 设置请求的URL
	 * @description en - Set the requested URL
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:48:48
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest setUrl(String url) {
		return setUrl(UrlBuilder.ofHttp(url, this.charset));
	}

	/**
	 * [设置请求的URL](Set the requested URL)
	 * @description zh - 设置请求的URL
	 * @description en - Set the requested URL
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:49:33
	 * @param urlBuilder URL生成器
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest setUrl(UrlBuilder urlBuilder) {
		this.url = urlBuilder;
		return this;
	}

	/**
	 * [设置 URLStreamHandler](set URLStreamHandler)
	 * @description zh - 设置 URLStreamHandler
	 * @description en - set URLStreamHandler
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:50:32
	 * @param urlHander URLStreamHandler
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest setUrlHandler(URLStreamHandler urlHandler) {
		this.urlHandler = urlHandler;
		return this;
	}

	/**
	 * [获取HTTP请求方法](Get HTTP request method)
	 * @description zh - 获取Http请求方法
	 * @description en - Get HTTP request method
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:52:33
	 * @return com.axios.core.requestMethod.RequestMethod
	 */
	public RequestMethod getMethod() {
		return this.method;
	}

	/**
	 * [设置请求方法](set request method)
	 * @description zh - 设置请求方法
	 * @description en - set request method
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:54:31
	 * @param method RequestMethod
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest setMethod(RequestMethod method) {
		return method(method);
	}

	/**
	 * [获取 Connection](get Connection)
	 * @description zh - 获取 Connection
	 * @description en - get Connection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:56:07
	 * @return com.axios.core.connection.Connection
	 */
	public Connection getConnection() {
		return this.httpConnection;
	}

	/**
	 * [设置请求方法](set request method)
	 * @description zh - 设置请求方法
	 * @description en - set request method
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:56:41
	 * @param method RequestMethod
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest method(RequestMethod method) {
		this.method = method;
		return this;
	}

	/** --------------- getter and setter --------------- */

	/**
	 * [设置contentType](set contentType)
	 * @description zh - 设置contentType
	 * @description en - set contentType
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 19:59:48
	 * @param contentType contentType
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest contentType(String contentType) {
		header(RequestHeader.CONTENT_TYPE, contentType);
		return this;
	}

	/**
	 * [是否是长链接](Is it a long link)
	 * @description zh - 是否是长链接
	 * @description en - Is it a long link
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 20:03:21
	 * @param isKeepAlive 是否长链接
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest keepAlive(boolean isKeepAlive) {
		header(RequestHeader.CONNECTION, isKeepAlive ? "Keep-Alive" : "Close");
		return this;
	}

	/**
	 * [获取是否为长连接](Gets whether it is a long connection)
	 * @description zh - 获取是否为长连接
	 * @description en - Gets whether it is a long connection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 20:04:58
	 * @return boolean
	 */
	public boolean isKeepAlive() {
		String connection = header(RequestHeader.CONNECTION);
		return connection == null ?
			false == HTTP_1_0.equalsIgnoreCase(httpVersion) :
			false == "close".equalsIgnoreCase(connection);
	}

	/**
	 * [获取内容长度](Get content length)
	 * @description zh - 获取内容长度
	 * @description en - Get content length
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 20:06:07
	 * @return java.lang.String
	 */
	public String contentLength() {
		return header(RequestHeader.CONTENT_LENGTH);
	}

	/**
	 * [设置内容长度](set content length)
	 * @description zh - 设置内容长度
	 * @description en - set content length
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 20:06:47
	 * @param value 长度
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest contentLength(int value) {
		header(RequestHeader.CONTENT_LENGTH, String.valueOf(value));
		return this;
	}

	/**
	 * [设置Cookie](set Cookie)
	 * @description zh - 设置Cookie
	 * @description en - set Cookie
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 20:07:49
	 * @param cookie Cookie值数组
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest cookie(Collection<HttpCookie> cookies) {
		return cookie(HttpTool.isEmpty(cookies) ? null : cookies.toArray(new HttpCookie[0]));
	}

	/**
	 * [设置Cookie](set Cookie)
	 * @description zh - 设置Cookie
	 * @description en - set Cookie
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 20:09:56
	 * @param cookie Cookie值数组
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest cookie(HttpCookie... cookies) {
		if (HttpTool.isEmpty(cookies)) {
			return disableCookie();
		}
		return cookie(HttpTool.join(cookies, "; ", null, null));
	}

	/**
	 * [设置Cookie](set Cookie)
	 * @description zh - 设置Cookie
	 * @description en - set Cookie
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 20:11:51
	 * @param cookie Cookie
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest cookie(String cookie) {
		this.cookie = cookie;
		return this;
	}

	/**
	 * [禁用默认Cookie行为](Disable default cookie behavior)
	 * @description zh - 禁用默认Cookie行为
	 * @description en - Disable default cookie behavior
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 20:12:27
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest disableCookie() {
		return cookie("");
	}

	/**
	 * [打开默认的Cookie行为](Open default cookie behavior)
	 * @description zh - 打开默认的Cookie行为
	 * @description en - Open default cookie behavior
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 20:14:26
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest enableDefaultCookie() {
		return cookie((String) null);
	}

	/** --------------- form --------------- */

	/**
	 * [设置表单数据](set form data)
	 * @description zh - 设置表单数据
	 * @description en - set form data
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 13:50:42
	 * @param name 键
	 * @param value 值
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest form(String name, Object value) {
		if (UrlTool.isBlank(name) || UrlTool.isNull(value)) {
			return this;
		}
		// 停用body
		this.bodyBytes = null;
		if (value instanceof File) {
			// 文件上传
			return this.form(name, (File) value);
		}
		// 普通值
		String strValue;
		if (value instanceof Iterable) {
			// 列表对象
			strValue = HttpTool.join((Iterable<?>) value, ",");
		} else if (HttpTool.isArray(value)) {
			if (File.class == HttpTool.getComponentType(value)) {
				// 多文件
				return this.form(name, (File[]) value);
			}
			// 数组对象
			strValue = HttpTool.join((Object[]) value, ",", null, null);
		} else {
			// 其他对象一律转换为字符串
			strValue = value.toString();
		}
		return putToForm(name, strValue);
	}

	/**
	 * [设置表单数据](set form data)
	 * @description zh - 设置表单数据
	 * @description en - set form data
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 13:51:44
	 * @param name 键
	 * @param value 值
	 * @param parameters 参数对
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest form(String name, Object value, Object... parameters) {
		form(name, value);

		for (int i = 0; i < parameters.length; i += 2) {
			form(parameters[i].toString(), parameters[i + 1]);
		}
		return this;
	}

	/**
	 * [设置map类型表单数据](Set map type form data)
	 * @description zh - 设置map类型表单数据
	 * @description en - Set map type form data
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 13:52:52
	 * @param formMap 表单内容
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest form(Map<String, Object> formMap) {
		if (UrlTool.isNotEmpty(formMap)) {
			formMap.forEach(this::form);
		}
		return this;
	}

	/**
	 * [设置map类型表单数据](Set map type form data)
	 * @description zh - 设置map类型表单数据
	 * @description en - Set map type form data
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 13:54:02
	 * @param formMapStr 表单内容
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest formStr(Map<String, String> formMapStr) {
		if (UrlTool.isNotEmpty(formMapStr)) {
			formMapStr.forEach(this::form);
		}
		return this;
	}

	/**
	 * [文件表单项](file form item)
	 * @description zh - 文件表单项
	 * @description en - file form item
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 13:54:59
	 * @param name 键
	 * @param files 文件数组
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest form(String name, File... files) {
		if (HttpTool.isEmpty(files)) {
			return this;
		}
		if (1 == files.length) {
			final File file = files[0];
			return form(name, file, file.getName());
		}
		return form(name, new MultiFileResource(files));
	}

	/**
	 * [文件表单项](file form item)
	 * @description zh - 文件表单项
	 * @description en - file form item
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 13:56:26
	 * @param name 键
	 * @param file 文件
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest form(String name, File file) {
		return form(name, file, file.getName());
	}

	/**
	 * [文件表单项](file form item)
	 * @description zh - 文件表单项
	 * @description en - file form item
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 13:57:25
	 * @param name 键
	 * @param file 文件
	 * @param fileName 文件名
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest form(String name, File file, String fileName) {
		if (null != file) {
			form(name, new FileResource(file, fileName));
		}
		return this;
	}

	/**
	 * [文件表单项](file form item)
	 * @description zh - 文件表单项
	 * @description en - file form item
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:01:43
	 * @param name 键
	 * @param fileBytes 上传的文件
	 * @param fileName 文件名
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest form(String name, byte[] fileBytes, String fileName) {
		if (null != fileBytes) {
			form(name, new BytesResource(fileBytes, fileName));
		}
		return this;
	}

	/**
	 * [文件表单项](file form item)
	 * @description zh - 文件表单项
	 * @description en - file form item
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:02:33
	 * @param name 键
	 * @param resource 数据源
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest form(String name, Resource resource) {
		if (null != resource) {
			if (false == isKeepAlive()) {
				keepAlive(true);
			}

			this.isMultiPart = true;
			return putToForm(name, resource);
		}
		return this;
	}

	/**
	 * [获取表单数据](Get form data)
	 * @description zh - 获取表单数据
	 * @description en - Get form data
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:03:05
	 * @return java.util.Map<java.lang.String, java.lang.Object>
	 */
	public Map<String, Object> form() {
		return this.form;
	}

	/**
	 * [获取文件表单数据](Get file form data)
	 * @description zh - 获取文件表单数据
	 * @description en - Get file form data
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:04:33
	 * @return java.util.Map<java.lang.String, Resource>
	 */
	public Map<String, Resource> fileForm() {
		final Map<String, Resource> result = new HashMap<>();
		this.form.forEach((key, value) -> {
			if (value instanceof Resource) {
				result.put(key, (Resource) value);
			}
		});
		return result;
	}

	/** --------------- body --------------- */

	/**
	 * [设置内容主体](Set content body)
	 * @description zh - 设置内容主体
	 * @description en - Set content body
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-28 20:07:22
	 * @param body 请求体
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest body(String body) {
		return this.body(body, null);
	}

	/**
	 * [设置内容主体](Set content body)
	 * @description zh - 设置内容主体
	 * @description en - Set content body
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-28 20:09:20
	 * @param body
	 * @param contentType
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest body(String body, String contentType) {
		byte[] bytes = bytes(body, this.charset);
		body(bytes);
		this.form = null;

		if (null != contentType) {
			this.contentType(contentType);
		} else {
			contentType = getContentTypeByRequestBody(body);
			if (null != contentType && ContentType.isDefault(this.header(RequestHeader.CONTENT_TYPE))) {
				if (null != this.charset) {
					contentType = ContentType.build(contentType, this.charset);
				}
				this.contentType(contentType);
			}
		}
		// 判断是否为rest请求
		if (null != getContainsStrIgnoreCase(contentType, "json", "xml")) {
			this.isRest = true;
			contentLength(bytes.length);
		}
		return this;
	}

	/**
	 * [设置主体字节码](Set body bytecode)
	 * @description zh - 设置主体字节码
	 * @description en - Set body bytecode
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-28 20:10:12
	 * @param bodyBytes 主体字节码
	 * @return com.axios.core.http.HttpRequest
	 */
	public HttpRequest body(byte[] bodyBytes) {
		if (null != bodyBytes) {
			this.bodyBytes = bodyBytes;
		}
		return this;
	}

	/** --------------- private --------------- */

	private HttpResponse doExecute(boolean isAsync, HttpInterceptor.Interceptor interceptors) {
		if (null != interceptors) {
			for (HttpInterceptor interceptor : interceptors) {
				interceptor.process(this);
			}
		}
		urlWithParamIfGet();
		initConnection();
		send();
		HttpResponse httpResponse = sendRedirectIfPossible(isAsync);
		if (null == httpResponse) {
			httpResponse = new HttpResponse(this.httpConnection, this.charset, isAsync, isIgnoreResponseBody());
		}
		return httpResponse;
	}

	private void initConnection() {
		if (null != this.httpConnection) {
			this.httpConnection.disconnectQuietly();
		}

		this.httpConnection = Connection
				.create(this.url.toURL(this.urlHandler), this.proxy)
				.setConnectTimeout(this.connectionTimeout)
				.setReadTimeout(this.readTimeout)
				.setMethod(this.method)
				.setHttpsInfo(this.hostnameVerifier, this.ssf)
				.setInstanceFollowRedirects(this.maxRedirectCount > 0)
				.setChunkedStreamingMode(this.blockSize)
				.header(this.headers, true);

		if (null != this.cookie) {
			this.httpConnection.setCookie(this.cookie);
		} else {
			GlobalCookieManager.add(this.httpConnection);
		}
		if (this.isDisableCache) {
			this.httpConnection.disableCache();
		}
	}

	private void urlWithParamIfGet() {
		if (RequestMethod.GET.equals(method) && false == this.isRest) {
			if (ArrayUtil.isNotEmpty(this.bodyBytes)) {
				this.url.getQuery().parse(StrUtil.str(this.bodyBytes, this.charset), this.charset);
			} else {
				this.url.getQuery().addAll(this.form);
			}
		}
	}

	private HttpResponse sendRedirectIfPossible(boolean isAsync) {
		if (this.maxRedirectCount < 1) {
			return null;
		}

		if (this.httpConnection.getHttpURLConnection().getInstanceFollowRedirects()) {
			int responseCode;
			try {
				responseCode = httpConnection.responseCode();
			} catch (IOException e) {
				this.httpConnection.disconnectQuietly();
				throw new HttpException(e);
			}

			if (responseCode != HttpURLConnection.HTTP_OK) {
				if (HttpStatus.isRedirected(responseCode)) {
					setUrl(httpConnection.header(RequestHeader.LOCATION));
					if (redirectCount < this.maxRedirectCount) {
						redirectCount++;
						return doExecute(isAsync, null);
					}
				}
			}
		}
		return null;
	}

	private void send() throws IORuntimeException {
		try {
			if (RequestMethod.POST.equals(this.method)
					|| RequestMethod.PUT.equals(this.method)
					|| RequestMethod.DELETE.equals(this.method)
					|| this.isRest) {
				if (isMultipart()) {
					sendMultipart();
				} else {
					sendFormUrlEncoded();
				}
			} else {
				this.httpConnection.connect();
			}
		} catch (IOException e) {
			this.httpConnection.disconnectQuietly();
			throw new IORuntimeException(e);
		}
	}

	private void sendFormUrlEncoded() throws IOException {
		if (UrlTool.isBlank(this.header(RequestHeader.CONTENT_TYPE))) {
			this.httpConnection.header(RequestHeader.CONTENT_TYPE, ContentType.FORM_URLENCODED.toString(this.charset), true);
		}

		byte[] content;
		if (HttpTool.isNotEmpty(this.bodyBytes)) {
			content = this.bodyBytes;
		} else {
			content = bytes(getFormUrlEncoded(), this.charset);
		}
		IoTool.write(this.httpConnection.getOutputStream(), true, content);
	}

	private String getFormUrlEncoded() {
		return UrlQuery.of(this.form, true).build(this.charset);
	}

	private void sendMultipart() throws IOException {
		setMultipart();

		try (OutputStream out = this.httpConnection.getOutputStream()) {
			MultipartBody.create(this.form, this.charset).write(out);
		}
	}

	private void setMultipart() {
		this.httpConnection.header(RequestHeader.CONTENT_TYPE, MultipartBody.getContentType(), true);
	}

	private boolean isIgnoreResponseBody() {
		return RequestMethod.HEAD == this.method
				|| RequestMethod.CONNECT == this.method
				|| RequestMethod.OPTIONS == this.method
				|| RequestMethod.TRACE == this.method;
	}

	private boolean isMultipart() {
		if (this.isMultiPart) {
			return true;
		}
		final String contentType = header(RequestHeader.CONTENT_TYPE);
		return UrlTool.isNotEmpty(contentType) &&
				contentType.startsWith(ContentType.MULTIPART.getValue());
	}

	private HttpRequest putToForm(String name, Object value) {
		if (null == name || null == value) {
			return this;
		}
		if (null == this.form) {
			this.form = new LinkedHashMap<>();
		}
		this.form.put(name, value);
		return this;
	}

	private byte[] bytes(CharSequence str, Charset charset) {
		if (str == null) {
			return null;
		}
		if (null == charset) {
			return str.toString().getBytes();
		}
		return str.toString().getBytes(charset);
	}

	private String getContainsStrIgnoreCase(CharSequence str, CharSequence... testStrs) {
		if (UrlTool.isEmpty(str) || HttpTool.isEmpty(testStrs)) {
			return null;
		}
		for (CharSequence testStr : testStrs) {
			if (containsIgnoreCase(str, testStr)) {
				return testStr.toString();
			}
		}
		return null;
	}

	private boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
		if (null == str) {
			return null == testStr;
		}
		return str.toString().toLowerCase().contains(testStr.toString().toLowerCase());
	}

	private String getContentTypeByRequestBody(String body) {
		final ContentType contentType = ContentType.get(body);
		return (null == contentType) ? null : contentType.toString();
	}
}
