package com.bycycle.android.services;

import android.support.annotation.StringDef;

import com.bycycle.android.apihandler.FileUploadResult;
import com.bycycle.android.utils.AppConstants;
import com.bycycle.android.utils.AppUtils;
import com.bycycle.android.utils.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static io.fabric.sdk.android.Fabric.TAG;

/**
 * Created by Ashish Kumar Khatri on 19/12/16.
 */

public class ApiRequestTask {


    private static final String URI = "uri";
    private static final String UPLOAD_FILE="uploadFile";


    HashMap<String,String> mParams= new HashMap<>();

    ResultListener mListner;

    public interface ResultListener {

        public void onResult(Object apiresult, Object tag);

        public void onError(String result, Object tag);

    }

    @RetrofitApiService.RetrofitApiServiceClient.ApiMethod String mMethod;

    Object mTag;


    public void addParam(String key,String value){
        mParams.put(key,value);
    }

    public static ApiRequestTask createRequest(@RetrofitApiService.RetrofitApiServiceClient.ApiMethod String method
            , ResultListener listener, Object tag) {

        ApiRequestTask task = new ApiRequestTask();
        task.mMethod = method;
        task.mListner = listener;
        task.mTag = tag;
        return task;
    }

    public static ApiRequestTask createUploadRequest(ResultListener listener,String path,Object tag) {

        ApiRequestTask task = new ApiRequestTask();
        task.mMethod =UPLOAD_FILE;
        task.mListner = listener;
        task.mTag = tag;
        task.addParam(URI,path);
        return task;
    }


    private class ApiCallBack implements Callback {


        ResultListener mListener;
        Object mTag;

        public ApiCallBack(ResultListener listener, Object tag) {

            mListener = listener;
            mTag = tag;

        }


        @Override
        public void onResponse(Call call, Response response) {

            if (response.isSuccessful() && mListener != null) {

                if (response.body() == null) {
                    mListener.onError("Some error", mTag);
                    return;
                }
                mListener.onResult(response.body(), mTag);
            } else if (mListener != null) {
                mListener.onError(response.message(), mTag);
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {

            if (mListener != null) {
                mListener.onError(t.getLocalizedMessage(),mTag);
            }
            t.printStackTrace();
        }
    }


    public Call uploadFile(String uri) {

        // use the FileUtils to get the actual file by uri

        Logger.log("uri recvd " + uri);
        File file = new File(uri);

        if (!file.exists()) {


            Logger.log("File not exist");
            return null;
        }


        Logger.log("File size " + file.length() + " bytes");
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);

        mParams.remove(URI);
        mParams.put("source", "android");
        // finally, execute the request
        Call<FileUploadResult> call = RetrofitApiService.getApiClient().upload(description, body, mParams);
        call.enqueue(new ApiCallBack(mListner, mTag));
        return call;
    }
    public void execute() {


        if (AppUtils.getUserInfo() != null) {
            mParams.put(AppConstants.USER_ID, AppUtils.getUserInfo().getId());
        }

        if(mMethod.equals(UPLOAD_FILE)){
            uploadFile(mParams.get(URI));
            return;
        }

        RetrofitApiService.RetrofitApiServiceClient apiServiceClient = RetrofitApiService.getApiClient();
        try {
            Method method = apiServiceClient.getClass().getMethod(mMethod, Map.class);
            Call handlerCall = (Call) method.invoke(apiServiceClient, mParams);
            handlerCall.enqueue(new ApiCallBack(mListner, mTag));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }



    }
}
