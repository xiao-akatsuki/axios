package com.xiaoHttp;

import com.xiaoHttp.ajax.Ajax;
import com.xiaoHttp.core.http.Http;
import com.xiaoHttp.request.Request;
import com.xiaoHttp.response.Response;

public class Axios implements Ajax {

    public static String url;

    private static Request params;

    public Axios(String url){
        Axios.url = url;
    }

    public Axios(String url,Request params){
        Axios.url = url;
        Axios.params = params;
    }

    public static Axios get(String url){
        return new Axios(url);
    }

    public static Axios get(String url,Request params){
        return new Axios(url,params);
    }

    @Override
    public Response get() throws Exception {
        return Http.get(Axios.url,Axios.params);
    }

    
}
