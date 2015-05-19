package com.example.marco.kamcordchallenge.network;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.marco.kamcordchallenge.context.MyApplication;

/**
 * Created by Marco on 2/9/2015.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance;
    private ImageLoader mImageLoader;
    private RequestQueue mRequestQueue;

    private VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache(){
            //Instantiating a cache for images. maxMemory() returns memory in bytes. '/1028' to convert to kilobytes. '/8' to use 1/8th of that memory
            private LruCache<String, Bitmap> imageCache = new LruCache<String, Bitmap>((int)Runtime.getRuntime().maxMemory()/1024/8);

            @Override
            public Bitmap getBitmap(String url) {
                return imageCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                imageCache.put(url, bitmap);
            }
        });
    }

    public static VolleySingleton getInstance(){
        if(sInstance == null)
            sInstance = new VolleySingleton();
        return sInstance;
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }
}
