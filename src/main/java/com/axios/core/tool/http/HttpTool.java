package com.axios.core.tool.http;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.axios.core.tool.text.StrJoiner;

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

}