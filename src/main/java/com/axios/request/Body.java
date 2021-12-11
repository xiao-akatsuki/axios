package com.axios.request;

import java.util.HashMap;
import java.util.Map;

public class Body {
	private Map<String,Object> body = new HashMap<>();

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

	public Map<String, Object> getBody() {
		return body;
	}

	public Body add(String key, Object value) {
        this.body.put(key, value);
        return this;
    }

	public String toBody(){
        StringBuffer json = new StringBuffer();
        json.append("{");

        for (Map.Entry<String, Object> entry : this.body.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            json.append("\"").append(key).append("\":\"").append(value).append("\",");
        }
        json.deleteCharAt(json.length()-1);
        json.append("}");

        return json.toString();
    }
}
