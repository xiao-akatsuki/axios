package com.axios;

import com.axios.ajax.Ajax;
import com.axios.core.http.HttpRequest;
import com.axios.core.requestMethod.RequestMethod;
import com.axios.header.Header;
import com.axios.request.Body;
import com.axios.request.Request;
import com.axios.response.HttpResponse;
import com.axios.response.Response;

public class Axios extends Conn implements Ajax {

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

	@Override
	public Response ajax() throws Exception {
		HttpRequest request = Conn.createRequest(method, url);

		if( null != this.param ){
			request.form(this.param.getParams());
		}

		if (null != this.header){
			this.header.getHeaders().forEach((key,value)->{
				request.header(key, value);
			});
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

}
