package com.axios.core.tool.mutable;

import java.io.Serializable;

/**
 * [可变 Object](Variable object)
 * @description zh - 可变 Object
 * @description en - Variable object
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-17 14:32:34
 */
public class MutableObj<T> implements Mutable<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private T value;

	public MutableObj() {
	}

	public MutableObj(final T value) {
		this.value = value;
	}

	@Override
	public T get() {
		return this.value;
	}

	@Override
	public void set(final T value) {
		this.value = value;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (this.getClass() == obj.getClass()) {
			final MutableObj<?> that = (MutableObj<?>) obj;
			return this.value.equals(that.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return value == null ? 0 : value.hashCode();
	}

	@Override
	public String toString() {
		return value == null ? "null" : value.toString();
	}
}
