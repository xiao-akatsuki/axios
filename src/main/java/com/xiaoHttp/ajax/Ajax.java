package com.xiaoHttp.ajax;


import com.xiaoHttp.method.Method;
import com.xiaoHttp.response.Response;

public interface Ajax {

    Response get() throws Exception;
    
    default void then(Method accept) throws Exception{
        accept.accept(get());
    }
}
