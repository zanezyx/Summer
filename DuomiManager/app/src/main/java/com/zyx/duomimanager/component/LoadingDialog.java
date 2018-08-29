package com.zyx.duomimanager.component;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.zyx.duomimanager.R;

/******
 * 加载进度提示框
 * @author bhl
 *
 */
public class LoadingDialog extends Dialog {
	public Dialog mDialog;

	public LoadingDialog(Context context) {
		super(context);
		mDialog=new AlertDialog.Builder(context).create();
		mDialog.setOnKeyListener(keyListener);
	}
	
	 OnKeyListener keyListener = new OnKeyListener()
     {
         @Override
         public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
         {
             if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_SEARCH)
             {
                 return true;
             }
             return false;
         }
     };
     
     /****
      * 显示提示框
      */
     public void showLoadingDialog(){
    	 if(mDialog!=null){
    		 mDialog.show();
    		 mDialog.setContentView(R.layout.common_loading_dialog_layout);
    	 }
     }
     
     /****
      * 显示提示框，有文字提示
      */
     public void showLoadingDialog(String msg){
    	 if(mDialog!=null){
    		 mDialog.show();
    		 mDialog.setContentView(R.layout.common_loading_dialog_layout);
    		 TextView tv = (TextView) mDialog.findViewById(R.id.tv_info);
    		 tv.setText(msg);
    		 tv.setVisibility(View.VISIBLE);
    	 }
     }
     
     /****
      * 关闭提示框
      */
     public void closeLoadingDialog(){
    	 try {
			if(mDialog!=null){
				 mDialog.dismiss();
				 mDialog=null;
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}
     }

}
