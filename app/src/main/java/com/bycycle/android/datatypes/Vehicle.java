package com.bycycle.android.datatypes;

import android.os.Parcel;
import android.os.Parcelable;

import com.bycycle.android.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ashish Kumar Khatri on 24/12/16.
 */

public class Vehicle implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.number);
        dest.writeSerializable(this.user);
        dest.writeParcelable(this.station, flags);
    }

    public Vehicle() {
    }

    protected Vehicle(Parcel in) {
        this.id = in.readString();
        this.number = in.readString();
        this.user = (UserInfo) in.readSerializable();
        this.station = in.readParcelable(Station.class.getClassLoader());
    }

    public static final Parcelable.Creator<Vehicle> CREATOR = new Parcelable.Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel source) {
            return new Vehicle(source);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };
}
