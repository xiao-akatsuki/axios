package com.axios.core.tool.http;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import com.axios.core.tool.UrlTool;
import com.axios.core.tool.regular.RegularTool;
import com.axios.core.tool.text.StrJoiner;
import com.axios.exception.IORuntimeException;

/**
 * [HTTP 工具类](HTTP Tool)
 * @description zh - HTTP 工具类
 * @description en - HTTP Tool
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-19 16:49:54
 */
public class HttpTool {

	/**
	 * 正则：Content-Type中的编码信息
	 */
	public static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

	/**
	 * 正则：匹配meta标签的编码信息
	 */
	public static final Pattern META_CHARSET_PATTERN = Pattern.compile("<meta[^>]*?charset\\s*=\\s*['\"]?([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

	/**
	 * [数组是否为空](is the array empty)
	 * @description zh - 数组是否为空
	 * @description en - is the array empty
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 20:16:39
	 * @param array 数组
	 * @return boolean
	 */
	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * [集合是否为空](Is the collection empty)
	 * @description zh - 集合是否为空
	 * @description en - Is the collection empty
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 16:50:44
	 * @param collection 集合
	 * @return boolean
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	/**
	 * [Map是否为空](Map is empty)
	 * @description zh - Map是否为空
	 * @description en - Map is empty
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:21:01
	 * @param map 集合
	 * @return boolean
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return null == map || map.isEmpty();
	}

	/**
	 * [将编码的byteBuffer数据转换为字符串](Converts encoded ByteBuffer data to a string)
	 * @description zh - 将编码的byteBuffer数据转换为字符串
	 * @description en - Converts encoded ByteBuffer data to a string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:34:54
	 * @param data 数据
	 * @param charset 字符集
	 * @return java.lang.String
	 */
	public static String str(byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		if (null == charset) {
			return new String(data);
		}
		return new String(data, charset);
	}

	/**
	 * [分隔符将数组转换为字符串](The delimiter converts the array to a string)
	 * @description zh - 分隔符将数组转换为字符串
	 * @description en - The delimiter converts the array to a string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-26 20:20:09
	 * @param array 数组
	 * @param delimiter 分隔符
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return java.lang.String
	 */
	public static <T> String join(T[] array, CharSequence delimiter, String prefix, String suffix) {
		if (null == array) {
			return null;
		}
		return StrJoiner.of(delimiter, prefix, suffix)
				.setWrapElement(true)
				.append(array)
				.toString();
	}

	/**
	 * [以 conjunction 为分隔符将集合转换为字符串](Converts a collection to a string with a conjunction as a delimiter)
	 * @description zh - 以 conjunction 为分隔符将集合转换为字符串
	 * @description en - Converts a collection to a string with a conjunction as a delimiter
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 21:11:37
	 * @param iterable Iterable
	 * @param conjunction 分隔符
	 * @return java.lang.String
	 */
	public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
		if (null == iterable) {
			return null;
		}
		return StrJoiner.of(conjunction).append(iterable.iterator()).toString();
	}

	/**
	 * [获取 Iterator](get Iterator)
	 * @description zh - 获取 Iterator
	 * @description en - get Iterator
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 21:04:20
	 * @param iterable 集合
	 * @return java.util.Iterator<T>
	 */
	public static <T> Iterator<T> getIter(Iterable<T> iterable) {
		return null == iterable ? null : iterable.iterator();
	}

	/**
	 * [对象是否为数组对象](Is the object an array object)
	 * @description zh - 对象是否为数组对象
	 * @description en - Is the object an array object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:54:05
	 * @param obj 对象
	 * @return boolean
	 */
	public static boolean isArray(Object obj) {
		return null != obj && obj.getClass().isArray();
	}

    public static URL url(String url) {
        try {
			return new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
    }

	public static Class<?> getComponentType(Object array) {
		return null == array ? null : array.getClass().getComponentType();
	}

	public static String getMimeType(String filePath, String defaultValue) {
		return defaultIfNull(getMimeType(filePath), defaultValue);
	}

	public static <T> T defaultIfNull(final T object, final T defaultValue) {
		return isNull(object) ? defaultValue : object;
	}

	public static boolean isNull(Object obj) {
		//noinspection ConstantConditions
		return null == obj || obj.equals(null);
	}

	/**
	 * [根据文件扩展名获得MimeType](Get mimeType from file extension)
	 * @description zh - 根据文件扩展名获得MimeType
	 * @description en - Get mimeType from file extension
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-04 10:54:22
	 * @param filePath 文件路径或文件名
	 * @return java.lang.String
	 */
	public static String getMimeType(String filePath) {
		String contentType = URLConnection.getFileNameMap().getContentTypeFor(filePath);
		if (null == contentType) {
			// 补充一些常用的mimeType
			if (filePath.endsWith(".css")) {
				contentType = "text/css";
			} else if (filePath.endsWith(".js")) {
				contentType = "application/x-javascript";
			}
		}

		// 补充
		if (null == contentType) {
			contentType = getMimeType(Paths.get(filePath));
		}

		return contentType;
	}

	/**
	 * [获得文件的MimeType](Gets the mimeType of the file)
	 * @description zh - 获得文件的MimeType
	 * @description en - Gets the mimeType of the file
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-04 10:53:52
	 * @param file 文件
	 * @return java.lang.String
	 */
	public static String getMimeType(Path file) {
		try {
			return Files.probeContentType(file);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * [从流中读取内容](Read content from stream)
	 * @description zh - 从流中读取内容
	 * @description en - Read content from stream
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-05 09:35:10
	 * @param contentBytes 内容byte数组
	 * @param charset 字符集
	 * @param isGetCharsetFromContent 是否从返回内容中获得编码信息
	 * @return java.lang.String
	 */
	public static String getString(byte[] contentBytes, Charset charset, boolean isGetCharsetFromContent) {
		if (null == contentBytes) {
			return null;
		}

		if (null == charset) {
			charset = StandardCharsets.UTF_8;
		}
		String content = new String(contentBytes, charset);
		if (isGetCharsetFromContent) {
			final String charsetInContentStr = RegularTool.get(META_CHARSET_PATTERN, content, 1);
			if (UrlTool.isNotBlank(charsetInContentStr)) {
				Charset charsetInContent = null;
				try {
					charsetInContent = Charset.forName(charsetInContentStr);
				} catch (Exception e) {
					if (UrlTool.containsIgnoreCase(charsetInContentStr, "utf-8") || UrlTool.containsIgnoreCase(charsetInContentStr, "utf8")) {
						charsetInContent = StandardCharsets.UTF_8;
					} else if (UrlTool.containsIgnoreCase(charsetInContentStr, "gbk")) {
						charsetInContent = Charset.forName("GBK");
					}
				}
				if (null != charsetInContent && false == charset.equals(charsetInContent)) {
					content = new String(contentBytes, charsetInContent);
				}
			}
		}
		return content;
	}

}
