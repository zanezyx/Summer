package com.deter.TaxManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.Device;
import com.deter.TaxManager.model.DndMac;
import com.deter.TaxManager.model.SpecialDevice;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.DialogUtils;
import com.deter.TaxManager.view.Progressview.EditTextDialogFragment;
import com.deter.TaxManager.view.adapter.BlackWhiteListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhongqiusheng on 2018/8/28 0027.
 */
public class BlackWhiteListActivity extends BaseActivity implements AdapterView
        .OnItemClickListener {

    final int init_data_code = 0x0021;

    final int delete_data_code = 0x0051;

    final int search_data_code = 0x0041;

    //存在本列表中
    final int already_exist_code = 0x0032;

    //存在黑名单列表中
    final int already_exist_black_list_code = 0x0033;

    //存在白名单列表中
    final int already_exist_white_list_code = 0x0049;

    //存在过滤列表中
    //final int already_exist_filter_list_code = 0x0076;

    private boolean isZh;

    private int fromType;

    private List<Object> totalList;

    private DataManager dataManager;

    private LinearLayout search_layout;

    private BlackWhiteListAdapter blackWhiteListAdapter;

    private PopupWindow springPopupWindow;

    private EditTextDialogFragment dialogFragment;

    private View parentView;

    private View menuDelete;

    private RelativeLayout bottomChoiceView;

    private CheckBox bottomCheckChoice;

    private Button deleteSelectBtn;

    private Toolbar toolbar;

    private TextView titleTv;

    private ImageView blackWhiteMenuMore;

    private ListView blackWhiteListview;

    private EditText blackWhiteSearchEt;

    private SearchRunnable searchRunnable;

    private Dialog promptDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case init_data_code:

                    if (null == totalList) {
                        totalList = new ArrayList<>();
                    } else {
                        totalList.clear();
                    }

                    List<Object> list = (List<Object>) msg.obj;
                    Collections.reverse(list);
                    totalList.addAll(list);
                    blackWhiteListAdapter.notifyDataSetChanged();
                    blackWhiteSearchEt.getText().clear();

                    onEmptyDataShow(init_data_code);
                    break;

                case delete_data_code:
                    totalList.clear();
                    List<Object> deleteList = (List<Object>) msg.obj;
                    Collections.reverse(deleteList);
                    totalList.addAll(deleteList);
                    onBackOpr();
                    onEmptyDataShow(delete_data_code);
                    break;

                case search_data_code:
                    totalList.clear();
                    List<Object> searchList = (List<Object>) msg.obj;
                    Collections.reverse(searchList);
                    totalList.addAll(searchList);
                    if (BlackWhiteListAdapter.DELETE_MODE == blackWhiteListAdapter.getOprType()) {
                        blackWhiteListAdapter.getSelectListItems().clear();
                        oprTypeStyle(BlackWhiteListAdapter.NORMAL_MODE);
                    } else {
                        blackWhiteListAdapter.notifyDataSetChanged();
                    }
                    onEmptyDataShow(search_data_code);
                    break;

                case already_exist_code:
                    String prompt = getResources().getString(R.string.mac_already_exist_prompt);
                    showPromptDialog(prompt);
                    break;

                case already_exist_black_list_code:
                    String blackPrompt = getResources().getString(R.string.mac_already_exist_black_list_prompt);
                    showPromptDialog(blackPrompt);
                    break;
                case already_exist_white_list_code:
                    String whitePrompt = getResources().getString(R.string.mac_already_exist_white_list_prompt);
                    showPromptDialog(whitePrompt);
                    break;
            }

        }
    };


    private void showPromptDialog(String prompt) {
        promptDialog = DialogUtils.showConfirmDialog(BlackWhiteListActivity.this, prompt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptDialog.dismiss();
            }
        });
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bottom_check_choice:
                    blackWhiteListAdapter.checkAllChoice();
                    break;

                case R.id.folders_select_btn:
                    deleteWhiteOrBlackList();
                    break;

                case R.id.search:
                    Intent intent = new Intent(BlackWhiteListActivity.this, SearchActivity.class);
                    startActivity(intent);
                    break;
                case R.id.black_white_menu_more:
                    showMoreMenu();
                    break;
                case R.id.menu_add:
                    springPopupWindow.dismiss();
                    showAddBlackOrWhitePopwindow();
                    if (null != search_layout && View.VISIBLE == search_layout.getVisibility()) {
                        hideSoftInputView();
                    }
                    break;
                case R.id.menu_delete:
                    springPopupWindow.dismiss();
                    oprTypeStyle(BlackWhiteListAdapter.DELETE_MODE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_black_white_list);

        titleTv = (TextView) findViewById(R.id.title);
        blackWhiteMenuMore = (ImageView) findViewById(R.id.black_white_menu_more);

        search_layout = (LinearLayout) findViewById(R.id.search_layout);

        blackWhiteListview = (ListView) findViewById(R.id.black_white_listview);
        TextView emptyView = (TextView) findViewById(R.id.emptyView);
        blackWhiteListview.setEmptyView(emptyView);

        blackWhiteMenuMore.setOnClickListener(onClickListener);

        bottomChoiceView = (RelativeLayout) findViewById(R.id.bottom_choice_view);
        bottomCheckChoice = (CheckBox) findViewById(R.id.bottom_check_choice);
        deleteSelectBtn = (Button) findViewById(R.id.folders_select_btn);

        blackWhiteListview.setOnItemClickListener(this);
        blackWhiteSearchEt = (EditText) findViewById(R.id.black_white_search_et);

        blackWhiteSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onSearchData(s.toString());
            }
        });

        bottomCheckChoice.setOnClickListener(onClickListener);
        deleteSelectBtn.setOnClickListener(onClickListener);
    }

    @Override
    public void initData() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackOpr();
            }
        });

        Intent intent = getIntent();
        if (null != intent) {
            isZh = APPUtils.isZh(getApplicationContext());
            fromType = intent.getIntExtra(APPConstans.KEY_DETAIL_TYPE, -1);
            String title = intent.getStringExtra(APPConstans.KEY_DETAIL_TITLE);

            if (null != title) {
                titleTv.setText(title);
            }

            if (totalList == null) {
                totalList = new ArrayList<>();
            }

            if (null != blackWhiteListview) {
                blackWhiteListAdapter = new BlackWhiteListAdapter(R.layout.black_white_list_item, BlackWhiteListActivity.this, totalList);
                blackWhiteListAdapter.setDeleteModeView(bottomCheckChoice,deleteSelectBtn);

                blackWhiteListview.setAdapter(blackWhiteListAdapter);
                //blackWhiteListAdapter.setOprType(BlackWhiteListAdapter.NORMAL_MODE);
                oprTypeStyle(BlackWhiteListAdapter.NORMAL_MODE);
            }

            new Thread(new Runnable() {

                @Override
                public void run() {
                    dataManager = DataManager.getInstance(getApplicationContext());
                    List<Object> list = getListData();
                    Message msg = mHandler.obtainMessage();
                    msg.what = init_data_code;
                    msg.obj = list;
                    mHandler.sendMessageDelayed(msg, 10);
                }
            }).start();
        }
    }

    //根据 黑名单或者白名单,过滤列表，获取列表数据
    private List<Object> getListData() {
        List<Object> returnList = new ArrayList<>();
        if (APPConstans.DETAIL_BLACKLIST == fromType) {
            List<SpecialDevice> list = dataManager.getBlackListFromDb();
            setVendor(list, null);
            returnList.addAll(list);
        } else if (APPConstans.DETAIL_WHITELIST == fromType) {
            List<SpecialDevice> list = dataManager.getWhiteListFromDb();
            setVendor(list, null);
            returnList.addAll(list);
        } else if (APPConstans.DETAIL_FILTER == fromType) {
            List<DndMac> dndMacList = dataManager.getDndMacListFromDb();
            setVendor(null, dndMacList);
            returnList.addAll(dndMacList);
        }

        return returnList;
    }


    private void setVendor(List<SpecialDevice> specialDevices, List<DndMac> dndMacList) {
        if (null == specialDevices) {
            for (DndMac dndMac : dndMacList) {
                setVendor(dndMac.getMac(), dndMac, null);
            }
        } else {
            List<SpecialDevice> onlineList = null;
            if (APPConstans.DETAIL_WHITELIST == fromType) {
                onlineList = BackgroundService.getLastOnlineWhiteList();
            } else if (APPConstans.DETAIL_BLACKLIST == fromType) {
                onlineList = BackgroundService.getLastOnlineBlackList();
            }

            if (null!=onlineList && !onlineList.isEmpty()) {
                for (SpecialDevice specialDevice : specialDevices) {
                    setVendor(specialDevice.getMac(), null, specialDevice);
                    setBlackOnLineStatus(specialDevice, onlineList);
                }
            }
        }
    }


    private void setVendor(String mac, DndMac dndMac, SpecialDevice specialDevice) {
        String[] vendor = dataManager.queryVendor(mac);
        if (null == vendor) {
            return;
        }

        String vendorString = "";
        if (isZh) {
            vendorString = vendor[0];
        } else {
            vendorString = vendor[1];
        }

        if (null != dndMac) {
            dndMac.setVender(vendorString);
        } else {
            specialDevice.setVendercn(vendorString);
        }
    }

    private void setBlackOnLineStatus(SpecialDevice specialDevice, List<SpecialDevice> onLineDevices) {
        for (SpecialDevice onLineDevice : onLineDevices) {
            if (null != specialDevice && null != onLineDevice) {
                if (specialDevice.getMac().equals(onLineDevice.getMac())) {
                    specialDevice.setOnLine(true);
                    break;
                }
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!totalList.isEmpty()) {
            Object device = totalList.get(position);
            if (null != device) {
                //删除模式下的点击action
                if (BlackWhiteListAdapter.DELETE_MODE == blackWhiteListAdapter.getOprType()) {
                    blackWhiteListAdapter.radioChoice(device);
                }

            }

        }
    }

    private void onSearchData(String mac) {
        if (null == searchRunnable) {
            searchRunnable = new SearchRunnable();
        }
        searchRunnable.setSearchCondition(mac);
        mHandler.removeCallbacks(searchRunnable);
        mHandler.postDelayed(searchRunnable, 200);
    }

    private void showMoreMenu() {

        if (null == springPopupWindow) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.layout_menu, null, false);
            parentView=view.findViewById(R.id.layout_menu_main_view);
            View menuAdd = view.findViewById(R.id.menu_add);
            menuDelete = view.findViewById(R.id.menu_delete);
            menuAdd.setOnClickListener(onClickListener);
            menuDelete.setOnClickListener(onClickListener);
            springPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            springPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            springPopupWindow.setOutsideTouchable(true);
            onEmptyDataShow(-1);
        }

        if (null != springPopupWindow && springPopupWindow.isShowing()) {
            springPopupWindow.dismiss();
        }
        springPopupWindow.showAsDropDown(findViewById(R.id.black_white_menu_more), 0, -30);
    }

    private void onEmptyDataShow(int resultCode) {

        if (null == totalList || totalList.isEmpty()) {
            if (resultCode != search_data_code &&
                    View.VISIBLE == search_layout.getVisibility()) {
                search_layout.setVisibility(View.GONE);
            }
            if (null != menuDelete) {
                menuDelete.setVisibility(View.GONE);
                switchSpringWindowBackground(false);
            }
        } else {
            if (View.GONE == search_layout.getVisibility()) {
                search_layout.setVisibility(View.VISIBLE);
            }
            if (null != menuDelete) {
                menuDelete.setVisibility(View.VISIBLE);
                switchSpringWindowBackground(true);
            }
        }
    }

    private void switchSpringWindowBackground(boolean isShowDelete) {
        Drawable curDrawable = null;
        Drawable shouldDrawable = null;
        if (null != parentView) {
            curDrawable = parentView.getBackground();
        }

        if (isShowDelete) {
            shouldDrawable = getResources().getDrawable(R.drawable.popupwindow_background);
        } else {
            shouldDrawable = getResources().getDrawable(R.drawable.popupwindow_background_add);
        }

        if (null == curDrawable || curDrawable != shouldDrawable) {
            parentView.setBackground(shouldDrawable);
        }
    }

    private void oprTypeStyle(int oprType) {
        if (BlackWhiteListAdapter.NORMAL_MODE == oprType) {
            blackWhiteMenuMore.setVisibility(View.VISIBLE);
            blackWhiteListAdapter.setOprType(BlackWhiteListAdapter.NORMAL_MODE);
            bottomChoiceView.setVisibility(View.GONE);
            bottomCheckChoice.setChecked(false);
            deleteSelectBtn.setEnabled(false);
        } else {
            blackWhiteMenuMore.setVisibility(View.GONE);
            blackWhiteListAdapter.setOprType(BlackWhiteListAdapter.DELETE_MODE);
            bottomChoiceView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 添加到黑白名单、过滤列表
     */
    private void showAddBlackOrWhitePopwindow() {

        //if (null == dialogFragment) {
            dialogFragment = new EditTextDialogFragment();
            dialogFragment.setComfirmListener(new EditTextDialogFragment.ComfirmListener() {
                @Override
                public void click(String inputStr) {
                    addBlackOrWhiteToDB(inputStr);
                }
            });
        //}

        dialogFragment.show(getFragmentManager(), "EdittextDialog");
    }

    private void addBlackOrWhiteToDB(String mac) {

        final Device device = new Device();
        //device.setSsid("unknow");
        mac = APPUtils.formateMacAddressToText(mac);
        device.setMac(mac);

        final DndMac dndMac = new DndMac();
        dndMac.setMac(mac);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Message msg = mHandler.obtainMessage();
                int code = -1;

                if (APPConstans.DETAIL_BLACKLIST == fromType) {
                    code = isExistInList(device.getMac());
                    if (-1 != code) {
                        if (code == already_exist_black_list_code) {
                            code = already_exist_code;
                        }
                    } else {
                        dataManager.addBlackListToDb(device);
                    }
                } else if (APPConstans.DETAIL_WHITELIST == fromType) {
                    code = isExistInList(device.getMac());
                    if (-1 != code) {
                        if (code == already_exist_white_list_code) {
                            code = already_exist_code;
                        }
                    } else {
                        dataManager.addWhiteListToDb(device);
                    }
                } else if (APPConstans.DETAIL_FILTER == fromType) {
                    List<DndMac> dndMacList = dataManager.queryMacInDndMaclist(dndMac.getMac());
                    if (!dndMacList.isEmpty()) {
                        code = already_exist_code;
                    } else {
                        dataManager.addDndMacToDb(dndMac);
                    }
                }

                if (-1 != code) {
                    msg.what = code;
                } else {
                    List<Object> list = getListData();
                    msg.what = init_data_code;
                    msg.obj = list;
                }

                mHandler.sendMessageDelayed(msg, 10);
            }
        }).start();
    }


    private int isExistInList(String mac) {
        boolean isExist = dataManager.queryMacIsInBlacklist(mac);
        if (isExist) {
            return already_exist_black_list_code;
        }

        isExist = dataManager.queryMacIsInWhitelist(mac);
        if (isExist) {
            return already_exist_white_list_code;
        }

        return -1;
    }

    private void deleteWhiteOrBlackList() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, Object> selectItems = blackWhiteListAdapter.getSelectListItems();

                for (String key : selectItems.keySet()) {
                    if (APPConstans.DETAIL_BLACKLIST == fromType) {
                        dataManager.removeBlackListFromDb(key);
                    } else if (APPConstans.DETAIL_WHITELIST == fromType) {
                        dataManager.removeWhiteListFromDb(key);
                    } else if (APPConstans.DETAIL_FILTER == fromType) {
                        dataManager.removeDndMacFromDb(key);
                    }

                    Object findDevice = selectItems.get(key);
                    if (totalList.contains(findDevice)) {
                        totalList.remove(findDevice);
                    }
                }

                //List<SpecialDevice> list = getListData();
                List<Object> list = new ArrayList<Object>();
                list.addAll(totalList);
                Message msg = mHandler.obtainMessage();
                msg.what = delete_data_code;
                msg.obj = list;
                mHandler.sendMessageDelayed(msg, 100);

            }
        }).start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackOpr();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void onBackOpr() {
        if (BlackWhiteListAdapter.DELETE_MODE == blackWhiteListAdapter.getOprType()) {
            blackWhiteListAdapter.getSelectListItems().clear();
            oprTypeStyle(BlackWhiteListAdapter.NORMAL_MODE);
        } else {
            finish();
        }
    }

    class SearchRunnable implements Runnable {

        private String searchCondition;

        public void setSearchCondition(String searchCondition) {
            this.searchCondition = searchCondition;
        }

        @Override
        public void run() {
            searchCondition.trim();
            List<Object> list = new ArrayList<>();
            if (!TextUtils.isEmpty(searchCondition)) {
                if (APPConstans.DETAIL_BLACKLIST == fromType) {
                    List<SpecialDevice> blacklist = dataManager.queryMacInBlacklist
                            (searchCondition);
                    list.addAll(blacklist);
                } else if (APPConstans.DETAIL_WHITELIST == fromType) {
                    List<SpecialDevice> whitelist = dataManager.queryMacInWhitelist
                            (searchCondition);
                    list.addAll(whitelist);
                } else if (APPConstans.DETAIL_FILTER == fromType) {
                    List<DndMac> dndMacList = dataManager.queryMacInDndMaclist(searchCondition);
                    list.addAll(dndMacList);
                }
            } else {
                list = getListData();
            }

            Message msg = mHandler.obtainMessage();
            msg.what = search_data_code;
            msg.obj = list;
            mHandler.sendMessageDelayed(msg, 100);
        }

    }

}
