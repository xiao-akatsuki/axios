package com.xiaoHttp.core.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.xiaoHttp.header.Header;
import com.xiaoHttp.request.Request;
import com.xiaoHttp.response.Response;

/**
 * [发送请求的方式](How to send the request)
 * @description zh - 发送请求的方式
 * @description en - How to send the request
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-09 21:38:56
 */
public class Http {

    /**
     * [发送HTTP请求](Send HTTP request)
     * @description zh - 发送HTTP请求
     * @description en - Send HTTP request
     * @version V1.0
     * @author XiaoXunYao
     * @since 2021-11-09 21:41:56
     * @param url URL
     * @param request 请求参数
     * @param header 请求头
     * @throws java.lang.Exception
     * @return com.xiaoHttp.response.Response
     */
    public static Response get(String url,String method,Request request,Header headers) throws Exception{
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
        
        //需要输出
        con.setDoOutput(true);  
        //需要输入 
        con.setDoInput(true);   
        //不允许缓存
        con.setUseCaches(false);  

        //设置请求类型
        if(method == null){
            //默认值GET
            con.setRequestMethod("GET");
        }else {
            method = method.replaceAll(" ", "");
            if(method.length() == 0){
                //默认值GET
                con.setRequestMethod("GET");
            }else{
                con.setRequestMethod(method);
            }
        }
        
        if(headers != null){
            headers.getHeaders().forEach((key,value)->{
                //添加请求头
                con.setRequestProperty(key, value);
            });
        }

        BufferedReader in = 
            new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        return new Response(
            con.getResponseCode(), 
            con.getHeaderFields(), 
            con.getResponseMessage(), 
            response.toString()
        );
    }

    public static Response post(String url,String method,Request body,Header headers) throws Exception{
        URL obj = new URL(url);
        
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        //需要输出
        con.setDoOutput(true);  
        //需要输入 
        con.setDoInput(true);   
        //不允许缓存
        con.setUseCaches(false);  

        //设置请求类型
        if(method == null){
            //默认值GET
            con.setRequestMethod("POST");
        }else {
            method = method.replaceAll(" ", "");
            if(method.length() == 0){
                //默认值GET
                con.setRequestMethod("POST");
            }else{
                con.setRequestMethod(method);
            }
        }

        //设置参数
        if(body != null){
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
            body.getParams().forEach((key,value)->{
                try {
                    writer.write( key + "=" + value);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            
            writer.flush();
            writer.close();
        }
        
        if(headers != null){
            headers.getHeaders().forEach((key,value)->{
                //添加请求头
                con.setRequestProperty(key, value);
            });
        }

        BufferedReader in = 
            new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
        return new Response(
            con.getResponseCode(), 
            con.getHeaderFields(), 
            con.getResponseMessage(), 
            response.toString()
        );
    }

}
