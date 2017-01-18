package com.bycycle.android.services;

import android.support.annotation.StringDef;

import com.bycycle.android.apihandler.CreateUserResult;
import com.bycycle.android.apihandler.CreateStationResult;
import com.bycycle.android.apihandler.FileUploadResult;
import com.bycycle.android.apihandler.GetJourneysResult;
import com.bycycle.android.apihandler.GetStationsResult;
import com.bycycle.android.apihandler.CreateVehicleResult;
import com.bycycle.android.apihandler.GetVehiclesResult;
import com.bycycle.android.apihandler.SimpleApiResult;
import com.bycycle.android.apihandler.StartJourneyResult;
import com.bycycle.android.utils.AppConstants;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public class RetrofitApiService {


    public static final String API_BASE_URL = AppConstants.URL;

    private static OkHttpClient httpClient;
    private static Retrofit.Builder builder;
    private static HttpLoggingInterceptor interceptor;

    static {
        interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                .readTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                .connectTimeout(300, java.util.concurrent.TimeUnit.SECONDS);

        if (AppConstants.ENABLE_LOG) {
            okHttpBuilder.addInterceptor(interceptor);
        }
        httpClient = okHttpBuilder.build();

        builder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(new JsonConverter());
    }


    public static RetrofitApiService.RetrofitApiServiceClient mApiClient;


    public static RetrofitApiServiceClient getApiClient() {

        if (mApiClient == null) {

            mApiClient = RetrofitApiService.createService(RetrofitApiService.RetrofitApiServiceClient.class);
        }

        return mApiClient;

    }


    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }


    public interface RetrofitApiServiceClient {


        String CREATE_USER="createUser";
        String GET_USER_INFO="getUserInfo";
        String CREATE_STATION="createStation";
        String GET_STATIONS="getStations";
        String CREATE_VEHICLE ="createVehicle";
        String GET_VEHICLES="getVehicles";
        String SET_STATION="setStation";
        String START_JOURNEY="startJourney";
        String STOP_JOURNEY ="stopJourney";
        String GET_JOURNEYS="getJourneys";
        String SAVE_FILE="saveFile";
        String POST_USER_KYC ="postUserKYC";


        @StringDef({CREATE_USER,CREATE_STATION,GET_STATIONS,CREATE_VEHICLE,GET_VEHICLES,SET_STATION,START_JOURNEY
        , STOP_JOURNEY,GET_JOURNEYS,GET_USER_INFO,SAVE_FILE, POST_USER_KYC})
        public @interface ApiMethod {

        }

        @GET(CREATE_USER)
        retrofit2.Call<CreateUserResult> createUser(@QueryMap Map<String, String> params);

        @GET(GET_USER_INFO)
        retrofit2.Call<CreateUserResult> getUserInfo(@QueryMap Map<String, String> params);

        @GET(CREATE_STATION)
        retrofit2.Call<CreateStationResult> createStation(@QueryMap Map<String, String> params);

        @GET(CREATE_VEHICLE)
        retrofit2.Call<CreateVehicleResult> createVehicle(@QueryMap Map<String, String> params);

        @GET(GET_STATIONS)
        retrofit2.Call<GetStationsResult> getStations(@QueryMap Map<String,String> params);


        @GET(GET_VEHICLES)
        retrofit2.Call<GetVehiclesResult> getVehicles(@QueryMap Map<String,String> params);


        @GET(SET_STATION)
        retrofit2.Call<CreateVehicleResult> setStation(@QueryMap Map<String,String> params);

        @GET(START_JOURNEY)
        retrofit2.Call<StartJourneyResult> startJourney(@QueryMap Map<String,String> params);

        @GET(STOP_JOURNEY)
        retrofit2.Call<StartJourneyResult> stopJourney(@QueryMap Map<String,String> params);

        @GET(GET_JOURNEYS)
        retrofit2.Call<GetJourneysResult> getJourneys(@QueryMap Map<String,String> params);

        @Multipart
        @POST(SAVE_FILE)
        Call<FileUploadResult> upload(@Part("description") RequestBody description,
                                      @Part MultipartBody.Part file, @QueryMap Map<String, String> params);


        @GET(POST_USER_KYC)
        retrofit2.Call<SimpleApiResult> postUserKYC(@QueryMap Map<String,String> params);



    }

}

