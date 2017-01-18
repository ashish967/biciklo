package com.bycycle.android.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;

import com.bycycle.android.R;
import com.bycycle.android.apihandler.CreateUserResult;
import com.bycycle.android.apihandler.CreateVehicleResult;
import com.bycycle.android.databinding.ActivityCreateVehicalBinding;
import com.bycycle.android.services.ApiRequestTask;
import com.bycycle.android.services.RetrofitApiService;
import com.bycycle.android.utils.AppUtils;
import com.bycycle.android.utils.Logger;


public class CreateVehicleActivity extends BaseActivity {

    ActivityCreateVehicalBinding mBinding;
    @Override
    public void getExtras() {

    }

    @Override
    public void initialize() {

        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_create_vehical);
    }

    @Override
    public void setup() {

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBinding.btnSignup.setOnClickListener(this);
        mBinding.etName.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_signup:
                handleSubmitVehicleAction();
        }
    }

    private void handleSubmitVehicleAction() {


        if(mBinding.etName.getText().toString().isEmpty()){

            AppUtils.showToast(this,"Invalid name");
            return;
        }

        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.CREATE_VEHICLE
                , new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {

                        mBinding.btnSignup.setEnabled(true);

                        CreateVehicleResult result= (CreateVehicleResult) apiresult;
                        if(!result.getStatus().isOk()){

                            AppUtils.showToast(CreateVehicleActivity.this,result.getStatus().getMsg());
                        }
                        else{
                            finish();

                        }

                        Logger.log("Result"+apiresult);
                        mBinding.btnSignup.setText("Submit");

                    }



                    @Override
                    public void onError(String result, Object tag) {
                        AppUtils.showToast(CreateVehicleActivity.this,result);
                        mBinding.btnSignup.setEnabled(true);
                        mBinding.btnSignup.setText("Submit");

                    }
                },0);

        task.addParam("number",mBinding.etName.getText().toString().trim());

        task.execute();

        mBinding.btnSignup.setEnabled(false);
        mBinding.btnSignup.setText("Submitting...");
    }
}
