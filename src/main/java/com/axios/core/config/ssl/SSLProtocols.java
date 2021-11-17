package com.axios.core.config.ssl;

/**
 * [SSL或TLS协议](SSL or TLS protocol)
 * @description zh - SSL或TLS协议
 * @description en - SSL or TLS protocol
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-17 13:34:22
 */
public interface SSLProtocols {

	/**
	 * Supports some version of SSL; may support other versions
	 */
	String SSL = "SSL";
	/**
	 * Supports SSL version 2 or later; may support other versions
	 */
	String SSLv2 = "SSLv2";
	/**
	 * Supports SSL version 3; may support other versions
	 */
	String SSLv3 = "SSLv3";

	/**
	 * Supports some version of TLS; may support other versions
	 */
	String TLS = "TLS";
	/**
	 * Supports RFC 2246: TLS version 1.0 ; may support other versions
	 */
	String TLSv1 = "TLSv1";
	/**
	 * Supports RFC 4346: TLS version 1.1 ; may support other versions
	 */
	String TLSv11 = "TLSv1.1";
	/**
	 * Supports RFC 5246: TLS version 1.2 ; may support other versions
	 */
	String TLSv12 = "TLSv1.2";

}
