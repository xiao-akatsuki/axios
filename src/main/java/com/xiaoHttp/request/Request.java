package com.xiaoHttp.request;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private Map<String,String> params = new HashMap<>();

    public Request add(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public Map<String,String> getParams(){
        return this.params;
    }


    
}
