package com.axios.core.map;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.axios.core.matcher.Matcher;
import com.axios.core.tool.UrlTool;
import com.axios.core.tool.http.HttpTool;
import com.axios.core.tool.ssl.SSLTool;

/**
 * [可重复键和值的Map](Map of repeatable keys and values)
 * @description zh - 可重复键和值的Map
 * @description en - Map of repeatable keys and values
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-22 19:48:31
 */
public class TableMap <K, V> implements Map<K, V>, Iterable<Map.Entry<K, V>>, Serializable {
	private static final long serialVersionUID = 1L;

	private final List<K> keys;
	private final List<V> values;

	public TableMap(int size) {
		this.keys = new ArrayList<>(size);
		this.values = new ArrayList<>(size);
	}

	public TableMap(K[] keys, V[] values) {
		this.keys = toList(keys);
		this.values = toList(values);
	}

	@Override
	public int size() {
		return keys.size();
	}

	@Override
	public boolean isEmpty() {
		return keys == null || keys.isEmpty();
	}

	@Override
	public boolean containsKey(Object key) {
		//noinspection SuspiciousMethodCalls
		return keys.contains(key);
	}

	@Override
	public boolean containsValue(Object value) {
		//noinspection SuspiciousMethodCalls
		return values.contains(value);
	}

	@Override
	public V get(Object key) {
		//noinspection SuspiciousMethodCalls
		final int index = keys.indexOf(key);
		if (index > -1 && index < values.size()) {
			return values.get(index);
		}
		return null;
	}

	/**
	 * [根据value获得对应的key](Obtain the corresponding key according to value)
	 * @description zh - 根据value获得对应的key
	 * @description en - Obtain the corresponding key according to value
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 19:57:54
	 * @param value 值
	 * @return K
	 */
	public K getKey(V value){
		final int index = values.indexOf(value);
		return index > -1 && index < keys.size() ? keys.get(index) : null;
	}

	/**
	 * [获取指定key对应的所有值](Gets all the values corresponding to the specified key)
	 * @description zh - 获取指定key对应的所有值
	 * @description en - Gets all the values corresponding to the specified key
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:19:55
	 * @param key 键
	 * @return java.util.List<V>
	 */
	public List<V> getValues(K key) {
		return getAny(
			this.values,
			indexOfAll(this.keys, (ele) -> equal(ele, key))
		);
	}

	/**
	 * [获取指定value对应的所有key](Gets all keys corresponding to the specified value)
	 * @description zh - 获取指定value对应的所有key
	 * @description en - Gets all keys corresponding to the specified value
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:21:01
	 * @param value 值
	 * @return java.util.List<K>
	 */
	public List<K> getKeys(V value) {
		return getAny(
			this.keys,
			indexOfAll(this.values, (ele) -> equal(ele, value))
		);
	}

	@Override
	public V put(K key, V value) {
		keys.add(key);
		values.add(value);
		return null;
	}


