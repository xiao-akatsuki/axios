package com.axios.core.http.url;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.axios.core.map.TableMap;
import com.axios.core.rfc.PercentCodec;
import com.axios.core.rfc.RFC3986;
import com.axios.core.tool.UrlTool;
import com.axios.core.tool.text.StrJoiner;

/**
 * [URL中查询字符串部分的封装](Encapsulation of query string part in URL)
 * @description zh - URL中查询字符串部分的封装
 * @description en - Encapsulation of query string part in URL
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-22 20:23:46
 */
public class UrlQuery {

	/** 可重复键和值的Map */
	private final TableMap<CharSequence, CharSequence> query;

	/** 是否为x-www-form-urlencoded模式，此模式下空格会编码为'+' */
	private final boolean isFormUrlEncoded;

	public UrlQuery() {
		this(null);
	}

	public UrlQuery(boolean isFormUrlEncoded) {
		this(null, isFormUrlEncoded);
	}

	public UrlQuery(Map<? extends CharSequence, ?> queryMap) {
		this(queryMap, false);
	}

	public UrlQuery(Map<? extends CharSequence, ?> queryMap, boolean isFormUrlEncoded) {
		if (null != queryMap && false == queryMap.isEmpty()) {
			query = new TableMap<>(queryMap.size());
			addAll(queryMap);
		} else {
			query = new TableMap<>(16);
		}
		this.isFormUrlEncoded = isFormUrlEncoded;
	}

	/**
	 * [构建UrlQuery](Build urlquery)
	 * @description zh - 构建UrlQuery
	 * @description en - Build urlquery
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:28:28
	 * @param queryMap 初始化的查询键值对
	 * @return com.axios.core.http.url.UrlQuery
	 */
	public static UrlQuery of(Map<? extends CharSequence, ?> queryMap) {
		return new UrlQuery(queryMap);
	}

	/**
	 * [构建UrlQuery](Build urlquery)
	 * @description zh - 构建UrlQuery
	 * @description en - Build urlquery
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:29:13
	 * @param queryMap 初始化的查询键值对
	 * @param isFormUrlEncoded 是否为x-www-form-urlencoded模式
	 * @return com.axios.core.http.url.UrlQuery
	 */
	public static UrlQuery of(Map<? extends CharSequence, ?> queryMap, boolean isFormUrlEncoded) {
		return new UrlQuery(queryMap, isFormUrlEncoded);
	}

	/**
	 * [构建UrlQuery](Build urlquery)
	 * @description zh - 构建UrlQuery
	 * @description en - Build urlquery
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:29:55
	 * @param queryStr 初始化的查询字符串
	 * @param charset decode用的编码
	 * @return com.axios.core.http.url.UrlQuery
	 */
	public static UrlQuery of(String queryStr, Charset charset) {
		return of(queryStr, charset, true);
	}

	/**
	 * [构建UrlQuery](Build urlquery)
	 * @description zh - 构建UrlQuery
	 * @description en - Build urlquery
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:30:36
	 * @param queryStr 初始化的查询字符串
	 * @param charset decode用的编码
	 * @param autoRemovePath 是否自动去除path部分
	 * @return com.axios.core.http.url.UrlQuery
	 */
	public static UrlQuery of(String queryStr, Charset charset, boolean autoRemovePath) {
		return of(queryStr, charset, autoRemovePath, false);
	}

	/**
	 * [构建UrlQuery](Build urlquery)
	 * @description zh - 构建UrlQuery
	 * @description en - Build urlquery
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:31:08
	 * @param queryStr 初始化的查询字符串
	 * @param charset decode用的编码
	 * @param autoRemovePath 是否自动去除path部分
	 * @param isFormUrlEncoded 是否为x-www-form-urlencoded模式
	 * @return com.axios.core.http.url.UrlQuery
	 */
	public static UrlQuery of(String queryStr, Charset charset, boolean autoRemovePath, boolean isFormUrlEncoded) {
		return new UrlQuery(isFormUrlEncoded).parse(queryStr, charset, autoRemovePath);
	}

