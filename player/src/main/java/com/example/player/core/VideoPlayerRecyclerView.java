package com.example.player.core;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.example.player.model.MediaObject;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.ArrayList;


/**
 * 可以播放视频的recyclerview组件
 *
 * 该VideoPlayerRecyclerView全局维护一个{@link PlayerView}，一个{@link com.google.android.exoplayer2.ExoPlayer}，在滑动列表的时候
 * 根据用户滑动的情况判断(见playVideo方法)当前要显示播放器的item,不断将PlayerView从旧的item上移除，添加到新的item上(在ExoPlayer状态为ready时添加)，同时
 * 根据DataSource.Factory创建MediaSourceFactory，进而创建指定url源的MediaSource，有了MediaSource,Exoplayer可以播放当前item对应音视频，并由于在ready的时候
 * Exoplayer与当前item的surfaceView绑定（PlayerView内部默认的视频显示是通过SurfaceView），所以可以看到surfaceview显示视频界面
 * @author Henry
 * @Date 2019-12-06  16:47
 * @Email 2427417167@qq.com
 */
public class VideoPlayerRecyclerView extends RecyclerView {

    // ui
    private ImageView thumbnail, volumeControl;
    private ProgressBar progressBar;
    private View itemView;//当前itemView
    private FrameLayout playerContainer;//当前playerView的父容器
    private PlayerView playerView;//全局一个PlayerView组件,默认是使用SurfaceView作为视图展示组件
    private SimpleExoPlayer player;

    // vars
    private ArrayList<MediaObject> mediaObjects = new ArrayList<>();
    private int videoSurfaceDefaultHeight = 0;//视频的默认高度
    private int screenDefaultHeight = 0;//屏幕高度
    private Context context;
    private int playPosition = -1;//全局记录当前正在播放的item位置
    private boolean isPlayerViewAdded;
    //private RequestManager requestManager;
    private String TAG = "VideoPlayerRecyclerView";

    public VideoPlayerRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public VideoPlayerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context.getApplicationContext();
        playerView = new PlayerView(this.context);
        //创建播放器,其中的大部分设置都是使用默认的，如DefaultTrackSelector
        player = ExoPlayerFactory.newSimpleInstance(context);

