package com.bycycle.android.services;

import com.bycycle.android.utils.Logger;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Ashish Kumar Khatri on 19/12/16.
 */

public class JsonConverter extends Converter.Factory {


    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {


        try{
            Class<?> innerClass = Class.forName(((Class) type).getName()+"$ResultConverter");
            return (Converter<ResponseBody, ?>) innerClass.newInstance();
        }
        catch (Exception e){
            Logger.log(e.getMessage());
        }

        return new DefaultResultCovert(type);
    }


    public static class DefaultResultCovert implements Converter<ResponseBody, Object> {

        static Gson gson = new Gson();

        Type mType;

        public DefaultResultCovert(Type type) {

            mType = type;
        }

        @Override
        public Object convert(ResponseBody value) throws IOException {
            return gson.fromJson(value.string(), mType);
        }
    }

}
