package com.example.filedemo.util;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ResponseSettingUtil {
    
    public JSONObject responseSetter(Object data, boolean success,String message, int statusCode) {
//      System.out.println("Received Data : " + data.toString() + " " + success + " " + message);       
        JSONObject resp = new JSONObject();
        resp.put("data", data);
        resp.put("success", success);
        resp.put("message", message);
        resp.put("statusCode", statusCode);
        return resp;
    }

}
