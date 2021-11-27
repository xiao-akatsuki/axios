package com.axios.core.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.axios.core.tool.io.IoTool;
import com.axios.exception.IORuntimeException;

/**
 * [定义资源接口](Define resource interface)
 * @description zh - 定义资源接口
 * @description en - Define resource interface
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-27 14:05:48
 */
public interface Resource {

	/**
	 * [获取资源名](Get resource name)
	 * @description zh - 获取资源名
	 * @description en - Get resource name
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:13:49
	 * @return java.lang.String
	 */
	String getName();

	/**
	 * [获得解析后的URL](Get the parsed URL)
	 * @description zh - 获得解析后的URL
	 * @description en - Get the parsed URL
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:14:16
	 * @return java.net.URL
	 */
	URL getUrl();

	/**
	 * [获得InputStream](get InputStream)
	 * @description zh - 获得InputStream
	 * @description en - get InputStream
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:14:46
	 * @return java.io.InputStream
	 */
	InputStream getStream();

	/**
	 * [将资源内容写出到流](Write out resource content to stream)
	 * @description zh - 将资源内容写出到流
	 * @description en - Write out resource content to stream
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:15:14
	 * @param out OutputStream
	 * @throws com.axios.exception.IORuntimeException
	 */
	default void writeTo(OutputStream out) throws IORuntimeException {

		try (InputStream in = getStream()) {
			IoTool.copy(in, out);
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * [获得Reader](get Reader)
	 * @description zh - 获得Reader
	 * @description en - get Reader
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:15:51
	 * @param charset 编码
	 * @return java.io.BufferedReader
	 */
	default BufferedReader getReader(Charset charset) {
		return IoTool.getReader(getStream(), charset);
	}

	/**
	 * [读取资源内容](Read resource content)
	 * @description zh - 读取资源内容
	 * @description en - Read resource content
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:16:29
	 * @param charset 编码
	 * @throws com.axios.exception.IORuntimeException
	 * @return java.lang.String
	 */
	default String readStr(Charset charset) throws IORuntimeException {
		return IoTool.read(getReader(charset),true);
	}

	/**
	 * [读取资源内容](Read resource content)
	 * @description zh - 读取资源内容
	 * @description en - Read resource content
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:17:16
	 * @throws com.axios.exception.IORuntimeException
	 * @return java.lang.String
	 */
	default String readUtf8Str() throws IORuntimeException {
		return readStr(StandardCharsets.UTF_8);
	}

	/**
	 * [读取资源内容](Read resource content)
	 * @description zh - 读取资源内容
	 * @description en - Read resource content
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-27 14:17:34
	 * @throws com.axios.exception.IORuntimeException
	 * @return byte[]
	 */
	default byte[] readBytes() throws IORuntimeException {
		return IoTool.readBytes(getStream(),true);
	}

}
