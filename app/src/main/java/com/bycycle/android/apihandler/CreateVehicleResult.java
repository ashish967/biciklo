package com.bycycle.android.apihandler;

import com.bycycle.android.datatypes.ApiStatus;
import com.bycycle.android.datatypes.UserInfo;
import com.bycycle.android.datatypes.Vehicle;
import com.bycycle.android.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Ashish Kumar Khatri on 24/12/16.
 */

public class CreateVehicleResult {

    ApiStatus status;

    Vehicle data;

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }

    public Vehicle getData() {
        return data;
    }

    public void setData(Vehicle data) {
        this.data = data;
    }

    public static class ResultConverter implements Converter<ResponseBody,CreateVehicleResult> {


        @Override
        public CreateVehicleResult convert(ResponseBody value) throws IOException {

            JSONObject object;
            try {
                object= new JSONObject(value.string());
            } catch (JSONException e) {
                e.printStackTrace();;
                return null;

            }

            CreateVehicleResult result=new CreateVehicleResult();
            result.setStatus(ApiStatus.create(JsonUtils.getJSONObject(object,"status")));
            result.setData(Vehicle.create(JsonUtils.getJSONObject(object,"data")));
            return result;




        }
    }
}
