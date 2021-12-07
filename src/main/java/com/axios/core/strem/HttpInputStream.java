package com.axios.core.strem;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import com.axios.exception.HttpException;
import com.axios.response.HttpResponse;
import com.axios.status.Status;

/**
 * [HTTP输入流](HTTP input strem)
 * @description zh - HTTP输入流
 * @description en - HTTP input strem
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-07 20:35:38
 */
public class HttpInputStream extends InputStream {

	/** 原始流 */
	private InputStream in;

	public HttpInputStream(HttpResponse response) {
		init(response);
	}

	@Override
	public int read() throws IOException {
		return this.in.read();
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return this.in.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return this.in.skip(n);
	}

	@Override
	public int available() throws IOException {
		return this.in.available();
	}

	@Override
	public void close() throws IOException {
		this.in.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		this.in.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		this.in.reset();
	}

	@Override
	public boolean markSupported() {
		return this.in.markSupported();
	}

	private void init(HttpResponse response) {
		try {
			this.in = (response.status < Status.HTTP_BAD_REQUEST) ? response.httpConnection.getInputStream() : response.httpConnection.getErrorStream();
		} catch (IOException e) {
			if (false == (e instanceof FileNotFoundException)) {
				throw new HttpException(e);
			}
		}
		if (null == this.in) {
			this.in = new ByteArrayInputStream(("Error request, response status: "+response.status).getBytes());
			return;
		}

		if (response.isGzip() && false == (response.in instanceof GZIPInputStream)) {
			try {
				this.in = new GZIPInputStream(this.in);
			} catch (IOException e) {
			}
		} else if (response.isDeflate() && false == (this.in instanceof InflaterInputStream)) {
			this.in = new InflaterInputStream(this.in, new Inflater(true));
		}
	}

}
