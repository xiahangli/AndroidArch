package com.example.base.dialog;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

import com.example.base.R;

import java.lang.ref.WeakReference;

/**
 * 基础的Dialog实现,dialog弹出在activity之上
 * @author Henry
 * @Date 2019-12-09  09:04
 * @Email 2427417167@qq.com
 */
public class BaseDialog extends AppCompatDialog {
    private int height;
    private int width;
    private Drawable dialogBackgroundDrawable;
    private boolean cancelable;//是否点击外部区域弹框消失
    private View view;
    //使用weakReference的原因是当只有该BaseDialog对activity弱应用可达时候，使activity可以被正常销毁防止activity内存泄漏
    private WeakReference<Activity> activityWeakReference ;
    private Application application;//应用上下文

    public BaseDialog(Builder builder) {
        this(builder,0);//没有主题
    }

    /**
     * dialog初始化
     * @param builder
     * @param themeResId
     */
    public BaseDialog(Builder builder, int themeResId) {
        super(builder.context, themeResId);
        //获取到builder中的值，为下面onCreate时穿件弹框的样式做准备
        height = builder.height;
        width = builder.width;
        view = builder.rootView;
        dialogBackgroundDrawable = builder.dialogBackgroundDrawable;
        this.cancelable = builder.cancelable;
        registerLifecycleCallback(builder.context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(view);
        setCanceledOnTouchOutside(cancelable);
        Window window = getWindow();
        if (window!=null){
            if (dialogBackgroundDrawable!=null)
                window.setBackgroundDrawable(dialogBackgroundDrawable);
            else
                //这里getContext得到的是dialog上下文,背景是白色圆角
                window.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.common_white_conor_dialog_bg));
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            //居中显示
            layoutParams.gravity = Gravity.CENTER;
            //设置宽高
            if (height > 0)
                layoutParams.height = height;
            else
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            if (width > 0)
                layoutParams.width = width;
            else
                layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            //应用宽高
            window.setAttributes(layoutParams);
        }

    }

    /**
     * 同步Activity生命周期与dialog生命周期
     * @param context activity
     */
    private void registerLifecycleCallback(Context context) {
        if (context instanceof Activity){
            activityWeakReference = new WeakReference<Activity>((Activity) context);//将activity弱引用包装
            application = ((Activity) context).getApplication();
            application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        }
    }

    /**
     * 弹框销毁
     */
    @Override
    public void dismiss() {
        super.dismiss();
        unRegisterLifecyclerCallback();
    }

    private void unRegisterLifecyclerCallback(){
        if (application!=null)
            application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }



    //解决Resources$NotFoundException，当传入了Application类作为context时，getTheme后得到resolveAttribute得到的值
    //会放到outValue的resourceId中，这时候获取到的resourceId（themeResId）为0
    private static int resolveDialogTheme(@NonNull Context context,@StyleRes int themeResId){
        //检测是否资源id是否合法
        //无符号右移动24，资源id是32bit,
        if ( ((themeResId >>>24)&0x000000ff)>=0x00000001 ){//资源合法
          return themeResId;
        }else{//不合法设置默认alertDialogTheme主题
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.alertDialogTheme,outValue,true);//生成的resourceId放入TypeValue中
            return outValue.resourceId;//返回alertDialogTheme资源id
        }
    }

    /**
     * 对外提供当前的dialog布局文件
     * @return 布局文件
     */
    public View getView() {
        return view;
    }

    /**
     * 返回当前dialog的宽度
     * @return dialog宽度
     */
    public int getWidth() {
        Window window = getWindow();
        if (window!=null){
           return window.getAttributes().width;
        }
        return 0;
    }

    public static class Builder{
        private Context context;
        private @StyleRes int themeResId;
        private View rootView;
        private int height;
        private int width;
        private boolean cancelable;
        private Drawable dialogBackgroundDrawable;

        public Builder(@NonNull  Context context){
            this(context,resolveDialogTheme(context,0));
        }

        public Builder(@NonNull  Context context,@StyleRes  int themeResId ){
            this.context = context;
            this.themeResId = themeResId;
        }

        public Builder setView(@NonNull  View rootView){
            this.rootView = rootView;
            return this;
        }

        public Builder setView(@LayoutRes int layoutId){
            this.rootView = LayoutInflater.from(context).inflate(layoutId,null);//不提供root布局
            return this;
        }

        public Builder setHeight(int height){
            this.height = height;
            return this;
        }

        public Builder setWidth(int width){
            this.width = width;
            return this;
        }

        public Builder setCancelable(boolean cancelable){
            this.cancelable = cancelable;
            return this;
        }


        public Builder setBackgroundDrawable(Drawable dialogBackgroundDrawable){
            this.dialogBackgroundDrawable =dialogBackgroundDrawable;
            return this;
        }


        public BaseDialog build(){
            return new BaseDialog(this,themeResId);
        }
    }

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks  = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {

        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
           //销毁dialog ,依据是正在显示，同时销毁的activity正是绑定在这个dialog上的activity
            if (isShowing() && activityWeakReference.get() !=null && activity.equals(activityWeakReference.get())){
                dismiss();
            }
        }
    };
}
