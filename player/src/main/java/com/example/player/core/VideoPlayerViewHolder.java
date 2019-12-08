package com.example.player.core;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.base.base.util.ObjectUtils;
import com.example.player.R;
import com.example.player.model.MediaObject;

/**
 * ViewHolder缓存类，是RecyclerView组件的缓存类
 */
public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {

    /**
     * 包级别访问权限，也即VideoPlayerViewHolder与访问他的类需要在同一个包名下
     */
    FrameLayout mediaContainer;
    TextView title;
    ImageView thumbnail, volumeControl;
    ProgressBar progressBar;
    View itemView;
    RequestManager requestManager;

    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        mediaContainer = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.title);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volume_control);
    }


    public void onBindViewHolder(MediaObject mediaObject, RequestManager requestManager) {
        ObjectUtils.requireNonNull(requestManager, "RequestManager is null");
        this.requestManager = requestManager;
        //这里为itemView设置了一个tag标记，
        //后面在滑动recyclerview需要播放特定播放器的时候根据itemView拿到tag标记的ViewHolder
        itemView.setTag(this);
        title.setText(mediaObject.getTitle());
        //glide加载封面
        this.requestManager
                .load(mediaObject.getThumbnail())
                .into(thumbnail);
    }

}