package com.example.arch.onepixel.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.arch.onepixel.OnePixelReceiver;
import com.example.base.base.util.ObjectUtils;

/**
 * @User Xiahangli
 * @Date 2019-12-05  10:24
 * @Email henryatxia@gmail.com
 * @Descrip 注：OnePixelReceiverManager的生命周期与service的生命周期一致
 */
public class OnePixelReceiverManager {

    public BroadcastReceiver receiver;



    public OnePixelReceiverManager(){
        createReceiver(new OnePixelReceiver());
    }

    /**
     * 创建receiver,用于接收系统的息屏亮屏广播
     * @param receiver OnePixelReceiver
     */
    private void createReceiver(BroadcastReceiver receiver){
        this.receiver = receiver;
    }

    /**
     * 用于向系统注册亮屏和息屏的广播
     * @param context 上下文，这里是传service
     */
    public void register(Context context) {
        ObjectUtils.requireNonNull(context,"context is null");
        ObjectUtils.requireNonNull(receiver,"receiver is null");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(receiver,filter);
    }

    /**
     * 取消亮屏和息屏的广播监听，解除OnePixelService与广播接收器OnePixelReceiver的绑定关系
     * @param context service服务
     */
    public void unregister(Context context) {
        ObjectUtils.requireNonNull(context,"context is null");
        ObjectUtils.requireNonNull(receiver,"receiver is null");
        context.unregisterReceiver(receiver);
//        receiver = null;
    }
}
