package com.axios.cookie;

import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;

/**
 * [线程隔离的Cookie存储](Thread isolated cookie store)
 * @description zh - 线程隔离的Cookie存储
 * @description en - Thread isolated cookie store
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-18 14:21:46
 */
public class ThreadLocalCookieStore implements CookieStore {

	private final static ThreadLocal<CookieStore> STORES = new ThreadLocal<CookieStore>() {
		@Override
		protected synchronized CookieStore initialValue() {
			/* InMemoryCookieStore */
			return (new CookieManager()).getCookieStore();
		}
	};

	/**
	 * [获取本线程下的CookieStore](Get the cookiestore under this thread)
	 * @description zh - 获取本线程下的CookieStore
	 * @description en - Get the cookiestore under this thread
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:22:17
	 * @return java.net.CookieStore
	 */
	public CookieStore getCookieStore() {
		return STORES.get();
	}

	/**
	 * [移除当前线程的Cookie](Remove cookie from current thread)
	 * @description zh - 移除当前线程的Cookie
	 * @description en - Remove cookie from current thread
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-18 14:22:43
	 * @return com.axios.cookie.ThreadLocalCookieStore
	 */
	public ThreadLocalCookieStore removeCurrent() {
		STORES.remove();
		return this;
	}

	@Override
	public void add(URI uri, HttpCookie cookie) {
		getCookieStore().add(uri, cookie);
	}

	@Override
	public List<HttpCookie> get(URI uri) {
		return getCookieStore().get(uri);
	}

	@Override
	public List<HttpCookie> getCookies() {
		return getCookieStore().getCookies();
	}

	@Override
	public List<URI> getURIs() {
		return getCookieStore().getURIs();
	}

	@Override
	public boolean remove(URI uri, HttpCookie cookie) {
		return getCookieStore().remove(uri, cookie);
	}

	@Override
	public boolean removeAll() {
		return getCookieStore().removeAll();
	}

}
