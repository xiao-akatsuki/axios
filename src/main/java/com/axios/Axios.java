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

	public static HttpRequest createRequest(RequestMethod method, String url) {
		return new HttpRequest(url).method(method);
	}

	public static HttpRequest createGet(String url) {
		return createGet(url, false);
	}

	public static HttpRequest createGet(String url, boolean isFollowRedirects) {
		return HttpRequest.get(url).setFollowRedirects(isFollowRedirects);
	}

	public static HttpRequest createPost(String url) {
		return HttpRequest.post(url);
	}

	public static HttpRequest createPut(String url) {
		return HttpRequest.put(url);
	}

	public static HttpRequest createDelete(String url) {
		return HttpRequest.delete(url);
	}

	public static HttpRequest createHeader(String url) {
		return HttpRequest.head(url);
	}

	public static HttpRequest createOptions(String url) {
		return HttpRequest.options(url);
	}

	public static HttpRequest createPatch(String url) {
		return HttpRequest.patch(url);
	}

	/** ------------------- get ------------------- */

	public static String get(String urlString, Charset customCharset) {
		return HttpRequest
				.get(urlString)
				.charset(customCharset)
				.execute()
				.body();
	}

	public static String get(String urlString) {
		return get(urlString, HttpGlobalConfig.timeout);
	}

	public static String get(String urlString, int timeout) {
		return HttpRequest
				.get(urlString)
				.timeout(timeout)
				.execute()
				.body();
	}

	public static String get(String urlString, Map<String, Object> paramMap) {
		return HttpRequest
				.get(urlString)
				.form(paramMap)
				.execute()
				.body();
	}
}