        //设置尺寸模式
        playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
        playerView.setUseController(true);
        //监听滑动的状态改变，在state_idle的时候，即The RecyclerView is not currently scrolling.
        addOnScrollListener(onScrollChangeListener);
        player.addListener(eventListener);
        //添加子视图的界面显示状态监听，在播放视频的item移除窗口时，从recyclerview中移除播放器视图
        addOnChildAttachStateChangeListener(onChildAttachStateChangeListener);
    }


    private void playVideo(boolean isEndOflist) {
        //1、寻找目标播放的位置
        int targetPosition;
        if (!isEndOflist) {//不是列表的边界item
            //得到recyclerview可见视图中的第一个item的position
            int startPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
            //同理，得到可见视图的最后一个item的position位置
            int endPosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();

            if (startPosition < 0 || endPosition < 0) {//错误处理
                return;
            }

            if (endPosition - startPosition > 1) {//可视视图中不止2个item的时候
                endPosition = startPosition + 1;//我们只考虑起始的position（startPosition）和下一个startPosition+1
            }

            if (endPosition != startPosition) {//视图至少2个item
                int startPositionVideoHeight = getVisibleVideoSurfaceHeight(startPosition);//获得可视视图中第一个视频可见高度
                int endPositionVideoHeight = getVisibleVideoSurfaceHeight(endPosition);//获得下一个视频的可见高度
                targetPosition = startPositionVideoHeight > endPositionVideoHeight ? startPosition : endPosition;
            } else {
                targetPosition = startPosition;
            }
        } else {//播放最后一个视频
            targetPosition = mediaObjects.size() - 1;
        }
        //2、播放视频
        if (targetPosition == playPosition) return;//如果要播放的视频就是正在播放的视频，无需播放
        playPosition = targetPosition;//更新播放的位置

        //先移除后添加
        if (playerView != null) {//videoSurfaceView不应该为null
            playerView.setVisibility(INVISIBLE);
            removePlayerView();//将playerview从recyclerview中移除
        }

        //得到item,以及更新recyclerview保存的当前item的组件的引用
        int firstVisibleItemPosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int childPosition = targetPosition - firstVisibleItemPosition;
        View item = getChildAt(childPosition);//得到播放的item
        if(item == null)return;//异常
        VideoPlayerViewHolder viewHolder = (VideoPlayerViewHolder) item.getTag();
        if (viewHolder == null)return;//异常
        thumbnail = viewHolder.thumbnail;//更新recyclerview中当前播放器正在播放的item的封面
        playerContainer = viewHolder.playerContainer;//playerview的父容器
        //requestManager = viewHolder.requestManager;//加载图片的请求管理器
        itemView = viewHolder.itemView;//
        progressBar = viewHolder.progressBar;//界面加载时候的圆形进度条

        //！重点：当前item的surfaceView与Exoplayer绑定，实现播放内容显示当前item的SurfaceView中
        playerView.setPlayer(player);

        //播放视频
        //创建一个默认的DataSource.Factory，DataSource.Factory用于创建DataSource，而DataSource是给meidiaSource提供流的
        DataSource.Factory datasourceFactory = new DefaultDataSourceFactory(getContext(), "androidArch");
        //]DataSource dataSource = datasourceFactory.createDataSource();//利用工厂创建数据源,最新版的exoplayer推荐使用ProgressiveMediaSourcFactory代替ExtractorMediaSource.Factory
        ProgressiveMediaSource.Factory factory = new ProgressiveMediaSource.Factory(datasourceFactory);
        String mediaUrl = mediaObjects.get(targetPosition).getMediaUrl();//拿到对应的媒体url
        if (mediaUrl != null) {
            Uri mediaUri = Uri.parse(mediaUrl);
            ProgressiveMediaSource progressiveMediaSource = factory.createMediaSource(mediaUri);//ProgressiveMediaSource与exoplayer组件绑定
            player.prepare(progressiveMediaSource);//ExoPlayer开始准备MediaSource
            player.setPlayWhenReady(true);//设置PlayerWhenReady变量，当准备ok的时候开始自动播放

        }
    }

    /**
     * 将itemview恢复成默认的样子，包括移除playerView,各种播放器相关状态变量复原
     */
    private void resetPlayerView(){
        if (isPlayerViewAdded){//添加过则移除playerView
            removePlayerView();
            playPosition = -1;
            thumbnail.setVisibility(VISIBLE);
            playerView.setVisibility(INVISIBLE);//将组件隐藏
        }
    }


    /**
     * 从recycler中移除playerview
     */
    private void removePlayerView() {
        ViewGroup parent = (ViewGroup) playerView.getParent();
        if (parent == null) return;
        int indexOfPlayerView = parent.indexOfChild(playerView);
        if (indexOfPlayerView >= 0) {
            parent.removeViewAt(indexOfPlayerView);//从父容器中移除playerview组件
            isPlayerViewAdded = false;//标记playerview没有添加到rv中
        }
    }

    /**
     * 添加videoview到recyclerview中
     */
    private void addVideoView() {
        //addView之前都是移除过的
        playerContainer.addView(playerView);
        playerView.setVisibility(VISIBLE);
        isPlayerViewAdded = true;
        thumbnail.setVisibility(GONE);

    }


    /**
     * 得到某个playPosition中可以看得到的视频的高度，
     * 如果视频部分可见，返回值小于videoSurfaceDefaultHeight
     *
     * @param playPosition 待检测的视频的所在的item位置 0开始
     * @return 可见的视频的高度
     */
    private int getVisibleVideoSurfaceHeight(int playPosition) {
        //找到第一个visible的item的位置
        int visiblePosition = playPosition - ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        //!getChildAt得到的是可视item的view,不在视图区域的得到的可能是null
        View item = getChildAt(visiblePosition);

        int[] location = new int[2];
        //得到item在 window中的位置，放入location数组中.location[0]代表item左上角在window的x坐标，location[1]为y坐标
        item.getLocationInWindow(location);

        //视频部分被挡
        if (location[1] < 0) {//视频上半部分被挡住，这时候startPostion的videoHeight是需要减去被挡住的|location[1]|值
            return location[1] + videoSurfaceDefaultHeight;
        } else {
            return screenDefaultHeight - location[1];//没有遮挡，那么需要screenheight-y,因为有可能该视频下部分被挡住
        }
    }


    public void setMediaObjects(ArrayList<MediaObject> mediaObjects) {
        this.mediaObjects = mediaObjects;
    }

    /**
     * 释放exoPlayer播放器组件
     */
    public void releasePlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
        itemView = null;
    }

    private OnChildAttachStateChangeListener onChildAttachStateChangeListener = new OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {

        }

        /**
         *item移出窗口的时候会回调,view是移除的item
         */
        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {
            if(itemView!=null&&itemView.equals(view)){//当前正在播放的视频移出窗口了
                resetPlayerView();//那么将播放器视图移出recyclerview,并设置不可见
            }
        }
    };

    /**
     * 监听用户滑动行为
     */
    private OnScrollListener onScrollChangeListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == SCROLL_STATE_IDLE) {
                Log.d(TAG, "onScrollStateChanged: called.");
                if (thumbnail != null) { // show the old thumbnail
                    thumbnail.setVisibility(VISIBLE);
                }

                //处理滑到最上面和最下面的边界case
                if (!recyclerView.canScrollVertically(1)) {
                    Log.d(TAG, "onScrollStateChanged: cannot go down");
                    playVideo(true);
                } else {
                    playVideo(false);
                }
            }
        }
    };

    private Player.EventListener eventListener = new Player.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray
        trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {//根据播放状态改变UI
                case Player.STATE_BUFFERING:
                    Log.e(TAG, "onPlayerStateChanged: Buffering video.");
                    if (progressBar != null) {
                        progressBar.setVisibility(VISIBLE);
                    }

                    break;
                case Player.STATE_ENDED://播放完成seek到起始位置
                    Log.d(TAG, "onPlayerStateChanged: Video ended.");
                    player.seekTo(0);
                    break;
                case Player.STATE_IDLE:

                    break;
                case Player.STATE_READY://添加playerview视图到item
                    Log.e(TAG, "onPlayerStateChanged: Ready to play.");
                    if (progressBar != null) {
                        progressBar.setVisibility(GONE);
                    }
                    if(!isPlayerViewAdded){
                        addVideoView();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {

        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {

        }

        @Override
        public void onPositionDiscontinuity(int reason) {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

        @Override
        public void onSeekProcessed() {

        }
    };


}
