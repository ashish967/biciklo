package com.bycycle.android.apihandler;

import com.bycycle.android.datatypes.ApiStatus;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Ashish Kumar Khatri on 5/1/17.
 */

public class SimpleApiResult {

    ApiStatus status;

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }


    public static class ResultConverter implements Converter<ResponseBody,SimpleApiResult> {


        @Override
        public SimpleApiResult convert(ResponseBody value) throws IOException {

            JSONObject object;
            try {
                object= new JSONObject(value.string());
            } catch (JSONException e) {
                e.printStackTrace();;
                return null;

            }

            SimpleApiResult result=new SimpleApiResult();
            result.setStatus(ApiStatus.create(JsonUtils.getJSONObject(object,"status")));
            return result;




        }
    }
}
