package com.bycycle.android.datatypes;

import com.bycycle.android.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ashish Kumar Khatri on 24/12/16.
 */

public class Journey implements Serializable{

    String id,userId,bill;
    Vehicle vehicle;
    Station startStation,endStation;
    long startTime,endTime;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Station getStartStation() {
        return startStation;
    }

    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public void setEndStation(Station endStation) {
        this.endStation = endStation;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public static Journey create(JSONObject data) {

        if(data==null){
            return null;
        }

        Journey journey= new Journey();
        journey.setStartTime(JsonUtils.getLong(data,"start_time"));
        journey.setEndTime(JsonUtils.getLong(data,"end_time"));
        journey.setId(JsonUtils.getString(data,"id"));
        journey.setStartStation(Station.create(JsonUtils.getJSONObject(data,"start_station")));
        journey.setEndStation(Station.create(JsonUtils.getJSONObject(data,"end_station")));
        journey.setUserId(JsonUtils.getString(data,"userid"));
        journey.setVehicle(Vehicle.create(JsonUtils.getJSONObject(data,"vehicle")));
        journey.setBill(JsonUtils.getString(data,"bill"));

        return journey;
    }


    public static ArrayList<Journey> create(JSONArray array){

        if(array==null){
            new ArrayList<Journey>();
        }

        ArrayList<Journey> journeys= new ArrayList<>();
        for(int i=0;i<array.length();i++){

            Journey journey= create(JsonUtils.getJSONObject(array,i));
            if(journey!=null){
                journeys.add(journey);
            }
        }

        return journeys;
    }

}
