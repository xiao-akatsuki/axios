package com.axios;

import com.axios.ajax.Ajax;
import com.axios.core.http.HttpRequest;
import com.axios.core.requestMethod.RequestMethod;
import com.axios.header.Header;
import com.axios.request.Body;
import com.axios.request.Request;
import com.axios.response.HttpResponse;
import com.axios.response.Response;

/**
 * [发送具体的HTTP请求](Send a specific HTTP request)
 * @description zh - 发送具体的HTTP请求
 * @description en - Send a specific HTTP request
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-12-11 20:55:20
 */
public class Axios implements Ajax {

	private String url;

	private RequestMethod method;

	private Header header;

	private Request param;

	private Body body;

	public Axios(){ }

	public Axios(String url) throws Exception{
		this(url, RequestMethod.GET);
	}

	public Axios(String url, RequestMethod method) throws Exception{
		this(url, method, null, null, null);
	}

	public Axios(String url, RequestMethod method, Request param) throws Exception{
		this(url, method, param,  null);
	}

	public Axios(String url, RequestMethod method, Body body) throws Exception{
		this(url,method,body,null);
	}

	public Axios(String url, RequestMethod method, Header header) throws Exception{
		this(url, method, null, null, header);
	}

	public Axios(String url , RequestMethod method, Request param, Header header)throws Exception{
		this(url, method, param, null, header);
	}

	public Axios(String url , RequestMethod method, Body body, Header header)throws Exception{
		this(url, method, null, body, header);
	}

	public Axios (String url, RequestMethod method, Request param, Body body, Header header) throws Exception{
		this.url = url;
		this.method = method;
		this.param = param;
		this.body = body;
		this.header = header;
		ajax();
	}

	/** ------------------- getter and setter ------------------- */

	public void setUrl(String url) {
		this.url = url;
	}

