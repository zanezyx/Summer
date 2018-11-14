package com.deter.TaxManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by yuxinz on 2018/11/14.
 */

public class DatePickerUtil implements DatePicker.OnDateChangedListener {

    private Context mContext;
    private int year, month, day, hour, minute;
    private StringBuffer date, time;

    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }

    public DatePickerUtil(Context context){

        mContext = context;
        initDateTime();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear+1;
        this.day = dayOfMonth;
    }


    public void showDatePickDialog(final OnDateSelectedListener onDateSelectedListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setPositiveButton(mContext.getResources().getString(R.string.comfirm_btn_str)
                , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //tvDate.setText(date.append(String.valueOf(year)).append("年")
                // .append(String.valueOf(month)).append("月").append(day).append("日"));
                dialog.dismiss();
                if(onDateSelectedListener!=null){
                    onDateSelectedListener.onDateSelected(year, month, day);
                }
            }
        });
        builder.setNegativeButton(mContext.getResources().getString(R.string.cancel_btn_str),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(mContext, R.layout.dialog_date, null);
        final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
        dialog.setTitle(mContext.getResources().getString(R.string.set_date));
        dialog.setView(dialogView);
        dialog.show();
        datePicker.init(year, month-1, day, this);
        Log.i("tax", "DatePickerUtil>>>showDatePickDialog");
    }

    public interface OnDateSelectedListener {

        void onDateSelected(int year, int monthOfYear, int dayOfMonth);
    }
}
