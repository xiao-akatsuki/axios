package com.axios.core.http.lang;

/**
 * [责任链接口](Responsibility chain interface)
 * @description zh - 责任链接口
 * @description en - Responsibility chain interface
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-24 20:19:07
 */
public interface Chain<E, T> extends Iterable<E>{
	/**
	 * 加入责任链
	 * @param element 责任链新的环节元素
	 * @return this
	 */
	T addChain(E element);

}
