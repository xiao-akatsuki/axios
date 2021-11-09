package com.xiaoHttp;

import com.xiaoHttp.header.Header;
import com.xiaoHttp.request.Request;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void test_get_url(){
        String url = "http://localhost:8080/demo/base/add";
        try {
            new Axios(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_get(){
        String url = "http://localhost:8080/demo/base/add";
        
        try{
            Axios.get(url).then(response ->{
                System.out.println(response.getStatus());
                System.out.println(response.getStatusText());
                System.out.println(response.getHeaders());
                System.out.println(response.getData());
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        
        System.out.println("/////////////////////////");

        try {
            Axios.get(
                url,
                new Request().add("value", "11111")
            ).then(response ->{
                System.out.println(response.getStatus());
                System.out.println(response.getStatusText());
                System.out.println(response.getHeaders());
                System.out.println(response.getData());
            });    
        } catch (Exception e) {
            //TODO: handle exception
        }
        
        System.out.println("/////////////////////////");

        try {
            Axios.get(
                url,
                new Request().add("value", "22222"),
                new Header().add("token", "22222")
            ).then(response ->{
                System.out.println(response.getStatus());
                System.out.println(response.getStatusText());
                System.out.println(response.getHeaders());
                System.out.println(response.getData());
            });    
        } catch (Exception e) {
            //TODO: handle exception
        }
        
        
        
    }

    @Test
    public void test_Request(){
        Request request = new Request()
            .add("account","account")
            .add("password","password")
            .add("key", "value");

        request.getParams().forEach((k,v)->{
            System.out.println("key : " + k + ", value : " + v);
        });        
    }
}
