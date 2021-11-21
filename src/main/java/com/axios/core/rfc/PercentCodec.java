package com.axios.core.rfc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.BitSet;

import com.axios.core.tool.UrlTool;

/**
 * rfc3986 : https://www.ietf.org/rfc/rfc3986.html
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-21 20:46:29
 */
public class PercentCodec implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Storage security code */
	private final BitSet safeCharacters;

	/** Whether the encoding space is+ */
	private boolean encodeSpaceAsPlus = false;

	public PercentCodec() {
		this(new BitSet(256));
	}

	public PercentCodec(BitSet safeCharacters) {
		this.safeCharacters = safeCharacters;
	}

	/**
	 * [从已知PercentCodec创建PercentCodec](Create a percentcodec from a known percentcodec)
	 * @description zh - 从已知PercentCodec创建PercentCodec
	 * @description en - Create a percentcodec from a known percentcodec
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:48:37
	 * @param codec PercentCodec
	 * @return com.axios.core.rfc.PercentCodec
	 */
	public static PercentCodec of(PercentCodec codec) {
		return new PercentCodec((BitSet) codec.safeCharacters.clone());
	}

	/**
	 * [创建PercentCodec](Create percentcodec)
	 * @description zh - 创建PercentCodec
	 * @description en - Create percentcodec
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:49:17
	 * @param chars 安全字符合集
	 * @return com.axios.core.rfc.PercentCodec
	 */
	public static PercentCodec of(CharSequence chars) {
		final PercentCodec codec = new PercentCodec();
		final int length = chars.length();
		for (int i = 0; i < length; i++) {
			codec.addSafe(chars.charAt(i));
		}
		return codec;
	}

	/**
	 * [增加安全字符](Add security characters)
	 * @description zh - 增加安全字符
	 * @description en - Add security characters
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:50:07
	 * @param c 字符
	 * @return com.axios.core.rfc.PercentCodec
	 */
	public PercentCodec addSafe(char c) {
		safeCharacters.set(c);
		return this;
	}

	/**
	 * [移除安全字符](Remove security characters)
	 * @description zh - 移除安全字符
	 * @description en - Remove security characters
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:50:44
	 * @param c	字符
	 * @return com.axios.core.rfc.PercentCodec
	 */
	public PercentCodec removeSafe(char c) {
		safeCharacters.clear(c);
		return this;
	}

	/**
	 * [增加安全字符到挡墙的PercentCodec](Add security characters to the percentcodec of the retaining wall)
	 * @description zh - 增加安全字符到挡墙的PercentCodec
	 * @description en - Add security characters to the percentcodec of the retaining wall
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:51:27
	 * @param codec PercentCodec
	 * @return com.axios.core.rfc.PercentCodec
	 */
	public PercentCodec or(PercentCodec codec) {
		this.safeCharacters.or(codec.safeCharacters);
		return this;
	}

	/**
	 * [组合当前PercentCodec和指定PercentCodec为一个新的PercentCodec](Combine the current percentcodec and specify percentcodec as a new percentcodec)
	 * @description zh - 组合当前PercentCodec和指定PercentCodec为一个新的PercentCodec
	 * @description en - Combine the current percentcodec and specify percentcodec as a new percentcodec
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:52:10
	 * @param codec PercentCodec
	 * @return com.axios.core.rfc.PercentCodec
	 */
	public PercentCodec orNew(PercentCodec codec) {
		return of(this).or(codec);
	}

	/**
	 * [是否将空格编码为+](Encode spaces as+)
	 * @description zh - 是否将空格编码为+
	 * @description en - Encode spaces as+
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:52:51
	 * @param encodeSpaceAsPlus 是否将空格编码为+
	 * @return com.axios.core.rfc.PercentCodec
	 */
	public PercentCodec setEncodeSpaceAsPlus(boolean encodeSpaceAsPlus) {
		this.encodeSpaceAsPlus = encodeSpaceAsPlus;
		return this;
	}

	public String encode(CharSequence path, Charset charset) {
		if (null == charset || UrlTool.isEmpty(path)) {
			return UrlTool.toString(path);
		}

		final StringBuilder rewrittenPath = new StringBuilder(path.length());
		final ByteArrayOutputStream buf = new ByteArrayOutputStream();
		final OutputStreamWriter writer = new OutputStreamWriter(buf, charset);

		int c;
		for (int i = 0; i < path.length(); i++) {
			c = path.charAt(i);
			if (safeCharacters.get(c)) {
				rewrittenPath.append((char) c);
			} else if (encodeSpaceAsPlus && c == ' ') {
				rewrittenPath.append('+');
			} else {
				// convert to external encoding before hex conversion
				try {
					writer.write((char) c);
					writer.flush();
				} catch (IOException e) {
					buf.reset();
					continue;
				}

				byte[] ba = buf.toByteArray();
				for (byte toEncode : ba) {
					// Converting each byte in the buffer
					rewrittenPath.append('%');
					appendHex(rewrittenPath, toEncode, false);
				}
				buf.reset();
			}
		}
		return rewrittenPath.toString();
	}

	/**
	 * [将byte值转为16进制并添加到 StringBuilder 中](Convert the byte value to hexadecimal and add it to StringBuilder)
	 * @description zh - 将byte值转为16进制并添加到 StringBuilder 中
	 * @description en - Convert the byte value to hexadecimal and add it to StringBuilder
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-21 20:56:03
	 */
	private void appendHex(StringBuilder builder, byte b, boolean toLowerCase) {
		final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		final char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
		final char[] toDigits = toLowerCase ? DIGITS_LOWER : DIGITS_UPPER;

		int high = (b & 0xf0) >>> 4;
		int low = b & 0x0f;
		builder.append(toDigits[high]);
		builder.append(toDigits[low]);
	}

}
