package com.deter.TaxManager.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.deter.TaxManager.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by Administrator on 2017/8/10 0010.
 */
public class DivisionEditText extends EditText {

    // 内容数组
    private String[] text;

    //数组实际长度
    private Integer length;

    // 允许输入的长度
    private Integer totalLength;

    // 每组的长度
    private Integer eachLength;

    // 分隔符
    private String delimiter;

    // 占位符
    private String placeHolder;

    public DivisionEditText(Context context) {
        super(context);
    }

    public DivisionEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditText);
            this.totalLength = typedArray.getInteger(R.styleable.EditText_totalLength, 0);
            this.eachLength = typedArray.getInteger(R.styleable.EditText_eachLength, 0);
            this.delimiter = typedArray.getString(R.styleable.EditText_delimiter);
            if (this.delimiter == null || this.delimiter.length() == 0) {
                this.delimiter = "-";
            }
            this.placeHolder = typedArray.getString(R.styleable.EditText_placeHolder);
            if (this.placeHolder == null || this.placeHolder.length() == 0) {
                this.placeHolder = " ";
            }
            typedArray.recycle();

            // 初始化
            init();

            this.addTextChangedListener(new DivisionTextWatcher());
            this.setOnFocusChangeListener(new DivisionFocusChangeListener());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DivisionEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static String stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字     a-zA-Z0-9
        String regEx = "[^a-zA-Z-:0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public void init() {
        int groupNum = 0;

        if (this.eachLength != 0) {
            groupNum = this.totalLength / this.eachLength;
        }

        length = this.totalLength + this.eachLength != 0 ? this.totalLength + groupNum - 1 : 0;

        text = new String[this.length];

        if (length > 0) {
            for (int i = 0; i < length; i++) {
                if (i != 0 && (i + 1) % (this.eachLength + 1) == 0) {
                    text[i] = this.delimiter;
                } else {
                    text[i] = placeHolder;
                }
            }
            //  mySetHintText();
            mySetSelection();
        }
    }

//    public String getResult() {
//        StringBuffer buffer = new StringBuffer();
//        for (String item : text) {
//            if (!placeHolder.equals(item) && !delimiter.equals(item)) {
//                buffer.append(item);
//            }
//        }
//        return buffer.toString();
//    }

    private StringBuffer getRroupText() {
        StringBuffer buffer = new StringBuffer();
        for (String item : text) {
            buffer.append(item);
        }
        //Log.e("DivisionEditText", "getRroupText  "+buffer.toString());
        return buffer;
    }

    private void mySetText() {
        StringBuffer buffer = getRroupText();
        // 设置文本
        setText(buffer);
    }

    private void mySetHintText() {
        StringBuffer buffer = getRroupText();
        // 设置提示文本
        setHint(buffer);
    }

    /**
     * 设置焦点
     */
    private void mySetSelection() {
        mySetSelection(fullSelection());
    }

    /**
     * 设置焦点
     *
     * @param index
     */
    private void mySetSelection(int index) {
        DivisionEditText.this.setSelection(index);
    }

    private int isBlank(int selection) {
        int index = -1;
        for (int i = selection - 1; i < length; i++) {
            if (-1 != i) {
                if (placeHolder.equals(text[i])) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    private int fullSelection() {
        int index = 0;
        for (int i = 0; i < length; i++) {
            if (!placeHolder.equals(text[i]) && !delimiter.equals(text[i])) {
                index = i + 1;
            }
        }
        return index;
    }

    public Integer getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Integer totalLength) {
        this.totalLength = totalLength;
    }

    public Integer getEachLength() {
        return eachLength;
    }

    public void setEachLength(Integer eachLength) {
        this.eachLength = eachLength;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    private class DivisionTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

//            String editable = getText().toString();
//            String str = stringFilter(editable.toString());
//            if (!editable.equals(str)) {
//                setText(str);
//            }

            if (s.length() < length) {

                int index = DivisionEditText.this.getSelectionStart();
                String deleteStr = text[index];
                if (delimiter.equals(deleteStr)) {
                    index--;
                }

                text[index] = placeHolder;
                if (index - 1 >= 0) {
                    if (delimiter.equals(text[index - 1])) {
                        index--;
                    }
                }
                mySetText();
                mySetSelection(index);
            }

            if (count == 1) {
                int index = isBlank(DivisionEditText.this.getSelectionStart());
                if (index != -1) {
                    String allStr = s.toString();
                    String inputStr = allStr.substring(start, start + count);
                    text[index] = inputStr;
                }

                mySetText();
                mySetSelection();
            } else {
                String curInputStr = s.toString();
                String lastInputString = getRroupText().toString();
                if (curInputStr.equals(lastInputString)) {
                    //Log.e("DivisionEditText", "paredDate  is same data");
                    return;
                }
                paredDate(curInputStr, start, count);
            }

        }
    }


    private void paredDate(String allStr, int start, int count) {

        String inputStr = allStr.substring(start, start + count);

        char[] inputArray = inputStr.toCharArray();

        int curIndex = start;

        if (curIndex >= length) {

        } else {
            for (int i = 0; i < inputArray.length; i++) {
                String inputChar = String.valueOf(inputArray[i]);
                if (delimiter.equals(inputChar)) {
                    continue;
                }

                curIndex = curIndex(curIndex);

                text[curIndex] = inputChar;

                if (curIndex == length - 1) {
                    //Log.e("DivisionEditText", "paredDate  " + curIndex);
                    break;
                }

                curIndex++;
            }
        }

        mySetText();
        mySetSelection();
    }

    private int curIndex(int selection) {
        int index = -1;

        if (!delimiter.equals(text[selection])) {
            index = selection;
        } else {
            selection++;
            index = selection;
        }

        return index;
    }

    private class DivisionFocusChangeListener implements OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                // 设置焦点
                mySetSelection(0);
            }
        }
    }


}
