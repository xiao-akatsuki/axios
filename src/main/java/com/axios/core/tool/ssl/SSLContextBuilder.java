package com.axios.core.tool.ssl;

import com.axios.core.config.ssl.SSLProtocols;
import com.axios.core.manager.DefaultTrustManager;
import com.axios.core.tool.UrlTool;
import com.axios.exception.IORuntimeException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * [SSLContext构建器](Sslcontext builder)
 * @description zh - SSLContext构建器
 * @description en - Sslcontext builder
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-17 13:50:01
 */
public class SSLContextBuilder implements SSLProtocols  {

	private String protocol = TLS;
	private KeyManager[] keyManagers;
	private TrustManager[] trustManagers = {DefaultTrustManager.INSTANCE};
	private SecureRandom secureRandom = new SecureRandom();

	/**
	 * [创建 SSLContextBuilder](Create sslcontextbuilder)
	 * @description zh - 创建 SSLContextBuilder
	 * @description en - Create sslcontextbuilder
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:40:35
	 */
	public static SSLContextBuilder create() {
		return new SSLContextBuilder();
	}

	/**
	 * [设置协议。例如TLS等](Set the protocol. For example, TLS, etc)
	 * @description zh - 设置协议。例如TLS等
	 * @description en - Set the protocol. For example, TLS, etc
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:42:26
	 */
	public SSLContextBuilder setProtocol(String protocol) {
		if (UrlTool.isNotBlank(protocol)) {
			this.protocol = protocol;
		}
		return this;
	}

	/**
	 * [设置信任信息](Set trust information)
	 * @description zh - 设置信任信息
	 * @description en - Set trust information
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:46:37
	 * @param trustManagers TrustManager列表
	 * @return com.axios.core.urlTool.ssl.SSLContextBuilder
	 */
	public SSLContextBuilder setTrustManagers(TrustManager... trustManagers) {
		if (UrlTool.isNotEmpty(trustManagers)) {
			this.trustManagers = trustManagers;
		}
		return this;
	}

	/**
	 * [设置 JSSE key managers](set JSSE key managers)
	 * @description zh - 设置 JSSE key managers
	 * @description en - set JSSE key managers
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:47:26
	 * @param keyManagers KeyManager
	 * @return com.axios.core.urlTool.ssl.SSLContextBuilder
	 */
	public SSLContextBuilder setKeyManagers(KeyManager... keyManagers) {
		if (UrlTool.isNotEmpty(keyManagers)) {
			this.keyManagers = keyManagers;
		}
		return this;
	}

	/**
	 * [设置 SecureRandom](set SecureRandom)
	 * @description zh - 设置 SecureRandom
	 * @description en - set SecureRandom
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:48:17
	 */
	public SSLContextBuilder setSecureRandom(SecureRandom secureRandom) {
		if (null != secureRandom) {
			this.secureRandom = secureRandom;
		}
		return this;
	}

	/**
	 * [构建](structure)
	 * @description zh - 构建
	 * @description en - structure
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:48:49
	 * @throws java.security.NoSuchAlgorithmException
	 * @throws java.security.KeyManagementException
	 * @return javax.net.ssl.SSLContext
	 */
	public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
		SSLContext sslContext = SSLContext.getInstance(protocol);
		sslContext.init(this.keyManagers, this.trustManagers, this.secureRandom);
		return sslContext;
	}

	/**
	 * [构建](structure)
	 * @description zh - 构建
	 * @description en - structure
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-17 13:48:49
	 * @throws com.axios.exception.IORuntimeException
	 * @return javax.net.ssl.SSLContext
	 */
	public SSLContext buildQuietly() throws IORuntimeException {
		try {
			return build();
		} catch (GeneralSecurityException e) {
			throw new IORuntimeException(e);
		}
	}
}
