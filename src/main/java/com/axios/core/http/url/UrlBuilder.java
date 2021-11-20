package com.axios.core.http.url;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.axios.core.assertion.Assert;
import com.axios.core.tool.UrlTool;
import com.axios.core.tool.http.HttpTool;

/**
 * [URL 生成器](URL generator)
 * @description zh - URL 生成器
 * @description en - URL generator
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-20 09:35:35
 */
public final class UrlBuilder implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String DEFAULT_SCHEME = "http";

	/**
	 * 协议，例如http
	 */
	private String scheme;

	/**
	 * 主机，例如127.0.0.1
	 */
	private String host;

	/**
	 * 端口，默认-1
	 */
	private int port = -1;

	/**
	 * 路径，例如/aa/bb/cc
	 */
	private UrlPath path;

	/**
	 * 查询语句，例如a=1&amp;b=2
	 */
	private UrlQuery query;

	/**
	 * 标识符，例如#后边的部分
	 */
	private String fragment;

	/**
	 * 编码，用于URLEncode和URLDecode
	 */
	private Charset charset;

	public static UrlBuilder create() {
		return new UrlBuilder();
	}

	public UrlBuilder() {
		this.charset = StandardCharsets.UTF_8;
	}

	public UrlBuilder(String scheme, String host, int port, UrlPath path, UrlQuery query, String fragment, Charset charset) {
		this.charset = charset;
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.path = path;
		this.query = query;
		this.setFragment(fragment);
	}

	/**
	 * [使用URI构建UrlBuilder](Building urlbuilder using URI)
	 * @description zh - 使用URI构建UrlBuilder
	 * @description en - Building urlbuilder using URI
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-20 09:37:12
	 * @param uri URI
	 * @param charset 编码
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public static UrlBuilder of(URI uri, Charset charset) {
		return of(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getPath(), uri.getRawQuery(), uri.getFragment(), charset);
	}

	/**
	 * [使用URL字符串构建UrlBuilder](Building urlbuilder using URL strings)
	 * @description zh - 使用URL字符串构建UrlBuilder
	 * @description en - Building urlbuilder using URL strings
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-20 09:43:53
	 * @param httpUrl URL字符串
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public static UrlBuilder ofHttpWithoutEncode(String httpUrl) {
		return ofHttp(httpUrl, null);
	}

	/**
	 * [使用URL字符串构建UrlBuilder](Building urlbuilder using URL strings)
	 * @description zh - 使用URL字符串构建UrlBuilder
	 * @description en - Building urlbuilder using URL strings
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-20 09:45:23
	 * @param httpUrl URL字符串
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public static UrlBuilder ofHttp(String httpUrl) {
		return ofHttp(httpUrl, StandardCharsets.UTF_8);
	}

	/**
	 * [使用URL字符串构建UrlBuilder](Building urlbuilder using URL strings)
	 * @description zh - 使用URL字符串构建UrlBuilder
	 * @description en - Building urlbuilder using URL strings
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-20 09:46:17
	 * @param httpUrl URL字符串
	 * @param charset 字符集
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public static UrlBuilder ofHttp(String httpUrl, Charset charset) {
		Assert.notBlank(httpUrl, "Http url must be not blank!");

		final int sepIndex = httpUrl.indexOf("://");
		if (sepIndex < 0) {
			httpUrl = "http://" + httpUrl.trim();
		}
		return of(httpUrl, charset);
	}

	/**
	 * [使用URL字符串构建UrlBuilder](Building urlbuilder using URL strings)
	 * @description zh - 使用URL字符串构建UrlBuilder
	 * @description en - Building urlbuilder using URL strings
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-20 09:47:01
	 * @param url URL字符串
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public static UrlBuilder of(String url) {
		return of(url, StandardCharsets.UTF_8);
	}

	/**
	 * [使用URL字符串构建UrlBuilder](Building urlbuilder using URL strings)
	 * @description zh - 使用URL字符串构建UrlBuilder
	 * @description en - Building urlbuilder using URL strings
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-20 09:48:41
	 * @param url URL
	 * @param charset 字符集
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public static UrlBuilder of(String url, Charset charset) {
		Assert.notBlank(url, "Url must be not blank!");
		return of(HttpTool.url(UrlTool.trim(url)), charset);
	}

	/**
	 * [使用URL构建UrlBuilder](Building urlbuilder using URL)
	 * @description zh - 使用URL构建UrlBuilder
	 * @description en - Building urlbuilder using URL
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-20 09:49:47
	 * @param url URL
	 * @param charset 字符集
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public static UrlBuilder of(URL url, Charset charset) {
		return of(url.getProtocol(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef(), charset);
	}

	/**
	 * [构建UrlBuilder](Build urlbuilder)
	 * @description zh - 构建UrlBuilder
	 * @description en - Build urlbuilder
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-20 09:50:52
	 * @param scheme 协议，默认http
	 * @param host 主机，例如127.0.0.1
	 * @param port 端口，-1表示默认端口
	 * @param path 路径，例如/aa/bb/cc
	 * @param query 查询，例如a=1&amp;b=2
	 * @param fragment 标识符例如#后边的部分
	 * @param charset  编码，用于URLEncode和URLDecode
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public static UrlBuilder of(String scheme, String host, int port, String path, String query, String fragment, Charset charset) {
		return of(scheme, host, port,
				UrlPath.of(path, charset),
				UrlQuery.of(query, charset, false), fragment, charset);
	}

	/**
	 * [构建UrlBuilder](Build urlbuilder)
	 * @description zh - 构建UrlBuilder
	 * @description en - Build urlbuilder
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-20 09:52:29
	 * @param scheme 协议，默认http
	 * @param host 主机，例如127.0.0.1
	 * @param port 端口，-1表示默认端口
	 * @param path 路径，例如/aa/bb/cc
	 * @param query 查询，例如a=1&amp;b=2
	 * @param fragment 标识符例如#后边的部分
	 * @param charset  编码，用于URLEncode和URLDecode
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public static UrlBuilder of(String scheme, String host, int port, UrlPath path, UrlQuery query, String fragment, Charset charset) {
		return new UrlBuilder(scheme, host, port, path, query, fragment, charset);
	}



}
