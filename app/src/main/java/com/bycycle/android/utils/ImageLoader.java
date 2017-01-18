package com.bycycle.android.utils;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Ashish Kumar Khatri on 29/12/16.
 */

public class ImageLoader {

    public static void loadImage(SimpleDraweeView view,String url){

        Uri uri= AppUtils.parse(url);
        view.setImageURI(uri);
    }

}
