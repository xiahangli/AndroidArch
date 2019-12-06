package com.example.arch.onepixel.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.arch.onepixel.OnePixelActivity;
import com.example.base.base.util.ObjectUtils;

import java.lang.ref.WeakReference;

/**
 * @User Xiahangli
 * @Date 2019-12-05  10:58
 * @Email henryatxia@gmail.com
 * @Descrip 单例模式，管理1px Activity的启动与销毁，
 */
public class OnePixelActivityManager  {
    private WeakReference<Activity> activityRef;

    /**
     * 注意这里使用单例的模式，每次系统发送
     */
    private OnePixelActivityManager(){

    }

    private final static class OnePixelActivityManagerHolder{
        private static final OnePixelActivityManager instance = new OnePixelActivityManager();
    }

    public static OnePixelActivityManager obtain(){
        return OnePixelActivityManagerHolder.instance;
    }

    /**
     * 保存{@link OnePixelActivity}的引用，用于后面监测到屏幕亮时关闭
     * 这里使用弱引用的原因在于，若系统回收了{@link OnePixelActivity}那么，就不需要保存
     * @param onePixActivity 1px 的activity
     */
    public void registerOnePixelActivity(OnePixelActivity onePixActivity){
        ObjectUtils.requireNonNull(onePixActivity,"activity is null");
        activityRef = new WeakReference<Activity>(onePixActivity);
    }

    public void startOnePixelActivity(Context context) {
        ObjectUtils.requireNonNull(context,"context is null");
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//开启新的任务栈
        intent.setClass(context, OnePixelActivity.class);
        context.startActivity(intent);
    }

    public void finishOnePixelActivity(){
        ObjectUtils.requireNonNull(activityRef,"onePixActivityWeakRef is null");//减少if else的判断
        Activity activity= activityRef.get();
        ObjectUtils.requireNonNull(activity,"onePixActivity is null");
        finishActivity(activity);
        activityRef = null;
    }

    private void finishActivity(Activity activity){
        ObjectUtils.requireNonNull(activity,"activity is null");
        activity.finish();
    }
}
