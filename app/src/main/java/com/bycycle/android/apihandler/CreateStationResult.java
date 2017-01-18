package com.bycycle.android.apihandler;

import com.bycycle.android.datatypes.ApiStatus;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.datatypes.UserInfo;
import com.bycycle.android.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Ashish Kumar Khatri on 20/12/16.
 */

public class CreateStationResult {

    ApiStatus status;

    Station data;

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }

    public Station getData() {
        return data;
    }

    public void setData(Station data) {
        this.data = data;
    }

    public static class ResultConverter implements Converter<ResponseBody,CreateStationResult> {


        @Override
        public CreateStationResult convert(ResponseBody value) throws IOException {

            JSONObject object;
            try {
                object= new JSONObject(value.string());
            } catch (JSONException e) {
                e.printStackTrace();;
                return null;

            }

            CreateStationResult result=new CreateStationResult();
            result.setStatus(ApiStatus.create(JsonUtils.getJSONObject(object,"status")));
            result.setData(Station.create(JsonUtils.getJSONObject(object,"data")));
            return result;




        }
    }
}
