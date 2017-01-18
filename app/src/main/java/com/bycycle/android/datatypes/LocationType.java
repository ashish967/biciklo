package com.bycycle.android.datatypes;

import java.io.Serializable;

/**
 * Created by Ashish Kumar Khatri on 16/12/16.
 */

public class LocationType  implements Serializable{

    double lat,lng;
    String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
