package com.bycycle.android.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.bycycle.android.R;
import com.bycycle.android.apihandler.CreateStationResult;
import com.bycycle.android.databinding.ActivityCreateStationBinding;
import com.bycycle.android.datatypes.LocationType;
import com.bycycle.android.services.ApiRequestTask;
import com.bycycle.android.services.RetrofitApiService;
import com.bycycle.android.utils.AppUtils;
import com.bycycle.android.utils.Logger;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceTypes;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.location.places.AutocompleteFilter.TYPE_FILTER_GEOCODE;
import static com.google.android.gms.location.places.AutocompleteFilter.TYPE_FILTER_REGIONS;

public class CreateStationActivity extends LocationFetcherActivity implements OnMapReadyCallback {


    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    ActivityCreateStationBinding mBinding;

    LocationType mLocation;

    GoogleMap mMap;

    @Override
    public void getExtras() {

    }

    @Override
    public void initialize() {

        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_create_station);
    }

    @Override
    public void setup() {

        mBinding.btnSubmit.setOnClickListener(this);
        mBinding.etStationName.setOnClickListener(this);

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void updateLocation(LocationType mCurrentLocation) {

        mLocation= mCurrentLocation;
        getGeocodeAddress(mLocation);
        mBinding.etStationName.setText(mLocation.getAddress());
        onMapReady(mMap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_submit:
                 handleCreateStationAction();
                 break;

            case R.id.et_station_name:
                 handleStationNameAction();
                 break;
        }
    }

    private void handleStationNameAction() {

        try {

            AutocompleteFilter filter= new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT).build();
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    private void handleCreateStationAction() {


        if(mBinding.etStationName.getText().toString().isEmpty()){

            AppUtils.showToast(this,"Please enter station name");
            return;
        }


        LatLng latLng=mMap.getCameraPosition().target;

        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.CREATE_STATION
                , new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {

                        CreateStationResult result= (CreateStationResult) apiresult;

                        if(result.getStatus().isOk()){

                            AppUtils.showToast(CreateStationActivity.this,"Success "+result.getData().getId());
                            finish();
                        }
                        else{

                            AppUtils.showToast(CreateStationActivity.this,result.getStatus().getMsg());
                        }
                    }

                    @Override
                    public void onError(String result, Object tag) {

                        AppUtils.showToast(CreateStationActivity.this,result);
                    }
                },0);

        task.addParam("name",mBinding.etStationName.getText().toString());
        task.addParam("lat",""+latLng.latitude);
        task.addParam("lng",""+latLng.longitude);
        task.execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if(mLocation!=null) {


            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLat(),mLocation.getLng()),15f));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Logger.log("Place: " + place.toString());
                handlePlaceSuggestion(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                AppUtils.showToast(this,"Some Error Occurred");

                Logger.log("Status "+status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void handlePlaceSuggestion(Place place) {

        if(place.getLatLng()==null){

            AppUtils.showToast(this,"Some Error Occurred");
            return;
        }

        LocationType selectedLocation= new LocationType();
        selectedLocation.setLng(place.getLatLng().longitude);
        selectedLocation.setLat(place.getLatLng().latitude);
        selectedLocation.setAddress(place.getAddress()+"");
        updateLocation(selectedLocation);

    }

    private void getGeocodeAddress(LocationType locationType) {

        Geocoder geocoder= new Geocoder(this, Locale.getDefault());
        try {
            List<Address> list=geocoder.getFromLocation(locationType.getLat(),locationType.getLng(),1);

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
                    locationType.setAddress(addrTextFromAddressLines);
                }
                else {

                    locationType.setAddress(addrText);
                }
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
