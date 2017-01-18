package com.bycycle.android.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bycycle.android.R;
import com.bycycle.android.databinding.JourneyListItemLayoutBinding;
import com.bycycle.android.datatypes.Journey;
import com.bycycle.android.utils.AppUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ashish Kumar Khatri on 24/12/16.
 */

public class JourneysListAdapter extends RecyclerView.Adapter<JourneysListAdapter.JourneyViewHolder> {


    ArrayList<Journey> mList;

    Activity mActivity;


    public JourneysListAdapter(Activity activity, ArrayList<Journey> vehicles){

        mActivity= activity;
        mList=vehicles;
    }

    public static class JourneyViewHolder extends RecyclerView.ViewHolder{

        SimpleDateFormat format= new SimpleDateFormat("EEE, MMM d 'at' HH:mm aaa");

        Activity mActivity;

        JourneyListItemLayoutBinding listItemLayoutBinding;

        Journey journey;

        public JourneyViewHolder(Activity activity,JourneyListItemLayoutBinding binding) {

            super(binding.getRoot());

            listItemLayoutBinding= binding;
            AppUtils.setTypeFace(binding.getRoot());
            mActivity= activity;

        }

        public void setVehicle(Journey vehicle) {

            journey = vehicle;

        }

        public void bindView(Journey journey) {

            this.journey =journey;
            listItemLayoutBinding.tvBill.setText("\u20b9 "+journey.getBill());
            listItemLayoutBinding.tvVehicleNumber.setText(journey.getVehicle().getNumber());
            listItemLayoutBinding.tvStartStation.setText(journey.getStartStation().getName());
            listItemLayoutBinding.tvEndStation.setText(journey.getEndStation().getName());
            listItemLayoutBinding.tvTime.setText(format.format(new Date(journey.getStartTime()*1000)));
        }
    }

    @Override
    public JourneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        JourneyListItemLayoutBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.journey_list_item_layout
                ,parent,false);
        return new JourneyViewHolder(mActivity,binding);
    }

    @Override
    public void onBindViewHolder(JourneyViewHolder holder, int position) {

        Journey journey=mList.get(position);

        holder.bindView(journey);


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
