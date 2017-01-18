package com.bycycle.android.datatypes;

import com.bycycle.android.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ashish Kumar Khatri on 24/12/16.
 */

public class Vehicle implements Serializable{

    private String id,number;

    private UserInfo user;

    private Station station;

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public static Vehicle create(JSONObject data) {

        if(data==null){
            return null;
        }

        Vehicle vehicle= new Vehicle();
        vehicle.setNumber(JsonUtils.getString(data,"number"));
        vehicle.setId(JsonUtils.getString(data,"id"));
        vehicle.setStation(Station.create(JsonUtils.getJSONObject(data,"station")));
        vehicle.setUser(UserInfo.create(JsonUtils.getJSONObject(data,"user")));

        return vehicle;
    }


    public static ArrayList<Vehicle> create(JSONArray array){

        if(array==null){
            new ArrayList<Station>();
        }

        ArrayList<Vehicle> vehicles= new ArrayList<>();
        for(int i=0;i<array.length();i++){

            Vehicle vehicle= create(JsonUtils.getJSONObject(array,i));
            if(vehicle!=null){
                vehicles.add(vehicle);
            }
        }

        return vehicles;
    }
}
