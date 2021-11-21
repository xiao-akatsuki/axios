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
import com.axios.core.rfc.RFC3986;
import com.axios.core.tool.UrlTool;
import com.axios.core.tool.http.HttpTool;
import com.axios.core.tool.ssl.SSLTool;

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

	/** getter and setter */

	/**
	 * [获取协议，例如http](Get protocol, such as http)
	 * @description zh - 获取协议，例如http
	 * @description en - Get protocol, such as http
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:30:40
	 * @return java.lang.String
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * [获取协议，例如http](Get protocol, such as http)
	 * @description zh - 获取协议，例如http
	 * @description en - Get protocol, such as http
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:32:52
	 * @return java.lang.String
	 */
	public String getSchemeWithDefault() {
		return UrlTool.emptyToDefault(this.scheme, DEFAULT_SCHEME);
	}

	/**
	 * [设置协议](Set protocol)
	 * @description zh - 设置协议
	 * @description en - Set protocol
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:33:31
	 * @param scheme 协议
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public UrlBuilder setScheme(String scheme) {
		this.scheme = scheme;
		return this;
	}

	/**
	 * [获取主机](get host)
	 * @description zh - 获取主机
	 * @description en - get host
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:34:08
	 * @return java.lang.String
	 */
	public String getHost() {
		return host;
	}

	/**
	 * [设置主机](set host)
	 * @description zh - 设置主机
	 * @description eh - set host
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:34:38
	 * @param host 主机地址
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public UrlBuilder setHost(String host) {
		this.host = host;
		return this;
	}

	/**
	 * [获取端口](get port)
	 * @description zh - 获取端口
	 * @description en - get port
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:35:33
	 * @return int
	 */
	public int getPort() {
		return port;
	}

	/**
	 * [设置端口](set port)
	 * @description zh - 设置端口
	 * @description en - set port
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:36:18
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public UrlBuilder setPort(int port) {
		this.port = port;
		return this;
	}

	/**
	 * [获得authority部分](Get the authority section)
	 * @description zh - 获得authority部分
	 * @description en - Get the authority section
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:37:20
	 * @return java.lang.String
	 */
	public String getAuthority() {
		return (port < 0) ? host : host + ":" + port;
	}

	/**
	 * [获取路径](Get path)
	 * @description zh - 获取路径
	 * @description en - Get path
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:37:56
	 * @return
	 */
	public UrlPath getPath() {
		return path;
	}

	/**
	 * [获得路径](Get path)
	 * @description zh - 获得路径
	 * @description en - Get path
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:38:51
	 * @return java.lang.String
	 */
	public String getPathStr() {
		return null == this.path ? "/" : this.path.build(charset);
	}

	/**
	 * [设置路径](set up path)
	 * @description zh - 设置路径
	 * @description en - set up path
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:39:37
	 * @param path 路径
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public UrlBuilder setPath(UrlPath path) {
		this.path = path;
		return this;
	}

	/**
	 * [增加路径](add path)
	 * @description zh - 增加路径
	 * @description en - add path
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:40:22
	 * @param path 路径
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public UrlBuilder addPath(CharSequence path) {
		UrlPath.of(path, this.charset).getSegments().forEach(this::addPathSegment);
		return this;
	}

	/**
	 * [增加路径节点](Add path node)
	 * @description zh - 增加路径节点
	 * @description en - Add path node
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:41:12
	 * @param segment 路径节点
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public UrlBuilder addPathSegment(CharSequence segment) {
		if (UrlTool.isEmpty(segment)) {
			return this;
		}
		if (null == this.path) {
			this.path = new UrlPath();
		}
		this.path.add(segment);
		return this;
	}

	/**
	 * [获取查询语句](Get query statement)
	 * @description zh - 获取查询语句
	 * @description en - Get query statement
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:42:19
	 * @return
	 */
	public UrlQuery getQuery() {
		return query;
	}

	/**
	 * [获取查询语句](Get query statement)
	 * @description zh - 获取查询语句
	 * @description en - Get query statement
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:42:49
	 * @return java.lang.String
	 */
	public String getQueryStr() {
		return null == this.query ? null : this.query.build(this.charset);
	}

	/**
	 * [设置查询语句](Set query statement)
	 * @description zh - 设置查询语句
	 * @description en - Set query statement
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:46:25
	 * @param query 查询语句
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public UrlBuilder setQuery(UrlQuery query) {
		this.query = query;
		return this;
	}

	/**
	 * [添加查询项](Add query item)
	 * @description zh - 添加查询项
	 * @description en - Add query item
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:47:28
	 * @param key 键
	 * @param value 值
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public UrlBuilder addQuery(String key, String value) {
		if (UrlTool.isEmpty(key)) {
			return this;
		}

		if (this.query == null) {
			this.query = new UrlQuery();
		}
		this.query.add(key, value);
		return this;
	}

	/**
	 * [获取标识符](Get identifier)
	 * @description zh - 获取标识符
	 * @description en - Get identifier
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:48:06
	 * @return java.lang.String
	 */
	public String getFragment() {
		return fragment;
	}

	/**
	 * [获取标识符](Get identifier)
	 * @description zh - 获取标识符
	 * @description en - Get identifier
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:50:03
	 * @return java.lang.String
	 */
	public String getFragmentEncoded() {
		return RFC3986.FRAGMENT.encode(this.fragment, this.charset);
	}

	/**
	 * [设置标识符](Set identifier)
	 * @description zh - 设置标识符
	 * @description en - Set identifier
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:51:24
	 * @param fragment 标识符
	 * @param com.axios.core.http.url.UrlBuilder
	 */
	public UrlBuilder setFragment(String fragment) {
		if (UrlTool.isEmpty(fragment)) {
			this.fragment = null;
		}
		this.fragment = UrlTool.removePrefix(fragment, "#");
		return this;
	}

	/**
	 * [获取编码](Get encoding)
	 * @description zh - 获取编码
	 * @description en - Get encoding
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:52:17
	 * @param java.nio.charset.Charset
	 */
	public Charset getCharset() {
		return charset;
	}

	/**
	 * [设置编码](Set encoding)
	 * @description zh - 设置编码
	 * @description en - Set encoding
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:52:48
	 * @param charset 字符集
	 * @return com.axios.core.http.url.UrlBuilder
	 */
	public UrlBuilder setCharset(Charset charset) {
		this.charset = charset;
		return this;
	}

	/**
	 * [创建URL字符串](Create URL string)
	 * @description zh - 创建URL字符串
	 * @description en - Create URL string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:53:37
	 * @return java.lang.String
	 */
	public String build() {
		return toURL().toString();
	}

	/**
	 * [转换为 URL 对象](Convert to URL object)
	 * @description zh - 转换为 URL 对象
	 * @description en - Convert to URL object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:54:11
	 * @return java.net.URL
	 */
	public URL toURL() {
		return toURL(null);
	}

	/**
	 * [转换为 URL 对象](Convert to URL object)
	 * @description zh - 转换为 URL 对象
	 * @description en - Convert to URL object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:55:54
	 * @param handler URLStreamHandler
	 * @return java.net.URL
	 */
	public URL toURL(URLStreamHandler handler) {
		final StringBuilder fileBuilder = new StringBuilder();

		// path
		fileBuilder.append(UrlTool.blankToDefault(getPathStr(), "/"));

		// query
		final String query = getQueryStr();
		if (UrlTool.isNotBlank(query)) {
			fileBuilder.append('?').append(query);
		}

		// fragment
		if (UrlTool.isNotBlank(this.fragment)) {
			fileBuilder.append('#').append(getFragmentEncoded());
		}

		try {
			return new URL(getSchemeWithDefault(), host, port, fileBuilder.toString(), handler);
		} catch (MalformedURLException e) {
			return null;
		}
	}

	/**
	 * [转换为 URL 对象](Convert to URL object)
	 * @description zh - 转换为 URL 对象
	 * @description en - Convert to URL object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 19:56:35
	 * @return java.net.URI
	 */
	public URI toURI() {
		try {
			return new URI(
					getSchemeWithDefault(),
					getAuthority(),
					getPathStr(),
					getQueryStr(),
					getFragmentEncoded());
		} catch (URISyntaxException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return build();
	}

}

