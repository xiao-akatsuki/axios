package com.axios.core.tool.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.axios.core.assertion.Assert;
import com.axios.exception.IORuntimeException;

/**
 * [InputStream 向 OutputStream 拷贝](Copy InputStream to OutputStream)
 * @description zh - InputStream 向 OutputStream 拷贝
 * @description en - Copy InputStream to OutputStream
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-08 20:13:41
 */
public class StreamCopier extends IoCopier<InputStream, OutputStream> {

	public StreamCopier() {
		this(2 << 12);
	}

	public StreamCopier(int bufferSize) {
		this(bufferSize, -1);
	}

	public StreamCopier(int bufferSize, long count) {
		super(bufferSize, count);
	}

	@Override
	public long copy(InputStream source, OutputStream target) {
		Assert.notNull(source, "InputStream is null !");
		Assert.notNull(target, "OutputStream is null !");

		final long size;
		try {
			size = doCopy(source, target, new byte[bufferSize(this.count)]);
			target.flush();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}

		return size;
	}

	private long doCopy(InputStream source, OutputStream target, byte[] buffer) throws IOException {
		long numToRead = this.count > 0 ? this.count : Long.MAX_VALUE;
		long total = 0;

		int read;
		while (numToRead > 0) {
			read = source.read(buffer, 0, bufferSize(numToRead));
			if (read < 0) {
				break;
			}
			target.write(buffer, 0, read);

			numToRead -= read;
			total += read;
		}

		return total;
	}
}