	/**
	 * [增加键值对](Add key value pair)
	 * @description zh - 增加键值对
	 * @description en - Add key value pair
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:32:45
	 * @param key 键
	 * @param value 值
	 * @return com.axios.core.http.url.UrlQuery
	 */
	public UrlQuery add(CharSequence key, Object value) {
		this.query.put(key, toStr(value));
		return this;
	}

	/**
	 * [批量增加键值对](Batch add key value pairs)
	 * @description zh - 批量增加键值对
	 * @description en - Batch add key value pairs
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:33:40
	 * @param queryMap query中的键值对
	 * @return com.axios.core.http.url.UrlQuery
	 */
	public UrlQuery addAll(Map<? extends CharSequence, ?> queryMap) {
		if (null != queryMap && false == queryMap.isEmpty()) {
			queryMap.forEach(this::add);
		}
		return this;
	}

	/**
	 * [解析URL中的查询字符串](Parse query string in URL)
	 * @description zh - 解析URL中的查询字符串
	 * @description en - Parse query string in URL
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:34:24
	 * @param queryStr 查询字符串
	 * @param charset decode编码
	 * @return com.axios.core.http.url.UrlQuery
	 */
	public UrlQuery parse(String queryStr, Charset charset) {
		return parse(queryStr, charset, true);
	}

	/**
	 * [解析URL中的查询字符串](Parse query string in URL)
	 * @description zh - 解析URL中的查询字符串
	 * @description en - Parse query string in URL
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 21:20:36
	 * @param queryStr 查询字符串
	 * @param charset decode编码
	 * @param autoRemovePath 是否自动去除path部分
	 * @return com.axios.core.http.url.UrlQuery
	 */
	public UrlQuery parse(String queryStr, Charset charset, boolean autoRemovePath) {
		if (UrlTool.isBlank(queryStr)) {
			return this;
		}
		if (autoRemovePath) {
			int pathEndPos = queryStr.indexOf('?');
			if (pathEndPos > -1) {
				queryStr = queryStr.substring(pathEndPos + 1, queryStr.length());
				if (UrlTool.isBlank(queryStr)) {
					return this;
				}
			}
		}
		return doParse(queryStr, charset);
	}

	/**
	 * [获得查询的Map](Get the map of the query)
	 * @description zh - 获得查询的Map
	 * @description en - Get the map of the query
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:51:13
	 * @param java.util.Map<java.lang.CharSequence,java.lang.CharSequence>
	 */
	public Map<CharSequence, CharSequence> getQueryMap() {
		return Collections.unmodifiableMap(this.query);
	}

	/**
	 * [获取查询值](Get query value)
	 * @description zh - 获取查询值
	 * @description en - Get query value
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:52:06
	 * @param key 键
	 * @return java.lang.CharSequence
	 */
	public CharSequence get(CharSequence key) {
		if (null == this.query || this.query.isEmpty()) {
			return null;
		}
		return this.query.get(key);
	}

	/**
	 * [构建URL查询字符串](Build URL query string)
	 * @description zh - 构建URL查询字符串
	 * @description en - Build URL query string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 21:00:34
	 * @param charset encode编码
	 * @return java.lang.String
	 */
	public String build(Charset charset) {
		if (isFormUrlEncoded) {
			return build(FormUrlencoded.ALL, FormUrlencoded.ALL, charset);
		}

		return build(RFC3986.QUERY_PARAM_NAME, RFC3986.QUERY_PARAM_VALUE, charset);
	}

