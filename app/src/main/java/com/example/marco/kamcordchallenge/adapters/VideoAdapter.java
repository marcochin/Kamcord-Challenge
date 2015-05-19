package com.example.marco.kamcordchallenge.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.marco.kamcordchallenge.R;
import com.example.marco.kamcordchallenge.network.VolleySingleton;
import com.example.marco.kamcordchallenge.pojo.VideoInfo;

import java.util.List;

/**
 * Created by Marco on 2/9/2015.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>{
    private LayoutInflater mInflater;
    private List<VideoInfo> mVideoInfoList;
    private OnThumbnailClickListener mOnThumbnailClickListener;

    public VideoAdapter(Context context, List<VideoInfo> videoInfo) {
        //Need inflater to convert xml to a view object;
        mInflater = LayoutInflater.from(context);
        mVideoInfoList = videoInfo;
    }

    @Override
    //In charge of creating a instance of the holder.
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.video_row, parent, false);
        VideoViewHolder holder = new VideoViewHolder(view);
        return holder;
    }

    @Override
    //In charge of setting the information of the holder that gets passed in.
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final VideoInfo videoInfo = mVideoInfoList.get(position);
        holder.mVideoTitle.setText(videoInfo.getTitle());
        holder.mVideoThumbnail.setImageUrl(videoInfo.getThumbnail(), VolleySingleton.getInstance().getImageLoader());

        //Allows thumbnail to be clickable.
        holder.mVideoThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Delegates the call to the main activity's clickListener because the Actvity should handle opening activites
                mOnThumbnailClickListener.onThumbnailClick(videoInfo.getVideoUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideoInfoList.size();
    }


    //ViewHolder holds references to the views, so findViewById doesn't get called numerous times.
    class VideoViewHolder extends RecyclerView.ViewHolder{
        TextView mVideoTitle;

        //NetworkImageView is a Custom View from Volley Library
        NetworkImageView mVideoThumbnail;

        public VideoViewHolder(View itemView) {
            super(itemView);

            mVideoTitle = (TextView)itemView.findViewById(R.id.video_title);
            mVideoThumbnail = (NetworkImageView)itemView.findViewById((R.id.video_thumbnail));
        }
    }

    //Interface for the MainActivity to implement
    public interface OnThumbnailClickListener{
        public void onThumbnailClick(String videoUrl);
    }

    public void setOnThumbnailClickListener(OnThumbnailClickListener onThumbnailClickListener){
        mOnThumbnailClickListener = onThumbnailClickListener;
    }
}
