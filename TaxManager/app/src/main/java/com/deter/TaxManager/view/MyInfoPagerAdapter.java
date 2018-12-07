package com.deter.TaxManager.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.deter.TaxManager.DatePickerUtil;
import com.deter.TaxManager.R;
import com.deter.TaxManager.model.BaseInfo;
import com.deter.TaxManager.model.TxDate;
import com.deter.TaxManager.model.ChildrenInfo;
import com.deter.TaxManager.model.DataManager;
import com.deter.TaxManager.model.ParentInfo;

/**
 * Created by xiaolu on 17-8-26.
 */

public class MyInfoPagerAdapter extends PagerAdapter {

    private static final String TAG = "MyInfoPagerAdapter";

    private final int[] TAB_TITLE = {R.string.tab_my_info,  R.string.tab_parent_info,R.string.tab_children_info};

    private LayoutInflater layoutInflater;

    private Activity context;
    private DatePickerUtil datePickerUtil;
    private Button btChooseBornDate;
    private Button btFatherBornDate;
    private Button btMotherBornDate;
    private Button btChildren1BornDate;
    private Button btChildren2BornDate;
    private String strYear;
    private String strMonth;
    private String strDay;
    private View mBaseInfoView;
    private View mParentInfoView;
    private View mChildrenInfoView;

    private TxDate myTxDate;
    private TxDate fatherTxDate;
    private TxDate motherTxDate;
    private TxDate firstChildTxDate;
    private TxDate secondChildTxDate;

    //base info
    private EditText etMyName;
    private EditText etMyTaxId;
    private EditText etMyCarrier;
    private EditText etCjId;
    private EditText etLsId;
    private EditText etMyMarriageId;
    private CheckBox ckOnlyOneChild;
    private EditText etMyCity;
    private EditText etMyAddress;
    private EditText etMyPhoneNumber;

    //parent info
    private EditText etFaName;
    private EditText etFaIdentity;
    private EditText etFaCarrier;
    private EditText etMoName;
    private EditText etMoIdentity;
    private EditText etMoCarrier;

    //child info
    private EditText etFirstChildName;
    private EditText etFirstChildIdentity;
    private EditText etFirstChildBornId;
    private EditText etSecondChildName;
    private EditText etSecondChildIdentity;
    private EditText etSecondChildBornId;



    public MyInfoPagerAdapter(Activity context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        datePickerUtil = new DatePickerUtil(context);
        strYear = context.getResources().getString(R.string.year);
        strMonth = context.getResources().getString(R.string.month);
        strDay = context.getResources().getString(R.string.day);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem");
        View view = null;
        switch(position){
            case 0:
                mBaseInfoView = layoutInflater.inflate(R.layout.fragment_base_info, container, false);
                initbaseInfoView(context);
                view = mBaseInfoView;
                break;
            case 1:
                mParentInfoView = layoutInflater.inflate(R.layout.fragment_parent_info, container, false);
                initParentsInfoView(context);
                view = mParentInfoView;
                break;
            case 2:
                mChildrenInfoView = layoutInflater.inflate(R.layout.fragment_children_info, container, false);
                initChildrenInfoView(context);
                view = mChildrenInfoView;
                break;
        }
        container.addView(view);
        return view;
    }




