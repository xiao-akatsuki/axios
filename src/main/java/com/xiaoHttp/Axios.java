package com.xiaoHttp;

import com.xiaoHttp.ajax.Ajax;

public class Axios implements Ajax {

    public static String url;

    public Axios(String url){
        Axios.url = url;
    }

    public static Axios get(String url){
        return new Axios(url);
    }

    
}
