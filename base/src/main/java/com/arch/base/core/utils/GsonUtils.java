// -*- Mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-

package com.arch.base.core.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonUtils {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    private static final Gson sLocalGson = createLocalGson();
    private static final Gson sRemoteGson = createRemoteGson();

    private static GsonBuilder createLocalGsonBuilder() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        gsonBuilder.setDateFormat(DATE_FORMAT);
        return gsonBuilder;
    }

    private static Gson createLocalGson() {
        return createLocalGsonBuilder().create();
    }

    private static Gson createRemoteGson() {
        return createLocalGsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    public static Gson getLocalGson() {
        return sLocalGson;
    }

    public static <T> T fromLocalJson(String json, Class<T> clazz) throws JsonSyntaxException {
        try {
            return sLocalGson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static <T> T fromLocalJson(String json, Type typeOfT) {
        return sLocalGson.fromJson(json, typeOfT);
    }

    /**
     * 转成list
     * 解决泛型问题
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> JsonToList(String json, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for(final JsonElement elem : array){
            list.add(sLocalGson.fromJson(elem, cls));
        }
        return list;
    }


    public static String toJson(Object src) {
        return sLocalGson.toJson(src);
    }

    public static String toRemoteJson(Object src) {
        return sRemoteGson.toJson(src);
    }

}
