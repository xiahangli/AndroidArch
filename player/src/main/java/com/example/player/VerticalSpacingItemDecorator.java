package com.example.player;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Henry
 * @Date 2019-12-06  16:48
 * @Email 2427417167@qq.com
 */
public class VerticalSpacingItemDecorator extends RecyclerView.ItemDecoration {

    private int verticalGap;
    public VerticalSpacingItemDecorator(int verticalGap){
        this.verticalGap = verticalGap;
    }

    /**修改item的间距为距离上一item的高度，可外部配置
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.top = verticalGap;
    }
}
