package com.deter.TaxManager.view.Progressview;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.deter.TaxManager.R;
import com.deter.TaxManager.utils.ToastUtils;

/**
 * Created by zhongqiusheng on 2017/10/23 0023.
 */
public class EditTextDialogFragment extends DialogFragment {

    private View contentView;
    private EditText editText;
    private ComfirmListener comfirmListener;
    InputMethodManager imm;

    public void setComfirmListener(ComfirmListener comfirmListener) {
        this.comfirmListener = comfirmListener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        contentView = inflater.inflate(R.layout.layout_add_black_white_list_popwindow, container);
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        getDialog().setCanceledOnTouchOutside(false);
        return contentView;
    }


    private void initView() {
        ImageButton closeBtn = getViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftInput();
                dismiss();
            }
        });

        editText = getViewById(R.id.et_add_white_black_mac);
        Button btnSubmitInputMacAddress = getViewById(R.id.btn_submit_input_mac_address);
        btnSubmitInputMacAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputStr = editText.getText().toString().trim();
                String defaultStr = "xx:xx:xx:xx:xx:xx";
                if (TextUtils.isEmpty(inputStr) || inputStr.equals(defaultStr)) {
                    ToastUtils.showShort(getActivity(), getActivity().getApplicationContext().getResources().getString(R.string.add_black_white_prompt_str));
                    return;
                }

                if (null != comfirmListener) {
                    hideSoftInput();
                    comfirmListener.click(inputStr);
                }

                dismiss();
            }
        });
    }


    public interface ComfirmListener {
        void click(String inputStr);
    }


    private void hideSoftInput() {
        boolean isOpen = imm.isActive();
        if (isOpen) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public <T extends View> T getViewById(int layoutID) {
        return (T) contentView.findViewById(layoutID);
    }


}
