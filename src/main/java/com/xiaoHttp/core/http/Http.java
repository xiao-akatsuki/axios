package com.xiaoHttp.core.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.xiaoHttp.response.Response;

public class Http {

    public static Response ajax(String url) throws Exception{
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
        
        return new Response(con.getResponseCode(), con.getHeaderFields(), con.getResponseMessage(), response.toString());
        
    }
    
}
