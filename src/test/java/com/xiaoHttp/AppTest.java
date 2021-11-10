package com.xiaoHttp;

import com.xiaoHttp.core.http.Http;
import com.xiaoHttp.core.requestMethod.RequestMethod;
import com.xiaoHttp.header.Header;
import com.xiaoHttp.request.Request;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void  test_default() throws Exception{
        String url = "http://localhost:8080/demo/base/remove";
        new Axios(
                url,
                RequestMethod.POST,
                new Request().add("name","post Value"),
                new Header().add("token","post Token")
        ).then(System.out::println);
    }

    @Test
    public void test_base_get(){
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

}
