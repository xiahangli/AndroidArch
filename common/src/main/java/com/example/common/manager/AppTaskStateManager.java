package com.example.common.manager;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @User Xiahangli
 * @Date 2019-12-04  15:07
 * @Email henryatxia@gmail.com
 * @Descrip app 状态管理器，包括是否在前后台显示
 * 多线程竞态的环境下
 */
public final class AppTaskStateManager {
    private static volatile boolean hasInit;

    private static AppTaskStateManager instance;

    private Map<Object, WeakReference<AppStateListener>> appStateListeners;

    private Map<Object, WeakReference<ActivityLifecycleCallbacks>> activityLifecycleCallbacks;


    public void registerAppState(Object object, AppStateListener appStateListener) {

    }


    /**
     * 初始化应用的task状态
     *
     * @param application app
     */
    public static synchronized void initialize(Application application) {
        if (!hasInit) {
            if (application == null) throw new IllegalArgumentException("application is null");//
            hasInit = true;
            instance = new AppTaskStateManager(application);
        }

    }

    /**
     * 私有构造函数
     *
     * @param application
     */
    private AppTaskStateManager(Application application) {
        appStateListeners = new HashMap<>(4);//初始化bucket容量为4的map
        activityLifecycleCallbacks = new HashMap<>(4);
        //注册lifecycler,在生命周期发生改变的时候通知各个activity生命周期的情况
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                //创建activity的时候将activity添加到管理器中
                ActivityStackManager.getInstance().addActivity(activity);
                //同步那些需要监听生命周期的activity
                notifyActivityLifecycle(activity, ActivityLifecycleCallbacks.onCreate, savedInstanceState);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                notifyActivityLifecycle(activity, ActivityLifecycleCallbacks.onStart);
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                notifyActivityLifecycle(activity, ActivityLifecycleCallbacks.onResume);
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                notifyActivityLifecycle(activity, ActivityLifecycleCallbacks.onPause);
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                notifyActivityLifecycle(activity, ActivityLifecycleCallbacks.onStop);
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                notifyActivityLifecycle(activity, ActivityLifecycleCallbacks.onsaveInstance,outState);
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                notifyActivityLifecycle(activity, ActivityLifecycleCallbacks.onDestroy);
            }
        });
    }

    /**
     * 不带状态的通知,调用3参的重载方法
     *
     * @param state 生命周期状态
     */
    private void notifyActivityLifecycle(Activity activity, int state) {
        notifyActivityLifecycle(activity, state, null);
    }

    /**
     * 在map中查找要通知的acitivity,找到则通知activity的生命周期
     *
     * @param state  生命周期状态
     * @param bundle bundle数据存储值
     */
    private void notifyActivityLifecycle(@NonNull Activity activity, @ActivityLifecycleCallbacks.ActivityLifecycle int state, @Nullable Bundle bundle) {
        //拿到entries集合
        Set<Map.Entry<Object, WeakReference<ActivityLifecycleCallbacks>>> entries = activityLifecycleCallbacks.entrySet();
        //迭代entries,迭代的元素是WeakReference<ActivityLifecycleCallbacks>
        Iterator<Map.Entry<Object, WeakReference<ActivityLifecycleCallbacks>>> iterator = entries.iterator();

        while (iterator.hasNext()) {
            Map.Entry<Object, WeakReference<ActivityLifecycleCallbacks>> next = iterator.next();//todo next有可能返回null
            if (next != null) {
                ActivityLifecycleCallbacks activityLifecycleCallbacks = next.getValue().get();//todo getValue返回值是否可能为null
                //存在ActivityLifecycleCallbacks，则回调监听者
                if (activityLifecycleCallbacks != null) {
                    activityLifecycleCallbacks.onActivityLifecycle(activity, state, bundle);//回调要监听的activity
                } else {
                    iterator.remove();
                }
            } else {
                iterator.remove();
            }
        }
    }


    private interface AppStateListener {
        /**
         * app退到后台
         */
        void appToBackground();

        /**
         * app回到前台
         */
        void appToForeground();
    }

    //todo 接口是否合理
    public interface ActivityLifecycleCallbacks {
        /**
         * 7中生命周期状态监听,多个状态使用{}组合
         */
        @IntDef({onCreate, onStart, onRestart, onPause, onsaveInstance, onStop, onDestroy})
        @interface ActivityLifecycle {

        }

        int onCreate = 0;
        int onStart = 1;
        int onRestart = 2;
        int onPause = 4;
        int onStop = 5;
        int onDestroy = 6;
        int onsaveInstance = 7;
        int onResume = 8;

        /**
         * @param activity  考察的activity
         * @param lifecycle 当前的生命周期
         * @param bundle    数据信息
         */
        void onActivityLifecycle(@NonNull Activity activity, @ActivityLifecycle int lifecycle, @Nullable Bundle bundle);

    }
}
