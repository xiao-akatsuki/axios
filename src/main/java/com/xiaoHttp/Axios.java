package com.xiaoHttp;

import com.xiaoHttp.ajax.Ajax;
import com.xiaoHttp.core.http.Http;
import com.xiaoHttp.core.requestMethod.RequestMethod;
import com.xiaoHttp.header.Header;
import com.xiaoHttp.request.Request;
import com.xiaoHttp.response.Response;

public class Axios implements Ajax {

    private String url;

    private String method;

    private Request params;

    private Header headers;

    public Axios(){  }

    public Axios(String url) throws Exception{
        this.url = url;
        ajax();
    }

    public Axios(String url, RequestMethod method) throws Exception{
        this.url = url;
        this.method = method.name();
        ajax();
    }

    public Axios(String url, RequestMethod method,Request params) throws Exception{
        this.url = url;
        this.method = method.name();
        this.params = params;
        ajax();
    }

    public Axios(String url, RequestMethod method,Header headers) throws Exception{
        this.url = url;
        this.method = method.name();
        this.headers = headers;
        ajax();
    }

    public Axios(String url,Request params) throws Exception {
        this.url = url;
        this.params = params;
        ajax();
    }

    public Axios(String url,Request params,Header headers) throws Exception {
        this.url = url;
        this.params = params;
        this.headers = headers;
        ajax();
    }

    public Axios(String url, RequestMethod method, Request params, Header headers) throws Exception {
        this.url = url;
        this.method = method.name();
        this.params = params;
        this.headers = headers;
        ajax();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(RequestMethod method) {
        this.method = method.name();
    }

    public Request getParams() {
        return params;
    }

    public void setParams(Request params) {
        this.params = params;
    }

    public Header getHeaders() {
        return headers;
    }

    public void setHeaders(Header headers) {
        this.headers = headers;
    }

    /** get ---------------------- get 请求 */


    public static Axios get(String url){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.GET);
        return axios;
    }

    public static Axios get(String url,Request params){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.GET);
        axios.setParams(params);
        return axios;
    }

    public static Axios get(String url,Request params,Header headers){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.GET);
        axios.setParams(params);
        axios.setHeaders(headers);
        return axios;
    }

    /** post ---------------------- post 请求 */

    public static Axios post(String url){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.POST);
        return axios;
    }

    public static Axios post(String url,Request params){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.POST);
        axios.setParams(params);
        return axios;
    }

    public static Axios post(String url,Request params,Header headers){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.POST);
        axios.setParams(params);
        axios.setHeaders(headers);
        return axios;
    }

    /** put ---------------------- put 请求 */

    public static Axios put(String url){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.PUT);
        return axios;
    }

    public static Axios put(String url,Request params){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.PUT);
        axios.setParams(params);
        return axios;
    }

    public static Axios put(String url,Request params,Header headers){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.PUT);
        axios.setParams(params);
        axios.setHeaders(headers);
        return axios;
    }

    /** put ---------------------- put 请求 */

    public static Axios delete(String url){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.DELETE);
        return axios;
    }

    public static Axios delete(String url,Request params){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.DELETE);
        axios.setParams(params);
        return axios;
    }

    public static Axios delete(String url,Request params,Header headers){
        Axios axios = new Axios();
        axios.setUrl(url);
        axios.setMethod(RequestMethod.DELETE);
        axios.setParams(params);
        axios.setHeaders(headers);
        return axios;
    }


    @Override
    public Response ajax() throws Exception {
        switch (this.method) {
            case "POST", "PUT" -> {
                return Http.post(this.url, this.method, this.params, this.headers);
            }
            default -> {
                return Http.get(this.url, this.method, this.params, this.headers);
            }
        }
    }

}
