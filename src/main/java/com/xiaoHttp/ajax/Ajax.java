package com.xiaoHttp.ajax;

import com.xiaoHttp.res.Res;

public interface Ajax {
    
    default void then(Res accept){
        System.out.println(accept);
    }
}
