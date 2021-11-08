package com.xiaoHttp.core.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.xiaoHttp.request.Request;
import com.xiaoHttp.response.Response;

public class Http {

    public static Response get(String url) throws Exception{
        return get(url, null);
    }

    public static Response get(String url,Request request) throws Exception{
        URL obj;
        StringBuffer params = new StringBuffer();
        if(request != null){
            
            request.getParams().forEach((key,value)->{
                params.append(key + "=%27" + value +"%27");
            });
            obj = new URL(url + "?" + params.toString());
        }else{
            obj = new URL(url);
        }
        
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setDoOutput(true);   //需要输出
        con.setDoInput(true);   //需要输入
        con.setUseCaches(false);  //不允许缓存

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
        
        return new Response(con.getResponseCode(), con.getHeaderFields(), con.getResponseMessage(), response.toString());
        
    }
    
}
