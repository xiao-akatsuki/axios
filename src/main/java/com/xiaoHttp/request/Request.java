package com.xiaoHttp.request;

import java.util.HashMap;
import java.util.Map;

/**
 * [请求参数](Request parameters)
 * @description zh - 请求参数
 * @description en - Request parameters
 * @version V1.0
 * @author XiaoXunYao
 * @since 2021-11-11 18:51:47
 */
public class Request {

    private Map<String,String> params = new HashMap<>();

    /**
     * [添加参数](Add parameter)
     * @description zh - 添加参数
     * @description en - Add parameter
     * @version V1.0
     * @author XiaoXunYao
     * @since 2021-11-11 18:52:12
     * @param key 键
     * @param value 值
     * @return com.xiaoHttp.request.Request
     */
    public Request add(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public Map<String,String> getParams(){
        return this.params;
    }

    /**
     * [将参数变成基础的get模式](Change the parameters to the basic get mode)
     * @description zh - 将参数变成基础的get模式
     * @description en - Change the parameters to the basic get mode
     * @version V1.0
     * @author XiaoXunYao
     * @since 2021-11-11 18:52:45
     * @return java.lang.String
     */
    public String toParam(){
        StringBuffer returnValue = new StringBuffer();
        this.params.forEach((key,value)->{
            returnValue.append(key + "=%27" + value +"%27");
        });
        return returnValue.toString();
    }

    /**
     * [将参数变成json类型](Change the parameter to JSON type)
     * @description zh - 将参数变成json类型
     * @description en - Change the parameter to JSON type
     * @version V1.0
     * @author XiaoXunYao
     * @since 2021-11-11 18:53:28
     * @return java.lang.String
     */
    public String toBody(){
        StringBuffer json = new StringBuffer();
        json.append("{");

        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            json.append("\"").append(key).append("\":\"").append(value).append("\",");
        }
        json.deleteCharAt(json.length()-1);
        json.append("}");

        return json.toString();
    }
    
}
