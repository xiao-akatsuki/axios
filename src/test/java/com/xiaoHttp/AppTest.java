package com.xiaoHttp;

import static org.junit.Assert.assertTrue;

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
    }
}
