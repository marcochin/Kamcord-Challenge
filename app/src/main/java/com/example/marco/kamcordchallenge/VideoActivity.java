package com.example.marco.kamcordchallenge;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;

/**
 * Created by Marco on 2/9/2015.
 */
public class VideoActivity extends Activity {
        // Declare variables
        ProgressDialog mProgressDialog;
        VideoView mVideoView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //Extract video url from extra that was pass in the intent.
            String videoUrl = getIntent().getStringExtra(MainActivity.INTENT_KEY_VIDEO_URL);

            setContentView(R.layout.activity_video);
            mVideoView = (VideoView) findViewById(R.id.video_view);

            // Create a progressbar
            mProgressDialog = ProgressDialog.show(VideoActivity.this, getString(R.string.pd_title_1),getString(R.string.pd_body_2), true);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                //If user cancels before video loads it will end this VideoActivity
                public void onCancel(DialogInterface dialog) {
                    finish();
                }
            });

            try {
                // Adding the UI controls for the video.
                MediaController mediacontroller = new MediaController(
                        VideoActivity.this);
                mediacontroller.setAnchorView(mVideoView);
                // Get the URI from String VideoURL
                Uri video = Uri.parse(videoUrl);
                mVideoView.setMediaController(mediacontroller);
                mVideoView.setVideoURI(video);

            } catch (Exception e) {
                e.printStackTrace();
            }

            mVideoView.requestFocus();
            mVideoView.setOnPreparedListener(new OnPreparedListener() {
                // Dismiss progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    mProgressDialog.dismiss();
                    mVideoView.start();
                }
            });

        }
}
