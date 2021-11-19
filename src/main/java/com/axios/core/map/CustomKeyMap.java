package com.axios.core.map;

import java.util.Map;

/**
 * [自定义键的Map，默认HashMap实现](The map of user-defined key is implemented by HashMap by default)
 * @description zh - 自定义键的Map，默认HashMap实现
 * @description en - The map of user-defined key is implemented by HashMap by default
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-19 17:01:09
 */
public abstract class CustomKeyMap<K, V> extends MapWrapper<K, V> {

	private static final long serialVersionUID = 4043263744224569870L;

	public CustomKeyMap(Map<K, V> m) {
		super(m);
	}

	@Override
	public V get(Object key) {
		return super.get(customKey(key));
	}

	@SuppressWarnings("unchecked")
	@Override
	public V put(K key, V value) {
		return super.put((K) customKey(key), value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		m.forEach(this::put);
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(customKey(key));
	}

	@Override
	public V remove(Object key) {
		return super.remove(customKey(key));
	}

	@Override
	public boolean remove(Object key, Object value) {
		return super.remove(customKey(key), value);
	}

	@Override
	public boolean replace(K key, V oldValue, V newValue) {
		//noinspection unchecked
		return super.replace((K) customKey(key), oldValue, newValue);
	}

	@Override
	public V replace(K key, V value) {
		//noinspection unchecked
		return super.replace((K) customKey(key), value);
	}

	/**
	 * [自定义键](Custom key)
	 * @description zh - 自定义键
	 * @description en - Custom key
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-19 17:00:20
	 * @param key KEY
	 * @return java.lang.Object
	 */
	protected abstract Object customKey(Object key);
}
