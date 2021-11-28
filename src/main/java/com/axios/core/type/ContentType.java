package com.axios.core.type;

import java.nio.charset.Charset;

import com.axios.core.tool.UrlTool;

/**
 * [常用Content-Type类型枚举](Enumeration of common content type types)
 * @description zh - 常用Content-Type类型枚举
 * @description en - Enumeration of common content type types
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-28 20:32:33
 */
public enum ContentType {

	/**
	 * 标准表单编码，当action为get时候，浏览器用x-www-form-urlencoded的编码方式把form数据转换成一个字串（name1=value1&amp;name2=value2…）
	 */
	FORM_URLENCODED("application/x-www-form-urlencoded"),
	/**
	 * 文件上传编码，浏览器会把整个表单以控件为单位分割，并为每个部分加上Content-Disposition，并加上分割符(boundary)
	 */
	MULTIPART("multipart/form-data"),
	/**
	 * Rest请求JSON编码
	 */
	JSON("application/json"),
	/**
	 * Rest请求XML编码
	 */
	XML("application/xml"),
	/**
	 * text/plain编码
	 */
	TEXT_PLAIN("text/plain"),
	/**
	 * Rest请求text/xml编码
	 */
	TEXT_XML("text/xml"),
	/**
	 * text/html编码
	 */
	TEXT_HTML("text/html");

	private final String value;

	ContentType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return getValue();
	}

	/**
	 * [输出Content-Type字符串](Output content type string)
	 * @description zh - 输出Content-Type字符串
	 * @description en - Output content type string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-28 20:30:43
	 * @param charset 字符编码
	 * @return java.lang.String
	 */
	public String toString(Charset charset) {
		return build(this.value, charset);
	}

	/**
	 * [是否为默认Content-Type](Is it the default content type)
	 * @description zh - 是否为默认Content-Type
	 * @description en - Is it the default content type
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-28 20:30:03
	 * @param contentType 类型
	 * @return boolean
	 */
	public static boolean isDefault(String contentType) {
		return null == contentType || isFormUrlEncode(contentType);
	}

	/**
	 * [是否为application/x-www-form-urlencoded](is it application/x-www-form-urlencoded)
	 * @description zh - 是否为application/x-www-form-urlencoded
	 * @description en - is it application/x-www-form-urlencoded
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-28 20:28:42
	 * @param contentType 类型
	 * @return boolean
	 */
	public static boolean isFormUrlEncode(String contentType) {
		return startWithIgnoreCase(contentType, FORM_URLENCODED.toString(),true,false);
	}

	/**
	 * [从请求参数的body中判断请求的Content-Type类型](Judge the content type of the request from the body of the request parameter)
	 * @description zh - 从请求参数的body中判断请求的Content-Type类型
	 * @description en - Judge the content type of the request from the body of the request parameter
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-28 20:24:02
	 * @param body 请求参数体
	 * @return com.axios.core.type.ContentType
	 */
	public static ContentType get(String body) {
		ContentType contentType = null;
		if (UrlTool.isNotBlank(body)) {
			char firstChar = body.charAt(0);
			switch (firstChar) {
				case '{':
				case '[':
					contentType = JSON;
					break;
				case '<':
					contentType = XML;
					break;

				default:
					break;
			}
		}
		return contentType;
	}

	/**
	 * [输出Content-Type字符串](Output content type string)
	 * @description zh - 输出Content-Type字符串
	 * @description en - Output content type string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-28 20:22:55
	 * @param contentType 类型
	 * @param charset 枚举类型
	 * @return java.lang.String
	 */
	public static String build(String contentType, Charset charset) {
		return contentType + ";charset=" + charset.name();
	}

	/**
	 * [输出Content-Type字符串](Output content type string)
	 * @description zh - 输出Content-Type字符串
	 * @description en - Output content type string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-28 20:21:14
	 * @param contentType Content-Type 枚举类型
	 * @param charset 枚举类型
	 * @return java.lang.String
	 */
	public static String build(ContentType contentType, Charset charset) {
		return build(contentType.getValue(), charset);
	}

	private static boolean startWithIgnoreCase(CharSequence str, CharSequence prefix, boolean ignoreCase, boolean ignoreEquals) {
		if (null == str || null == prefix) {
			if (false == ignoreEquals) {
				return false;
			}
			return null == str && null == prefix;
		}

		boolean isStartWith;
		if (ignoreCase) {
			isStartWith = str.toString().toLowerCase().startsWith(prefix.toString().toLowerCase());
		} else {
			isStartWith = str.toString().startsWith(prefix.toString());
		}

		if (isStartWith) {
			return (false == ignoreEquals) || (false == equals(str, prefix, ignoreCase));
		}
		return false;
	}

	private static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
		if (null == str1) {
			return str2 == null;
		}
		if (null == str2) {
			return false;
		}

		if (ignoreCase) {
			return str1.toString().equalsIgnoreCase(str2.toString());
		} else {
			return str1.toString().contentEquals(str2);
		}
	}

}
