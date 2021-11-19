package com.axios.core.tool.text;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Function;

import com.axios.core.tool.UrlTool;
import com.axios.core.tool.http.HttpTool;
import com.axios.exception.IORuntimeException;

public class StrJoiner implements Appendable, Serializable {
	private static final long serialVersionUID = 1L;

	private Appendable appendable;

	private CharSequence delimiter;

	private CharSequence prefix;

	private CharSequence suffix;

	private boolean wrapElement;

	private NullMode nullMode = NullMode.NULL_STRING;

	private String emptyResult = "";

	private boolean hasContent;

	public StrJoiner(Appendable appendable, CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		if (null != appendable) {
			this.appendable = appendable;
			checkHasContent(appendable);
		}

		this.delimiter = delimiter;
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public StrJoiner(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		this(null, delimiter, prefix, suffix);
	}

	public StrJoiner(Appendable appendable, CharSequence delimiter) {
		this(appendable, delimiter, null, null);
	}

	public StrJoiner(CharSequence delimiter) {
		this(null, delimiter);
	}

	/**
	 * [根据已有StrJoiner配置新建一个新的StrJoiner](Create a new strjoiner according to the existing strjoiner configuration)
	 * @description zh - 根据已有StrJoiner配置新建一个新的StrJoiner
	 * @description en - Create a new strjoiner according to the existing strjoiner configuration
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:45:06
	 * @param delimiter 分隔符
	 * @param prefix 前缀
	 * @param suffix 后缀
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public static StrJoiner of(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		return new StrJoiner(delimiter, prefix, suffix);
	}

	/**
	 * [根据已有StrJoiner配置新建一个新的StrJoiner](Create a new strjoiner according to the existing strjoiner configuration)
	 * @description zh - 根据已有StrJoiner配置新建一个新的StrJoiner
	 * @description en - Create a new strjoiner according to the existing strjoiner configuration
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:44:28
	 * @param delimiter 分隔符
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public static StrJoiner of(CharSequence delimiter) {
		return new StrJoiner(delimiter);
	}

	/**
	 * [根据已有StrJoiner配置新建一个新的StrJoiner](Create a new strjoiner according to the existing strjoiner configuration)
	 * @description zh - 根据已有StrJoiner配置新建一个新的StrJoiner
	 * @description en - Create a new strjoiner according to the existing strjoiner configuration
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:43:31
	 * @param joiner 已有StrJoiner
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public static StrJoiner of(StrJoiner joiner) {
		StrJoiner joinerNew = new StrJoiner(joiner.delimiter, joiner.prefix, joiner.suffix);
		joinerNew.wrapElement = joiner.wrapElement;
		joinerNew.nullMode = joiner.nullMode;
		joinerNew.emptyResult = joiner.emptyResult;

		return joinerNew;
	}

	/**
	 * [设置分隔符](Set separator)
	 * @description zh - 设置分隔符
	 * @description en - Set separator
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:46:50
	 * @param delimiter 分隔符
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public StrJoiner setDelimiter(CharSequence delimiter) {
		this.delimiter = delimiter;
		return this;
	}

	/**
	 * [设置前缀](Set prefix)
	 * @description zh - 设置前缀
	 * @description en - Set prefix
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:47:35
	 * @param prefix 前缀
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public StrJoiner setPrefix(CharSequence prefix) {
		this.prefix = prefix;
		return this;
	}

	/**
	 * [设置后缀](Set suffix)
	 * @description zh - 设置后缀
	 * @description en - Set suffix
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:48:24
	 * @param suffix 后缀
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public StrJoiner setSuffix(CharSequence suffix) {
		this.suffix = suffix;
		return this;
	}

	/**
	 * [设置前缀和后缀是否包装每个元素](Sets whether prefixes and suffixes wrap each element)
	 * @description zh - 设置前缀和后缀是否包装每个元素
	 * @description en - Sets whether prefixes and suffixes wrap each element
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:49:06
	 * @param wrapElement true表示包装每个元素，false包装整个字符串
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public StrJoiner setWrapElement(boolean wrapElement) {
		this.wrapElement = wrapElement;
		return this;
	}

	/**
	 * [设置 null 元素处理逻辑](Set null element processing logic)
	 * @description zh - 设置 null 元素处理逻辑
	 * @description en - Set null element processing logic
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:49:55
	 * @param nullMode null 元素处理逻辑
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public StrJoiner setNullMode(NullMode nullMode) {
		this.nullMode = nullMode;
		return this;
	}

	/**
	 * [设置当没有任何元素加入时，默认返回的字符串](Sets the string returned by default when no element is added)
	 * @description zh - 设置当没有任何元素加入时，默认返回的字符串
	 * @description en - Sets the string returned by default when no element is added
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:50:43
	 * @param emptyResult 默认字符串
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public StrJoiner setEmptyResult(String emptyResult) {
		this.emptyResult = emptyResult;
		return this;
	}

	/**
	 * [追加对象到拼接器中](Append objects to splicer)
	 * @description zh - 追加对象到拼接器中
	 * @description en - Append objects to splicer
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:56:02
	 * @param obj 对象
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public <T> StrJoiner append(Object obj) {
		if (null == obj) {
			append((CharSequence) null);
		} else if (HttpTool.isArray(obj)) {
			append(new ArrayIter<>(obj));
		} else if (obj instanceof Iterator) {
			append((Iterator<?>) obj);
		} else if (obj instanceof Iterable) {
			append(((Iterable<?>) obj).iterator());
		} else {
			append(String.valueOf(obj));
		}
		return this;
	}

	/**
	 * [追加对象到拼接器中](Append objects to splicer)
	 * @description zh - 追加对象到拼接器中
	 * @description en - Append objects to splicer
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:56:40
	 * @param array 数组
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public <T> StrJoiner append(T[] array) {
		if (null == array) {
			return this;
		}
		return append(new ArrayIter<>(array));
	}

	/**
	 * [追加对象到拼接器中](Append objects to splicer)
	 * @description zh - 追加对象到拼接器中
	 * @description en - Append objects to splicer
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:57:03
	 * @param iterator 元素列表
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public <T> StrJoiner append(Iterator<T> iterator) {
		if (null != iterator) {
			while (iterator.hasNext()) {
				append(iterator.next());
			}
		}
		return this;
	}

	/**
	 * [追加数组中的元素到拼接器中](Append the elements in the array to the splicer)
	 * @description zh - 追加数组中的元素到拼接器中
	 * @description en - Append the elements in the array to the splicer
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:57:59
	 * @param array 元素列表
	 * @param toStrFunc 元素对象转换为字符串的函数
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public <T> StrJoiner append(T[] array, Function<T, ? extends CharSequence> toStrFunc) {
		return append((Iterator<T>) new ArrayIter<>(array), toStrFunc);
	}

	/**
	 * [追加Iterator中的元素到拼接器中](Append elements from iterator to splicer)
	 * @description zh - 追加Iterator中的元素到拼接器中
	 * @description en - Append elements from iterator to splicer
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:58:52
	 * @param iterable 元素列表
	 * @param toStrFunc 元素对象转换为字符串的函数
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public <T> StrJoiner append(Iterable<T> iterable, Function<T, ? extends CharSequence> toStrFunc) {
		return append(HttpTool.getIter(iterable), toStrFunc);
	}

	/**
	 * [追加Iterator中的元素到拼接器中](Append elements from iterator to splicer)
	 * @description zh - 追加Iterator中的元素到拼接器中
	 * @description en - Append elements from iterator to splicer
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:59:56
	 * @param iterable 元素列表
	 * @param toStrFunc 元素对象转换为字符串的函数
	 * @return com.axios.core.tool.text.StrJoiner
	 */
	public <T> StrJoiner append(Iterator<T> iterator, Function<T, ? extends CharSequence> toStrFunc) {
		if (null != iterator) {
			while (iterator.hasNext()) {
				append(toStrFunc.apply(iterator.next()));
			}
		}
		return this;
	}

	@Override
	public StrJoiner append(CharSequence csq) {
		if (null == csq) {
			switch (this.nullMode) {
				case IGNORE:
					return this;
				case TO_EMPTY:
					csq = "";
					break;
				case NULL_STRING:
					csq = "null";
			}
		}
		try {
			final Appendable appendable = prepare();
			if (wrapElement && UrlTool.isNotEmpty(this.prefix)) {
				appendable.append(prefix);
			}
			appendable.append(csq);
			if (wrapElement && UrlTool.isNotEmpty(this.suffix)) {
				appendable.append(suffix);
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return this;
	}


	@Override
	public StrJoiner append(CharSequence csq, int startInclude, int endExclude) {
		return append(csq.subSequence(startInclude, endExclude));
	}

	@Override
	public StrJoiner append(char c) {
		return append(String.valueOf(c));
	}

	@Override
	public String toString() {
		if (null == this.appendable) {
			return emptyResult;
		}
		if (false == wrapElement && UrlTool.isNotEmpty(this.suffix)) {
			try {
				this.appendable.append(this.suffix);
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		}
		return this.appendable.toString();
	}

	/**
	 * [准备连接器](Prepare the connector)
	 * @description zh - 准备连接器
	 * @description en - Prepare the connector
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 21:02:19
	 * @throws java.io.IOException
	 * @return java.lang.Appendable
	 */
	private Appendable prepare() throws IOException {
		if (hasContent) {
			this.appendable.append(delimiter);
		} else {
			if (null == this.appendable) {
				this.appendable = new StringBuilder();
			}
			if (false == wrapElement && UrlTool.isNotEmpty(this.prefix)) {
				this.appendable.append(this.prefix);
			}
			this.hasContent = true;
		}
		return this.appendable;
	}

	/**
	 * [检查用户传入的 Appendable 是否已经存在内容](Check whether the appendable passed in by the user already exists)
	 * @description zh - 检查用户传入的 Appendable 是否已经存在内容
	 * @description en - Check whether the appendable passed in by the user already exists
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 21:03:14
	 * @param appendable Appendable
	 */
	private void checkHasContent(Appendable appendable) {
		if (appendable instanceof CharSequence) {
			final CharSequence charSequence = (CharSequence) appendable;
			if (charSequence.length() > 0 && UrlTool.endWith(charSequence, delimiter)) {
				this.hasContent = true;
			}
		} else {
			final String initStr = appendable.toString();
			if (UrlTool.isNotEmpty(initStr) && false == UrlTool.endWith(initStr, delimiter)) {
				this.hasContent = true;
			}
		}
	}

	/**
	 * [null 处理的模式](Null processing mode)
	 * @description zh - null 处理的模式
	 * @description en - Null processing mode
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 20:42:30
	 */
	public enum NullMode {
		/**
		 * 忽略 null
		 */
		IGNORE,
		/**
		 * null 转为""
		 */
		TO_EMPTY,
		/**
		 * null 转为null字符串
		 */
		NULL_STRING
	}
}
