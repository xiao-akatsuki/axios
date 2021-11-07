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
        String a = HttpRequest.get("https://www.baidu.com").then();
        System.out.println(a);
        
    }
}
