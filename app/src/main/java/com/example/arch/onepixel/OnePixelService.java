package com.example.arch.onepixel;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.arch.onepixel.manager.OnePixelReceiverManager;

/**
 * @User Xiahangli
 * @Date 2019-12-04  22:52
 * @Email henryatxia@gmail.com
 * @Descrip 监听系统的亮屏息屏广播，，用于1像素保活的服务
 */
public class OnePixelService extends Service {
    private static final String TAG = OnePixelService.class.getName();
    OnePixelReceiverManager manager;



    /**
     * 服务开启的时候，注册当前的service上下文到系统广播中，
     * 并创建广播接收器用于接收系统的亮屏息屏广播（监听广播用于启动1px的activity或者关闭），
     * @param intent
     * @param flags
     * @param startId
     * @return 粘性的服务,如用于music播放等
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        manager = new OnePixelReceiverManager();//创建管理1px亮屏息屏的广播
        manager.register(this);//注册广播接收者
        return START_STICKY;//粘性服务
    }

    /**
     * 服务销毁的时候调用,取消监听亮屏息屏广播
     * 解除【当前service】与【OnePixelReceiverManager绑定的OnePixelReceiver】的关系
     *
     */
    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        manager.unregister(this);//解除注册
        manager = null;//方便回收
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}