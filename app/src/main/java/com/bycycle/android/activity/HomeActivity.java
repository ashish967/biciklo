package com.bycycle.android.activity;

import android.app.LoaderManager;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.View;

import com.bycycle.android.R;
import com.bycycle.android.apihandler.CreateUserResult;
import com.bycycle.android.apihandler.GetStationsResult;
import com.bycycle.android.apihandler.StartJourneyResult;
import com.bycycle.android.databinding.ActivityMainBinding;
import com.bycycle.android.datatypes.Journey;
import com.bycycle.android.datatypes.LocationType;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.services.ApiRequestTask;
import com.bycycle.android.services.RetrofitApiService;
import com.bycycle.android.services.SyncService;
import com.bycycle.android.utils.AppProvider;
import com.bycycle.android.utils.AppUtils;
import com.bycycle.android.utils.Logger;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.bycycle.android.R.id.stations;

public class HomeActivity extends LocationFetcherActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    LocationType mLocation;

    Marker mMarker;

    ActivityMainBinding mBinding;

    Station mStartStation,mEndStation;

    Journey mCurrentJourney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void getExtras() {

        mStartStation= AppUtils.getStationInfo(this);
        mCurrentJourney= AppUtils.getJourney(this);

    }

    @Override
    public void initialize() {

        AppUtils.setStatusBarColor(this, Color.BLACK);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void setup() {

        Logger.log(AppUtils.getUserInfo().getName());
        mBinding.includeLeftMenu.tvName.setText(AppUtils.getUserInfo().getName());
        mBinding.includeLeftMenu.tvAdminPanel.setOnClickListener(this);
        mBinding.ivMenu.setOnClickListener(this);
        mBinding.tvRequestRide.setOnClickListener(this);
        mBinding.includeLeftMenu.tvRides.setOnClickListener(this);
        mBinding.includeLeftMenu.tvVerify.setOnClickListener(this);
        mBinding.includeLeftMenu.tvAbout.setOnClickListener(this);
        mBinding.includeLeftMenu.tvPayment.setOnClickListener(this);
        updateActionButtons();

        getUserInfo();


    }

    private void getUserInfo(){

        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.GET_USER_INFO
                , new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {


                        CreateUserResult result= (CreateUserResult) apiresult;
                        if(!result.getStatus().isOk()){

                        }
                        else{
                            AppUtils.saveUserInfo(result.getData(),HomeActivity.this);
                            mBinding.includeLeftMenu.tvName.setText(AppUtils.getUserInfo().getName());

                        }
                        Logger.log("Result"+apiresult);
                    }



                    @Override
                    public void onError(String result, Object tag) {

                    }
                },0);


        task.execute();

    }
    @Override
    protected void onStart() {
        super.onStart();

        startService(new Intent(this, SyncService.class));

    }

    private void getStations() {




        getSupportLoaderManager().initLoader(0, null, new android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                return   new CursorLoader(getApplicationContext(), AppProvider.getStationUri(null),null,null,null,null);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

                updateMap(data);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {

            }
        });

    }

    ArrayList<Marker> markers=new ArrayList<>();

    private void updateMap(Cursor data) {


        for(int i=0;i<markers.size();i++){
            markers.get(i).remove();
        }

        markers.clear();
        data.moveToFirst();
        for(int i=0;i<data.getCount();i++){


            Station station= Station.getStation(data);
            if(mMap!=null) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(station.getLat(), station.getLng()))
                        .title(station.getName()));
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.station)));

                marker.setTag(station);

                markers.add(marker);

                data.moveToNext();
            }

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Station station= (Station) marker.getTag();
                handleMarkerTap(station);
                return true;
            }
        });

        if(mLocation!=null) {
            /*if(mMarker ==null) {
                this.mMarker =mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mLocation.getLat(), mLocation.getLng())));
            }
            else{
                mMarker.setPosition(new LatLng(mLocation.getLat(), mLocation.getLng()));
            }
*/
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLat(), mLocation.getLng()),15f));
        }

        getStations();

    }



    @Override
    protected void updateLocation(LocationType mCurrentLocation) {

        mLocation=mCurrentLocation;
        geoCodeLocation();
        onMapReady(mMap);
    }

    private void geoCodeLocation() {

        Geocoder geocoder= new Geocoder(this, Locale.getDefault());
        try {
            List<Address> list=geocoder.getFromLocation(mLocation.getLat(),mLocation.getLng(),1);

            if(list!=null&&list.size()>0){

                Address address=list.get(0);
                Logger.log(address.toString());
                String addrText=address.getLocality()+", "+address.getSubLocality();
                String addrTextFromAddressLines="";

                if(address.getAddressLine(0)!=null){
                    addrTextFromAddressLines+=address.getAddressLine(0);
                }

                if(address.getAddressLine(1)!=null){
                    addrTextFromAddressLines+=", "+address.getAddressLine(1);
                }
                if(!addrTextFromAddressLines.isEmpty()){
                    mBinding.tvCurrentLocation.setText(addrTextFromAddressLines);
                }
                else {
                    mBinding.tvCurrentLocation.setText(addrText);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.iv_menu:
                mBinding.drawerLayout.openDrawer(Gravity.LEFT);
                break;


            case R.id.self_view:
                startActivity(new Intent(this,SignupActivity.class));
                break;

            case R.id.tv_admin_panel:
                startActivity(new Intent(this,AdminPanelActivity.class));
                break;

            case R.id.tv_request_ride:
                handleRequestRideAction();
                break;

            case R.id.tv_rides:
                startActivity(new Intent(this,JourneysActivity.class));
                break;

            case R.id.tv_verify:
                startActivity(new Intent(this,VerifyKYCActivity.class));
                break;

            case R.id.tv_about:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;

            case R.id.tv_payment:
                AppUtils.showToast(this,"This option not enabled for simulation");
                break;

        }
    }

    private void handleMarkerTap(Station station) {


        if(mCurrentJourney==null) {
            mStartStation = station;
        }
        else{
            mEndStation =station;
        }

        updateActionButtons();
    }

    private void updateActionButtons() {

        if(mStartStation==null){

            mBinding.tvJourneyMessage.setVisibility(View.GONE);
            mBinding.tvRequestRide.setText("Request A Ride");

            return;

        }


        if(mStartStation!=null&&mCurrentJourney!=null){
            mBinding.tvRequestRide.setText("End Ride");
        }
        else{
            mBinding.tvRequestRide.setText("Request A Ride");
        }
        String message="Riding from : "+mStartStation.getName();

        if(mEndStation!=null){
            message+="\nRide ends at : "+mEndStation.getName();
        }

        mBinding.tvJourneyMessage.setText(message);
        mBinding.tvJourneyMessage.setVisibility(View.VISIBLE);
    }

    private void handleRequestRideAction() {




        if(mCurrentJourney!=null){

            stopJourney();
        }
        else{

            startJourney();
        }




    }

    private void startJourney() {

        if(mStartStation ==null){

            AppUtils.showToast(this,"Please tap on marker to select station");
            return;
        }



        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.START_JOURNEY
                , new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {

                        StartJourneyResult result= (StartJourneyResult) apiresult;

                        if(result.getStatus().isOk()){

                            AppUtils.showToast(HomeActivity.this,"Your journey has started");
                            mCurrentJourney= result.getData();
                            AppUtils.saveJourney(HomeActivity.this,mCurrentJourney,mStartStation);
                            updateActionButtons();
                        }
                        else{
                            AppUtils.showToast(HomeActivity.this,result.getStatus().getMsg());
                        }


                    }

                    @Override
                    public void onError(String result, Object tag) {

                        AppUtils.showToast(HomeActivity.this,result);
                    }

                },0);

        task.addParam("startStationId", mStartStation.getId());
        task.execute();;

    }

    private void stopJourney() {


        if(mEndStation ==null){

            AppUtils.showToast(this,"Please tap on marker to stop at station");
            return;
        }



        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.STOP_JOURNEY
                , new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {

                        StartJourneyResult result= (StartJourneyResult) apiresult;

                        if(result.getStatus().isOk()){

                            AppUtils.showToast(HomeActivity.this,"Your journey Ends");
                            AppUtils.saveJourney(HomeActivity.this,null,null);
                            mStartStation=null;
                            mEndStation=null;
                            mCurrentJourney=null;
                            updateActionButtons();
                        }
                        else{
                            AppUtils.showToast(HomeActivity.this,result.getStatus().getMsg());
                        }


                    }

                    @Override
                    public void onError(String result, Object tag) {

                        AppUtils.showToast(HomeActivity.this,result);
                    }

                },0);

        task.addParam("endStationId", mEndStation.getId());
        task.execute();;


    }
}
