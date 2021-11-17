package com.axios.core.assertion;

import java.util.function.Supplier;

/**
 * [HTTP断言](HTTP assertion)
 * @description zh - HTTP断言
 * @description en - HTTP assertion
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-15 20:46:38
 */
public class Assert {

	/**
	 * [断言对象是否不为 null](Asserts whether the object is not null)
	 * @description zh - 断言对象是否不为 null
	 * @description en - Asserts whether the object is not null
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 15:02:27
	 * @param object 判断的对象
	 * @param errorSupplier 错误抛出异常附带的消息生产接口
	 * @return T
	 */
	public static <T, X extends Throwable> T notNull(T object, Supplier<X> errorSupplier) throws X {
		if (null == object) {
			throw errorSupplier.get();
		}
		return object;
	}

	/**
	 * [断言对象是否不为 null](Asserts whether the object is not null)
	 * @description zh - 断言对象是否不为 null
	 * @description en - Asserts whether the object is not null
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 15:04:04
	 * @param object 对象
	 * @param errorMssage 错误信息
	 * @throws java.lang.IllegalArgumentException
	 * @return T
	 */
	public static <T> T notNull(T object, String errorMssage) throws IllegalArgumentException {
		return notNull(object, () -> new IllegalArgumentException(errorMssage));
	}

	/**
	 * [断言对象是否不为 null](Asserts whether the object is not null)
	 * @description zh - 断言对象是否不为 null
	 * @description en - Asserts whether the object is not null
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 15:04:49
	 * @param object 对象
	 * @throws java.lang.IllegalArgumentException
	 * @return T
	 */
	public static <T> T notNull(T object) throws IllegalArgumentException {
		return notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	/**
	 * [检查给定字符串是否为空白](Checks whether the given string is blank)
	 * @description zh - 检查给定字符串是否为空白
	 * @description en - Checks whether the given string is blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 15:05:06
	 * @param text 对象
	 * @throws java.lang.IllegalArgumentException
	 * @return T
	 */
	public static <T extends CharSequence> T notBlank(T text) throws IllegalArgumentException {
		return notBlank(text, "[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	/**
	 * [检查给定字符串是否为空白](Checks whether the given string is blank)
	 * @description zh - 检查给定字符串是否为空白
	 * @description en - Checks whether the given string is blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 14:50:00
	 * @param text 判断的文本
	 * @param errorMessage 错误信息
	 * @return T
	 */
	public static <T extends CharSequence> T notBlank(T text, String errorMessage) throws IllegalArgumentException {
		return notBlank(text, () -> new IllegalArgumentException(errorMessage));
	}

	/**
	 * [检查给定字符串是否为空白](Checks whether the given string is blank)
	 * @description zh - 检查给定字符串是否为空白
	 * @description en - Checks whether the given string is blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 20:49:58
	 * @param text 判断的文本
	 * @param errorMessage 错误信息
	 * @return T
	 */
	public static <T extends CharSequence, X extends Throwable> T notBlank(T text, Supplier<X> errorMessage) throws X {
		if (isBlank(text)) {
			throw errorMessage.get();
		}
		return text;
	}

	/**
	 * [字符串是否为空白](Is the string blank)
	 * @description zh - 字符串是否为空白
	 * @description en - Is the string blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 20:49:20
	 * @param text 文本
	 * @return boolean
	 */
	public static boolean isBlank(CharSequence text) {
		int length;
		if ((text == null) || ((length = text.length()) == 0)) {
			return true;
		}
		for (int i = 0; i < length; i++) {
			// As long as there is one non empty character, it is a non empty string
			if (false == isBlankChar(text.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * [是否空白符](Is it blank)
	 * @description zh - 是否空白符
	 * @description en - Is it blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 20:48:57
	 * @param text 字符
	 * @return boolean
	 */
	public static boolean isBlankChar(char text) {
		return isBlankChar((int) text);
	}

	/**
	 * [是否空白符](Is it blank)
	 * @description zh - 是否空白符
	 * @description en - Is it blank
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-15 20:48:20
	 * @param text 字符
	 * @return boolean
	 */
	public static boolean isBlankChar(int text) {
		return Character.isWhitespace(text)
				|| Character.isSpaceChar(text)
				|| text == '\ufeff'
				|| text == '\u202a'
				|| text == '\u0000';
	}


}
