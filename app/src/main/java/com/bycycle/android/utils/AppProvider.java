package com.bycycle.android.utils;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bycycle.android.BuildConfig;
import com.bycycle.android.datatypes.Station;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by Ashish Kumar Khatri on 14/1/17.
 */

public class AppProvider extends ContentProvider {


    static final String PROVIDER_NAME = BuildConfig.APPLICATION_ID;
    static final String URL = "content://" + PROVIDER_NAME + "/stations";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final UriMatcher uriMatcher;

    static final int STATIONS =1;
    static final int STATION_ID=2;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "stations", STATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "stations/#", STATION_ID);
    }

    public static Uri getStationUri(@Nullable  String stationId){

        if(stationId==null){

            return CONTENT_URI;
        }

        return Uri.parse(URL+"/"+stationId);

    }

    public static class DB{

        static String key="stations";
        ArrayList<Station> mStationsList;

        Context mContext;

        static DB mDb;

        public synchronized static DB get(Context context){

            if(mDb==null){
                init(context);
            }

            return mDb;

        }
        private synchronized static DB init(Context context){

            mDb=new DB();
            mDb.mContext=context;
            String data = AppUtils.getValue(key, context);
            if (data == null) {
                mDb.mStationsList = new ArrayList<>();
            } else {
                Logger.log("data: "+data);
                mDb.mStationsList = (ArrayList<Station>) AppUtils.convertFromJson(data, new TypeToken<ArrayList<Station>>() {
                }.getType());
            }
            return mDb;

        }
        public void add(Station station){


            for(int i=0;i<mStationsList.size();){

                if(mStationsList.get(i).getId().equals(station.getId())){

                    mStationsList.remove(i);
                    continue;
                }
                i++;
            }

            mStationsList.add(station);
            AppUtils.saveValue(key,AppUtils.convertToJson(mStationsList),mContext);

        }


        public Cursor getStations(String id){

            String[] menuCols = new String[] { "id", "name", "lat","lng" };

            MatrixCursor cursor=new MatrixCursor(menuCols);

            for(int i=0;i<mStationsList.size();i++){

                if(id==null){
                    cursor.addRow(new Object[]{mStationsList.get(i).getId(),mStationsList.get(i).getName(),mStationsList.get(i).getLat(),mStationsList.get(i).getLng()});
                }
                else if(id.equals(mStationsList.get(i).getId())){
                    cursor.addRow(new Object[]{mStationsList.get(i).getId(),mStationsList.get(i).getName(),mStationsList.get(i).getLat(),mStationsList.get(i).getLng()});
                }
            }

            return cursor;
        }


        public void add(ArrayList<Station> stations) {


            for(int i=0;i<mStationsList.size();){

                boolean match=false;
                for(int j=0;j<stations.size();j++) {

                    if (mStationsList.get(i).getId().equals(stations.get(j).getId())) {
                        mStationsList.remove(i);
                        match=true;
                    }
                }
                if(!match) {
                    i++;
                }

            }

            mStationsList.addAll(stations);
            AppUtils.saveValue(key,AppUtils.convertToJson(mStationsList),mContext);


        }
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {


        if(uriMatcher.match(uri)==STATIONS){
            Logger.log("get stations");
            return DB.get(getContext()).getStations(null);
        }
        else if(uriMatcher.match(uri)==STATION_ID){
            Logger.log("get station "+uri.getLastPathSegment());
            return DB.get(getContext()).getStations(uri.getLastPathSegment());
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            /**
             * Get all student records
             */
            case STATIONS:
                return "vnd.android.cursor.dir/vnd.example.station";
            /**
             * Get a particular student
             */
            case STATION_ID:
                return "vnd.android.cursor.item/vnd.example.stations";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        if(uriMatcher.match(uri)==STATION_ID){
            Station station= Station.getStation(contentValues);
            DB.get(getContext()).add(station);
            return uri;

        }
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        if(uriMatcher.match(uri)==STATIONS){

            ArrayList<Station> stations= new ArrayList<>();
            for(int i=0;i<values.length;i++){
                stations.add(Station.getStation(values[i]));
            }
            DB.get(getContext()).add(stations);

        }

        return values.length;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
