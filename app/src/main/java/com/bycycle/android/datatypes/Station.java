package com.bycycle.android.datatypes;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.bycycle.android.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ashish Kumar Khatri on 21/12/16.
 */

public class Station implements Parcelable {

    String id;
    String name;
    double lat,lng;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Station create(JSONObject data) {

        if(data==null){
            return null;
        }

        Station station= new Station();
        station.setName(JsonUtils.getString(data,"name"));
        station.setId(JsonUtils.getString(data,"id"));
        station.setLat(JsonUtils.getDouble(data,"lat"));
        station.setLng(JsonUtils.getDouble(data,"lng"));

        return station;
    }


    public static ArrayList<Station> create(JSONArray array){

        if(array==null){
            new ArrayList<Station>();
        }

        ArrayList<Station> stations= new ArrayList<>();
        for(int i=0;i<array.length();i++){

            Station station= create(JsonUtils.getJSONObject(array,i));
            if(station!=null){
                stations.add(station);
            }
        }

        return stations;
    }

    public static ContentValues create(Station station) {

        ContentValues contentValues=new ContentValues();
        contentValues.put("id",station.getId());
        contentValues.put("name",station.getName());
        contentValues.put("lat",station.getLat());
        contentValues.put("lng",station.getLng());
        return contentValues;
    }

    public static Station getStation(ContentValues contentValues){

        Station station= new Station();
        station.setId(contentValues.getAsString("id"));
        station.setName(contentValues.getAsString("name"));
        station.setLat(contentValues.getAsDouble("lat"));
        station.setLng(contentValues.getAsDouble("lng"));
        return station;


    }
    public static Station getStation(Cursor contentValues){

        Station station= new Station();
        station.setId(contentValues.getString(0));
        station.setName(contentValues.getString(1));
        station.setLat(contentValues.getDouble(2));
        station.setLng(contentValues.getDouble(3));
        return station;


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lng);
    }

    public Station() {
    }

    protected Station(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.lat = in.readDouble();
        this.lng = in.readDouble();
    }

    public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel source) {
            return new Station(source);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };
}