    void initbaseInfoView(Context context){

        if(mBaseInfoView==null){
            return;
        }
        final BaseInfo baseInfo = DataManager.getInstance(context).getmBaseInfo();
        btChooseBornDate = (Button) mBaseInfoView.findViewById(R.id.tvChooseBornDate);
        btChooseBornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                        Log.i("tax", "MyInfoFragment>>>initbaseInfoView>>>" +
                                "year:"+year+" monthOfYear:"+monthOfYear+" dayOfMonth:"+dayOfMonth);
                        myTxDate = new TxDate(year,monthOfYear, dayOfMonth);
                        btChooseBornDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
                    }
                });
            }
        });

        etMyName = (EditText)mBaseInfoView.findViewById(R.id.etMyName);
        etMyTaxId = (EditText)mBaseInfoView.findViewById(R.id.etMyTaxId);
        etMyCarrier = (EditText)mBaseInfoView.findViewById(R.id.etMyCarrier);
        etCjId = (EditText)mBaseInfoView.findViewById(R.id.etCjId);
        etLsId = (EditText)mBaseInfoView.findViewById(R.id.etLsId);
        etMyMarriageId = (EditText)mBaseInfoView.findViewById(R.id.etMyMarrierId);
        ckOnlyOneChild = (CheckBox) mBaseInfoView.findViewById(R.id.cbIsDszn);
        etMyCity = (EditText)mBaseInfoView.findViewById(R.id.etMyCity);
        etMyAddress = (EditText)mBaseInfoView.findViewById(R.id.etMyAddressDetail);
        etMyPhoneNumber = (EditText)mBaseInfoView.findViewById(R.id.etMyPhone);

        Log.i("tax", "MyInfoPagerAdapter>>>initbaseInfoView");
        if(baseInfo!=null){
            Log.i("tax", "MyInfoPagerAdapter>>>initbaseInfoView baseInfo.getName():"+baseInfo.getName());
            TxDate date = baseInfo.getTxDate();
            if(date!=null){
                Log.i("tax", "MyInfoPagerAdapter>>>initbaseInfoView"
                        +date.getYear()+strYear+date.getMonth()+strMonth+date.getDay()+strDay);
                myTxDate = date;
                btChooseBornDate.setText(date.getYear()+strYear+date.getMonth()+strMonth+date.getDay()+strDay);
            }else{
                Log.i("tax", "MyInfoPagerAdapter>>>initbaseInfoView>>>date null");
            }
            etMyName.setText(baseInfo.getName());
            etMyTaxId.setText(baseInfo.getTaxpayerId());
            etMyCarrier.setText(baseInfo.getCarrier());
            etCjId.setText(baseInfo.getDeformityId());
            etLsId.setText(baseInfo.getMartyrId());
            etMyMarriageId.setText(baseInfo.getMarriageId());
            ckOnlyOneChild.setChecked(baseInfo.isOnlyChild());
            etMyCity.setText(baseInfo.getCity());
            etMyAddress.setText(baseInfo.getAddress());
            etMyPhoneNumber.setText(baseInfo.getPhoneNumber());
        }
    }

    void initParentsInfoView(Context context){
        if(mParentInfoView==null){
            return;
        }
        btFatherBornDate = (Button) mParentInfoView.findViewById(R.id.btFatherBornDate);
        btFatherBornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                        fatherTxDate = new TxDate(year,monthOfYear, dayOfMonth);
                        btFatherBornDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
                    }
                });
            }
        });

        btMotherBornDate = (Button) mParentInfoView.findViewById(R.id.btMotherBornDate);
        btMotherBornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                        motherTxDate = new TxDate(year,monthOfYear, dayOfMonth);
                        btMotherBornDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
                    }
                });
            }
        });
        etFaName= (EditText)mParentInfoView.findViewById(R.id.etFaName);
        etFaIdentity= (EditText)mParentInfoView.findViewById(R.id.etFaPersionId);
        etFaCarrier= (EditText)mParentInfoView.findViewById(R.id.etFaCarrier);
        etMoName= (EditText)mParentInfoView.findViewById(R.id.etMoName);
        etMoIdentity= (EditText)mParentInfoView.findViewById(R.id.etMoPersionId);
        etMoCarrier= (EditText)mParentInfoView.findViewById(R.id.etMoCarrier);

        ParentInfo fatherInfo = DataManager.getInstance(context).getmFatherInfo();
        if(fatherInfo!=null){
            fatherTxDate = fatherInfo.getTxDate();
            etFaName.setText(fatherInfo.getName());
            etFaIdentity.setText(fatherInfo.getIdentity());
            etFaCarrier.setText(fatherInfo.getCarrier());
            if(fatherTxDate !=null){
                btFatherBornDate.setText(fatherTxDate.getYear()+strYear
                        + fatherTxDate.getMonth()+strMonth+ fatherTxDate.getDay()+strDay);
            }
        }

        ParentInfo montherInfo = DataManager.getInstance(context).getmMontherInfo();
        if(montherInfo!=null){
            motherTxDate = montherInfo.getTxDate();
            etMoName.setText(montherInfo.getName());
            etMoIdentity.setText(montherInfo.getIdentity());
            etMoCarrier.setText(montherInfo.getCarrier());
            if(motherTxDate !=null){
                btMotherBornDate.setText(motherTxDate.getYear()+strYear
                        + motherTxDate.getMonth()+strMonth+ motherTxDate.getDay()+strDay);
            }
        }
    }

    void initChildrenInfoView(Context context){
        if(mChildrenInfoView==null){
            return;
        }
        btChildren1BornDate = (Button) mChildrenInfoView.findViewById(R.id.btChildren1BornDate);
        btChildren1BornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                        firstChildTxDate = new TxDate(year,monthOfYear, dayOfMonth);
                        btChildren1BornDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
                    }
                });
            }
        });

        btChildren2BornDate = (Button) mChildrenInfoView.findViewById(R.id.btChildren2BornDate);
        btChildren2BornDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerUtil.showDatePickDialog(new DatePickerUtil.OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(int year, int monthOfYear, int dayOfMonth) {
                        secondChildTxDate = new TxDate(year,monthOfYear, dayOfMonth);
                        btChildren2BornDate.setText(year+strYear+monthOfYear+strMonth+dayOfMonth+strDay);
                    }
                });
            }
        });
        etFirstChildName= (EditText)mChildrenInfoView.findViewById(R.id.etFirstChildName);
        etFirstChildIdentity= (EditText)mChildrenInfoView.findViewById(R.id.etFirstChildPId);
        etFirstChildBornId= (EditText)mChildrenInfoView.findViewById(R.id.etFirstChildBornId);
        etSecondChildName= (EditText)mChildrenInfoView.findViewById(R.id.etSecondChildName);
        etSecondChildIdentity= (EditText)mChildrenInfoView.findViewById(R.id.etSecondChildPId);
        etSecondChildBornId= (EditText)mChildrenInfoView.findViewById(R.id.etSecondChildBornId);
        ChildrenInfo firstChildInfo = DataManager.getInstance(context).getmFirstChildInfo();
        if(firstChildInfo!=null){
            etFirstChildName.setText(firstChildInfo.getName());
            etFirstChildIdentity.setText(firstChildInfo.getIdentity());
            etFirstChildBornId.setText(firstChildInfo.getBornId());
            firstChildTxDate = firstChildInfo.getTxDate();
            if(firstChildTxDate !=null){
                btChildren1BornDate.setText(firstChildTxDate.getYear()+strYear
                        + firstChildTxDate.getMonth()+strMonth+ firstChildTxDate.getDay()+strDay);
            }
        }

        ChildrenInfo secondChildInfo = DataManager.getInstance(context).getmSecondChildInfo();
        if(secondChildInfo!=null){
            etSecondChildName.setText(secondChildInfo.getName());
            etSecondChildIdentity.setText(secondChildInfo.getIdentity());
            etSecondChildBornId.setText(secondChildInfo.getBornId());
            secondChildTxDate = secondChildInfo.getTxDate();
            secondChildTxDate = secondChildInfo.getTxDate();
            if(secondChildTxDate !=null){
                btChildren2BornDate.setText(secondChildTxDate.getYear()+strYear
                        + secondChildTxDate.getMonth()+strMonth+ secondChildTxDate.getDay()+strDay);
            }
        }
    }


    @Override
    public int getCount() {
        return TAB_TITLE.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getString(TAB_TITLE[position]);
    }

    public void saveAllInfoToFile(){
        Log.i("tax", "MyInfoPagerAdapter>>>enter saveAllInfoToFile");
        BaseInfo baseInfo = new BaseInfo();
        baseInfo.setName(etMyName.getText().toString());
        baseInfo.setTaxpayerId(etMyTaxId.getText().toString());
        baseInfo.setCarrier(etMyCarrier.getText().toString());
        baseInfo.setDeformityId(etCjId.getText().toString());
        baseInfo.setMartyrId(etLsId.getText().toString());
        baseInfo.setMarriageId(etMyMarriageId.getText().toString());
        baseInfo.setOnlyChild(ckOnlyOneChild.isChecked());
        baseInfo.setCity(etMyCity.getText().toString());
        baseInfo.setAddress(etMyAddress.getText().toString());
        baseInfo.setPhoneNumber(etMyPhoneNumber.getText().toString());
        baseInfo.setTxDate(myTxDate);
        if(myTxDate !=null){
            Log.i("tax", "saveAllInfoToFile>>>"+ myTxDate.getYear()+" "+ myTxDate.getMonth()+" "
                    + myTxDate.getDay());
        }
        DataManager.getInstance(context).setmBaseInfo(baseInfo);
        ParentInfo fatherInfo = new ParentInfo();
        fatherInfo.setName(etFaName.getText().toString());
        fatherInfo.setIdentity(etFaIdentity.getText().toString());
        fatherInfo.setCarrier(etFaCarrier.getText().toString());
        fatherInfo.setTxDate(fatherTxDate);
        DataManager.getInstance(context).setmFatherInfo(fatherInfo);

        ParentInfo motherInfo = new ParentInfo();
        motherInfo.setName(etMoName.getText().toString());
        motherInfo.setIdentity(etMoIdentity.getText().toString());
        motherInfo.setCarrier(etMoCarrier.getText().toString());
        motherInfo.setTxDate(motherTxDate);
        DataManager.getInstance(context).setmMontherInfo(motherInfo);


        ChildrenInfo firstChildInfo = new ChildrenInfo();
        firstChildInfo.setName(etFirstChildName.getText().toString());
        firstChildInfo.setIdentity(etFirstChildIdentity.getText().toString());
        firstChildInfo.setBornId(etFirstChildBornId.getText().toString());
        firstChildInfo.setTxDate(firstChildTxDate);
        DataManager.getInstance(context).setmFirstChildInfo(firstChildInfo);

        ChildrenInfo secondChildInfo = new ChildrenInfo();
        secondChildInfo.setName(etSecondChildName.getText().toString());
        secondChildInfo.setIdentity(etSecondChildIdentity.getText().toString());
        secondChildInfo.setBornId(etSecondChildBornId.getText().toString());
        secondChildInfo.setTxDate(secondChildTxDate);
        DataManager.getInstance(context).setmSecondChildInfo(secondChildInfo);
        Log.i("tax", "MyInfoPagerAdapter>>>saveAllInfoToFile 1");
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager.getInstance(context).saveAllInfo();
            }
        }).start();
        Log.i("tax", "MyInfoPagerAdapter>>>saveAllInfoToFile");
    }

}

