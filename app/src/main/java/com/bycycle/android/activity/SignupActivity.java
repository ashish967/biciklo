package com.bycycle.android.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.bycycle.android.R;
import com.bycycle.android.apihandler.CreateUserResult;
import com.bycycle.android.databinding.ActivitySignupBinding;
import com.bycycle.android.services.ApiRequestTask;
import com.bycycle.android.services.RetrofitApiService;
import com.bycycle.android.utils.AppUtils;
import com.bycycle.android.utils.Logger;

public class SignupActivity extends BaseActivity {


    ActivitySignupBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void getExtras() {

    }

    @Override
    public void initialize() {


        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_signup);
    }

    @Override
    public void setup() {

        setSupportActionBar(mBinding.toolbar);
        mBinding.btnSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btn_signup:
                 handleSignupAction();
        }

    }

    private void handleSignupAction() {


        if(mBinding.etName.getText().toString().isEmpty()){

            AppUtils.showToast(this,"Invalid name");

        }

        if(mBinding.etPhone.getText().toString().isEmpty()
                || !AppUtils.isValidPhoneNumber(mBinding.etPhone.getText().toString())){

            AppUtils.showToast(this,"Invalid phone");
        }


        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.CREATE_USER
                , new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {

                        mBinding.btnSignup.setEnabled(true);

                        CreateUserResult result= (CreateUserResult) apiresult;
                        if(!result.getStatus().isOk()){

                            AppUtils.showToast(SignupActivity.this,result.getStatus().getMsg());
                        }
                        else{
                            AppUtils.saveUserInfo(result.getData(),SignupActivity.this);
                            goNext();
                        }
                        Logger.log("Result"+apiresult);
                    }



                    @Override
                    public void onError(String result, Object tag) {
                        mBinding.btnSignup.setEnabled(true);

                    }
                },0);

        task.addParam("name",mBinding.etName.getText().toString());
        task.addParam("phone",mBinding.etPhone.getText().toString());

        task.execute();

        mBinding.btnSignup.setEnabled(false);

    }

    private void goNext() {

        Intent intent= SplashScreen.getNextIntent(SignupActivity.this);
        startActivity(intent);
        finish();
    }
}
