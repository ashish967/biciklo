package com.bycycle.android.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.bycycle.android.R;
import com.bycycle.android.databinding.ActivityAdminPanelBinding;


public class AdminPanelActivity extends BaseActivity {

    ActivityAdminPanelBinding mBinding;
    @Override
    public void getExtras() {

    }

    @Override
    public void initialize() {

        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_admin_panel);
    }

    @Override
    public void setup() {

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBinding.btnCreateStation.setOnClickListener(this);
        mBinding.btnCreateVehical.setOnClickListener(this);
        mBinding.btnVehicleList.setOnClickListener(this);
        mBinding.btnStationList.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_create_station:
                 startActivity(new Intent(this,CreateStationActivity.class));
                 break;

            case R.id.btn_create_vehical:
                startActivity(new Intent(this,CreateVehicleActivity.class));
                break;

            case R.id.btn_station_list:
                startActivity(new Intent(this,StationsListActivity.class));
                break;

            case R.id.btn_vehicle_list:
                startActivity(VehiclesListActivity.createIntent(this,null));
                break;

        }
    }
}
