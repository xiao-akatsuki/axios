package com.axios.core.tool.regular;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.axios.core.mutable.MutableObj;

/**
 * [正则工具类](regular tool)
 * @description zh - 正则工具类
 * @description en - regular tool
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-05 09:46:36
 */
public class RegularTool {

	public static String get(Pattern pattern, CharSequence content, int groupIndex) {
		if (null == content || null == pattern) {
			return null;
		}

		final MutableObj<String> result = new MutableObj<>();
		get(pattern, content, matcher -> result.set(matcher.group(groupIndex)));
		return result.get();
	}

	public static void get(Pattern pattern, CharSequence content, Consumer<Matcher> consumer) {
		if (null == content || null == pattern || null == consumer) {
			return;
		}
		final Matcher m = pattern.matcher(content);
		if (m.find()) {
			consumer.accept(m);
		}
	}

}
