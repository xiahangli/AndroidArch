package com.example.player.core;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.base.base.util.CollectionUtils;
import com.example.player.R;
import com.example.player.model.MediaObject;

import java.util.ArrayList;

/**
 * @User Xiahangli
 * @Date 2019-12-06  10:38
 * @Email henryatxia@gmail.com
 * @Descrip recyclerview基础适配器，用于承载播放器的
 */
public class VideoPlayerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MediaObject> mediaObjects;
    private RequestManager requestManager;


    /**
     * constructor
     *
     * @param mediaObjects   播放器显示相关的实体
     * @param requestManager 加载封面的glide请求管理器
     */
    public VideoPlayerRecyclerAdapter(ArrayList<MediaObject> mediaObjects, RequestManager requestManager) {
        this.mediaObjects = mediaObjects;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//     third pram:   If false, root is only used to create the correct subclass of LayoutParams for the root view in the XML.
        return new VideoPlayerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.player_video_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoPlayerViewHolder) {//绚烂界面
            if (CollectionUtils.sizeValid(mediaObjects)) {
                ((VideoPlayerViewHolder) holder).onBindViewHolder(mediaObjects.get(position),requestManager );
            }
        }
    }

    @Override
    public int getItemCount() {
        return CollectionUtils.sizeValid(mediaObjects) ? mediaObjects.size() : 0;
    }
}
