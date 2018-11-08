package com.deter.TaxManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.deter.TaxManager.utils.DateUtil;
import com.deter.TaxManager.utils.FileUtils;
import com.deter.TaxManager.utils.ToastUtils;
import com.deter.TaxManager.view.adapter.FoldersManageAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Created by zhongqiusheng on 2017/08/02
 */
public class FoldersManageActivity extends BaseActivity implements FoldersManageAdapter
        .RadioCallBack {

    public final static String OPEN_FILE_PATH = "OPEN_FILE_PATH";

    public final static String OPEN_FILE_TYPE = "OPEN_FILE_TYPE";

    public final static int TYPE_DIRECTORY = 0x0032;

    public final static int TYPE_FILE = 0x0021;

    final int SORT_BY_WORD = 1;

    final int SORT_BY_TIME = 2;

    public int oprType;

    private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

    private FoldersManageAdapter adapter;

    private HashMap<String, Object> curRadioSelectItem;

    private Stack<String> folderStack = new Stack<String>();

    private String TEMP_BASE = null;

    // 当前的目录
    private String nowFolder;

    private TextView titleView;

    private TextView folderTv;

    private ListView listView;

    private Button foldersSelectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_folders_manage);
        titleView = (TextView) findViewById(R.id.title);
        folderTv = (TextView) findViewById(R.id.folder_path_tv);
        findViewById(R.id.bottom_check_choice).setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.fileList);
        foldersSelectBtn = (Button) findViewById(R.id.folders_select_btn);
        foldersSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                String path = curRadioSelectItem.get("absolutePath").toString();
                intent.putExtra(OPEN_FILE_PATH, path);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void initTitle() {
        super.initTitle();
        if (null != toolbar) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backOpr();
                }
            });
        }
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        oprType = intent.getExtras().getInt(OPEN_FILE_TYPE);

        if (oprType == TYPE_DIRECTORY) {
            titleView.setText(R.string.ssid_choice_folders);
        } else {
            titleView.setText(R.string.ssid_choice_file);
        }

        String openPath = intent.getStringExtra(OPEN_FILE_PATH);
        File sdFile = null;
        if (TextUtils.isEmpty(openPath)) {
            String rootPath = FileUtils.getSDPath();
            sdFile = new File(rootPath);
            if (null == sdFile) {
                ToastUtils.showShort(FoldersManageActivity.this, getString(R.string.no_sdcard));
                return;
            }
        } else {
            sdFile = new File(openPath);
            if (!sdFile.exists()) {
                ToastUtils.showShort(FoldersManageActivity.this, getString(R.string.nofile));
                return;
            }
        }


        String basePath = sdFile.getAbsolutePath();
        TEMP_BASE = sdFile.getAbsolutePath();
        // 初始化配置對象
        //initSetting(basePath);

        folderStack.setSize(25);
        folderStack.clear();
        pushData(basePath);
        setNowFolder(basePath);

        if (null == listView) {
            listView = (ListView) findViewById(R.id.fileList);
        }

        listView.setCacheColorHint(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> thisAdapter, View view, int index, long arg3) {
                fileClick(thisAdapter, index);
            }
        });

        adapter = new FoldersManageAdapter(R.layout.folder_file_item, this, listItem, oprType);
        adapter.setRadioCallBack(this);

        listView.setAdapter(adapter);

        // 刷新列表
        freshList(basePath, null);
        folderTv.setText(nowFolder);

    }

    @Override
    public void onRadioClick(HashMap<String, Object> item) {
        this.curRadioSelectItem = item;
        if (null != item) {
            foldersSelectBtn.setEnabled(true);
            String path = curRadioSelectItem.get("absolutePath").toString();
            folderTv.setText(path);
        } else {
            foldersSelectBtn.setEnabled(false);
        }
    }

    private void pushData(String data) {
        folderStack.push(data);
    }

    private String popData() {
        if (folderStack.isEmpty()) {
            return null;
        }
        return folderStack.pop();
    }

    public String getNowFolder() {
        return nowFolder;
    }

    public void setNowFolder(String nowFolder) {
        this.nowFolder = nowFolder;
    }

    //数据源处理
    private void freshList(String path, HashMap<String, Object> selectItem) {
        List<File> fileList = FileUtils.getSortedFiles(new File(path), SORT_BY_TIME, true);
        ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = null;
        for (File file : fileList) {

            if (oprType == TYPE_DIRECTORY && file.isFile()) {
                continue;
            }

            map = new HashMap<String, Object>();
            map.put("fileTypeImg", FileUtils.getFileType(file.isDirectory(), file.getName()));
            map.put("fileName", file.getName());
            map.put("lastModify", DateUtil.formatDate(file.lastModified()));
            map.put("absolutePath", file.getAbsolutePath());
            map.put("isFolder", file.isDirectory());

            if (file.isDirectory()) {
                result.add(map);
            }

            if (oprType == TYPE_FILE && file.isFile()) {
                String fileName = file.getName();
                int index = fileName.lastIndexOf(".");
                if (index > 0) {
                    String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                    if ("txt".contains(fileType)) {
                        result.add(map);
                    }
                }
            }

        }

        if (result.isEmpty()) {
            if (oprType == TYPE_DIRECTORY) {
                adapter.onItemClick(selectItem);
                return;
            } else {
                ToastUtils.showShort(FoldersManageActivity.this, getString(R.string.nofile));
            }
        }

        if (null != adapter) {
            adapter.getSelectListItem().clear();
        }
        onRadioClick(null);
        listItem.clear();
        listItem.addAll(result);
        adapter.notifyDataSetChanged();
    }


    private void fileClick(AdapterView<?> thisAdapter, int index) {
        HashMap<String, Object> map = (HashMap<String, Object>) (thisAdapter.getAdapter().getItem
                (index));
        boolean isFolder = Boolean.parseBoolean(String.valueOf(map.get("isFolder")));
        String absolutePath = String.valueOf(map.get("absolutePath"));
        File file = new File(absolutePath);
        if (isFolder) {
            if (file.exists()) {
                pushData(getNowFolder());
                //rollbackIndex();
                setNowFolder(file.getAbsolutePath());
                freshList(nowFolder, map);
                folderTv.setText(nowFolder);
            }
        } else {
            String fileName = file.getName();
            adapter.onItemClick(map);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 返回键按下时，返回刚才操作的目录，如果已經到了最後一層，則按再次退出
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return backOpr();
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击返回按钮时
     *
     * @return
     */
    private boolean backOpr() {

        if (TextUtils.isEmpty(nowFolder)) {
            finish();
            return true;
        }

        // 退到根节点后
        if (nowFolder.equalsIgnoreCase(TEMP_BASE)) {
            finish();
            return true;
        }

        String popStr = popData();
        if (TextUtils.isEmpty(popStr)) {
            finish();
            return true;
        }

        setNowFolder(popStr);
        freshList(nowFolder, null);
        folderTv.setText(nowFolder);
        return true;
    }


}
