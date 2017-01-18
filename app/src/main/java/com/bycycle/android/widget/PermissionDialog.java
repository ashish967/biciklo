package com.bycycle.android.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.TextView;

import com.bycycle.android.R;

/**
 * Created by Ashish Kumar Khatri on 16/12/16.
 */

public class PermissionDialog extends BottomSheetDialog implements View.OnClickListener {

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.ok_action:
                 mListener.reRequest(mRequestCode);
                 break;

            case R.id.cancel_action:
                 mListener.onCancel();
                 break;
        }
    }

    public interface InteractionListener{
        public void onCancel();
        public void reRequest(int code);
    }

    InteractionListener mListener;

    int mRequestCode;


    public PermissionDialog(@NonNull Context context
    ,String message,InteractionListener listener,int requestCode) {
        super(context);

        mListener= listener;
        mRequestCode= requestCode;

        setCancelable(false);

        setContentView(R.layout.layout_permission_dialog);

        TextView tvMessage= (TextView) findViewById(R.id.tv_message);

        tvMessage.setText(message);

        findViewById(R.id.ok_action).setOnClickListener(this);
        findViewById(R.id.cancel_action).setOnClickListener(this);
    }
}
