package com.example.player;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.player.core.VideoPlayerRecyclerAdapter;
import com.example.player.core.VideoPlayerRecyclerView;
import com.example.player.model.MediaObject;
import com.example.player.model.MediaResources;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 测试播放器的代码
 * @User Xiahangli
 * @Date 2019-12-06  16:15
 * @Email henryatxia@gmail.com
 */
public class TestPlayerActivity extends AppCompatActivity {

    private VideoPlayerRecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.player_activity_test_player);
        recyclerView = findViewById(R.id.recycler_view);
        initRecyclerView();//显示recycler内容
    }
    private void initRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        recyclerView.addItemDecoration(itemDecorator);

        ArrayList<MediaObject> mediaObjects = new ArrayList<MediaObject>(Arrays.asList(MediaResources.MEDIA_OBJECTS));
        recyclerView.setMediaObjects(mediaObjects);
        VideoPlayerRecyclerAdapter adapter = new VideoPlayerRecyclerAdapter(mediaObjects, initGlide());
        recyclerView.setAdapter(adapter);
    }


    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.player_white_background)
                .error(R.drawable.player_white_background);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }


    @Override
    protected void onDestroy() {
        if(recyclerView !=null)
            recyclerView.releasePlayer();
        super.onDestroy();
    }
}
