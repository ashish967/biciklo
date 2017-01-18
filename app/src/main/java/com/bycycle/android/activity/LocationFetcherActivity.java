package com.bycycle.android.activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bycycle.android.datatypes.LocationType;
import com.bycycle.android.utils.AppUtils;
import com.bycycle.android.utils.Logger;
import com.bycycle.android.utils.MPermissionManager;
import com.bycycle.android.widget.PermissionDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

/**
 * Created by Ashish Kumar Khatri on 16/12/16.
 */

public abstract class LocationFetcherActivity extends BaseActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final int LOCATION_UPDATE = 1000;

    private static final int GO_SETTINGS = 1001;
    private static final int REQUEST_CHECK_SETTINGS = 22;

    protected GoogleApiClient mGoogleApiClient;

    Location mLocation;

    LocationType mCurrentLocation;

    LocationRequest mLocationRequest;



    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;


    protected abstract void updateLocation(LocationType mCurrentLocation);


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        createLocationRequest();

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        startLocationUpdates();

    }

    protected void startLocationUpdates() {
        Logger.log("Location enabled: " + AppUtils.isLocationEnabled(this));


        if(mGoogleApiClient!=null&&mGoogleApiClient.isConnected()) {

            MPermissionManager.askForPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, new MPermissionManager.InteractionListener() {
                @Override
                public void showRationaleUI(String[] permissions, int[] grantResults) {

                    showPermissionDialog("Location permission is required to continue",LOCATION_UPDATE);
                }

                @Override
                public void permissionDenied(String[] permissions, int[] grantResults) {

                    showPermissionDialog("Location permission is required to continue",GO_SETTINGS);
                }

                @Override
                public void permissionGranted(String[] permissions) {


                    handleLocationPermissionGranted();
                }
            });


        }

    }

    private void handleLocationPermissionGranted() {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {

                final Status status = result.getStatus();
                final LocationSettingsStates  states= result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                mGoogleApiClient, mLocationRequest, LocationFetcherActivity.this);
                        Logger.log("Location update started");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    LocationFetcherActivity.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }
            }
        }) ;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        handleLocationPermissionGranted();
    }

    private void showPermissionDialog(String s, int code) {

        PermissionDialog permissionDialog= new PermissionDialog(this, s, new PermissionDialog.InteractionListener() {
            @Override
            public void onCancel() {

                finish();

            }

            @Override
            public void reRequest(int code) {

                if(code==GO_SETTINGS){
                    MPermissionManager.startPermissionDetailActivity(LocationFetcherActivity.this);
                }
                else {
                    startLocationUpdates();
                }
            }
        },code);

        permissionDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

        mLocation= location;

        if(mLocation!=null){

            mCurrentLocation= new LocationType();
            mCurrentLocation.setLat(location.getLatitude());
            mCurrentLocation.setLng(location.getLongitude());

            updateLocation(mCurrentLocation);

            stopLocationUpdates();
        }
    }

    protected void stopLocationUpdates() {


        if(mGoogleApiClient!=null&&
                mGoogleApiClient.isConnected()) {

            Logger.log("Location update stopped");
            MPermissionManager.askForPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, new MPermissionManager.InteractionListener() {
                @Override
                public void showRationaleUI(String[] permissions, int[] grantResults) {

                    showPermissionDialog("Location permission is required to continue",LOCATION_UPDATE);
                }

                @Override
                public void permissionDenied(String[] permissions, int[] grantResults) {

                    showPermissionDialog("Location permission is required to continue",GO_SETTINGS);
                }

                @Override
                public void permissionGranted(String[] permissions) {
                    LocationServices.FusedLocationApi.removeLocationUpdates(
                            mGoogleApiClient, LocationFetcherActivity.this);
                    Logger.log("Location update stoped");
                }
            });

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MPermissionManager.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

}
