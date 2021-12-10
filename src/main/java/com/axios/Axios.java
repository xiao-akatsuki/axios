package com.axios;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.axios.core.config.global.HttpGlobalConfig;
import com.axios.core.http.HttpDownloader;
import com.axios.core.http.HttpRequest;
import com.axios.core.http.url.UrlQuery;
import com.axios.core.requestMethod.RequestMethod;
import com.axios.core.rfc.RFC3986;
import com.axios.core.tool.URLEncoder;
import com.axios.core.tool.UrlTool;
import com.axios.core.tool.file.FileTool;
import com.axios.core.tool.http.HttpTool;

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

	/** ------------------- post ------------------- */

	/**
	 * [发送PUT请求](Send put request)
	 * @description zh - 发送PUT请求
	 * @description en - Send put request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:01:12
	 * @param url URL
	 * @param paramMap 表单数据
	 * @return java.lang.String
	 */
	public static String put(String url, Map<String, Object> paramMap) {
		return put(url, paramMap, HttpGlobalConfig.timeout);
	}

	/**
	 * [发送PUT请求](Send put request)
	 * @description zh - 发送PUT请求
	 * @description en - Send put request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:08:06
	 * @param url URL
	 * @param paramMap 表单数据
	 * @param timeout 超时时间
	 * @return java.lang.String
	 */
	public static String put(String url, Map<String, Object> paramMap, int timeout) {
		return HttpRequest
				.put(url)
				.form(paramMap)
				.timeout(timeout)
				.execute()
				.body();
	}

	/**
	 * [发送PUT请求](Send put request)
	 * @description zh - 发送PUT请求
	 * @description en - Send put request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:08:53
	 * @param url URL
	 * @param body post表单数据
	 * @return java.lang.String
	 */
	public static String put(String url, String body) {
		return put(url, body, HttpGlobalConfig.timeout);
	}

	/**
	 * [发送PUT请求](Send put request)
	 * @description zh - 发送PUT请求
	 * @description en - Send put request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:09:20
	 * @param url URL
	 * @param body post表单数据
	 * @param timeout 超时时间
	 * @return java.lang.String
	 */
	public static String put(String url, String body, int timeout) {
		return HttpRequest
				.put(url)
				.timeout(timeout)
				.body(body)
				.execute()
				.body();
	}

	/** ------------------- download ------------------- */

	/**
	 * [下载远程文本](Download remote text)
	 * @description zh - 下载远程文本
	 * @description en - Download remote text
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:31:19
	 * @param url URL
	 * @param customCharsetName 自定义的字符集
	 * @return java.lang.String
	 */
	public static String downloadString(String url, String customCharsetName) {
		return downloadString(url, UrlTool.isBlank(customCharsetName) ? Charset.defaultCharset() : Charset.forName(customCharsetName));
	}

	/**
	 * [下载远程文本](Download remote text)
	 * @description zh - 下载远程文本
	 * @description en - Download remote text
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:32:28
	 * @param url URL
	 * @param customCharsetName 自定义的字符集
	 * @return java.lang.String
	 */
	public static String downloadString(String url, Charset customCharset) {
		return HttpDownloader.downloadString(url, customCharset);
	}

	/**
	 * [下载远程文件](Download remote file)
	 * @description zh - 下载远程文件
	 * @description en - Download remote file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:32:54
	 * @param url URL
	 * @param dest 目标文件或目录
	 * @return long
	 */
	public static long downloadFile(String url, String dest) {
		return downloadFile(url, FileTool.file(dest));
	}

	/**
	 * [下载远程文件](Download remote file)
	 * @description zh - 下载远程文件
	 * @description en - Download remote file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:36:01
	 * @param url URL
	 * @param dest 目标文件或目录
	 * @return long
	 */
	public static long downloadFile(String url, File destFile) {
		return downloadFile(url, destFile, -1);
	}

	/**
	 * [下载远程文件](Download remote file)
	 * @description zh - 下载远程文件
	 * @description en - Download remote file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:37:21
	 * @param url URL
	 * @param destFile 目标文件或目录
	 * @param timeout 超时时间
	 * @return long
	 */
	public static long downloadFile(String url, File destFile, int timeout) {
		return HttpDownloader.downloadFile(url, destFile, timeout);
	}

	/**
	 * [下载远程文件](Download remote file)
	 * @description zh - 下载远程文件
	 * @description en - Download remote file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:37:58
	 * @param url URL
	 * @param dest 目标文件或目录
	 * @return java.io.File
	 */
	public static File downloadFileFromUrl(String url, String dest) {
		return downloadFileFromUrl(url, FileTool.file(dest));
	}

	/**
	 * [下载远程文件](Download remote file)
	 * @description zh - 下载远程文件
	 * @description en - Download remote file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:38:23
	 * @param url URL
	 * @param destFile 目标文件或目录
	 * @return java.io.File
	 */
	public static File downloadFileFromUrl(String url, File destFile) {
		return downloadFileFromUrl(url, destFile, -1);
	}

	/**
	 * [下载远程文件](Download remote file)
	 * @description zh - 下载远程文件
	 * @description en - Download remote file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:38:57
	 * @param url URL
	 * @param destFile 目标文件或目录
	 * @param timeout 超时时间
	 * @return java.io.File
	 */
	public static File downloadFileFromUrl(String url, File destFile, int timeout) {
		return HttpDownloader.downloadForFile(url, destFile, timeout);
	}

	/**
	 * [下载远程文件](Download remote file)
	 * @description zh - 下载远程文件
	 * @description en - Download remote file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:39:21
	 * @param url URL
	 * @param out OutputStream
	 * @param isCloseOut 将下载内容写到输出流中
	 * @return long
	 */
	public static long download(String url, OutputStream out, boolean isCloseOut) {
		return HttpDownloader.download(url, out, isCloseOut);
	}

	/**
	 * [下载远程文件数据，支持30x跳转](Download remote file data and support 30x jump)
	 * @description zh - 下载远程文件数据，支持30x跳转
	 * @description en - Download remote file data and support 30x jump
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-09 19:40:02
	 * @param url URL
	 * @return byte[]
	 */
	public static byte[] downloadBytes(String url) {
		return HttpDownloader.downloadBytes(url);
	}

	/** ------------------- param ------------------- */

	/**
	 * [将Map形式的Form表单数据转换为Url参数形式](Convert form data in map form to URL parameter form)
	 * @description zh - 将Map形式的Form表单数据转换为Url参数形式
	 * @description en - Convert form data in map form to URL parameter form
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-10 20:50:46
	 * @param paramMap 表单的数据
	 * @return java.lang.String
	 */
	public static String toParams(Map<String, ?> paramMap) {
		return toParams(paramMap, StandardCharsets.UTF_8);
	}

	/**
	 * [将Map形式的Form表单数据转换为Url参数形式](Convert form data in map form to URL parameter form)
	 * @description zh - 将Map形式的Form表单数据转换为Url参数形式
	 * @description en - Convert form data in map form to URL parameter form
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-10 20:51:56
	 * @param paramMap 表单的数据
	 * @param charset 字符集
	 * @return java.lang.String
	 */
	public static String toParams(Map<String, ?> paramMap, Charset charset) {
		return toParams(paramMap, charset, false);
	}

	/**
	 * [将Map形式的Form表单数据转换为Url参数形式](Convert form data in map form to URL parameter form)
	 * @description zh - 将Map形式的Form表单数据转换为Url参数形式
	 * @description en - Convert form data in map form to URL parameter form
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-10 20:52:30
	 * @param paramMap 表单的数据
	 * @param charset 字符集
	 * @param isFormUrlEncoded 是否为x-www-form-urlencoded模式
	 * @return java.lang.String
	 */
	public static String toParams(Map<String, ?> paramMap, Charset charset, boolean isFormUrlEncoded) {
		return UrlQuery
				.of(paramMap, isFormUrlEncoded)
				.build(charset);
	}

	/**
	 * [对URL参数做编码，只编码键和值](Encode the URL parameters, only the key and value)
	 * @description zh - 对URL参数做编码，只编码键和值
	 * @description en - Encode the URL parameters, only the key and value
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-10 20:53:56
	 * @param urlWithParams url和参数
	 * @param charset 编码
	 * @return java.lang.String
	 */
	public static String encodeParams(String urlWithParams, Charset charset) {
		if (UrlTool.isBlank(urlWithParams)) {
			return "";
		}
		String urlPart = null;
		String paramPart;
		final int pathEndPos = urlWithParams.indexOf('?');
		if (pathEndPos > -1) {
			urlPart = UrlTool.sub(urlWithParams, 0, pathEndPos);
			paramPart = UrlTool.subSuf(urlWithParams, pathEndPos + 1);
			if (UrlTool.isBlank(paramPart)) {
				return urlPart;
			}
		} else if (false == UrlTool.contains(urlWithParams, '=')) {
			return urlWithParams;
		} else {
			paramPart = urlWithParams;
		}
		paramPart = normalizeParams(paramPart, charset);
		return UrlTool.isBlank(urlPart) ? paramPart : urlPart + "?" + paramPart;
	}

	/**
	 * [标准化参数字符串](Normalized parameter string)
	 * @description zh - 标准化参数字符串
	 * @description en - Normalized parameter string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-10 20:55:20
	 * @param paramPart 参数字符串
	 * @param charset 编码
	 * @return java.lang.String
	 */
	public static String normalizeParams(String paramPart, Charset charset) {
		final StringBuilder builder = new StringBuilder();
		final int len = paramPart.length();
		String name = null;
		int pos = 0;
		char c;
		int i;
		for (i = 0; i < len; i++) {
			c = paramPart.charAt(i);
			if (c == '=') {
				if (null == name) {
					name = (pos == i) ? "" : paramPart.substring(pos, i);
					pos = i + 1;
				}
			} else if (c == '&') {
				if (pos != i) {
					if (null == name) {
						name = paramPart.substring(pos, i);
						builder.append(RFC3986.QUERY_PARAM_NAME.encode(name, charset)).append('=');
					} else {
						builder.append(RFC3986.QUERY_PARAM_NAME.encode(name, charset)).append('=')
								.append(RFC3986.QUERY_PARAM_VALUE.encode(paramPart.substring(pos, i), charset)).append('&');
					}
					name = null;
				}
				pos = i + 1;
			}
		}
		if (null != name) {
			builder.append(URLEncoder.QUERY.encode(name, charset)).append('=');
		}
		if (pos != i) {
			builder.append(
					null == name && pos > 0 ?
						"=" :
						URLEncoder.QUERY.encode(paramPart.substring(pos, i), charset)
			);
		}
		int lastIndex = builder.length() - 1;
		if ('&' == builder.charAt(lastIndex)) {
			builder.deleteCharAt(lastIndex);
		}
		return builder.toString();
	}

	/**
	 * [将URL参数解析为Map](Resolve URL parameters to map)
	 * @description zh - 将URL参数解析为Map
	 * @description en - Resolve URL parameters to map
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-10 20:56:20
	 * @param paramStr 参数字符串
	 * @param charset 编码
	 * @return java.util.Map<java.lang.CharSequence, java.lang.CharSequence>
	 */
	public static Map<CharSequence, CharSequence> decodeParamMap(String paramsStr, Charset charset) {
		final Map<CharSequence, CharSequence> queryMap = UrlQuery.of(paramsStr, charset).getQueryMap();
		return HttpTool.isEmpty(queryMap) ? Collections.emptyMap() : queryMap;
	}

	/**
	 * [将URL参数解析为Map](Resolve URL parameters to map)
	 * @description zh - 将URL参数解析为Map
	 * @description en - Resolve URL parameters to map
	 * @description en -
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-10 20:57:59
	 * @param paramStr 参数字符串
	 * @param charset 编码
	 * @return java.util.Map<java.lang.String, java.util.List<java.lang.String>>
	 */
	public static Map<String, List<String>> decodeParams(String paramsStr, String charset) {
		return decodeParams(paramsStr, UrlTool.isBlank(charset) ? Charset.defaultCharset() : Charset.forName(charset));
	}

	/**
	 * [将URL参数解析为Map](Resolve URL parameters to map)
	 * @description zh - 将URL参数解析为Map
	 * @description en - Resolve URL parameters to map
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-10 20:59:12
	 * @param paramStr 参数字符串
	 * @param charset 编码
	 * @return java.util.Map<java.lang.String, java.util.List<java.lang.String>>
	 */
	public static Map<String, List<String>> decodeParams(String paramsStr, Charset charset) {
		final Map<CharSequence, CharSequence> queryMap = UrlQuery.of(paramsStr, charset).getQueryMap();
		if (HttpTool.isEmpty(queryMap)) {
			return Collections.emptyMap();
		}
		final Map<String, List<String>> params = new LinkedHashMap<>();
		queryMap.forEach((key, value) -> {
			final List<String> values = params.computeIfAbsent(null == key ? null : key.toString(), k -> new ArrayList<>(1));
			values.add(null == value ? null : value.toString());
		});
		return params;
	}





}
