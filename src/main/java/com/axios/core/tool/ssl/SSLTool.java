package com.axios.core.tool.ssl;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.axios.exception.IORuntimeException;

public class SSLTool {

	/**
	 * [创建 SSLContext](set SSLContext)
	 * @description zh - 创建 SSLContext
	 * @description en - set SSLContext
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:50:57
	 * @param protocol SSL协议，例如TLS等
	 * @return javax.net.ssl.SSLContext
	 */
	public static SSLContext createSSLContext(String protocol) throws IORuntimeException{
		return SSLContextBuilder.create().setProtocol(protocol).buildQuietly();
	}

	/**
	 * [创建 SSLContext](set SSLContext)
	 * @description zh - 创建 SSLContext
	 * @description en - set SSLContext
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:51:58
	 * @param protocol SSL协议，例如TLS等
	 * @param keyManager 密钥管理器
	 * @param trustManager 信任管理器
	 * @throws com.axios.exception.IORuntimeException
	 * @return javax.net.ssl.SSLContext
	 */
	public static SSLContext createSSLContext(String protocol, KeyManager keyManager, TrustManager trustManager) throws IORuntimeException {
		return createSSLContext(protocol,
				keyManager == null ? null : new KeyManager[]{keyManager},
				trustManager == null ? null : new TrustManager[]{trustManager});
	}

	/**
	 * [创建和初始化 SSLContext](Create and initialize sslcontext)
	 * @description zh - 创建和初始化 SSLContext
	 * @description en - Create and initialize sslcontext
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:52:56
	 * @param protocol SSL协议，例如TLS等
	 * @param keyManager 密钥管理器
	 * @param trustManager 信任管理器
	 * @throws com.axios.exception.IORuntimeException
	 * @return javax.net.ssl.SSLContext
	 */
	public static SSLContext createSSLContext(String protocol, KeyManager[] keyManagers, TrustManager[] trustManagers) throws IORuntimeException {
		return SSLContextBuilder.create()
				.setProtocol(protocol)
				.setKeyManagers(keyManagers)
				.setTrustManagers(trustManagers).buildQuietly();
	}

}
