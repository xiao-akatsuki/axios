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

    public String toParam(){
        StringBuffer returnValue = new StringBuffer();
        this.params.forEach((key,value)->{
            returnValue.append(key + "=%27" + value +"%27");
        });
        return returnValue.toString();
    }

    public String toBody(){
        StringBuffer json = new StringBuffer();
        json.append("{");

        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            json.append("\"").append(key).append("\":\"").append(value).append("\",");
        }
        json.deleteCharAt(json.length()-1);
        json.append("}");

        return json.toString();
    }
    
}
