package com.axios.core.http.url;

import com.axios.core.rfc.PercentCodec;
import com.axios.core.rfc.RFC3986;

/**
 * https://url.spec.whatwg.org/#urlencoded-serializing
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-22 21:19:36
 */
public class FormUrlencoded {
	public static final PercentCodec ALL = PercentCodec.of(RFC3986.UNRESERVED)
			.removeSafe('~').addSafe('*').setEncodeSpaceAsPlus(true);
}
