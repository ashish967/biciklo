package com.bycycle.android.apihandler;

import com.bycycle.android.datatypes.ApiStatus;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.datatypes.Vehicle;
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

public class GetVehiclesResult {

    ApiStatus status;

    ArrayList<Vehicle> data;

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }

    public ArrayList<Vehicle> getData() {
        return data;
    }

    public void setData(ArrayList<Vehicle> data) {
        this.data = data;
    }


    public static class ResultConverter implements Converter<ResponseBody,GetVehiclesResult> {


        @Override
        public GetVehiclesResult convert(ResponseBody value) throws IOException {

            JSONObject object;
            try {
                object= new JSONObject(value.string());
            } catch (JSONException e) {
                e.printStackTrace();;
                return null;

            }

            GetVehiclesResult result=new GetVehiclesResult();
            result.setStatus(ApiStatus.create(JsonUtils.getJSONObject(object,"status")));
            result.setData(Vehicle.create(JsonUtils.getJSONArray(object,"data")));
            return result;




        }
    }
}
