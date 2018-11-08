package com.deter.TaxManager;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AroundActivity extends AppCompatActivity {

    private int type;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search:
                    Intent intent = new Intent(AroundActivity.this, TaskDetailActivity.class);
                    intent.putExtra(APPConstans.KEY_DETAIL_TYPE, type);
                    startActivity(intent);
                    break;
                case R.id.menu_more:
                    showMoreMenu();
                    break;
                case R.id.menu_add:
                    break;
                case R.id.menu_delete:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_around);
        Intent intent = getIntent();
        type = intent.getIntExtra(APPConstans.KEY_DETAIL_TYPE, 1);
        String title = intent.getStringExtra(APPConstans.KEY_DETAIL_TITLE);
        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(title);
        View back = findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (type == APPConstans.DETAIL_BLACKLIST || type == APPConstans.DETAIL_WHITELIST) {
            View menuMore = findViewById(R.id.menu_more);
            menuMore.setVisibility(View.VISIBLE);
            menuMore.setOnClickListener(onClickListener);

        } else if (type != APPConstans.DETAIL_SPECIAL_SEARCH) {
            View search = findViewById(R.id.search);
            search.setOnClickListener(onClickListener);
            search.setVisibility(View.VISIBLE);
        }
        Fragment fragment = new AroundDevicesFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    private void showMoreMenu() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.layout_menu, null, false);
        View menuAdd = view.findViewById(R.id.menu_add);
        View menuDelete = view.findViewById(R.id.menu_delete);
        menuAdd.setOnClickListener(onClickListener);
        menuDelete.setOnClickListener(onClickListener);
        PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(findViewById(R.id.menu_more), 0, -30);

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }


}
