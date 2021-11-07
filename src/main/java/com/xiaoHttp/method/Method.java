package com.xiaoHttp.method;

import com.xiaoHttp.response.Response;

@FunctionalInterface
public interface Method {

    void accept(Response response);

}
