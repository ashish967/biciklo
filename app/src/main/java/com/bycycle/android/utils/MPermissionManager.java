package com.bycycle.android.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MPermissionManager {


    private static Random random;

    private static ArrayList<PermissionRequest> permissionRequests = new ArrayList<PermissionRequest>();


    public interface InteractionListener {

        public void showRationaleUI(String[] permissions, int[] grantResults);

        public void permissionDenied(String[] permissions, int[] grantResults);

        public void permissionGranted(String[] permissions);
    }

    public static void askForPermissions(Activity activity, String permission, InteractionListener interactionListener) {
        askForPermissions(activity, new String[]{permission}, interactionListener);
    }

    public static void askForPermissions(Activity activity, String[] permissions, InteractionListener interactionListener) {
        if (interactionListener == null) {
            return;
        }
        if (hasPermission(activity, permissions)) {
            interactionListener.permissionGranted(permissions);
            return;
        }

        PermissionRequest permissionRequest = new PermissionRequest(new ArrayList<String>(Arrays.asList(permissions)), interactionListener);
        permissionRequests.add(permissionRequest);

        ActivityCompat.requestPermissions(activity, permissions, permissionRequest.getRequestCode());
    }

    private static boolean shouldShowRequestPermissionRationale(Activity activity, String permissions) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions);
    }

    public static boolean hasPermission(Activity activity, String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Returns true if the Activity has access to all given permissions.
     */
    private static boolean hasPermission(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (permissions.length == 0 || grantResults.length == 0) return;
        PermissionRequest requestResult = new PermissionRequest(requestCode);
        if (permissionRequests.contains(requestResult)) {
            PermissionRequest permissionRequest = permissionRequests.get(permissionRequests.indexOf(requestResult));
            if (verifyPermissions(grantResults)) {
                //Permission has been granted
                permissionRequest.getInteractionListener().permissionGranted(permissions);
            } else {

                boolean showRatioanleUI = false;
                for (String permission : permissions) {
                    if (MPermissionManager.shouldShowRequestPermissionRationale(activity, permission)) {
                        showRatioanleUI = true;
                        break;
                    }
                }

                if (showRatioanleUI) {
                    permissionRequest.getInteractionListener().showRationaleUI(permissions, grantResults);
                } else {
                    permissionRequest.getInteractionListener().permissionDenied(permissions, grantResults);
                }
            }
            permissionRequests.remove(requestResult);
        }
    }

    public static void startPermissionDetailActivity(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

}