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
     * @param requestManager 加载封面的glide请求管理器，全局只有一个
     */
    public VideoPlayerRecyclerAdapter(ArrayList<MediaObject> mediaObjects, RequestManager requestManager) {
        this.mediaObjects = mediaObjects;
        this.requestManager = requestManager;
    }

    /**
     * 创建缓存item的VH
     * @param parent item的父布局，也就是recyclerView
     * @param viewType view的类型，可通过getItemViewType指定
     * @return VH
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  attachToRoot设为false, root is only used to create the correct subclass of LayoutParams for the root view in the XML.
        return new VideoPlayerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.player_video_list_item, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     *绑定渲染item界面
     * @param holder 待绑定的viewholder
     * @param position adapter数据集的item的位置
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VideoPlayerViewHolder) {//绚烂界面
            if (CollectionUtils.sizeValid(mediaObjects)) {
                ((VideoPlayerViewHolder) holder).onBindViewHolder(mediaObjects.get(position),requestManager );
            }
        }
    }

    /**
     * 数据源的个数，必须指定
     * @return 总数据源的个数,大于等于0
     */
    @Override
    public int getItemCount() {
        return CollectionUtils.sizeValid(mediaObjects) ? mediaObjects.size() : 0;
    }
}
