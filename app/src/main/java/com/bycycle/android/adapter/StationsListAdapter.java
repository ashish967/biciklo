package com.bycycle.android.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bycycle.android.R;
import com.bycycle.android.activity.StationsListActivity;
import com.bycycle.android.databinding.StationListItemLayoutBinding;
import com.bycycle.android.datatypes.Station;
import com.bycycle.android.datatypes.Vehicle;
import com.bycycle.android.utils.AppUtils;

import java.util.ArrayList;

/**
 * Created by Ashish Kumar Khatri on 24/12/16.
 */

public class StationsListAdapter extends RecyclerView.Adapter<StationsListAdapter.StationViewHolder> {


    ArrayList<Station> mList;

    Activity mActivity;

    public StationsListAdapter(Activity activity, ArrayList<Station> vehicles){

        mActivity= activity;
        mList=vehicles;
    }

    public static class StationViewHolder extends RecyclerView.ViewHolder{


        Activity mActivity;

        StationListItemLayoutBinding listItemLayoutBinding;

        Station mStation;

        public StationViewHolder(Activity activity, StationListItemLayoutBinding binding) {

            super(binding.getRoot());

            listItemLayoutBinding= binding;
            mActivity= activity;
            listItemLayoutBinding.tvAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((StationsListActivity)mActivity).handleAction(mStation);
                }
            });
            AppUtils.setTypeFace(binding.getRoot());

        }

        public void setVehicle(Station vehicle) {

            mStation = vehicle;

        }
    }

    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        StationListItemLayoutBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.station_list_item_layout
                ,parent,false);
        return new StationViewHolder(mActivity,binding);
    }

    @Override
    public void onBindViewHolder(StationViewHolder holder, int position) {

        Station vehicle=mList.get(position);

        holder.setVehicle(vehicle);

        holder.listItemLayoutBinding.tvStationName.setText(vehicle.getName());

        if(mActivity instanceof StationsListActivity){

            if(((StationsListActivity) mActivity).selectionMode()){
                holder.listItemLayoutBinding.tvAction.setText(mActivity.getString(R.string.select_station));
            }
            else{
                holder.listItemLayoutBinding.tvAction.setText(mActivity.getString(R.string.show_vehicle));
            }
        }



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

