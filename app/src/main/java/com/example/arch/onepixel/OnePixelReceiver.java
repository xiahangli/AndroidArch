package com.example.arch.onepixel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.arch.onepixel.manager.OnePixelActivityManager;
import com.example.base.base.util.ObjectUtils;

/**
 * @User Xiahangli
 * @Date 2019-12-05  10:36
 * @Email henryatxia@gmail.com
 * @Descrip 接收息屏亮屏广播用于1px的activity的开启和关闭 ，
 *          注：OnePixelReceiver的生命周期与OnePixelService的生命周期一致,service销毁则广播接收器销毁
 */
public class OnePixelReceiver extends BroadcastReceiver {
    private OnePixelActivityManager onePixelActivityManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        onePixelActivityManager = OnePixelActivityManager.obtain();
        ObjectUtils.requireNonNull(action,"action is null");
        if (action.equals(Intent.ACTION_SCREEN_ON)){
            onePixelActivityManager.finishOnePixelActivity();//后台服务OnePixelService在屏幕亮的时候关闭1px Activity
        } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
            onePixelActivityManager.startOnePixelActivity(context);
        }
    }
}
