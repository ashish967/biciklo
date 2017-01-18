package com.bycycle.android.apihandler;

import com.bycycle.android.datatypes.ApiStatus;
import com.bycycle.android.datatypes.FileType;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.utils.JsonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Ashish Kumar Khatri on 29/12/16.
 */

public class FileUploadResult {

    ApiStatus status;

    FileType data;

    public FileType getData() {
        return data;
    }

    public void setData(FileType data) {
        this.data = data;
    }

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }

    public static class ResultConverter implements Converter<ResponseBody,FileUploadResult> {


        @Override
        public FileUploadResult convert(ResponseBody value) throws IOException {

            JSONObject object;
            try {
                object= new JSONObject(value.string());
            } catch (JSONException e) {
                e.printStackTrace();;
                return null;

            }

            FileUploadResult result=new FileUploadResult();
            result.setStatus(ApiStatus.create(JsonUtils.getJSONObject(object,"status")));
            result.setData(FileType.create(JsonUtils.getJSONObject(object,"data")));
            return result;




        }
    }
}
