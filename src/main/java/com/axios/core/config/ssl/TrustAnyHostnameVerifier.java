package com.axios.core.config.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * [https 域名校验](HTTPS domain name verification)
 * @description zh - https 域名校验
 * @description en - HTTPS domain name verification
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-16 18:53:16
 */
public class TrustAnyHostnameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}
}
