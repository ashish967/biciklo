package com.bycycle.android.apihandler;

import com.bycycle.android.datatypes.ApiStatus;
import com.bycycle.android.datatypes.Journey;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Ashish Kumar Khatri on 24/12/16.
 */

public class StartJourneyResult{

    ApiStatus status;

    Journey data;

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }

    public Journey getData() {
        return data;
    }

    public void setData(Journey data) {
        this.data = data;
    }

    public static class ResultConverter implements Converter<ResponseBody,StartJourneyResult> {


        @Override
        public StartJourneyResult convert(ResponseBody value) throws IOException {

            JSONObject object;
            try {
                object= new JSONObject(value.string());
            } catch (JSONException e) {
                e.printStackTrace();;
                return null;

            }

            StartJourneyResult result=new StartJourneyResult();
            result.setStatus(ApiStatus.create(JsonUtils.getJSONObject(object,"status")));
            result.setData(Journey.create(JsonUtils.getJSONObject(object,"data")));
            return result;




        }
    }
}
