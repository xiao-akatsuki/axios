package com.axios.core.config.ssl;

import com.axios.exception.IORuntimeException;

public class AndroidSupportSSLFactory extends CustomProtocolsSSLFactory {

	private static final String[] protocols = {
			SSLProtocols.SSLv3, SSLProtocols.TLSv1, SSLProtocols.TLSv11, SSLProtocols.TLSv12};

	public AndroidSupportSSLFactory() throws IORuntimeException {
		super(protocols);
	}

}
