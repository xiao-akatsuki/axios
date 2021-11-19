package com.axios.core.tool.http;

import java.util.Collection;

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



}