	/**
	 * [构建URL查询字符串](Build URL query string)
	 * @description zh - 构建URL查询字符串
	 * @description en - Build URL query string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 21:02:04
	 * @param keyCoder 键
	 * @param valueCoder 值
	 * @param charset encode编码
	 * @return java.lang.String
	 */
	public String build(PercentCodec keyCoder, PercentCodec valueCoder, Charset charset) {
		if (null == this.query || this.query.isEmpty()) {
			return "";
		}

		final StringBuilder sb = new StringBuilder();
		CharSequence name;
		CharSequence value;
		for (Map.Entry<CharSequence, CharSequence> entry : this.query) {
			name = entry.getKey();
			if (null != name) {
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(keyCoder.encode(name, charset));
				value = entry.getValue();
				if (null != value) {
					sb.append("=").append(valueCoder.encode(value, charset));
				}
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return build(null);
	}

	/**
	 * [解析URL中的查询字符串](Parse query string in URL)
	 * @description zh - 解析URL中的查询字符串
	 * @description en - Parse query string in URL
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 21:03:21
	 * @param queryStr 查询字符串
	 * @param charset encode编码
	 */
	private UrlQuery doParse(String queryStr, Charset charset) {
		final int len = queryStr.length();
		String name = null;
		int pos = 0;
		int i;
		char c;
		for (i = 0; i < len; i++) {
			c = queryStr.charAt(i);
			switch (c) {
				case '=':
					if (null == name) {
						name = queryStr.substring(pos, i);
						pos = i + 1;
					}
					break;
				case '&':
					addParam(name, queryStr.substring(pos, i), charset);
					name = null;
					if (i + 4 < len && "amp;".equals(queryStr.substring(i + 1, i + 5))) {
						i += 4;
					}
					pos = i + 1;
					break;
			}
		}
		if (i - pos == len) {
			if (queryStr.startsWith("http") || queryStr.contains("/")) {
				return this;
			}
		}
		addParam(name, queryStr.substring(pos, i), charset);
		return this;
	}

	/**
	 * [对象转换为字符串](Object to string)
	 * @description zh - 对象转换为字符串
	 * @description en - Object to string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 21:04:53
	 * @param value 值
	 * @return java.lang.String
	 */
	private static String toStr(Object value) {
		String result;
		if (value instanceof Iterable) {
			result = join((Iterable<?>) value, ",");
		} else if (value instanceof Iterator) {
			result = join((Iterator<?>) value, ",");

		} else {
			result = value.toString();
		}
		return result;
	}

	/**
	 * [数组转为一个不可变List](Convert array to an immutable list)
	 * @description zh - 数组转为一个不可变List
	 * @description en - Convert array to an immutable list
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 21:16:32
	 * @param iterable Iterable
	 * @param value 分隔符
	 * @return java.lang.String
	 */
	private static String join(Iterable<?> iterable, String value) {
		return StrJoiner.of(value).append(iterable).toString();
	}

	/**
	 * [数组转为一个不可变List](Convert array to an immutable list)
	 * @description zh - 数组转为一个不可变List
	 * @description en - Convert array to an immutable list
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 21:17:29
	 * @param iterator Iterator
	 * @param value 分隔符
	 * @return java.lang.String
	 */
	private static String join(Iterator<?> iterator, String value) {
		return StrJoiner.of(value).append(iterator).toString();
	}

	/**
	 * [将键值对加入到值为List类型的Map中](Add key value pairs to a map with a value of list type)
	 * @description zh - 将键值对加入到值为List类型的Map中
	 * @description en - Add key value pairs to a map with a value of list type
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 21:05:45
	 * @param key 键
	 * @param value 值
	 * @param charset encode编码
	 */
	private void addParam(String key, String value, Charset charset) {
		if (null != key) {
			final String actualKey = URLDecoder.decode(key, charset, isFormUrlEncoded);
			this.query.put(actualKey, UrlTool.nullToEmpty(URLDecoder.decode(value, charset, isFormUrlEncoded)));
		} else if (null != value) {
			this.query.put(URLDecoder.decode(value, charset, isFormUrlEncoded), null);
		}
	}

}
