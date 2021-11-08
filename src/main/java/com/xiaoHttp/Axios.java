package com.xiaoHttp;

import com.xiaoHttp.ajax.Ajax;
import com.xiaoHttp.core.http.Http;
import com.xiaoHttp.response.Response;

public class Axios implements Ajax {

    public static String url;

    public Axios(String url){
        Axios.url = url;
    }

    public static Axios get(String url){
        return new Axios(url);
    }

    @Override
    public Response get() throws Exception {
        return Http.ajax(Axios.url);
    }

    
}
