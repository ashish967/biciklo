package com.bycycle.android.utils;

import java.util.ArrayList;
import java.util.Random;

public class PermissionRequest {
    private static Random random;
    private ArrayList<String> permissions;
    private int requestCode;
    private MPermissionManager.InteractionListener mInteractionListener;

    public PermissionRequest(int requestCode) {
        this.requestCode = requestCode;
    }

    public PermissionRequest(ArrayList<String> permissions, MPermissionManager.InteractionListener mInteractionListener) {
        this.permissions = permissions;
        this.mInteractionListener = mInteractionListener;
        if (random == null) {
            random = new Random();
        }
        this.requestCode = random.nextInt(255);
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public MPermissionManager.InteractionListener getInteractionListener() {
        return mInteractionListener;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof PermissionRequest) {
            return ((PermissionRequest) object).requestCode == this.requestCode;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return requestCode;
    }
}