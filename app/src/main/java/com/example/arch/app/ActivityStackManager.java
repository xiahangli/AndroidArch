package com.example.arch.app;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Stack;

/**
 * @User Xiahangli
 * @Date 2019-12-04  13:54
 * @Email henryatxia@gmail.com
 * @Descrip 单例activity状态管理器
 */
public class ActivityStackManager {

    //可以用set,stack,linkedlist但是stack更轻量级，适合
    //用弱引用包装防止内存泄漏
    Stack<WeakReference<Activity>> activityStack;

    private final static class ActivityStackManagerHolder {
        private final static ActivityStackManager instance = new ActivityStackManager();
    }


    public static ActivityStackManager getInstance() {
        return ActivityStackManagerHolder.instance;
    }


    /**
     * activity入栈
     *
     * @param activity 添加的activity
     * @return 是否添加成功
     */
    public synchronized boolean addActivity(Activity activity) {
        if (activity == null) return false;
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        if (!activityStack.contains(new WeakReference<Activity>(activity))) {//存在不添加，不存在添加到栈中
            activityStack.add(new WeakReference<Activity>(activity));//将弱引用包装的activity添加到栈中
            return true;
        }
        return false;
    }

    public synchronized boolean removeActivity(Activity activity) {
//        assert todo asset语句

        if (activity == null || getSize()<=0) {//边界条件
            return false;
        }
        if (activityStack == null)return false;
        Iterator<WeakReference<Activity>> it = activityStack.iterator();//通过迭代器迭代Activity
        while (it.hasNext()){//有下一个元素
            WeakReference<Activity> next = it.next();//iterator.next有可能返回为null,因为Stack
            if (next!=null){
                Activity tmpActivity = next.get();
                if (tmpActivity !=null && tmpActivity.equals(activity)){//移除特定的一个activity
                    it.remove();//移除activity的弱引用WeakReference<Activity>
                    return true;
                }
            }
        }
        //所有遍历完成没有找到，则代表找不到
        return false;
    }

    /**
     * 遍历已有的栈， 返回未被回收的activity size
     * 若弱引用为null，代表被回收掉了，那么就
     *
     * @return 栈深
     */
    private int getSize() {
        int size = 0;
        for (WeakReference<Activity> activityWeakReference :activityStack) {
            size++;
            Activity activity = activityWeakReference.get();
            if (activity !=null){//
                size++;
            }
        }
        return size;
    }

    /**
     * 对外提供
     * @return
     */
    public int getActivityStackSize(){
        return getSize();
    }

    public synchronized void finishAllActivity(){
        if (getSize() == 0)return ;
        Iterator<WeakReference<Activity>> iterator = activityStack.iterator();
        while (iterator.hasNext()){//hasNext就需要remove,因为是移除所有的activity，保证调用这个
            WeakReference<Activity> next = iterator.next();
            if (next!=null){
                Activity activity = next.get();
                if (activity!=null){
                    activity.finish();
                }
            }
            iterator.remove();//移除最后一个元素
        }
    }


    /**
     *
     * todo 是否多线程考虑
     *根据类型删除指定的activity
     * @param clazz 类类型
     * @return 是否执行activity删除操作
     */
    public boolean finishActivity(Class clazz){
        Iterator<WeakReference<Activity>> iterator = activityStack.iterator();
        while (iterator.hasNext()){
            WeakReference<Activity> next = iterator.next();
            if (next!=null){
                Activity activity = next.get();
                if (activity!=null&&activity.getClass().equals(clazz)){//Class.equals(Class)，类类型相等
                    iterator.remove();//先从容器中删除，不管是否删除成功
                    activity.finish();//找到即删除并退出
                    activity = null;
                    return true;
                }
            }
        }
        return false;
    }
    public void  finishActivity(Activity activity){
        if (activity!=null){
            activityStack.remove(new WeakReference<Activity>(activity));//尝试容器中删除,如果移除成功，返回true
            activity.finish();//消除activity
//            activity = null;//
        }
    }



}
