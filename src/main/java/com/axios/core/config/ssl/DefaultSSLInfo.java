package com.axios.core.config.ssl;

import javax.net.ssl.SSLSocketFactory;

import com.axios.core.urlTool.UrlTool;

/**
 * [默认的全局SSL配置](Default global SSL configuration)
 * @description zh - 默认的全局SSL配置
 * @description en - Default global SSL configuration
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-16 18:56:39
 */
public class DefaultSSLInfo {
	/**
	 * 默认信任全部的域名校验器
	 */
	public static final TrustAnyHostnameVerifier TRUST_ANY_HOSTNAME_VERIFIER;
	/**
	 * 默认的SSLSocketFactory，区分安卓
	 */
	public static final SSLSocketFactory DEFAULT_SSF;

	static {
		TRUST_ANY_HOSTNAME_VERIFIER = new TrustAnyHostnameVerifier();
		if (UrlTool.equalsIgnoreCase("dalvik", System.getProperty("java.vm.name"))) {
			// 兼容android低版本SSL连接
			DEFAULT_SSF = new AndroidSupportSSLFactory();
		} else {
			DEFAULT_SSF = new DefaultSSLFactory();
		}
	}
}
