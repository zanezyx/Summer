package com.deter.TaxManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zhongqiusheng on 2017/7/27 0027.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initTitle();
        initData();
    }

    public void initTitle() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (null != toolbar) {
//            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//        }
    }

    public abstract void initView();

    public abstract void initData();

    public void hideSoftInputView() {
/*        InputMethodManager manager = (InputMethodManager) this.getSystemService("input_method");
        if (this.getWindow().getAttributes().softInputMode != 2 && this.getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 2);
        }*/


        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow
                (getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void toggleSoftInputView(EditText editText) {
        editText.requestFocus();
        InputMethodManager manager = (InputMethodManager) this.getSystemService("input_method");
        manager.toggleSoftInput(0, 1);
    }


    public void setTitleText(int resid) {
        TextView textView = getView(R.id.title);
        textView.setText(resid);
    }

    public void setBackListener(View.OnClickListener backListener) {
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(backListener);
    }

    public <T extends View> T getView(int viewId) {
        View view = findViewById(viewId);
        return (T) view;
    }

}
