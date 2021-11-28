package com.axios.core.resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.charset.Charset;

import com.axios.exception.IORuntimeException;

/**
 * [基于byte[]的资源获取器](Resource acquisition based on byte [])
 * @description zh - 基于byte[]的资源获取器
 * @description en - Resource acquisition based on byte []
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-28 15:10:10
 */
public class BytesResource implements Resource, Serializable {

	private static final long serialVersionUID = 1L;

	private final byte[] bytes;
	private final String name;

	public BytesResource(byte[] bytes) {
		this(bytes, null);
	}

	public BytesResource(byte[] bytes, String name) {
		this.bytes = bytes;
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public URL getUrl() {
		return null;
	}

	@Override
	public InputStream getStream() {
		return new ByteArrayInputStream(this.bytes);
	}

	@Override
	public String readStr(Charset charset) throws IORuntimeException {
		return toString(this.bytes, charset);
	}

	private String toString(byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		if (null == charset) {
			return new String(data);
		}
		return new String(data, charset);
	}

	@Override
	public byte[] readBytes() throws IORuntimeException {
		return this.bytes;
	}

}
