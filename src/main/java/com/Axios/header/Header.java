package com.Axios.header;

import java.util.HashMap;
import java.util.Map;

/**
 * [请求头](Request header)
 * @description zh - 请求头
 * @description en - Request header
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-09 21:42:54
 */
public class Header {

    private Map<String,String> headers = new HashMap<>();

    /**
     * [添加参数](Add parameter)
     * @description zh - 添加参数
     * @description en - Add parameter
     * @version V1.0
     * @author XiaoXunYao
     * @since 2021-11-09 21:43:19
     * @param key 键
     * @param value 值
     * @return com.xiaoHttp.header.Header
     */
    public Header add(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    /**
     * [获取header](Get header)
     * @description zh - 获取header
     * @description en - Get header
     * @version V1.0
     * @author XiaoXunYao
     * @since 2021-11-09 21:45:35
     * @param java.util.Map<java.lang.String, java.lang.String>
     */
    public Map<String,String> getHeaders(){
        return this.headers;
    }

}
