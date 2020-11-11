package com.gt.wan_gt.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用于操作Json字符串的工具类
 */
public class GsonTool {

    private Gson gson = null;

    private static GsonTool gsonTool = null;

    private GsonTool() {
        if (gson == null) {
            gson = new Gson();
        }
    }

    public static GsonTool getInstance() {
        if (gsonTool == null) {
            gsonTool = new GsonTool();
        }
        return gsonTool;
    }

    /**
     * 将Json字符串转换成Java对象
     *
     * @param <T>
     * @param json
     * @param clz
     * @return
     */
    public <T> T fromJsonToObject(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }

    public <T> T fromJsonToObject(InputStream json, Class<T> clz) {
        return gson.fromJson(new InputStreamReader(json), clz);
    }


    /**
     * 将Java对象转换成Json字符串
     *
     * @param obj
     * @return
     */
    public String fromObjectToJson(Object obj) {
        return gson.toJson(obj);
    }


    /**
     * json字符串转成list
     */
    public <T> List<T> jsonToList(String json, Class<T> cls) {
        ArrayList<T> mList = new ArrayList<T>();

        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            mList.add(gson.fromJson(elem, cls));
        }
        return mList;
    }
    /**
     * json字符串转成list中有map的
     */
    public <T> List<Map<String, T>> gsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    /**
     * json字符串转成map的
     */
    public <T> Map<String, T> gsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}
