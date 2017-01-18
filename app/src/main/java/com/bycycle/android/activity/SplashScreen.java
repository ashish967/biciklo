package com.bycycle.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.bycycle.android.R;
import com.bycycle.android.utils.AppUtils;


public class SplashScreen extends BaseActivity {


    Intent mIntent;

    @Override
    public void getExtras() {

    }

    @Override
    public void initialize() {
        AppUtils.setWindowFullScreen(getWindow());
    }

    @Override
    public void setup() {


        mIntent= getNextIntent(this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(mIntent);
                finish();
            }
        },2000);


    }

    public static Intent getNextIntent(Activity activity) {

        Intent mIntent;
        if(AppUtils.getUserInfo()==null){
            mIntent= new Intent(activity,SignupActivity.class);
        }
        else{
            mIntent= new Intent(activity,HomeActivity.class);
        }

        return mIntent;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    public void onClick(View view) {

    }
}
