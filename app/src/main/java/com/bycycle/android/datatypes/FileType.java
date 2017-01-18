package com.bycycle.android.datatypes;

import com.bycycle.android.utils.JsonUtils;

import org.json.JSONObject;

/**
 * Created by Ashish Kumar Khatri on 29/12/16.
 */

public class FileType {

    String url;

    String fileType;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public static FileType create(JSONObject data) {

        if(data==null) {
            return null;
        }


        FileType file= new FileType();
        file.setFileType(JsonUtils.getString(data,"file_type"));
        file.setUrl(JsonUtils.getString(data,"url"));

        return file;
    }
}