	@Override
	public V remove(Object key) {
		//noinspection SuspiciousMethodCalls
		int index = keys.indexOf(key);
		if (index > -1) {
			keys.remove(index);
			if (index < values.size()) {
				values.remove(index);
			}
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
			this.put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public void clear() {
		keys.clear();
		values.clear();
	}

	@Override
	public Set<K> keySet() {
		return new HashSet<>(keys);
	}

	@Override
	public Collection<V> values() {
		return Collections.unmodifiableList(this.values);
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		final Set<Map.Entry<K, V>> hashSet = new LinkedHashSet<>();
		for (int i = 0; i < size(); i++) {
			hashSet.add(new Entry<>(keys.get(i), values.get(i)));
		}
		return hashSet;
	}

	@Override
	public Iterator<Map.Entry<K, V>> iterator() {
		return new Iterator<Map.Entry<K, V>>() {
			private final Iterator<K> keysIter = keys.iterator();
			private final Iterator<V> valuesIter = values.iterator();

			@Override
			public boolean hasNext() {
				return keysIter.hasNext() && valuesIter.hasNext();
			}

			@Override
			public Map.Entry<K, V> next() {
				return new Entry<>(keysIter.next(), valuesIter.next());
			}

			@Override
			public void remove() {
				keysIter.remove();
				valuesIter.remove();
			}
		};
	}

	@Override
	public String toString() {
		return "TableMap{" +
				"keys=" + keys +
				", values=" + values +
				'}';
	}

	/**
	 * [键值对](Key value pair)
	 * @description zh - 键值对
	 * @description en - Key value pair
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:22:35
	 */
	private static class Entry<K, V> implements Map.Entry<K, V> {

		private final K key;
		private final V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return value;
		}

		@Override
		public V setValue(V value) {
			throw new UnsupportedOperationException("setValue not supported.");
		}

		@Override
		public final boolean equals(Object o) {
			if (o == this)
				return true;
			if (o instanceof Map.Entry) {
				Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
				return Objects.equals(key, e.getKey()) &&
						Objects.equals(value, e.getValue());
			}
			return false;
		}

		@Override
		public int hashCode() {
			//copy from 1.8 HashMap.Node
			return Objects.hashCode(key) ^ Objects.hashCode(value);
		}
	}

	/** private ---------------------- 私有的 */

	/**
	 * [比较 Object 的大小](Compare the size of Object)
	 * @description zh - 比较 Object 的大小
	 * @description en - Compare the size of Object
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:19:29
	 * @param bigNum1 对象1
	 * @param bigNum2 对象2
	 * @return boolean
	 */
	private boolean equal(Object obj1, Object obj2) {
		if (obj1 instanceof BigDecimal && obj2 instanceof BigDecimal) {
			return equals((BigDecimal) obj1, (BigDecimal) obj2);
		}
		return Objects.equals(obj1, obj2);
	}

	/**
	 * [比较 BigDecimal 的大小](Compare the size of BigDecimal)
	 * @description zh - 比较 BigDecimal 的大小
	 * @description en - Compare the size of BigDecimal
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:18:46
	 * @param bigNum1 数字1
	 * @param bigNum2 数字2
	 * @return boolean
	 */
	private boolean equals(BigDecimal bigNum1, BigDecimal bigNum2) {
		//noinspection NumberEquality
		if (bigNum1 == bigNum2) {
			return true;
		}
		if (bigNum1 == null || bigNum2 == null) {
			return false;
		}
		return 0 == bigNum1.compareTo(bigNum2);
	}

	/**
	 * [获取匹配规则定义中匹配到元素的所有位置](Gets all positions that match elements in the matching rule definition)
	 * @description zh - 获取匹配规则定义中匹配到元素的所有位置
	 * @description en - Gets all positions that match elements in the matching rule definition
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:17:59
	 * @param collection 集合
	 * @param matcher 接口
	 * @return java.lang.Integer[]
	 */
	private <T> Integer[] indexOfAll(List<T> collection, Matcher<T> matcher) {
		final List<Integer> indexList = new ArrayList<>();
		if (null != collection) {
			int index = 0;
			for (T t : collection) {
				if (null == matcher || matcher.match(t)) {
					indexList.add(index);
				}
				index++;
			}
		}

		return indexList.stream().toArray(Integer[]::new);
	}

	/**
	 * [获取集合中指定多个下标的元素值](Gets the element value of the specified multiple subscripts in the collection)
	 * @description zh - 获取集合中指定多个下标的元素值
	 * @description en - Gets the element value of the specified multiple subscripts in the collection
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 20:17:01
	 * @param collection 集合
	 * @param indexes 下标
	 * @return java.util.List<T>
	 */
	private <T> List<T> getAny(Collection<T> collection, Integer... indexes) {
		final int size = collection.size();
		final ArrayList<T> result = new ArrayList<>();
		if (collection instanceof List) {
			final List<T> list = ((List<T>) collection);
			for (int index : indexes) {
				if (index < 0) {
					index += size;
				}
				result.add(list.get(index));
			}
		} else {
			final Object[] array = collection.toArray();
			for (int index : indexes) {
				if (index < 0) {
					index += size;
				}
				result.add((T) array[index]);
			}
		}
		return result;
	}

	/**
	 * [数组转为List](Convert array to list)
	 * @description zh - 数组转为List
	 * @description en - Convert array to list
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-22 19:55:27
	 * @param values 数组
	 * @return java.util.List<T>
	 */
	private <T> List<T> toList(T[] values) {
		if (null == values || values.length == 0) {
			return new ArrayList<>();
		}
		final List<T> arrayList = new ArrayList<>(values.length);
		Collections.addAll(arrayList, values);
		return arrayList;
	}



}