	public void setMethod(RequestMethod method) {
		this.method = method;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public void setParam(Request param) {
		this.param = param;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public String getUrl() {
		return url;
	}

	public Header getHeader() {
		return header;
	}

	public RequestMethod getMethod() {
		return method;
	}

	public Request getParam() {
		return param;
	}

	public Body getBody() {
		return body;
	}

	/** ------------------- override ------------------- */

	@Override
	public Response ajax() throws Exception {
		HttpRequest request = Conn.createRequest(method, url);

		if( null != this.param ){
			request.form(this.param.getParams());
		}

		if (null != this.header){
			this.header.getHeaders().forEach(request::header);
		}

		if (null != this.body){
			request.body(this.body.toBody());
		}

		HttpResponse response = request.execute();
		return new Response(
				response.getStatus(),
				response.headers(),
				response.body()
		);
	}

	/** ------------------- get ------------------- */

	/**
	 * [使用Axios发送Get请求](Use Axios to send Get request)
	 * @description zh - 使用Axios发送Get请求
	 * @description en - Use Axios to send Get request
	 * @version V1.0
	 * @author LZH
	 * @date 15:33 2021/12/11
	 * @param url URL
	 * @return com.axios.Axios
	 **/
	public static Axios get(String url) throws Exception{
		return get(url, (Header) null);
	}

	/**
	 * [使用Axios发送Get请求](Use Axios to send Get request)
	 * @description zh - 使用Axios发送Get请求
	 * @description en - Use Axios to send Get request
	 * @version V1.0
	 * @author LZH
	 * @date 15:47 2021/12/11
	 * @param url URL
	 * @param header 请求头
	 * @return com.axios.Axios
	 **/
	public static Axios get(String url,Header header) throws Exception {
		return get(url,null,header);
	}

	/**
	 * [使用Axios发送Get请求](Use Axios to send Get request)
	 * @description zh - 使用Axios发送Get请求
	 * @description en - Use Axios to send Get request
	 * @version V1.0
	 * @author LZH
	 * @date 15:47 2021/12/11
	 * @param url URL
	 * @param param 请求内容
	 * @return com.axios.Axios
	 **/
	public static Axios get(String url,Request param) throws Exception {
		return get(url,param,null);
	}

	/**
	 * [使用Axios发送Get请求](Use Axios to send Get request)
	 * @description zh - 使用Axios发送Get请求
	 * @description en - Use Axios to send Get request
	 * @version V1.0
	 * @author LZH
	 * @date 15:48 2021/12/11
	 * @param url URL
	 * @param param 请求内容
	 * @param header 请求头
	 * @return com.axios.Axios
	 **/
	public static Axios get(String url,Request param,Header header) throws Exception{
		return new Axios(url,RequestMethod.GET,param,header);
	}

	/** ------------------- post ------------------- */

	/**
	 * [使用Axios发送Post请求](Use Axios to send Post request)
	 * @description zh - 使用Axios发送Post请求
	 * @description en - Use Axios to send Post request
	 * @version V1.0
	 * @author LZH
	 * @date 15:43 2021/12/11
	 * @param url URL
	 * @throws java.lang.Exception
	 * @return com.axios.Axios
	 **/
	public static Axios post(String url) throws Exception{
		return post(url,null,null);
	}

	/**
	 * [发送post请求](Send post request)
	 * @description zh - 发送post请求
	 * @description en - Send post request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-11 20:32:39
	 * @param url URL
	 * @param body 请求体
	 * @throws java.lang.Exception
	 * @return com.axios.Axios
	 */
	public static Axios post(String url,Body body) throws Exception{
		return post(url, body, null);
	}

	/**
	 * [发送post请求](Send post request)
	 * @description zh - 发送post请求
	 * @description en - Send post request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-11 20:33:33
	 * @param url URL
	 * @param header 请求头
	 * @throws java.lang.Exception
	 * @return com.axios.Axios
	 */
	public static Axios post(String url,Header header) throws Exception{
		return post(url, null, header);
	}

	/**
	 * [使用Axios发送Post请求](Use Axios to send Post request)
	 * @description zh - 使用Axios发送Post请求
	 * @description en - Use Axios to send Post request
	 * @version V1.0
	 * @author LZH
	 * @date 15:47 2021/12/11
	 * @param url URL
	 * @param body 请求体
	 * @param header 请求头
	 * @return com.axios.Axios
	 **/
	public static Axios post(String url,Body body,Header header) throws Exception {
		return new Axios(url,RequestMethod.POST,body,header);
	}

	/** ------------------- put ------------------- */

	/**
	 * [发送PUT请求](send PUT request)
	 * @description zh - 发送PUT请求
	 * @description en - send PUT request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-11 20:46:17
	 * @param url URL
	 * @throws java.lang.Exception
	 * @return com.axios.Axios
	 */
	public static Axios put(String url) throws Exception{
		return put(url,null,null);
	}

	/**
	 * [发送PUT请求](send PUT request)
	 * @description zh - 发送PUT请求
	 * @description en - send PUT request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-11 20:47:02
	 * @param url URL
	 * @param body 请求体
	 * @throws java.lang.Exception
	 * @return com.axios.Axios
	 */
	public static Axios put(String url,Body body) throws Exception{
		return put(url, body, null);
	}

	/**
	 * [发送PUT请求](send PUT request)
	 * @description zh - 发送PUT请求
	 * @description en - send PUT request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-11 20:47:19
	 * @param url URL
	 * @param header 请求头
	 * @throws java.lang.Exception
	 * @return com.axios.Axios
	 */
	public static Axios put(String url,Header header) throws Exception{
		return put(url, null, header);
	}

	/**
	 * [发送PUT请求](send PUT request)
	 * @description zh - 发送PUT请求
	 * @description en - send PUT request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-11 20:47:34
	 * @param url URL
	 * @param body 请求体
	 * @param header 请求头
	 * @throws java.lang.Exception
	 * @return com.axios.Axios
	 */
	public static Axios put(String url,Body body,Header header) throws Exception{
		return new Axios(url,RequestMethod.PUT,body,header);
	}

	/** ------------------- delete ------------------- */

	/**
	 * [发送DELETE请求](send DELETE request)
	 * @description zh - 发送DELETE请求
	 * @description en - send DELETE request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-11 20:48:45
	 * @param url URL
	 * @throws java.lang.Exception
	 * @return com.axios.Axios
	 */
	public static Axios delete(String url) throws Exception{
		return delete(url,null,null);
	}

	/**
	 * [发送DELETE请求](send DELETE request)
	 * @description zh - 发送DELETE请求
	 * @description en - send DELETE request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-11 20:49:52
	 * @param url URL
	 * @param request 请求体
	 * @throws java.lang.Exception
	 * @return com.axios.Axios
	 */
	public static Axios delete(String url,Request request) throws Exception{
		return delete(url, request, null);
	}

	/**
	 * [发送DELETE请求](send DELETE request)
	 * @description zh - 发送DELETE请求
	 * @description en - send DELETE request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-11 20:50:14
	 * @param url URL
	 * @param header 请求头
	 * @throws java.lang.Exception
	 * @return com.axios.Axios
	 */
	public static Axios delete(String url,Header header) throws Exception{
		return delete(url, null, header);
	}

	/**
	 * [发送DELETE请求](send DELETE request)
	 * @description zh - 发送DELETE请求
	 * @description en - send DELETE request
	 * @version V1.0
	 * @author XiaoXunYao
	 * @since 2021-12-11 20:50:28
	 * @param url URL
	 * @param request 请求体
	 * @param header 请求头
	 * @throws java.lang.Exception
	 * @return com.axios.Axios
	 */
	public static Axios delete(String url,Request request,Header header) throws Exception{
		return new Axios(url,RequestMethod.DELETE,request,header);
	}

	/** ------------------- body ------------------- */

	/**
	 * [获取Ajax的响应信息](Get Ajax response information)
	 * @description zh - 获取Ajax的响应信息
	 * @description en - Get Ajax response information
	 * @version V2.0
	 * @author LZH
	 * @date 15:44 2021/12/11
	 * @return java.lang.String
	 **/
	public Response body() throws Exception {
		return ajax();
	}
}
