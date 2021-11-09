package com.xiaoHttp;

import com.xiaoHttp.ajax.Ajax;
import com.xiaoHttp.core.http.Http;
import com.xiaoHttp.header.Header;
import com.xiaoHttp.request.Request;
import com.xiaoHttp.response.Response;

public class Axios implements Ajax {

    public static String url;

    public static Request params;

    public static Header headers;

    public Axios(){  }

    public Axios(String url) throws Exception{
        Axios.url = url;
        get();
    }

    public Axios(String url,Request params) throws Exception {
        Axios.url = url;
        Axios.params = params;
        get();
    }

    public Axios(String url,Request params,Header headers) throws Exception {
        Axios.url = url;
        Axios.params = params;
        Axios.headers = headers;
        get();
    }

    public static Axios get(String url){
        Axios axios = new Axios();
        axios.url = url;
        return axios;
    }

    public static Axios get(String url,Request params){
        Axios axios = new Axios();
        axios.url = url;
        axios.params = params;
        return axios;
    }

    public static Axios get(String url,Request params,Header headers){
        Axios axios = new Axios();
        axios.url = url;
        axios.params = params;
        axios.headers = headers;
        return axios;
    }

    @Override
    public Response get() throws Exception {
        return Http.get(Axios.url,Axios.params,Axios.headers);
    }

}
