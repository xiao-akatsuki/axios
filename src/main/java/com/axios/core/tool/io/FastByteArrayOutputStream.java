package com.axios.core.tool.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import com.axios.core.tool.UrlTool;
import com.axios.exception.IORuntimeException;

/**
 * [基于快速缓冲FastByteBuffer的OutputStream，随着数据的增长自动扩充缓冲区](The OutputStream based on fast buffer fastbytebuffer automatically expands the buffer as the data grows)
 * @description zh - 基于快速缓冲FastByteBuffer的OutputStream，随着数据的增长自动扩充缓冲区
 * @description en - The OutputStream based on fast buffer fastbytebuffer automatically expands the buffer as the data grows
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-28 14:41:21
 */
public class FastByteArrayOutputStream extends OutputStream {

	private final FastByteBuffer buffer;

	public FastByteArrayOutputStream() {
		this(1024);
	}

	public FastByteArrayOutputStream(int size) {
		buffer = new FastByteBuffer(size);
	}

	@Override
	public void write(byte[] b, int off, int len) {
		buffer.append(b, off, len);
	}

	@Override
	public void write(int b) {
		buffer.append((byte) b);
	}

	public int size() {
		return buffer.size();
	}

	@Override
	public void close() {
		// nop
	}

	public void reset() {
		buffer.reset();
	}

	public void writeTo(OutputStream out) throws IORuntimeException {
		final int index = buffer.index();
		if(index < 0){
			return;
		}
		byte[] buf;
		try {
			for (int i = 0; i < index; i++) {
				buf = buffer.array(i);
				out.write(buf);
			}
			out.write(buffer.array(index), 0, buffer.offset());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}


	public byte[] toByteArray() {
		return buffer.toArray();
	}

	@Override
	public String toString() {
		return toString(Charset.defaultCharset());
	}

	public String toString(String charsetName) {
		return toString(charset(charsetName));
	}

	public String toString(Charset charset) {
		return new String(toByteArray(),
				defaultIfNull(charset, Charset.defaultCharset()));
	}

	private <T> T defaultIfNull(T obj, T defaultObj) {
		return null == obj || obj.equals(null) ? defaultObj : obj ;
	}

	private Charset charset(String charsetName) throws UnsupportedCharsetException {
		return UrlTool.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
	}

}
