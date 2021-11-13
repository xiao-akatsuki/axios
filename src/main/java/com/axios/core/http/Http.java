package com.axios.core.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import com.axios.header.Header;
import com.axios.request.Request;
import com.axios.response.Response;

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
     * @param method 参数方法
     * @param params 请求参数
     * @param headers 请求头
     * @throws java.lang.Exception
     * @return com.xiaoHttp.response.Response
     */
    public static Response get(String url,String method,Request params,Header headers) throws Exception{
        URL obj;

        if(params != null){
            obj = new URL(url + "?" + params.toParam());
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

    /**
     * [发送类似post请求](Send similar post requests)
     * @description zh - 发送类似post请求
     * @description en - Send similar post requests
     * @version V1.0
     * @author XiaoXunYao
     * @since 2021-11-10 21:49:54
     * @param url URL
     * @param method 参数方法
     * @param params 请求参数
     * @param headers 请求头
     * @throws java.lang.Exception
     * @return com.xiaoHttp.response.Response
     */
    public static Response post(String url,String method,Request body,Header headers) throws Exception{
        URL url_con = new URL(url);
        // 打开和URL之间的连接
        HttpURLConnection con = (HttpURLConnection)url_con.openConnection();
        //请求方式
        con.setRequestMethod(method);
        // Post请求不能使用缓存
        con.setUseCaches(false);
        // 设置是否从HttpURLConnection输入，默认值为 true
        con.setDoInput(true);
        // 设置是否使用HttpURLConnection进行输出，默认值为 false
        con.setDoOutput(true);

        if (headers != null){
            headers.getHeaders().forEach((key,value)->{
                con.setRequestProperty(key, value);
            });
        }
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("isTree", "true");
        con.setRequestProperty("isLastPage", "true");


        // 建立实际的连接
        con.connect();

        // 得到请求的输出流对象
		if (body != null) {
			OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			writer.write(body.toBody());
			writer.flush();
		}

        // 获取服务端响应，通过输入流来读取URL的响应
        InputStream is = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sbf = new StringBuffer();
        String strRead = null;
        while ((strRead = reader.readLine()) != null) {
            sbf.append(strRead);
            sbf.append("\r\n");
        }
        reader.close();

        // 关闭连接
        con.disconnect();

        // 打印读到的响应结果
        return new Response(
                con.getResponseCode(),
                con.getHeaderFields(),
                con.getResponseMessage(),
                sbf.toString()
        );
    }

}
