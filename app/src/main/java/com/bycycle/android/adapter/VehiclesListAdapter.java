package com.bycycle.android.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bycycle.android.R;
import com.bycycle.android.activity.VehiclesListActivity;
import com.bycycle.android.databinding.VehicleListItemLayoutBinding;
import com.bycycle.android.datatypes.Vehicle;
import com.bycycle.android.utils.AppUtils;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Ashish Kumar Khatri on 24/12/16.
 */

public class VehiclesListAdapter extends RecyclerView.Adapter<VehiclesListAdapter.VehicleViewHolder> {


    ArrayList<Vehicle> mList;

    Activity mActivity;

    public VehiclesListAdapter(Activity activity, ArrayList<Vehicle> vehicles){

        mActivity= activity;
        mList=vehicles;
    }

    public static class VehicleViewHolder extends RecyclerView.ViewHolder{


        Activity mActivity;

        VehicleListItemLayoutBinding listItemLayoutBinding;

        Vehicle mVehicle;

        public VehicleViewHolder(Activity activity,VehicleListItemLayoutBinding binding) {

            super(binding.getRoot());

            listItemLayoutBinding= binding;
            mActivity= activity;
            listItemLayoutBinding.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((VehiclesListActivity)mActivity).setStation(mVehicle);
                }
            });
            AppUtils.setTypeFace(binding.getRoot());

        }

        public void setVehicle(Vehicle vehicle) {

            mVehicle= vehicle;

        }
    }

    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        VehicleListItemLayoutBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.vehicle_list_item_layout
        ,parent,false);
        return new VehicleViewHolder(mActivity,binding);
    }

    @Override
    public void onBindViewHolder(VehicleViewHolder holder, int position) {

        Vehicle vehicle=mList.get(position);

        holder.setVehicle(vehicle);

        holder.listItemLayoutBinding.tvVehicleNumber.setText(vehicle.getNumber());

        if(vehicle.getStation()!=null){
            holder.listItemLayoutBinding.tvStationId.setText(" "+vehicle.getStation().getName());
//            holder.listItemLayoutBinding.tvAction.setText("Change Station Id");
        }
        else{
            holder.listItemLayoutBinding.tvStationId.setText(mActivity.getString(R.string.no_station_specified));
//            holder.listItemLayoutBinding.tvAction.setText("Set Station");

        }


        if(vehicle.getUser()!=null){
            holder.listItemLayoutBinding.tvUserId.setText(mActivity.getString(R.string.assigned_to)+" "+vehicle.getUser().getName());
        }
        else{
            holder.listItemLayoutBinding.tvUserId.setText(mActivity.getString(R.string.ready_for_ride));
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
