package com.axios.core.map;

import java.util.HashMap;
import java.util.Map;

public class CaseInsensitiveMap<K, V> extends CustomKeyMap<K, V> {
	private static final long serialVersionUID = 4043263744224569870L;

	/**
	 * [构造](structure)
	 * @description zh - 构造
	 * @description en - structure
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 17:04:04
	 */
	public CaseInsensitiveMap() {
		this(DEFAULT_INITIAL_CAPACITY);
	}

	/**
	 * [构造](structure)
	 * @description zh - 构造
	 * @description en - structure
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 17:03:33
	 * @param initialCapacity 初始大小
	 */
	public CaseInsensitiveMap(int initialCapacity) {
		this(initialCapacity, DEFAULT_LOAD_FACTOR);
	}

	/**
	 * [构造](structure)
	 * @description zh - 构造
	 * @description en - structure
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 17:04:14
	 * @param map 集合
	 */
	public CaseInsensitiveMap(Map<? extends K, ? extends V> map) {
		this(DEFAULT_LOAD_FACTOR, map);
	}

	/**
	 * [构造](structure)
	 * @description zh - 构造
	 * @description en - structure
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 17:04:51
	 * @param loadFactor 加载因子
	 * @param map 集合
	 */
	public CaseInsensitiveMap(float loadFactor, Map<? extends K, ? extends V> map) {
		this(map.size(), loadFactor);
		this.putAll(map);
	}

	/**
	 * [构造](structure)
	 * @description zh - 构造
	 * @description en - structure
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 17:05:20
	 * @param initialCapacity 初始大小
	 * @param loadFactor 加载因子
	 */
	public CaseInsensitiveMap(int initialCapacity, float loadFactor) {
		super(new HashMap<>(initialCapacity, loadFactor));
	}

	@Override
	protected Object customKey(Object key) {
		if (key instanceof CharSequence) {
			key = key.toString().toLowerCase();
		}
		return key;
	}

}
