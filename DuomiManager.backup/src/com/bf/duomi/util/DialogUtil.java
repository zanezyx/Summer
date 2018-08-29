package com.bf.duomi.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.bf.duomi.activity.LoginActivity;
import com.bf.duomi.view.CustomDialog;

public class DialogUtil {
	public void showAlertDialog(final Context context) {

		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setMessage("对不起，您还未登陆！");
		builder.setTitle("温馨提示");
		builder.setPositiveButton("登陆", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent();
				intent.setClass(context, LoginActivity.class);
				context.startActivity(intent);
				// 设置你的操作事项
			}
		});

		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();

	}
}
