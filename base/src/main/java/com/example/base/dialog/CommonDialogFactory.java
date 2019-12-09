package com.example.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.base.R;

/**
 * @author Henry
 * @Date 2019-12-09  09:15
 * @Email 2427417167@qq.com
 */
public class CommonDialogFactory extends DialogFactory {

    private Builder builder;//当前CommonDialogFactory的构造参数
    private DialogCallback positiveCallback;//确认回调
    private DialogCallback negativeCallback;//取消回调
    private BaseDialog dialog;
    private View view;

    public CommonDialogFactory(Builder builder){
        this.builder = builder;
        //为了方便调用，赋值成成员变量
        this.positiveCallback = builder.positiveCallback;
        this.negativeCallback = builder.negativeCallback;
    }

    @Override
    public Dialog createDialog() {
        int layoutId = R.layout.common_dialog;//提供布局
        if (builder.dialogType == DialogType.NORMAL){
            layoutId = R.layout.common_dialog;
        }
        //根据布局和尺寸创建dialog并初始化相关页面与监听事件
        switch (builder.dialogSize){
            case LARGE:
                return createSmallDialog(layoutId);
            case MIDDLE:
            default:
                return createMiddleDialog(layoutId);

        }

    }

    /**
     * 创建dialog,如要显示dialog,使用{@link Dialog}的show方法
     * @param layoutId dialog整体布局
     * @return
     */
    private BaseDialog createSmallDialog(@LayoutRes  int layoutId) {
        dialog = new BaseDialog.Builder(builder.context,builder.themeResId)
                .setHeight(builder.context.getResources().getDimensionPixelSize(R.dimen.dp_px_200))
                .setHeight(builder.context.getResources().getDimensionPixelSize(R.dimen.dp_px_200))
                .setView(layoutId)//提供根布局
                .build();
        View view = dialog.getView();
        initView(view);
        return dialog;
    }

    private Dialog createMiddleDialog(int layoutId){
        dialog = new BaseDialog.Builder(builder.context,builder.themeResId)
                .setHeight(builder.context.getResources().getDimensionPixelSize(R.dimen.dp_px_300))
                .setHeight(builder.context.getResources().getDimensionPixelSize(R.dimen.dp_px_300))
                .setView(layoutId)
                .build();
        View view = dialog.getView();
        initView(view);
        return dialog;
    }

    /**
     *初始化布局，监听等
     * @param view
     */
    private void initView(View view) {
       TextView titleView =  view.findViewById(R.id.title_view);
       titleView.setText(builder.title);
        FrameLayout mainLayout = view.findViewById(R.id.main_layout);
        if (builder.messageView != null) {//提供了外部布局
            mainLayout.removeAllViews();//移除容器内部所有view
            mainLayout.addView(builder.messageView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }else{//使用默认的详情布局
            TextView messageView = view.findViewById(R.id.message_view);
            messageView.setMovementMethod(ScrollingMovementMethod.getInstance());//message可以滚动显示
            messageView.setText(builder.message);
        }

        //取消安妮
        Button cancelButton = view.findViewById(R.id.cancel_view);
        if (builder.negativeButtonText !=null)
            cancelButton.setText(builder.negativeButtonText);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //代理到用户设置的监听
                if (negativeCallback != null)
                    negativeCallback.onClick(dialog);//业务层点击事件回调
                dialog.dismiss();//销毁弹框
            }
        });

        //点击确认按钮
        Button sureButton = view.findViewById(R.id.ensure_view);
        if (builder.positiveButtonText !=null)
            sureButton.setText(builder.positiveButtonText);

        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (positiveCallback !=null)
                    positiveCallback.onClick(dialog);
                dialog.dismiss();
            }
        });

    }


    /**
     * 建造者类，对外构建dialog
     */
    public static final class Builder{
        private Context context;
        private int themeResId;
        private DialogType dialogType = DialogType.NORMAL;
        private DialogSize dialogSize = DialogSize.SMALL;
        private CharSequence title;//标题
        private CharSequence message;//具体消息
        private DialogCallback positiveCallback;//确定
        private DialogCallback negativeCallback;//取消
        private View messageView;
        private String positiveButtonText;
        private String negativeButtonText;

        private boolean cancalable = true;//是否点击外部可取消

        public Builder(@NonNull Context context){
            this(context,0);
        }

        public Builder(@NonNull Context context,int themeResId){
            this.context = context;
            this.themeResId = themeResId;
        }

        public Builder setTitle(@Nullable CharSequence message){
            this.message = message;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean cancelable){
            this.cancalable = cancelable;
            return this;
        }

        public Builder setDialogType(@NonNull DialogSize dialogSize){
            this.dialogSize = dialogSize;
            return this;
        }

        public Builder setPositiveButtonText(@NonNull String positiveButtonText){
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        public Builder setNegativeButtonText(@NonNull String negativeButtonText){
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        /**
         *外部提供布局
         */
        public Builder setMessageView(View messageView){
            this.messageView = messageView;
            return this;
        }

        public Dialog build(){
            CommonDialogFactory factory = new CommonDialogFactory(this);//创建工厂实例
            return factory.createDialog();//创建dialog
        }

    }
}
