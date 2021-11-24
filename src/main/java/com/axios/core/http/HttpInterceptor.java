package com.axios.core.http;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.axios.core.http.lang.Chain;

/**
 * [Http拦截器接口](HTTP interceptor interface)
 * @description zh - Http拦截器接口
 * @description en - HTTP interceptor interface
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-24 20:07:53
 */
@FunctionalInterface
public interface HttpInterceptor {

	/**
	 * [处理请求](Processing requests)
	 * @description zh - 处理请求
	 * @description en - Processing requests
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-24 20:08:20
	 * @param request HttpRequest
	 */
	void process(HttpRequest request);

	/**
	 * [拦截器链](Interceptor chain )
	 * @description zh - 拦截器链
	 * @description en - Interceptor chain
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-11-24 20:21:05
	 */
	class Interceptor implements Chain<HttpInterceptor, Interceptor> {
		private final List<HttpInterceptor> interceptors = new LinkedList<>();


		@Override
		public Interceptor addChain(HttpInterceptor element) {
			interceptors.add(element);
			return this;
		}

		@Override
		public Iterator<HttpInterceptor> iterator() {
			return interceptors.iterator();
		}
	}



}
