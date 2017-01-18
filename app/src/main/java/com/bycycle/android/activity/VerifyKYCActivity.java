package com.bycycle.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bycycle.android.R;
import com.bycycle.android.apihandler.FileUploadResult;
import com.bycycle.android.apihandler.SimpleApiResult;
import com.bycycle.android.apihandler.StartJourneyResult;
import com.bycycle.android.databinding.ActivityVerifyKycBinding;
import com.bycycle.android.datatypes.FileType;
import com.bycycle.android.services.ApiRequestTask;
import com.bycycle.android.services.RetrofitApiService;
import com.bycycle.android.utils.AppUtils;
import com.bycycle.android.utils.FileUtils;
import com.bycycle.android.utils.ImageLoader;
import com.bycycle.android.utils.MPermissionManager;
import com.bycycle.android.widget.PermissionDialog;


public class VerifyKYCActivity extends BaseActivity {


    ActivityVerifyKycBinding mBinding;

    public static final int PHOTO_GALLARY=1;
    private static final int GO_SETTINGS = 1001;


    FileType mFile;
    @Override
    public void getExtras() {

    }

    @Override
    public void initialize() {

        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_verify_kyc);
    }

    @Override
    public void setup() {

        mBinding.ivKycImage.setOnClickListener(this);
        mBinding.btnSubmit.setOnClickListener(this);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.iv_kyc_image:
                 handleImageAction();
                 break;

            case R.id.btn_submit:
                 handleSubmitAction();
                 break;
        }
    }



    private void handleImageAction() {

        MPermissionManager.askForPermissions(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, new MPermissionManager.InteractionListener() {

            @Override
            public void showRationaleUI(String[] permissions, int[] grantResults) {
                showPermissionDialog("Before selecting picture from gallery, you must grant necessary permission.", PHOTO_GALLARY);
            }

            @Override
            public void permissionDenied(String[] permissions, int[] grantResults) {
                showPermissionDialog("Please allow storage permission in the settings", GO_SETTINGS);
            }

            @Override
            public void permissionGranted(String[] permissions) {
                startPhotoGallaryIntent();
            }
        });
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
                    MPermissionManager.startPermissionDetailActivity(VerifyKYCActivity.this);
                }
                else {
                    handleImageAction();
                }
            }
        },code);

        permissionDialog.show();
    }


    private void startPhotoGallaryIntent() {

        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PHOTO_GALLARY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_GALLARY && resultCode == Activity.RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            handleMedia(FileUtils.getPath(this,selectedImage));

        }
    }

    private void handleMedia(String path) {


        ApiRequestTask task= ApiRequestTask.createUploadRequest(new ApiRequestTask.ResultListener() {
            @Override
            public void onResult(Object apiresult, Object tag) {

                FileUploadResult result= (FileUploadResult) apiresult;

                if(result.getStatus().isOk()){


                    mFile= result.getData();
                    handleUploadedFile();
                }
            }

            @Override
            public void onError(String result, Object tag) {

            }
        },path,0);



        task.execute();


    }

    private void handleSubmitAction() {

        if(mFile==null){
            AppUtils.showToast(this,getString(R.string.attach_kyc_error_message));
            return;
        }

        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.POST_USER_KYC
                , new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {

                        SimpleApiResult result= (SimpleApiResult) apiresult;

                        if(result.getStatus().isOk()){

                            AppUtils.showToast(VerifyKYCActivity.this,result.getStatus().getMsg());
                        }
                        else{
                            AppUtils.showToast(VerifyKYCActivity.this,result.getStatus().getMsg());
                        }


                    }

                    @Override
                    public void onError(String result, Object tag) {

                        AppUtils.showToast(VerifyKYCActivity.this,result);
                    }

                },0);

        task.addParam("kyc", mFile.getUrl());
        task.execute();;

    }


    private void handleUploadedFile() {
        ImageLoader.loadImage(mBinding.ivKycImage,mFile.getUrl());
    }
}
