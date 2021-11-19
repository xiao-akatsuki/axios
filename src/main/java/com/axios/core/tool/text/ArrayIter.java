package com.axios.core.tool.text;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.NoSuchElementException;

/**
 * [数组Iterator对象](Array iterator object)
 * @description zh - 数组Iterator对象
 * @description en - Array iterator object
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-19 21:16:22
 */
public class ArrayIter<E> implements IterableIter<E>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 数组
	 */
	private final Object array;
	/**
	 * 起始位置
	 */
	private int startIndex;
	/**
	 * 结束位置
	 */
	private int endIndex;
	/**
	 * 当前位置
	 */
	private int index;

	public ArrayIter(E[] array) {
		this((Object) array);
	}

	public ArrayIter(Object array) {
		this(array, 0);
	}

	public ArrayIter(Object array, int startIndex) {
		this(array, startIndex, -1);
	}

	public ArrayIter(final Object array, final int startIndex, final int endIndex) {
		this.endIndex = Array.getLength(array);
		if (endIndex > 0 && endIndex < this.endIndex) {
			this.endIndex = endIndex;
		}

		if (startIndex >= 0 && startIndex < this.endIndex) {
			this.startIndex = startIndex;
		}
		this.array = array;
	}

	@Override
	public boolean hasNext() {
		return (index < endIndex);
	}

	@Override
	@SuppressWarnings("unchecked")
	public E next() {
		if (hasNext() == false) {
			throw new NoSuchElementException();
		}
		return (E) Array.get(array, index++);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("remove() method is not supported");
	}

	/**
	 * [获得原始数组对象](Get original array object)
	 * @description zh - 获得原始数组对象
	 * @description en - Get original array object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 21:17:36
	 * @return java.lang.Object
	 */
	public Object getArray() {
		return array;
	}

	/**
	 * [重置数组位置](Reset array position)
	 * @description zh - 重置数组位置
	 * @description en - Reset array position
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 21:18:11
	 */
	public void reset() {
		this.index = this.startIndex;
	}
}
