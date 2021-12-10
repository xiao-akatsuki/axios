package com.axios.core.http;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;

import com.axios.Conn;
import com.axios.core.assertion.Assert;
import com.axios.core.tool.io.FastByteArrayOutputStream;
import com.axios.exception.HttpException;
import com.axios.response.HttpResponse;

/**
 * [下载的封装](Downloaded packages)
 * @description zh - 下载的封装
 * @description en - Downloaded packages
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-09 19:46:02
 */
public class HttpDownloader {

	public static String downloadString(String url, Charset customCharset) {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		download(url, out, true);
		return null == customCharset ? out.toString() : out.toString(customCharset);
	}

	public static byte[] downloadBytes(String url) {
		return requestDownload(url, -1).bodyBytes();
	}

	public static long downloadFile(String url, File targetFileOrDir, int timeout) {
		return requestDownload(url, timeout).writeBody(targetFileOrDir);
	}

	public long downloadFile(String url, File targetFileOrDir, String tempFileSuffix, int timeout) {
		return requestDownload(url, timeout).writeBody(targetFileOrDir, tempFileSuffix);
	}

	public static File downloadForFile(String url, File targetFileOrDir, int timeout) {
		return requestDownload(url, timeout).writeBodyForFile(targetFileOrDir);
	}

	public static long download(String url, OutputStream out, boolean isCloseOut) {
		Assert.notNull(out, "[out] is null !");
		return requestDownload(url, -1).writeBody(out, isCloseOut);
	}

	private static HttpResponse requestDownload(String url, int timeout) {
		Assert.notBlank(url, "[url] is blank !");

		final HttpResponse response = Conn.createGet(url, true)
				.timeout(timeout)
				.executeAsync();

		if (response.isOk()) {
			return response;
		}
		throw new HttpException("Server response error with status code: [" + response.getStatus() + "]");
	}

}
