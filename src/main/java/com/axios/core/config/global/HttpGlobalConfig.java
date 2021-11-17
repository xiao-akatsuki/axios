package com.axios.core.config.global;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.CookieManager;
import java.net.HttpURLConnection;

public class HttpGlobalConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	protected static int timeout = -1;
	private static boolean isAllowPatch = false;


}
