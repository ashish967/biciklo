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
import com.bycycle.android.adapter.VehiclesListAdapter;
import com.bycycle.android.apihandler.GetStationsResult;
import com.bycycle.android.apihandler.GetVehiclesResult;
import com.bycycle.android.databinding.ActivityVehiclesListBinding;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.datatypes.Vehicle;
import com.bycycle.android.services.ApiRequestTask;
import com.bycycle.android.services.RetrofitApiService;
import com.bycycle.android.utils.AppUtils;

import java.util.ArrayList;


public class VehiclesListActivity extends BaseActivity {


    ActivityVehiclesListBinding mBinding;

    boolean mSelectionMode;

    public static final String STATION_INFO="station_info";

    private ArrayList<Vehicle> mList = new ArrayList<>();

    Station mStation;

    public static Intent createIntent(Context context,@Nullable Station station){

        Intent intent= new Intent(context,VehiclesListActivity.class);
        intent.putExtra(STATION_INFO,station);
        return intent;
    }
    @Override
    public void getExtras() {

        mStation= (Station) getIntent().getSerializableExtra(STATION_INFO);
    }

    @Override
    public void initialize() {

        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_vehicles_list);
    }

    @Override
    public void setup() {

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(mStation!=null){
            mBinding.toolbar.setTitle("Vehicles of Station "+mStation.getName());
        }
        else{
            mBinding.toolbar.setTitle("Vehicles");
        }


        mBinding.vehicles.setLayoutManager(new LinearLayoutManager(this));
        mBinding.vehicles.setAdapter(new VehiclesListAdapter(this,mList));
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

        mBinding.vehicles.getAdapter().notifyDataSetChanged();;
    }
    private void getList() {

        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.GET_VEHICLES
                , new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {

                        GetVehiclesResult result= (GetVehiclesResult) apiresult;
                        hideRefreshLayout();

                        if(result.getStatus().isOk()){

                            mList.clear();
                            mList.addAll(result.getData());
                            notifyAdapter();
                        }
                        else{
                            AppUtils.showToast(VehiclesListActivity.this,result.getStatus().getMsg());
                        }
                    }

                    @Override
                    public void onError(String result, Object tag) {
                        AppUtils.showToast(VehiclesListActivity.this,result);
                        hideRefreshLayout();

                    }
                },0);

        if(mStation!=null){
            task.addParam("stationId",mStation.getId());
        }

        task.execute();
    }

    private void hideRefreshLayout() {

        mBinding.swipeRefreshLayout.setEnabled(true);
        mBinding.swipeRefreshLayout.setRefreshing(false);

    }

    public boolean isSelectionMode(){

        return mSelectionMode;
    }
    @Override
    public void onClick(View view) {

    }

    public void setStation(Vehicle mVehicle) {

        Intent intent= StationsListActivity.createIntent(this,mVehicle);
        startActivity(intent);
    }
}
