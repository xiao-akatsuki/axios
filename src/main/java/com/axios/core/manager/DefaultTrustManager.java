package com.axios.core.manager;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.X509Certificate;

/**
 * [默认信任管理器](Default trust manager)
 * @description zh - 默认信任管理器
 * @description en - Default trust manager
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-17 13:39:08
 */
public class DefaultTrustManager extends X509ExtendedTrustManager {

	public static DefaultTrustManager INSTANCE = new DefaultTrustManager();

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType) {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType) {
	}

	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) {
	}

	@Override
	public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) {
	}

}
