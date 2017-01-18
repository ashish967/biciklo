package com.bycycle.android.datatypes;

import com.bycycle.android.utils.JsonUtils;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ashish Kumar Khatri on 19/12/16.
 */

public class UserInfo implements Serializable{

    String name;
    String id;
    String phoneNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static UserInfo create(JSONObject data) {

        if(data==null){
            return null;
        }

        UserInfo info= new UserInfo();
        info.setName(JsonUtils.getString(data,"name"));
        info.setId(JsonUtils.getString(data,"id"));
        info.setPhoneNumber(JsonUtils.getString(data,"phonenumber"));

        return info;
    }
}
