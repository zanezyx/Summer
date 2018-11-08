package com.deter.TaxManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.SSID;
import com.deter.TaxManager.model.TopAP;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DialogUtils;
import com.deter.TaxManager.utils.Log;
import com.deter.TaxManager.utils.PermissionsUtils;
import com.deter.TaxManager.utils.StreamUtils;
import com.deter.TaxManager.utils.ToastUtils;
import com.deter.TaxManager.view.SpinerPopupWindow;
import com.deter.TaxManager.view.adapter.BaseSpinerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiusheng on 2017/7/26
 */
public class SsidPwdLibraryActivity extends BaseActivity implements View.OnClickListener {

    private PermissionsUtils permissionsUtils;

    private boolean hasPermission;

    private PopupWindow springPopupWindow;

    private StreamUtils<SSID> streamUtils;

    private ImageView topDeleteIv;

    private boolean hasContent;

    private RelativeLayout importSsidFileRl;

    private RelativeLayout exportSsidFileRl;

    private RelativeLayout giveSsidHistoryRl;

    private EditText inputSsidAccountEt;

    private TextView ssidInputPwdTypeStr;

    private EditText inputSsidPwdEt;

    private Button submitSsidInfoBtn;

    private DataManager dataManager;

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String ssid = inputSsidAccountEt.getText().toString();
            String pwd = inputSsidPwdEt.getText().toString();
            //String pwdTypeStr = ssidInputPwdTypeStr.getText().toString();
            String defaultPwdType = getResources().getString(R.string.ssid_pwd_type_str);
            if (TextUtils.isEmpty(ssid) || TextUtils.isEmpty(pwd) /*|| pwdTypeStr.contains(defaultPwdType)*/) {
                if (hasContent) {
                    hasContent = false;
                    submitSsidInfoBtn.setTextColor(getApplicationContext().getResources().getColor(R.color.ssid_pwd_library_text_color));
                }
            } else {
                if (!hasContent) {
                    hasContent = true;
                    submitSsidInfoBtn.setTextColor(getApplicationContext().getResources().getColor(R.color.tabs_selected));
                }
            }
        }

    };

    private SpinerPopupWindow<String> spinerPopupWindow;

    private String[] spinerArray;

    private List<String> spinerList;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_ssid_pwd_library);

        setTitleText(R.string.title_ssid_pwd_library_manager_str);
        setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        topDeleteIv =getView(R.id.search);
        topDeleteIv.setImageResource(R.drawable.start_map_task_top_delete_icon);
        topDeleteIv.setVisibility(View.VISIBLE);
        topDeleteIv.setOnClickListener(this);

        importSsidFileRl = getView(R.id.import_ssid_file_rl);
        exportSsidFileRl = getView(R.id.export_ssid_file_rl);
        giveSsidHistoryRl = getView(R.id.give_ssid_history_rl);
        inputSsidAccountEt = getView(R.id.input_ssid_account_et);
        ssidInputPwdTypeStr = getView(R.id.ssid_input_pwd_type_str);
        inputSsidPwdEt = getView(R.id.input_ssid_pwd_et);
        submitSsidInfoBtn = getView(R.id.submit_ssid_info_btn);

        importSsidFileRl.setOnClickListener(this);
        exportSsidFileRl.setOnClickListener(this);
        giveSsidHistoryRl.setOnClickListener(this);
        //ssidInputPwdTypeStr.setOnClickListener(this);
        submitSsidInfoBtn.setOnClickListener(this);
        inputSsidAccountEt.addTextChangedListener(textWatcher);
        inputSsidPwdEt.addTextChangedListener(textWatcher);
    }

    @Override
    public void initData() {

        if (null == dataManager) {
            dataManager = DataManager.getInstance(getApplicationContext());
        }

        spinerList = new ArrayList<String>();
        spinerArray = getResources().getStringArray(R.array.ssid_spiner_array);
        for (String s : spinerArray) {
            if (null != s) {
                spinerList.add(s);
            }
        }

        streamUtils = new StreamUtils<>();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search:
                showMoreMenu();
                break;

            case R.id.import_ssid_file_rl:
                showFileChooser(FoldersManageActivity.TYPE_FILE);
                break;

            case R.id.export_ssid_file_rl:
                showFileChooser(FoldersManageActivity.TYPE_DIRECTORY);
                break;

            case R.id.give_ssid_history_rl:
                Intent intent = new Intent(SsidPwdLibraryActivity.this, SSIDHistoryActivity.class);
                //intent.putExtra("path", path);
                startActivity(intent);
                break;

            case R.id.ssid_input_pwd_type_str:
                showSpinerPopupWindow();
                break;

            case R.id.submit_ssid_info_btn:
                submitInputSsidInfo();
                break;

            case R.id.menu_add:
                Intent starMapIntent = new Intent(SsidPwdLibraryActivity.this, StarMapTaskListActivity.class);
                starMapIntent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_SSID);
                startActivity(starMapIntent);
                break;

            case R.id.menu_delete:
                Intent topApIntent = new Intent(SsidPwdLibraryActivity.this, StarMapTaskListActivity.class);
                topApIntent.putExtra(APPConstans.KEY_DETAIL_TYPE, APPConstans.DETAIL_TOP_AP_LIST);
                startActivity(topApIntent);
                break;
        }
    }


    private void showMoreMenu() {

        if (null == springPopupWindow) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.layout_menu, null, false);
            View menuAdd = view.findViewById(R.id.menu_add);
            View menuAddIv = view.findViewById(R.id.menu_add_iv);
            menuAddIv.setVisibility(View.GONE);
            TextView textViewadd = (TextView) view.findViewById(R.id.menu_add_tv);
            textViewadd.setText("    "+getResources().getString(R.string.title_ssid_pwd_manager_list_str));
            View menuDelete = view.findViewById(R.id.menu_delete);
            View menuDeleteIv = view.findViewById(R.id.menu_delete_iv);
            menuDeleteIv.setVisibility(View.GONE);
            TextView textViewdelete = (TextView) view.findViewById(R.id.menu_delete_tv);
            textViewdelete.setText("    "+getResources().getString(R.string.title_top_list_manager_list_str));
            menuAdd.setOnClickListener(this);
            menuDelete.setOnClickListener(this);
            springPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            springPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            springPopupWindow.setOutsideTouchable(true);
        }

        if (null != springPopupWindow && springPopupWindow.isShowing()) {
            springPopupWindow.dismiss();
        }
        springPopupWindow.showAsDropDown(topDeleteIv, 0, -30);
    }

    /**
     * 目录管理界面
     *
     * @param type
     */
    private void showFileChooser(int type) {

        if (hasPermission) {
            Intent intent = new Intent(SsidPwdLibraryActivity.this, FoldersManageActivity.class);
            intent.putExtra(FoldersManageActivity.OPEN_FILE_TYPE, type);
            startActivityForResult(intent, type);
            return;
        }
        processPermissions(type);
    }

    private void processPermissions(final int type) {

        if (null == permissionsUtils) {
            permissionsUtils = new PermissionsUtils(this, new PermissionsUtils
                    .PermissionsListener() {
                @Override
                public void onGranted() {
                    hasPermission = true;
                    showFileChooser(type);
                }

                @Override
                public void onDenied(List<String> deniedPermission) {

                }
            });
        }

        permissionsUtils.requestRunPermisssion(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            String selectPath = null;
            Bundle bundle = data.getExtras();
            if (null != bundle) {
                selectPath = bundle.getString(FoldersManageActivity.OPEN_FILE_PATH);
            }

            if (requestCode == FoldersManageActivity.TYPE_FILE) {
                importSSIDPwdFile(selectPath);
            }

            if (requestCode == FoldersManageActivity.TYPE_DIRECTORY) {
                exportSSIDPwdFile(selectPath);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showSpinerPopupWindow() {
        if (null == spinerPopupWindow) {
            spinerPopupWindow = new SpinerPopupWindow<String>(this);
            spinerPopupWindow.setWidth(ssidInputPwdTypeStr.getWidth());
            spinerPopupWindow.refreshData(spinerList, 0);
            spinerPopupWindow.setItemListener(new BaseSpinerAdapter.IOnItemSelectListener() {
                @Override
                public void onItemClick(int pos) {
                    if (-1 < pos && spinerList.size() > pos) {
                        ssidInputPwdTypeStr.setText(spinerList.get(pos));
                    }
                }
            });
        }

        if (null != spinerPopupWindow) {
            if (spinerPopupWindow.isShowing()) {
                spinerPopupWindow.dismiss();
            } else {
                spinerPopupWindow.showAsDropDown(ssidInputPwdTypeStr);
            }
        }
    }


    /**
     * 保存手动输入的SSID信息
     */
    private void submitInputSsidInfo() {

        String ssid = inputSsidAccountEt.getText().toString().trim();
        String pwd = inputSsidPwdEt.getText().toString();
        //String pwdTypeStr = ssidInputPwdTypeStr.getText().toString();
        if (TextUtils.isEmpty(ssid)) {
            ToastUtils.showShort(this, getResources().getString(R.string.ssid_account_input_prompt));
            return;
        }

//        String defaultPwdType = getResources().getString(R.string.ssid_pwd_type_str);
//        if (pwdTypeStr.contains(defaultPwdType)) {
//            ToastUtils.showShort(this, getResources().getString(R.string
// .ssid_type_select_prompt));
//            return;
//        }

//        if (TextUtils.isEmpty(pwd)) {
//            ToastUtils.showShort(this, getResources().getString(R.string.ssid_pwd_input_prompt));
//            return;
//        }

        final TopAP topAP = new TopAP();
        topAP.setSsid(ssid);
        if (TextUtils.isEmpty(pwd)){
            topAP.setPassword(null);
        }else{
            topAP.setPassword(pwd);
        }
        topAP.setEncryption("WPA2");

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isExist = APPUtils.topApIsExistInTopListLibrary(topAP, dataManager);
                if (isExist) {
                    ToastUtils.showShort(SsidPwdLibraryActivity.this, getResources().getString(R.string.ssid_already_exist_prompt));
                    return;
                }

                if (null == getApplicationContext()) {
                    return;
                }

                dataManager.addTopApToDb(topAP);
                ToastUtils.showShort(SsidPwdLibraryActivity.this, getResources().getString(R.string.ssid_add_success_prompt));
                inputSsidAccountEt.getText().clear();
                inputSsidPwdEt.getText().clear();
            }
        }, 100);


    }





    private void importSSIDPwdFile(final String selectPath) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<TopAP> topAPList = streamUtils.readListFromSdCard(selectPath, null);

                if (topAPList.isEmpty() || null==getApplicationContext()){
                    ToastUtils.showShort(SsidPwdLibraryActivity.this, getResources().getString(R.string.ssid_not_exist_data_import_prompt));
                    return;
                }

                if (!topAPList.isEmpty()) {
                    for (TopAP topAP : topAPList) {
                        if ("null".equals(topAP.getPassword())) {
                            topAP.setPassword(null);
                        }

                        boolean isExist = APPUtils.topApIsExistInTopListLibrary(topAP,dataManager);
                        if (!isExist) {
                            dataManager.addTopApToDb(topAP);
                        }
                    }

                    DialogUtils.showOprResultPopupWindow(SsidPwdLibraryActivity.this, R.string.ssid_opr_status_import_succeed, R.drawable.ssid_opr_succeed);
                } else {
                    DialogUtils.showOprResultPopupWindow(SsidPwdLibraryActivity.this, R.string.ssid_opr_status_import_fail, R.drawable.ssid_opr_fail);
                }
            }
        }, 30);
    }

    private void exportSSIDPwdFile(final String selectPath) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (null == getApplicationContext()) {
                    DialogUtils.showOprResultPopupWindow(SsidPwdLibraryActivity.this, R.string.ssid_opr_status_export_fail, R.drawable.ssid_opr_fail);
                    return;
                }

                List<TopAP> list = DataManager.getInstance(getApplicationContext()).getAllTopApFromDb();

                if (list.isEmpty()){
                    ToastUtils.showShort(SsidPwdLibraryActivity.this, getResources().getString(R.string.ssid_not_exist_data_export_prompt));
                    return;
                }

                boolean isOk = false;

                File file = new File(selectPath, APPConstans.EXPORT_FILENAME);
                if (file.exists()) {
                    if (!file.delete()) {
                        DialogUtils.showOprResultPopupWindow(SsidPwdLibraryActivity.this, R.string.ssid_opr_status_export_fail, R.drawable.ssid_opr_fail);
                        Log.i("SSidPwdLibraryActivity", "exportSSIDPwdFile >>>> current file exist file delete fail Opration stop");
                        return;
                    }
                }

                for (TopAP topAP : list) {
                    String ssidJson = "{\"ssid\": {\"ssid\": \"" + topAP.getSsid() + "\"," +
                            "\"password\": \"" + topAP.getPassword() + "\",\"type\": \"" + topAP
                            .getEncryption() + "\"}}";

                    isOk = streamUtils.writObjectIntoSDcard(selectPath, APPConstans.EXPORT_FILENAME, ssidJson);
                    if (!isOk) {
                        break;
                    }
                }

                if (isOk) {
                    DialogUtils.showOprResultPopupWindow(SsidPwdLibraryActivity.this, R.string.ssid_opr_status_export_succeed, R.drawable.ssid_opr_succeed);
                } else {
                    DialogUtils.showOprResultPopupWindow(SsidPwdLibraryActivity.this, R.string.ssid_opr_status_export_fail, R.drawable.ssid_opr_fail);
                }

            }
        }, 30);
    }


}
