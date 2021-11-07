package com.xiaoHttp.core.httpRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import com.xiaoHttp.core.method.Method;

public class HttpRequest {

    /**
     * [发送请求的url](URL to send the request)
     * @description zh - 发送请求的url
     * @description en - URL to send the request
     * @version V1.0
     * @author XiaoXunYao
     * @since 2021-11-07 09:23:50
     */
    public static String url;

    public HashMap<String,String> params;

    public HashMap<String,String> bodys;

    public int then;
    
    public static Method method;

    
    public HttpRequest(String url,Method method){
        HttpRequest.url = url;
        HttpRequest.method = method;
    }

    public static HttpRequest get(String url){
        return new HttpRequest(url,Method.GET);
    }

    public static String then() throws Exception{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //默认值GET
        con.setRequestMethod("GET");
        
        //添加请求头
        // con.setRequestProperty("User-Agent", "Mozilla/5.0");

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public String then(Object res) {
        return null;
    }

}
