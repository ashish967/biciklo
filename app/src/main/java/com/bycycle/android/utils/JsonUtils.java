package com.bycycle.android.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

public class JsonUtils {



    public static Object convertToObject(String json, Type type){

            return new Object();

    }


    public static String getString(JSONObject jsonObject, String key) {

        try {
            return  jsonObject.getString(key);
        } catch (JSONException e) {
        }

        return null;
    }

    public static long getLong(JSONObject jsonObject, String key) {

        try {
            return  jsonObject.getLong(key);
        } catch (JSONException e) {
        }

        return 0;
    }

    public static double getDouble(JSONObject jsonObject, String key) {

        try {
            return  jsonObject.getDouble(key);
        } catch (JSONException e) {
        }

        return 0;
    }



    public static JSONObject getJSONObject(JSONObject jsonObject, String key) {

        try {
            return  jsonObject.getJSONObject(key);
        } catch (JSONException e) {
        }

        return null;
    }

    public static JSONArray getJSONArray(JSONObject jsonObject, String key) {

        try {
            return  jsonObject.getJSONArray(key);
        } catch (JSONException e) {
        }

        return null;
    }

    public static JSONObject getJSONObject(JSONArray media_json, int i) {

        try {
            return media_json.getJSONObject(i);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return null;
    }

    public static int getInt(JSONObject jsonObject, String key) {

        try {
            return jsonObject.getInt(key);
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return 0;
    }

    public static String getString(JSONArray soryBy, int i) {
        try {
            return (String)soryBy.getJSONObject(i).toString();
        } catch (JSONException e) {
//            e.printStackTrace();
        }
        return null;
    }
}