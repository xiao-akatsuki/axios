package com.axios.core.resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.axios.core.tool.http.HttpTool;
import com.axios.exception.IORuntimeException;

/**
 * [多资源组合资源](Multi resource combination resource)
 * @description zh - 多资源组合资源
 * @description en - Multi resource combination resource
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-28 14:44:13
 */
public class MultiResource implements Resource, Iterable<Resource>, Iterator<Resource>, Serializable  {
	private static final long serialVersionUID = 1L;

	private final List<Resource> resources;
	private int cursor;

	public MultiResource(Resource... resources) {
		this((ArrayList<Resource>)newArrayList(false,resources));
	}

	public MultiResource(Collection<Resource> resources) {
		if(resources instanceof List) {
			this.resources = (List<Resource>)resources;
		}else {
			this.resources = newArrayList(resources);
		}
	}

	@Override
	public String getName() {
		return resources.get(cursor).getName();
	}

	@Override
	public URL getUrl() {
		return resources.get(cursor).getUrl();
	}

	@Override
	public InputStream getStream() {
		return resources.get(cursor).getStream();
	}

	@Override
	public BufferedReader getReader(Charset charset) {
		return resources.get(cursor).getReader(charset);
	}

	@Override
	public String readStr(Charset charset) throws IORuntimeException {
		return resources.get(cursor).readStr(charset);
	}

	@Override
	public String readUtf8Str() throws IORuntimeException {
		return resources.get(cursor).readUtf8Str();
	}

	@Override
	public byte[] readBytes() throws IORuntimeException {
		return resources.get(cursor).readBytes();
	}

	@Override
	public Iterator<Resource> iterator() {
		return resources.iterator();
	}

	@Override
	public boolean hasNext() {
		return cursor < resources.size();
	}

	@Override
	public synchronized Resource next() {
		if (cursor >= resources.size()) {
			throw new ConcurrentModificationException();
		}
		this.cursor++;
		return this;
	}

	@Override
	public void remove() {
		this.resources.remove(this.cursor);
	}

	public synchronized void reset() {
		this.cursor = 0;
	}

	public MultiResource add(Resource resource) {
		this.resources.add(resource);
		return this;
	}

	private static <T> List<T> newArrayList(boolean isLinked, T[] values) {
		if (HttpTool.isEmpty(values)) {
			return new ArrayList<>();
		}
		final List<T> arrayList = isLinked ? new LinkedList<>() : new ArrayList<>(values.length);
		Collections.addAll(arrayList, values);
		return arrayList;
	}

	private static <T> List<T> newArrayList(Collection<T> collection) {
		if (null == collection) {
			return new ArrayList<>();
		}
		return new ArrayList<>(collection);
	}
}
