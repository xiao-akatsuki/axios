package com.axios.core.tool.io;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import com.axios.core.assertion.Assert;
import com.axios.exception.IORuntimeException;

public class IoTool {

	/**
	 * [执行拷贝](Execute Copy )
	 * @description zh - 执行拷贝
	 * @description en - Execute Copy
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:32:02
	 * @param source InputStream
	 * @param target OutputStream
	 * @throws com.axios.exception.IORuntimeException
	 * @return long
	 */
	public static long copy(InputStream source, OutputStream target) throws IORuntimeException {
		Assert.notNull(source, "InputStream is null !");
		Assert.notNull(target, "OutputStream is null !");

		final long size;
		try {
			size = doCopy(source, target, new byte[bufferSize(Long.MAX_VALUE)]);
			target.flush();
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
		return size;
	}

	/**
	 * [缓存大小](Cache size)
	 * @description zh - 缓存大小
	 * @description en - Cache size
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:32:47
	 * @param count 缓存大小
	 * @return int
	 */
	private static int bufferSize(long count) {
		return (int) Math.min(8192 , count);
	}

	/**
	 * [执行拷贝](Execute Copy )
	 * @description zh - 执行拷贝
	 * @description en - Execute Copy
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:33:21
	 * @param source InputStream
	 * @param target OutputStream
	 * @param buffer 缓存
	 * @throws java.io.IOException
	 * @return long
	 */
	private static long doCopy(InputStream source, OutputStream target, byte[] buffer) throws IOException {
		long numToRead = Long.MAX_VALUE;
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

	/**
	 * [获得Reader](get Reader)
	 * @description zh - 获得Reader
	 * @description en - get Reader
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:36:40
	 * @param in InputStream
	 * @param charset 字符集
	 * @return java.io.BufferedReader
	 */
	public static BufferedReader getReader(InputStream in, Charset charset) {
		return null == in ?
			null :
			new BufferedReader(
				null == charset ?
					new InputStreamReader(in) :
					new InputStreamReader(in, charset)
			);
	}

	/**
	 * [从Reader中读取String](Read string from reader)
	 * @description zh - 从Reader中读取String
	 * @description en - Read string from reader
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:40:55
	 * @param reader Reader
	 * @param isClose 是否关闭
	 * @throws com.axios.exception.IORuntimeException
	 * @return java.lang.String
	 */
	public static String read(Reader reader, boolean isClose) throws IORuntimeException {
		final StringBuilder builder = new StringBuilder();
		final CharBuffer buffer = CharBuffer.allocate(8192);
		try {
			while (-1 != reader.read(buffer)) {
				builder.append(buffer.flip());
			}
		} catch (IOException e) {
			throw new IORuntimeException(e);
		} finally {
			if (isClose) {
				close(reader);
			}
		}
		return builder.toString();
	}

	/**
	 * [从流中读取bytes](Read bytes from stream)
	 * @description zh - 从流中读取bytes
	 * @description en - Read bytes from stream
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:43:44
	 * @param in InputStream
	 * @param isClose 是否关闭输入流
	 * @throws com.axios.exception.IORuntimeException
	 * @return byte[]
	 */
	public static byte[] readBytes(InputStream in, boolean isClose) throws IORuntimeException {
		if (in instanceof FileInputStream) {
			// 文件流的长度是可预见的，此时直接读取效率更高
			final byte[] result;
			try {
				final int available = in.available();
				result = new byte[available];
				final int readLength = in.read(result);
				if (readLength != available) {
					throw new IOException("File length is ["+available+"] but read ["+readLength+"]!");
				}
			} catch (IOException e) {
				throw new IORuntimeException(e);
			} finally {
				if (isClose) {
					close(in);
				}
			}
			return result;
		}

		// 未知bytes总量的流
		return read(in, isClose).toByteArray();
	}

	/**
	 * [从流中读取内容](Read content from stream)
	 * @description zh - 从流中读取内容
	 * @description en - Read content from stream
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-28 14:40:25
	 * @param in InputStream
	 * @param isClose 读取完毕后是否关闭流
	 * @throws com.axios.exception.IORuntimeException
	 * @return com.axios.core.tool.io.FastByteArrayOutputStream
	 */
	public static FastByteArrayOutputStream read(InputStream in, boolean isClose) throws IORuntimeException {
		final FastByteArrayOutputStream out;
		if (in instanceof FileInputStream) {
			try {
				out = new FastByteArrayOutputStream(in.available());
			} catch (IOException e) {
				throw new IORuntimeException(e);
			}
		} else {
			out = new FastByteArrayOutputStream();
		}
		try {
			copy(in, out);
		} finally {
			if (isClose) {
				close(in);
			}
		}
		return out;
	}

	/**
	 * [关闭流](close steam)
	 * @description zh - 关闭流
	 * @description en - close steam
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:40:07
	 * @param closeable 被关闭的对象
	 */
	public static void close(Closeable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			} catch (Exception e) {
				// 静默关闭
			}
		}
	}
}
