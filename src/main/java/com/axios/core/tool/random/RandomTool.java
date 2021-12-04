package com.axios.core.tool.random;

import java.util.concurrent.ThreadLocalRandom;

import com.axios.core.tool.UrlTool;

/**
 * [随机工具](Random tools)
 * @description zh - 随机工具
 * @description en - Random tools
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-04 10:41:36
 */
public class RandomTool {

	/**
	 * 用于随机选的数字
	 */
	public static final String BASE_NUMBER = "0123456789";
	/**
	 * 用于随机选的字符
	 */
	public static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyz";
	/**
	 * 用于随机选的字符和数字
	 */
	public static final String BASE_CHAR_NUMBER = BASE_CHAR + BASE_NUMBER;

	/**
	 * [获得一个随机的字符串](Get a random string)
	 * @description zh - 获得一个随机的字符串
	 * @description en - Get a random string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-04 10:43:14
	 * @param length 长度
	 * @return java.lang.String
	 */
	public static String randomString(int length) {
		return randomString(BASE_CHAR_NUMBER, length);
	}

	/**
	 * [获得一个随机的字符串](Get a random string)
	 * @description zh - 获得一个随机的字符串
	 * @description en - Get a random string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-04 10:42:55
	 * @param baseString 基础字符串
	 * @param length 长度
	 * @return java.lang.String
	 */
	public static String randomString(String baseString, int length) {
		if (UrlTool.isEmpty(baseString)) {
			return "";
		}
		final StringBuilder sb = new StringBuilder(length);

		if (length < 1) {
			length = 1;
		}
		int baseLength = baseString.length();
		for (int i = 0; i < length; i++) {
			int number = randomInt(baseLength);
			sb.append(baseString.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * [获得指定范围内的随机数](Gets a random number within the specified range)
	 * @description zh - 获得指定范围内的随机数
	 * @description en - Gets a random number within the specified range
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-04 10:44:10
	 * @param limit 限制随机数的范围
	 * @return int
	 */
	public static int randomInt(int limit) {
		return ThreadLocalRandom.current().nextInt(limit);
	}

}
