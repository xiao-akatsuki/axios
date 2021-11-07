package com.xiaoHttp.response;

import java.util.List;
import java.util.Map;

public class Response {

    private int status;
    
    private Map<String,List<String>> headers;

    private String statusText;

    private String data;

    public Response(){
    }

    public Response(int status,
        Map<String,List<String>> headers,
        String statusText,
        String data){
        this.status = status;
        this.headers = headers;
        this.statusText = statusText;
        this.data = data;
    }
    
    public void setStatus(int status){
        this.status = status;
    }

    public void setHeaders(Map<String,List<String>> headers){
        this.headers = headers;
    }

    public void setStatusTest(String statusTest){
        this.statusText = statusTest;
    }

    public void setData(String data){
        this.data = data;
    }

    

}
