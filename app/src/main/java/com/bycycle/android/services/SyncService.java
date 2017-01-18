package com.bycycle.android.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.bycycle.android.apihandler.GetStationsResult;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.utils.AppProvider;
import com.bycycle.android.utils.Logger;

import java.util.ArrayList;

/**
 * Created by Ashish Kumar Khatri on 14/1/17.
 */

public class SyncService extends IntentService {


    public SyncService(){
        this(SyncService.class.getSimpleName());
    }
    public SyncService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Uri uri= AppProvider.getStationUri(null);

        Cursor cursor= getContentResolver().query(uri,null,null,null,null);

        while (cursor.moveToNext()){
            Logger.log("Cursor "+cursor.getString(0)+","+cursor.getString(1));
        }

        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.GET_STATIONS,
                new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {

                        GetStationsResult result= (GetStationsResult) apiresult;

                        if(result.getStatus().isOk()){

                           save(result.getData());
                        }
                    }

                    @Override
                    public void onError(String result, Object tag) {

                    }
                },0);
        task.execute();


    }

    private void save(ArrayList<Station> data) {

        Uri uri= AppProvider.getStationUri(null);

        ContentValues[] values= new ContentValues[data.size()];

        int i=0;
        for(Station station:data){
            values[i]=Station.create(station);
            i++;
        }
        getContentResolver().bulkInsert(uri,values);

    }
}
