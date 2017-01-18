package com.bycycle.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bycycle.android.application.BycycleApplication;
import com.bycycle.android.datatypes.Journey;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.datatypes.UserInfo;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by Ashish Kumar Khatri on 16/12/16.
 */

public class AppUtils {

    public static boolean isLocationEnabled(Context context) {

        LocationManager manager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        boolean isGpsEnabled = false, isNetworkEnabled = false;
        try {
            isGpsEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        } catch (Exception ex) {
        }

        return (isGpsEnabled && isNetworkEnabled);
    }

    public static void setWindowFullScreen(Window windowFullScreen) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            windowFullScreen.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            windowFullScreen.setStatusBarColor(Color.TRANSPARENT);
        } else {
            windowFullScreen.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public static void setTypeFace(View v){

        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setTypeFace(child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Montserrat-Light.otf"));
            }
        } catch (Exception e) {
            Logger.log(e.getMessage());
        }
    }



    public void setWeightCondesed(TextView v){

        v.setTypeface(Typeface.createFromAsset(v.getContext().getAssets(), "fonts/Montserrat-Medium.otf"));
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            activity.getWindow().setStatusBarColor(color);
        } else return;
    }

    static UserInfo mUserInfo;

    public static void saveValue(String key, String value, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(AppConstants.APP_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, (value));
        editor.commit();
    }

    public static String getValue(String key,Context context){
        SharedPreferences sharedpreferences = context.getSharedPreferences(AppConstants.APP_PREFS, Context.MODE_PRIVATE);
        return sharedpreferences.getString(key,null);

    }

    static Gson mGson= new Gson();

    public static String convertToJson(Object object){

        if(object==null){
            return null;
        }
        return mGson.toJson(object);
    }

    public static Object convertFromJson(String json,Type type){

        return mGson.fromJson(json,type);
    }

    public static void showToast(Context context,String message){

        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidPhoneNumber(String phone){
        return (android.util.Patterns.PHONE.matcher(phone).matches() && phone.length()==10);
    }

    public static UserInfo getUserInfo() {

        if(mUserInfo==null){

            String user= getValue(AppConstants.USER_INFO, BycycleApplication.mContext);

            if(user==null){
                return null;
            }

            mUserInfo= (UserInfo) convertFromJson(user,UserInfo.class);
        }

        return mUserInfo;

    }

    public static void saveJourney(Context context,Journey journey, Station station){

        saveValue(AppConstants.JOURNEY_INFO,convertToJson(journey),context);
        saveValue(AppConstants.STATION_INFO,convertToJson(station),context);
    }

    public static Journey getJourney(Context context){

        String journey=getValue(AppConstants.JOURNEY_INFO,context);

        if(journey==null){
            return null;
        }

        return (Journey) convertFromJson(journey,Journey.class);
    }


    public static Station getStationInfo(Context context){

        String station=getValue(AppConstants.STATION_INFO,context);

        if(station==null){
            return null;
        }

        return (Station) convertFromJson(station,Station.class);
    }

    public static void saveUserInfo(UserInfo data,Context context) {

        mUserInfo= data;
        saveValue(AppConstants.USER_INFO,convertToJson(data),context);

    }


    public static Uri parse(String url) {
        try{
            return Uri.parse(url);
        }
        catch (Exception e){

        }
        return null;
    }
}
