package com.example.base.dialog;

import android.app.Dialog;

/**
 * @author Henry
 * @Date 2019-12-09  09:37
 * @Email 2427417167@qq.com
 */
public interface DialogCallback {
    /**
     * dialog上按钮的点击回调
     * @param dialog 当前的dialog
     */
    void onClick(Dialog dialog);
}
