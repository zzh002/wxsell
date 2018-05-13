package com.hnust.wxsell.utils;

import com.google.gson.*;

/**
 * Create by HJT
 * 2018/3/19 11:16
 **/
public class JsonUtil {

    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }

    public static String getKeyValue(String json,String key){
        JsonParser parse = new JsonParser();
        JsonElement element = parse.parse(json);
        JsonObject object = element.getAsJsonObject();
        String value = object.get(key).getAsString();
        return value;
    }

}
