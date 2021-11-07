package com.xiaoHttp;

import static org.junit.Assert.assertTrue;

import com.xiaoHttp.core.httpRequest.HttpRequest;

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
        // String url = "https://www.boyucode.com/competition/hero/getHero";
        // String a = HttpRequest.get(url).then();
        
        Axios.get(url).then(json ->{
            System.out.println(json);
        });        
    }
}
