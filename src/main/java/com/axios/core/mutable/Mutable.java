package com.axios.core.mutable;

/**
 * [提供可变值类型接口](Provide variable value type interface)
 * @description zh - 提供可变值类型接口
 * @description en - Provide variable value type interface
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-05 09:39:46
 */
public interface Mutable<T> {

	/**
	 * [获得原始值](Get original value)
	 * @description zh - 获得原始值
	 * @description en - Get original value
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-05 09:38:48
	 * @return T
	 */
	T get();

	/**
	 * [设置值](set value)
	 * @description zh - 设置值
	 * @description en - set value
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-05 09:39:16
	 * @param value 值
	 */
	void set(T value);

}
