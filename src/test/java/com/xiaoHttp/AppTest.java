package com.xiaoHttp;

import static org.junit.Assert.assertTrue;

import com.xiaoHttp.request.Request;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue(){
        assertTrue( true );
    }

    @Test
    public void test_get() throws Exception{
        String url = "http://localhost:8080/demo/base/add";
        
        Axios.get(url).then(response ->{
            System.out.println(response.getStatus());
            System.out.println(response.getStatusText());
            System.out.println(response.getHeaders());
            System.out.println(response.getData());
        });

        System.out.println("/////////////////////////");

        Axios.get(
            url,
            new Request().add("value", "11111")
        ).then(response ->{
            System.out.println(response.getStatus());
            System.out.println(response.getStatusText());
            System.out.println(response.getHeaders());
            System.out.println(response.getData());
        });
        
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
