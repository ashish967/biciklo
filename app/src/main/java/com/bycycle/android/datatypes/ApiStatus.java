package com.bycycle.android.datatypes;

import com.bycycle.android.utils.JsonUtils;

import org.json.JSONObject;

/**
 * Created by Ashish Kumar Khatri on 19/12/16.
 */

public class ApiStatus {

    String msg;
    int status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isOk(){

        return status==1;
    }

    public static ApiStatus create(JSONObject object) {

        if(object==null)
        return null;

        ApiStatus status= new ApiStatus();
        status.setMsg(JsonUtils.getString(object,"msg"));
        status.setStatus(JsonUtils.getInt(object,"status"));

        return status;

    }
}
