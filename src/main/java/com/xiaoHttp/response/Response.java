package com.xiaoHttp.response;

import java.util.List;
import java.util.Map;

/**
 * [返回值参数](Return value parameter)
 * @description zh - 返回值参数
 * @description en - Return value parameter
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-11 18:50:57
 */
public class Response {

    private int status;
    
    private Map<String,List<String>> headers;

    private String statusText;

    private String data;

    public Response(){
    }

    public Response(
                    int status,
                    Map<String,List<String>> headers,
                    String statusText,
                    String data
    ){
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

    public int getStatus() {
        return status;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", headers=" + headers +
                ", statusText='" + statusText + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
