package com.example.marco.kamcordchallenge.pojo;

import android.graphics.Bitmap;

/**
 * Created by Marco on 2/9/2015.
 */
public class VideoInfo {
    private String mTitle;
    private String mThumbnail;
    private String mVideoUrl;

    public VideoInfo(String title, String thumbnail, String videoUrl){
        mTitle = title;
        mThumbnail = thumbnail;
        mVideoUrl = videoUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }
}
