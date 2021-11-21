package com.axios.core.http.url;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * [URL解码](URL decoding)
 * @description zh - URL解码
 * @description en - URL decoding
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-21 20:30:47
 */
public class URLDecoder implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final byte ESCAPE_CHAR = '%';

	/**
	 * [解码](decode)
	 * @description zh - 解码
	 * @description en - decode
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:35:43
	 * @param str 路径
	 * @param charset 字符集
	 * @return java.lang.String
	 */
	public static String decodeForPath(String str, Charset charset) {
		return decode(str, charset, false);
	}

	/**
	 * [解码](decode)
	 * @description zh - 解码
	 * @description en - decode
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:38:30
	 * @param str 包含URL编码后的字符串
	 * @param charset 是否+转换为空格
	 * @param isPlusToSpace 编码
	 * @return java.lang.String
	 */
	public static String decode(String str, Charset charset, boolean isPlusToSpace) {
		return null == charset ? str : toString(decode(bytes(str, charset), isPlusToSpace), charset);
	}

	/**
	 * [解码字节码](Decode bytecode)
	 * @description zh - 解码字节码
	 * @description en - Decode bytecode
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:39:43
	 * @param data 字符串
	 * @param charset 字符集
	 * @return java.lang.String
	 */
	private static String toString(byte[] data, Charset charset) {
		if (data == null) {
			return null;
		}

		if (null == charset) {
			return new String(data);
		}
		return new String(data, charset);
	}

	/**
	 * [编码字符串](Encoding string)
	 * @description zh - 编码字符串
	 * @description en - Encoding string
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:40:26
	 * @param data 字符串
	 * @param charset 字符集
	 * @return java.lang.String
	 */
	private static byte[] bytes(String str, Charset charset) {
		if (str == null) {
			return null;
		}

		if (null == charset) {
			return str.toString().getBytes();
		}
		return str.toString().getBytes(charset);
	}

	/**
	 * [解码](decode)
	 * @description zh - 解码
	 * @description en - decode
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:34:10
	 * @param bytes url编码的bytes
	 * @return byte[]
	 */
	public static byte[] decode(byte[] bytes) {
		return decode(bytes, true);
	}

	/**
	 * [解码](decode)
	 * @description zh - 解码
	 * @description en - decode
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:33:15
	 * @param bytes url编码的bytes
	 * @param isPlusToSpace 是否+转换为空格
	 * @return byte[]
	 */
	public static byte[] decode(byte[] bytes, boolean isPlusToSpace) {
		if (bytes == null) {
			return null;
		}
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream(bytes.length);
		int b;
		for (int i = 0; i < bytes.length; i++) {
			b = bytes[i];
			if (b == '+') {
				buffer.write(isPlusToSpace ? ' ' : b);
			} else if (b == ESCAPE_CHAR) {
				if (i + 1 < bytes.length) {
					final int u = digit16(bytes[i + 1]);
					if (u >= 0 && i + 2 < bytes.length) {
						final int l = digit16(bytes[i + 2]);
						if (l >= 0) {
							buffer.write((char) ((u << 4) + l));
							i += 2;
							continue;
						}
					}
				}
				buffer.write(b);
			} else {
				buffer.write(b);
			}
		}
		return buffer.toByteArray();
	}

	/**
	 * [获取给定字符的16进制数值](Gets the hexadecimal value of the given character)
	 * @description zh - 获取给定字符的16进制数值
	 * @description en - Gets the hexadecimal value of the given character
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:32:25
	 * @param b int
	 * @return int
	 */
	private static int digit16(int b) {
		return Character.digit(b, 16);
	}

}
