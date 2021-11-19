package com.axios.core.tool.text;

import java.util.Iterator;

/**
 * [提供合成接口](Provide synthetic interface)
 * @description zh - 提供合成接口
 * @description en - Provide synthetic interface
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-19 21:14:23
 */
public interface IterableIter<T> extends Iterable<T>, Iterator<T>{

	@Override
	default Iterator<T> iterator() {
		return this;
	}

}
