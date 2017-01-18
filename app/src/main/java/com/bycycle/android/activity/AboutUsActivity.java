package com.bycycle.android.activity;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import com.bycycle.android.R;
import com.bycycle.android.databinding.ActivityAboutUsBinding;
import com.bycycle.android.utils.Logger;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

public class AboutUsActivity extends BaseActivity {


    ActivityAboutUsBinding mBinding;

    @Override
    public void getExtras() {

    }

    @Override
    public void initialize() {
        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_about_us);
    }

    String aboutUs="<h1><strong>What is <u>Biciklo?</u></strong>\n" +
            "</h1>\n" +
            "<p>You can now get rental service of bicycle near to your location. Download app an enjoy easy and smooth experience of bicycle riding.\n" +
            "</p>\n" +
            "<h1><strong>Who Should use Biciklo?</strong>\n" +
            "</h1>\n" +
            "<p >Any one who is looking for some cheap and healthy medium for travel, where he/she can request a bicycle through phone or just want to opt bicycle ride for shorter distance or care about the environment and are ready to use this service for their local commute.\n" +
            "</p>";
    @Override
    public void setup() {

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Build.VERSION.SDK_INT<=Build.VERSION_CODES.M) {
            Logger.log(getString(R.string.about_us));
            mBinding.tvAbout.setText(Html.fromHtml(getString(R.string.about_us)));
        }
        else{
            mBinding.tvAbout.setText(Html.fromHtml(getString(R.string.about_us),FROM_HTML_MODE_LEGACY));

        }
    }

    @Override
    public void onClick(View view) {

    }
}
