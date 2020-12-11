package com.sghost.diems;

import android.content.Context;
import android.media.Image;

import com.google.firebase.database.Exclude;

public class Upload {
    private String mTitle;
    private String mUser;
    private String mUrl;
    private int height;
    private int width;
    private String mKey;

    public Upload() {  }

    public Upload(String title, String user, String url, int h, int w) {
        if (title.trim().equals("")) {
            title = " Notice ";
        }
        mTitle = title;
        mUser = user;
        mUrl = url;
        height = h;
        width = w;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmUser() {
        return mUser;
    }

    public void setmUser(String mUser) {
        this.mUser = mUser;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public int getHeight() { return height; }

    public void setHeight(int height) { this.height = height; }

    public int getWidth() { return width; }

    public void setWidth(int width) { this.width = width; }

    @Exclude
    public String getmKey() { return mKey; }

    @Exclude
    public void setmKey(String mKey) { this.mKey = mKey; }

}
