package com.bycycle.android.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bycycle.android.R;
import com.bycycle.android.adapter.StationsListAdapter;
import com.bycycle.android.apihandler.CreateVehicleResult;
import com.bycycle.android.apihandler.GetStationsResult;
import com.bycycle.android.apihandler.GetVehiclesResult;
import com.bycycle.android.databinding.ActivityStationsListBinding;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.datatypes.Vehicle;
import com.bycycle.android.services.ApiRequestTask;
import com.bycycle.android.services.RetrofitApiService;
import com.bycycle.android.utils.AppUtils;
import com.bycycle.android.utils.Logger;

import java.util.ArrayList;


public class StationsListActivity extends BaseActivity {

    public static final String VEHICLE_INFO="vehicle_info";


    Vehicle mVehicle;

    ActivityStationsListBinding mBinding;

    ArrayList<Station> mList=new ArrayList<>();


    public static Intent createIntent(Context context,@Nullable Vehicle mVehicle) {

        Intent intent= new Intent(context,StationsListActivity.class);
        intent.putExtra(VEHICLE_INFO,mVehicle);
        return intent;
    }

    @Override
    public void getExtras() {

        mVehicle= (Vehicle) getIntent().getParcelableExtra(VEHICLE_INFO);
    }

    @Override
    public void initialize() {

        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_stations_list);
    }

    @Override
    public void setup() {
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(mVehicle!=null){
            mBinding.toolbar.setTitle("Select Station for Vehicle "+mVehicle.getNumber());
        }
        else{
            mBinding.toolbar.setTitle("Stations");
        }

        mBinding.stations.setLayoutManager(new LinearLayoutManager(this));
        mBinding.stations.setAdapter(new StationsListAdapter(this,mList));

        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
                mBinding.swipeRefreshLayout.setEnabled(false);

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        getList();

    }

    private void notifyAdapter(){

        mBinding.stations.getAdapter().notifyDataSetChanged();;
    }


    private void hideRefreshLayout() {

        mBinding.swipeRefreshLayout.setEnabled(true);
        mBinding.swipeRefreshLayout.setRefreshing(false);

    }

    private void getList() {

        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.GET_STATIONS
                , new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {

                        GetStationsResult result= (GetStationsResult) apiresult;
                        hideRefreshLayout();

                        if(result.getStatus().isOk()){

                            mList.clear();
                            mList.addAll(result.getData());
                            notifyAdapter();
                        }
                        else{
                            AppUtils.showToast(StationsListActivity.this,result.getStatus().getMsg());
                        }
                    }

                    @Override
                    public void onError(String result, Object tag) {
                        AppUtils.showToast(StationsListActivity.this,result);
                        hideRefreshLayout();

                    }
                },0);



        task.execute();
    }

    @Override
    public void onClick(View view) {

    }

    public void handleAction(Station mStation) {


        if(!selectionMode()){

            Intent intent= VehiclesListActivity.createIntent(this,mStation);
            startActivity(intent);
            return;

        }



        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.SET_STATION
                , new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {

                        CreateVehicleResult result= (CreateVehicleResult) apiresult;
                        hideRefreshLayout();

                        if(result.getStatus().isOk()){
                            AppUtils.showToast(StationsListActivity.this,result.getStatus().getMsg());
                        }
                        else{
                            AppUtils.showToast(StationsListActivity.this,result.getStatus().getMsg());
                        }

                        finish();
                    }

                    @Override
                    public void onError(String result, Object tag) {
                        AppUtils.showToast(StationsListActivity.this,result);
                        hideRefreshLayout();

                    }
                },0);


        task.addParam("stationId",mStation.getId());
        task.addParam("vehicleId",mVehicle.getId());
        Logger.log("Vehicle Id "+mVehicle.getId()+" Station Id "+mStation.getId());
        task.execute();
    }

    public boolean selectionMode() {

        return mVehicle!=null;
    }
}
