/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.deter.TaxManager.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deter.TaxManager.R;
import com.deter.TaxManager.view.BasePopupWindow;

/**
 * 对话框工具箱
 *
 * @author xiaopan
 */
public class DialogUtils {
    /**
     * 显示一个对话框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param title 标题
     * @param message 消息
     * @param confirmButton 确认按钮
     * @param confirmButtonClickListener 确认按钮点击监听器
     * @param centerButton 中间按钮
     * @param centerButtonClickListener 中间按钮点击监听器
     * @param cancelButton 取消按钮
     * @param cancelButtonClickListener 取消按钮点击监听器
     * @param onShowListener 显示监听器
     * @param cancelable 是否允许通过点击返回按钮或者点击对话框之外的位置关闭对话框
     * @param onCancelListener 取消监听器
     * @param onDismissListener 销毁监听器
     * @return 对话框
     */
    public static AlertDialog showAlert(Context context, String title, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener, String centerButton, DialogInterface.OnClickListener centerButtonClickListener, String cancelButton, DialogInterface.OnClickListener cancelButtonClickListener, DialogInterface.OnShowListener onShowListener, boolean cancelable, DialogInterface.OnCancelListener onCancelListener, DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog.Builder promptBuilder = new AlertDialog.Builder(context);
        if (title != null) {
            promptBuilder.setTitle(title);
        }
        if (message != null) {
            promptBuilder.setMessage(message);
        }
        if (confirmButton != null) {
            promptBuilder.setPositiveButton(confirmButton,
                    confirmButtonClickListener);
        }
        if (centerButton != null) {
            promptBuilder.setNeutralButton(centerButton,
                    centerButtonClickListener);
        }
        if (cancelButton != null) {
            promptBuilder.setNegativeButton(cancelButton,
                    cancelButtonClickListener);
        }
        promptBuilder.setCancelable(true);
        if (cancelable) {
            promptBuilder.setOnCancelListener(onCancelListener);
        }
        AlertDialog alertDialog = promptBuilder.create();
        if (!(context instanceof Activity)) {
            alertDialog.getWindow()
                       .setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        alertDialog.setOnDismissListener(onDismissListener);
        alertDialog.setOnShowListener(onShowListener);
        alertDialog.show();
        return alertDialog;
    }


    /**
     * 显示一个对话框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param title 标题
     * @param message 消息
     * @param confirmButton 确认按钮
     * @param confirmButtonClickListener 确认按钮点击监听器
     * @param cancelButton 取消按钮
     * @param cancelButtonClickListener 取消按钮点击监听器
     * @return 对话框
     */
    public static AlertDialog showAlert(Context context, String title, String message, String confirmButton, DialogInterface.OnClickListener confirmButtonClickListener, String cancelButton, DialogInterface.OnClickListener cancelButtonClickListener) {
        return showAlert(context, title, message, confirmButton,
                confirmButtonClickListener, null, null, cancelButton,
                cancelButtonClickListener, null, true, null, null);
    }


    /**
     * 显示一个提示框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message 提示的消息
     * @param confirmButton 确定按钮的名字
     */
    public static AlertDialog showPrompt(Context context, String message, String confirmButton) {
        return showAlert(context, null, message, confirmButton, null, null,
                null, null, null, null, true, null, null);
    }


    /**
     * 显示一个提示框
     *
     * @param context 上下文对象，最好给Activity，否则需要android.permission.SYSTEM_ALERT_WINDOW
     * @param message 提示的消息
     */
    public static AlertDialog showPrompt(Context context, String message) {
        return showAlert(context, null, message, "OK", null, null, null, null,
                null, null, true, null, null);
    }


    public static BasePopupWindow showOprResultPopupWindow(AppCompatActivity activity, final int textResid, final int imgResid) {
        BasePopupWindow popupWindow = new BasePopupWindow(activity, R.layout.layout_ssid_pwdlibrary_opr_popwindow, false) {
            @Override
            public void convert(View contentView) {
                TextView promptTv = getViewById(R.id.ssid_opr_status_tv);
                promptTv.setText(textResid);
                ImageView imageView = getViewById(R.id.ssid_opr_status_iv);
                imageView.setImageResource(imgResid);
                getViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });

                getViewById(R.id.ssid_view).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        dismiss();
                        return true;
                    }
                });
            }
        };

        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        return popupWindow;
    }

    public static Dialog showConfirmAndCancelDialog(Activity acvtivity, String promptStr, View.OnClickListener cancelListener, View.OnClickListener confirmListener) {

        Dialog dialog = showConfirmDialog(acvtivity, promptStr, cancelListener, confirmListener);
        return dialog;
    }

    public static Dialog showConfirmDialog(Activity acvtivity, String promptStr, View.OnClickListener confirmListener) {
        Dialog dialog = showConfirmDialog(acvtivity, promptStr, null, confirmListener);
        return dialog;
    }


    @NonNull
    private static Dialog showConfirmDialog(Activity acvtivity, String promptStr, View.OnClickListener cancelListener, View.OnClickListener confirmListener) {
        Dialog dialog = new Dialog(acvtivity, R.style.Theme_dialog);

        View view = LayoutInflater.from(acvtivity).inflate(R.layout.dialog_reminder_message_confirm_or_cancel_layout, null);

        TextView promptTv = (TextView) view.findViewById(R.id.prompt_contents_tv);
        if (null != promptStr) {
            promptTv.setText(promptStr);
        }

        Button mCancelBtn = (Button) view.findViewById(R.id.btn_reminder_cancel);
        Button mComfirmBtn = (Button) view.findViewById(R.id.btn_reminder_comfirm);

        if (null == cancelListener) {
            mCancelBtn.setVisibility(View.GONE);
            view.findViewById(R.id.split_line_view).setVisibility(View.GONE);
            mComfirmBtn.setBackground(acvtivity.getApplicationContext().getDrawable(R.drawable.dialog_reminder_message_confirm_btn_bottom_bg));
        }

        if (null != cancelListener) {
            mCancelBtn.setOnClickListener(cancelListener);
        }
        mComfirmBtn.setOnClickListener(confirmListener);

        //mReminderDialog = new Dialog(mContext, R.style.Theme_dialog);
        dialog.setContentView(view);

        WindowManager manager = acvtivity.getWindowManager();
        Display display = manager.getDefaultDisplay();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        window.setGravity(Gravity.CENTER);

        params.width = (int) (display.getWidth() * 0.8);
        // params.height=(int) (display.getHeight()*0.4);

        dialog.onWindowAttributesChanged(params);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }


    @NonNull
    public static Dialog showConfirmDialog(Activity acvtivity, int type, View.OnClickListener confirmListener) {
        Dialog dialog = new Dialog(acvtivity, R.style.Theme_dialog);
        View view = LayoutInflater.from(acvtivity).inflate(R.layout.dialog_reminder_synchronize_export_data_layout, null);

        LinearLayout synchronizeDataPartView = (LinearLayout) view.findViewById(R.id.synchronize_data_part_view);
        LinearLayout exportDataPartView = (LinearLayout) view.findViewById(R.id.export_data_part_view);
        TextView reminderTitleTv = (TextView) view.findViewById(R.id.reminder_title_tv);

        if (0 == type) {
            reminderTitleTv.setText(R.string.synchronize_data_success);
            exportDataPartView.setVisibility(View.GONE);
            synchronizeDataPartView.setVisibility(View.VISIBLE);
        } else {
            reminderTitleTv.setText(R.string.ssid_opr_status_export_succeed);
            synchronizeDataPartView.setVisibility(View.GONE);
            exportDataPartView.setVisibility(View.VISIBLE);
        }

        Button mComfirmBtn = (Button) view.findViewById(R.id.btn_reminder_comfirm);
        mComfirmBtn.setOnClickListener(confirmListener);

        //mReminderDialog = new Dialog(mContext, R.style.Theme_dialog);
        dialog.setContentView(view);

        WindowManager manager = acvtivity.getWindowManager();
        Display display = manager.getDefaultDisplay();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        window.setGravity(Gravity.CENTER);

        params.width = (int) (display.getWidth() * 0.8);
        // params.height=(int) (display.getHeight()*0.4);

        dialog.onWindowAttributesChanged(params);

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }


}
