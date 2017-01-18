package com.bycycle.android.apihandler;

import com.bycycle.android.datatypes.ApiStatus;
import com.bycycle.android.datatypes.Journey;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Ashish Kumar Khatri on 24/12/16.
 */

public class GetJourneysResult {

    ApiStatus status;

    ArrayList<Journey> data;

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }

    public ArrayList<Journey> getData() {
        return data;
    }

    public void setData(ArrayList<Journey> data) {
        this.data = data;
    }


    public static class ResultConverter implements Converter<ResponseBody,GetJourneysResult> {


        @Override
        public GetJourneysResult convert(ResponseBody value) throws IOException {

            JSONObject object;
            try {
                object= new JSONObject(value.string());
            } catch (JSONException e) {
                e.printStackTrace();;
                return null;

            }

            GetJourneysResult result=new GetJourneysResult();
            result.setStatus(ApiStatus.create(JsonUtils.getJSONObject(object,"status")));
            result.setData(Journey.create(JsonUtils.getJSONArray(object,"data")));
            return result;




        }
    }
}
