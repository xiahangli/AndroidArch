package com.example.arch.onepixel;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arch.onepixel.manager.OnePixelActivityManager;
import com.example.base.base.util.SystemUtils;

/**
 * @User Xiahangli
 * @Date 2019-12-04  22:52
 * @Email henryatxia@gmail.com
 * @Descrip 用于1像素保活
 */
public class OnePixelActivity extends AppCompatActivity {
    private OnePixelActivityManager onePixelActivityManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindowAttributes();
        registActivity();
    }

    private void registActivity() {
        onePixelActivityManager =  OnePixelActivityManager.obtain();
        onePixelActivityManager.registerOnePixelActivity(this);//将引用传给OnePixelActivityManager
    }

    /**
     * 放置在左上角
     */
    private void setWindowAttributes(){
        Window window = getWindow();
        window.setGravity(Gravity.LEFT|Gravity.TOP);//左上角
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
    }
}
