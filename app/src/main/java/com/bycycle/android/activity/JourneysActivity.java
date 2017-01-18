package com.bycycle.android.activity;

import android.databinding.DataBindingUtil;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.bycycle.android.R;
import com.bycycle.android.adapter.JourneysListAdapter;
import com.bycycle.android.apihandler.GetJourneysResult;
import com.bycycle.android.databinding.ActivityRidesBinding;
import com.bycycle.android.datatypes.Journey;
import com.bycycle.android.services.ApiRequestTask;
import com.bycycle.android.services.RetrofitApiService;
import com.bycycle.android.utils.AppUtils;

import java.util.ArrayList;

public class JourneysActivity extends BaseActivity {


    ActivityRidesBinding mBinding;

    ArrayList<Journey> mList=new ArrayList<>();

    @Override
    public void getExtras() {

    }

    @Override
    public void initialize() {

        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_rides);
    }

    @Override
    public void setup() {

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        mBinding.vehicles.setLayoutManager(new LinearLayoutManager(this));
        mBinding.vehicles.setAdapter(new JourneysListAdapter(this,mList));
        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getList();
                mBinding.swipeRefreshLayout.setEnabled(false);

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        getList();

    }

    private void notifyAdapter(){

        mBinding.vehicles.getAdapter().notifyDataSetChanged();;
    }
    private void getList() {

        ApiRequestTask task= ApiRequestTask.createRequest(RetrofitApiService.RetrofitApiServiceClient.GET_JOURNEYS
                , new ApiRequestTask.ResultListener() {
                    @Override
                    public void onResult(Object apiresult, Object tag) {

                        GetJourneysResult result= (GetJourneysResult) apiresult;
                        hideRefreshLayout();

                        if(result.getStatus().isOk()){

                            mList.clear();
                            mList.addAll(result.getData());
                            notifyAdapter();
                        }
                        else{
                            AppUtils.showToast(JourneysActivity.this,result.getStatus().getMsg());
                        }
                    }

                    @Override
                    public void onError(String result, Object tag) {
                        AppUtils.showToast(JourneysActivity.this,result);
                        hideRefreshLayout();

                    }
                },0);


        task.execute();
    }

    private void hideRefreshLayout() {

        mBinding.swipeRefreshLayout.setEnabled(true);
        mBinding.swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onClick(View view) {

    }
}
