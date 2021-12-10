package com.axios.method;

import com.axios.response.Response;

/**
 * [定义请求结束之后的操作](Define the action after the request ends)
 * @description zh - 定义请求结束之后的操作
 * @description en - Define the action after the request ends
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-10 21:51:34
 */
@FunctionalInterface
public interface Method {

    /**
     * [之后的操作](Subsequent operations)
     * @description zh - 之后的操作
     * @description en - Subsequent operations
     * @version V1.0
     * @author XiaoXunYao
     * @since 2021-11-10 21:52:10
     * @param response 请求结束之后的参数
     */
    void accept(String response);

}
