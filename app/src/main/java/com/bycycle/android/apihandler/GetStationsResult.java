package com.bycycle.android.apihandler;

import com.bycycle.android.datatypes.ApiStatus;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Ashish Kumar Khatri on 21/12/16.
 */

public class GetStationsResult {

    ApiStatus status;

    ArrayList<Station> data;

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }

    public ArrayList<Station> getData() {
        return data;
    }

    public void setData(ArrayList<Station> data) {
        this.data = data;
    }


    public static class ResultConverter implements Converter<ResponseBody,GetStationsResult> {


        @Override
        public GetStationsResult convert(ResponseBody value) throws IOException {

            JSONObject object;
            try {
                object= new JSONObject(value.string());
            } catch (JSONException e) {
                e.printStackTrace();;
                return null;

            }

            GetStationsResult result=new GetStationsResult();
            result.setStatus(ApiStatus.create(JsonUtils.getJSONObject(object,"status")));
            result.setData(Station.create(JsonUtils.getJSONArray(object,"data")));
            return result;




        }
    }
}
