package com.axios.ajax;

import com.axios.Axios;
import com.axios.method.Method;
import com.axios.response.Response;

/**
 * [定义接口完成基础的ajax操作](Define interfaces to complete basic Ajax operations)
 * @description zh - 定义接口完成基础的ajax操作
 * @description en - Define interfaces to complete basic Ajax operations
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-10 21:46:44
 */
public interface Ajax {

    /**
     * [定义一个方法来具体实现方法](Define a method to implement the method)
     * @description zh - 定义一个方法来具体实现方法
     * @description en - Define a method to implement the method
     * @version V1.0
     * @author XiaoXunYao
     * @since 2021-11-10 21:48:06
     * @throws java.lang.Exception
     * @return com.xiaoHttp.response.Response
     */
    Response ajax() throws Exception;

    /**
     * [定义完成请求之后的操作](Define the action after completing the request)
     * @description zh - 定义完成请求之后的操作
     * @description en - Define the action after completing the request
     * @version V1.0
     * @author XiaoXunYao
     * @since 2021-11-10 21:48:59
     * @throws java.lang.Exception
     */
    default void then(Method accept) throws Exception{
        accept.accept(ajax());
    }
}
