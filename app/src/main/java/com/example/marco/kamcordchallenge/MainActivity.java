package com.example.marco.kamcordchallenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.marco.kamcordchallenge.adapters.VideoAdapter;
import com.example.marco.kamcordchallenge.network.VolleySingleton;
import com.example.marco.kamcordchallenge.pojo.VideoInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements VideoAdapter.OnThumbnailClickListener{

    //Declare constants and variables
    public static final String URL_KAMCORD_FEED = "https://www.kamcord.com/app/v2/videos/feed/?feed_id=0";
    public static final String JSON_KEY_RESPONSE = "response";
    public static final String JSON_KEY_VIDEO_LIST = "video_list";
    public static final String JSON_KEY_TITLE = "title";
    public static final String JSON_KEY_THUMBNAILS = "thumbnails";
    public static final String JSON_KEY_REGULAR = "REGULAR";
    public static final String JSON_KEY_VIDEO_URL = "video_url";
    public static final String INTENT_KEY_VIDEO_URL = "videoUrl";

    private RecyclerView mRecyclerView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        //Network objects to check for internet
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //Makes sure device has internet. If it does, retrieve JSON info.
        if (networkInfo != null && networkInfo.isConnected()) {

            //Create a progressDialog while loading.
            mProgressDialog = ProgressDialog.show(MainActivity.this, getString(R.string.pd_title_1), getString(R.string.pd_body_1), true);
            mProgressDialog.setCancelable(true);
            //If user cancels while loading, the activity will close.
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });

            getJSONinfo();
        }else{
            //Show toast and quit if no internet.
            Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void getJSONinfo() {
        //Initialize requestQueue from VolleySingleton Class
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();

        //Setup the request to retrieve JSON.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_KAMCORD_FEED, null,
        new Response.Listener<JSONObject>() {

            @Override
            //OnResponse will be called when we get the data we requested.
            public void onResponse(JSONObject response) {
                parseJSONResponse(response);
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        //Send the request to the queue to be processed.
        requestQueue.add(request);
    }

    private void initializeViews() {
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_video_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    //Parsing the data that we need
    private void parseJSONResponse(JSONObject response){
        if (response == null || response.length() == 0)
            return;

        try {
            JSONObject data = response.getJSONObject(JSON_KEY_RESPONSE);
            JSONArray videosArray = data.getJSONArray(JSON_KEY_VIDEO_LIST);
            populateVideoList(videosArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void populateVideoList(JSONArray videosArray) throws JSONException{
        List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();
        String title;
        String thumbnail;
        String videoUrl;

        //Loop through all objects in the JSON video_list array.
        for(int i = 0; i<videosArray.length(); i++) {
        JSONObject current = videosArray.getJSONObject(i);

            //Parsing more of the specific data that we need
            title = current.getString(JSON_KEY_TITLE);
            JSONObject thumbnails = current.getJSONObject(JSON_KEY_THUMBNAILS);
            thumbnail = thumbnails.getString(JSON_KEY_REGULAR);
            videoUrl = current.getString(JSON_KEY_VIDEO_URL);

            //Create and add videoInfo
            VideoInfo videoInfo = new VideoInfo(title, thumbnail, videoUrl);
            videoInfoList.add(videoInfo);
        }

        setupAdapter(videoInfoList);
        mProgressDialog.dismiss();
    }

    private void setupAdapter(List<VideoInfo> videoInfoList){
        VideoAdapter videoAdapter = new VideoAdapter(this, videoInfoList);
        videoAdapter.setOnThumbnailClickListener(this);
        mRecyclerView.setAdapter(videoAdapter);
    }

    @Override
    //Called when user clicks on the thumbnail
    public void onThumbnailClick(String videoUrl) {
        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
        intent.putExtra(INTENT_KEY_VIDEO_URL, videoUrl);
        startActivity(intent);
    }
}