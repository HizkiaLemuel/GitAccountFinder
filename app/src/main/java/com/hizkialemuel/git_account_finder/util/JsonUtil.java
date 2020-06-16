package com.hizkialemuel.git_account_finder.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class JsonUtil {
    protected static Gson gson = new Gson();

    public static String toString(Object obj) {
        return gson.toJson(obj);
    }

    public static Object toObject(String jsonString, Object type) {
        jsonString = jsonString.replace("&nbsp;", "");
        jsonString = jsonString.replace("&amp;", "");

        if (type instanceof Type) {
            try {
                return gson.fromJson(jsonString, (Type) type);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        } else if (type instanceof Class<?>) {
            try {
                return gson.fromJson(jsonString, (Class<?>) type);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new RuntimeException("Only Class <?> Or TypeToken obtained by TypeToken");
        }
    }

    public static Object toObject(String jsonString, TypeToken type) {
        jsonString = jsonString.replace("&nbsp;", "");
        jsonString = jsonString.replace("&amp;", "");

        if (type instanceof Type) {
            try {
                return gson.fromJson(jsonString, (Type) type);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            throw new RuntimeException("Only Class <?> Or TypeToken obtained by TypeToken");
        }
    }
}
