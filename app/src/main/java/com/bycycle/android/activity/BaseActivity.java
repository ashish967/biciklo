package com.bycycle.android.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.bycycle.android.utils.AppUtils;

/**
 * Created by Ashish Kumar Khatri on 16/12/16.
 */

public  abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {


    public abstract void getExtras() ;
    public abstract void initialize() ;
    public abstract void setup() ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {

        getExtras();
        initialize();
        setup();
        AppUtils.setTypeFace(getWindow().getDecorView());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                 finish();
                 break;
        }
        return true;
    }
}
