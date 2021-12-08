package com.axios;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.axios.core.config.global.HttpGlobalConfig;
import com.axios.core.http.HttpRequest;
import com.axios.core.requestMethod.RequestMethod;

/**
 * [发送具体请求](Send specific request)
 * @description zh - 发送具体请求
 * @description en - Send specific request
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-11 18:49:03
 */
public class Axios {

	/** Encoding information in content type */
	public static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);
	/** Encoding information matching meta tags */
	public static final Pattern META_CHARSET_PATTERN = Pattern.compile("<meta[^>]*?charset\\s*=\\s*['\"]?([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

	/**
	 * [是否是https请求](Is it an HTTPS request)
	 * @description zh - 是否是https请求
	 * @description en - Is it an HTTPS request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 20:36:45
	 * @param url URL
	 * @return boolean
	 */
	public static boolean isHttps(String url) {
		return url.toLowerCase().startsWith("https:");
	}

	/**
	 * [是否是http请求](Is it an HTTP request)
	 * @description zh - 是否是http请求
	 * @description en - Is it an HTTP request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 20:38:07
	 * @param url URL
	 * @return boolean
	 */
	public static boolean isHttp(String url) {
		return url.toLowerCase().startsWith("http:");
	}

	/** ------------------- create ------------------- */

	/**
	 * [创建HTTP请求对象](Create HTTP request object)
	 * @description zh - 创建HTTP请求对象
	 * @description en - Create HTTP request object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:24:17
	 * @param method RequestMethod
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest createRequest(RequestMethod method, String url) {
		return new HttpRequest(url).method(method);
	}

	/**
	 * [创建一个HTTP的GET请求对象](create a HTTP GET request object)
	 * @description zh - 创建一个HTTP的GET请求对象
	 * @description en - create a HTTP GET request object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:25:53
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest createGet(String url) {
		return createGet(url, false);
	}

	/**
	 * [创建一个HTTP的GET请求对象](create a HTTP GET request object)
	 * @description zh - 创建一个HTTP的GET请求对象
	 * @description en - create a HTTP GET request object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:26:50
	 * @param url URL
	 * @param isFollowRedirects 是否重定向
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest createGet(String url, boolean isFollowRedirects) {
		return HttpRequest.get(url).setFollowRedirects(isFollowRedirects);
	}

	/**
	 * [创建一个HTTP的POST请求对象](create a HTTP POST request object)
	 * @description zh - 创建一个HTTP的POST请求对象
	 * @description en - create a HTTP POST request object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:28:07
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest createPost(String url) {
		return HttpRequest.post(url);
	}

	/**
	 * [创建一个HTTP的PUT请求对象](create a HTTP PUT request object)
	 * @description zh - 创建一个HTTP的PUT请求对象
	 * @description en - create a HTTP PUT request object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:28:54
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest createPut(String url) {
		return HttpRequest.put(url);
	}

	/**
	 * [创建一个HTTP的DELETE请求对象](create a HTTP DELETE request object)
	 * @description zh - 创建一个HTTP的DELETE请求对象
	 * @description en - create a HTTP DELETE request object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:31:57
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest createDelete(String url) {
		return HttpRequest.delete(url);
	}

	/**
	 * [创建一个HTTP的HEADER请求对象](create a HTTP HEADER request object)
	 * @description zh - 创建一个HTTP的HEADER请求对象
	 * @description en - create a HTTP HEADER request object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:32:30
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest createHeader(String url) {
		return HttpRequest.head(url);
	}

	/**
	 * [创建一个HTTP的OPTIONS请求对象](create a HTTP OPTIONS request object)
	 * @description zh - 创建一个HTTP的OPTIONS请求对象
	 * @description en - create a HTTP OPTIONS request object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:34:00
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest createOptions(String url) {
		return HttpRequest.options(url);
	}

	/**
	 * [创建一个HTTP的PATCH请求对象](create a HTTP PATCH request object)
	 * @description zh - 创建一个HTTP的PATCH请求对象
	 * @description en - create a HTTP PATCH request object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:34:20
	 * @param url URL
	 * @return com.axios.core.http.HttpRequest
	 */
	public static HttpRequest createPatch(String url) {
		return HttpRequest.patch(url);
	}

	/** ------------------- get ------------------- */

	/**
	 * [发送get请求](send get request)
	 * @description zh - 发送get请求
	 * @description en - send get request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:39:23
	 * @param url URL
	 * @param customCharset 自定义请求字符集
	 * @return java.lang.String
	 */
	public static String get(String url, Charset customCharset) {
		return HttpRequest
				.get(url)
				.charset(customCharset)
				.execute()
				.body();
	}

	/**
	 * [发送get请求](send get request)
	 * @description zh - 发送get请求
	 * @description en - send get request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:41:22
	 * @param url URL
	 * @return java.lang.String
	 */
	public static String get(String url) {
		return get(url, HttpGlobalConfig.timeout);
	}

	/**
	 * [发送get请求](send get request)
	 * @description zh - 发送get请求
	 * @description en - send get request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:42:36
	 * @param url URL
	 * @param timeout 超时时间
	 * @return java.lang.String
	 */
	public static String get(String url, int timeout) {
		return HttpRequest
				.get(url)
				.timeout(timeout)
				.execute()
				.body();
	}

	/**
	 * [发送get请求](send get request)
	 * @description zh - 发送get请求
	 * @description en - send get request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:43:38
	 * @param url URL
	 * @param paramMap 表单数据
	 * @return java.lang.Stirng
	 */
	public static String get(String url, Map<String, Object> paramMap) {
		return HttpRequest
				.get(url)
				.form(paramMap)
				.execute()
				.body();
	}

	/**
	 * [发送get请求](send get request)
	 * @description zh - 发送get请求
	 * @description en - send get request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:45:34
	 * @param url URL
	 * @param paramMap 表单数据
	 * @param timeout 超时时长，-1表示默认超时，单位毫秒
	 * @return java.lang.String
	 */
	public static String get(String url, Map<String, Object> paramMap, int timeout) {
		return HttpRequest
				.get(url)
				.form(paramMap)
				.timeout(timeout)
				.execute()
				.body();
	}

	/** ------------------- post ------------------- */

	/**
	 * [发送post请求](Send post request)
	 * @description zh - 发送post请求
	 * @description en - Send post request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:52:14
	 * @param url URL
	 * @param paramMap 表单数据
	 * @return java.lang.String
	 */
	public static String post(String url, Map<String, Object> paramMap) {
		return post(url, paramMap, HttpGlobalConfig.timeout);
	}

	/**
	 * [发送post请求](Send post request)
	 * @description zh - 发送post请求
	 * @description en - Send post request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:52:55
	 * @param url URL
	 * @param paramMap 表单数据
	 * @param timeout 超时时间
	 * @return java.lang.String
	 */
	public static String post(String url, Map<String, Object> paramMap, int timeout) {
		return HttpRequest
				.post(url)
				.form(paramMap)
				.timeout(timeout)
				.execute()
				.body();
	}

	/**
	 * [发送post请求](Send post request)
	 * @description zh - 发送post请求
	 * @description en - Send post request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:53:34
	 * @param url URL
	 * @param body post表单数据
	 * @return java.lang.String
	 */
	public static String post(String url, String body) {
		return post(url, body, HttpGlobalConfig.timeout);
	}

	/**
	 * [发送post请求](Send post request)
	 * @description zh - 发送post请求
	 * @description en - Send post request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-08 21:55:17
	 * @param url URL
	 * @param body post表单数据
	 * @param timeout 超时时间
	 * @return java.lang.String
	 */
	public static String post(String url, String body, int timeout) {
		return HttpRequest
				.post(url)
				.timeout(timeout)
				.body(body)
				.execute()
				.body();
	}


}
