package com.axios.core.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.BitSet;

public class URLEncoder implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 存放安全编码
	 */
	private final BitSet safeCharacters;

	/**
	 * 是否编码空格为+
	 */
	private boolean encodeSpaceAsPlus = false;

	public URLEncoder() {
		this(new BitSet(256));

		// unreserved
		addAlpha();
		addDigit();
	}

	private URLEncoder(BitSet safeCharacters) {
		this.safeCharacters = safeCharacters;
	}

	private void addAlpha() {
		for (char i = 'a'; i <= 'z'; i++) {
			addSafeCharacter(i);
		}
		for (char i = 'A'; i <= 'Z'; i++) {
			addSafeCharacter(i);
		}
	}

	private void addDigit() {
		for (char i = '0'; i <= '9'; i++) {
			addSafeCharacter(i);
		}
	}

	public static final URLEncoder QUERY = createQuery();

	public static URLEncoder createQuery() {
		final URLEncoder encoder = new URLEncoder();
		// Special encoding for space
		encoder.setEncodeSpaceAsPlus(true);
		// Alpha and digit are safe by default
		// Add the other permitted characters
		encoder.addSafeCharacter('*');
		encoder.addSafeCharacter('-');
		encoder.addSafeCharacter('.');
		encoder.addSafeCharacter('_');

		encoder.addSafeCharacter('=');
		encoder.addSafeCharacter('&');

		return encoder;
	}

	public void setEncodeSpaceAsPlus(boolean encodeSpaceAsPlus) {
		this.encodeSpaceAsPlus = encodeSpaceAsPlus;
	}

	public void addSafeCharacter(char c) {
		safeCharacters.set(c);
	}

	public String encode(String path, Charset charset) {
		if (null == charset || UrlTool.isEmpty(path)) {
			return path;
		}

		final StringBuilder rewrittenPath = new StringBuilder(path.length());
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(buf, charset);

		int c;
		for (int i = 0; i < path.length(); i++) {
			c = path.charAt(i);
			if (safeCharacters.get(c)) {
				rewrittenPath.append((char) c);
			} else if (encodeSpaceAsPlus && c == ' ') {
				// 对于空格单独处理
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
					UrlTool.appendHex(rewrittenPath, toEncode, false);
				}
				buf.reset();
			}
		}
		return rewrittenPath.toString();
	}

}
