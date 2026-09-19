package com.yusuf.yusufmart.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Utility for JSON serialization and deserialization using Gson.
 */
public class JsonUtil {

    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .serializeNulls()
            .create();

    private JsonUtil() {
    }

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    public static Gson getGson() {
        return GSON;
    }
}
