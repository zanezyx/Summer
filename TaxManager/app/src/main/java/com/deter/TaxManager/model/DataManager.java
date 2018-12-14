package com.deter.TaxManager.model;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.deter.TaxManager.R;
import com.deter.TaxManager.network.DtConstant;
import com.deter.TaxManager.utils.APPUtils;
import com.deter.TaxManager.utils.ExcelUtils;
import com.deter.TaxManager.utils.FileUtils;
import com.deter.TaxManager.utils.Log;
import com.deter.TaxManager.utils.RandomUtils;
import com.deter.TaxManager.utils.TimeUtils;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by yuxinz on 2017/7/18.
 */

public class DataManager {

    private Context context;
    private static DataManager instance;
    private BaseInfo mBaseInfo;
    private ParentInfo mFatherInfo;
    private ParentInfo mMontherInfo;
    private ChildrenInfo mFirstChildInfo;
    private ChildrenInfo mSecondChildInfo;
    private SupportParentInfo mSupportParentInfo;
    private RaiseChildrenInfo mRaiseChildrenInfo;
    private RoanInterestInfo mRoanInterestInfo;
    private InsuranceInfo mInsuranceInfo;
    private TreatmentInfo mTreatmentInfo;
    private EducationInfo mEducationInfo;
    private VocationInfo mVocationInfo;
    private RentInfo mRentInfo;
    private SalaryInfo mSalaryInfo;

    public static String CACHE_FILE_DIRECTORY = Environment.getExternalStorageDirectory().toString()+"/";
    public final static String BASE_INFO_CACHE_FILE = "base_info.bin";
    public final static String FATHER_INFO_CACHE_FILE = "father_info.bin";
    public final static String MONTHER_INFO_CACHE_FILE = "monther_info.bin";
    public final static String FIRST_CHILD_INFO_CACHE_FILE = "first_child_info.bin";
    public final static String SECOND_CHILD_INFO_CACHE_FILE = "second_child_info.bin";
    public final static String SUPPORT_PARENT_INFO_CACHE_FILE = "support_parent_info.bin";
    public final static String RAISE_PARENT_INFO_CACHE_FILE = "raise_children_info.bin";
    public final static String ROAN_INTEREST_INFO_CACHE_FILE = "roan_interest_info.bin";
    public final static String INSURANCE_INFO_CACHE_FILE = "insurance_info.bin";

    public final static String TREATMENT_INFO_CACHE_FILE = "treatment_info.bin";
    public final static String EDUCATION_INFO_CACHE_FILE = "education_info.bin";
    public final static String VOCATION_INFO_CACHE_FILE = "vocation_info.bin";
    public final static String RENT_INFO_CACHE_FILE = "rent_info.bin";
    public final static String SALARY_INFO_CACHE_FILE = "salary_info.bin";


    private DataManager(Context context) {

        this.context = context;
    }


    public static DataManager getInstance(Context context){
        if(instance == null){
            synchronized (DataManager.class){
                if(instance == null){
                    instance = new DataManager(context.getApplicationContext());
                }
            }
        }
        return instance;
    }


    public void initAllInfo()
    {
        mBaseInfo = (BaseInfo) CacheManager.readObject(context,BASE_INFO_CACHE_FILE);
        mFatherInfo = (ParentInfo) CacheManager.readObject(context,FATHER_INFO_CACHE_FILE);
        mMontherInfo = (ParentInfo) CacheManager.readObject(context,MONTHER_INFO_CACHE_FILE);
        mFirstChildInfo = (ChildrenInfo) CacheManager.readObject(context,FIRST_CHILD_INFO_CACHE_FILE);
        mSecondChildInfo = (ChildrenInfo) CacheManager.readObject(context,SECOND_CHILD_INFO_CACHE_FILE);
        Log.i("tax", "DataManager>>>initAllInfo "+BASE_INFO_CACHE_FILE);
        if(mBaseInfo==null){
            Log.i("tax", "DataManager>>>initAllInfo>>>mBaseInfo:null");
        }else{
            Log.i("tax", "DataManager>>>initAllInfo>>>mBaseInfo getname:"+mBaseInfo.getName());
        }
    }

    public void saveAllInfo()
    {
        Log.i("tax", "DataManager>>>saveAllInfo "+BASE_INFO_CACHE_FILE);
        CacheManager.saveObject(context,mBaseInfo,BASE_INFO_CACHE_FILE);
        CacheManager.saveObject(context,mFatherInfo,FATHER_INFO_CACHE_FILE);
        CacheManager.saveObject(context,mMontherInfo,MONTHER_INFO_CACHE_FILE);
        CacheManager.saveObject(context,mFirstChildInfo,FIRST_CHILD_INFO_CACHE_FILE);
        CacheManager.saveObject(context,mSecondChildInfo,SECOND_CHILD_INFO_CACHE_FILE);
    }

    public void initSupportParentInfo()
    {
        mSupportParentInfo = (SupportParentInfo) CacheManager.readObject(context,SUPPORT_PARENT_INFO_CACHE_FILE);
    }

    public void saveSupportParentInfo(SupportParentInfo supportParentInfo)
    {
        mSupportParentInfo = supportParentInfo;
        CacheManager.saveObject(context,supportParentInfo,SUPPORT_PARENT_INFO_CACHE_FILE);
    }

    public void initRaiseChildrenInfo()
    {
        mRaiseChildrenInfo = (RaiseChildrenInfo) CacheManager.readObject(context,RAISE_PARENT_INFO_CACHE_FILE);
    }

    public void saveRaiseChildrenInfo(RaiseChildrenInfo raiseChildrenInfo)
    {
        mRaiseChildrenInfo = raiseChildrenInfo;
        CacheManager.saveObject(context,raiseChildrenInfo,RAISE_PARENT_INFO_CACHE_FILE);
    }


    public void initRoanInterestInfo()
    {
        mRoanInterestInfo = (RoanInterestInfo) CacheManager.readObject(context,ROAN_INTEREST_INFO_CACHE_FILE);
    }

    public void saveRoanInterestInfo(RoanInterestInfo roanInterestInfo)
    {
        mRoanInterestInfo = roanInterestInfo;
        CacheManager.saveObject(context,roanInterestInfo,ROAN_INTEREST_INFO_CACHE_FILE);
    }

    public void initInsuranceInfo()
    {
        mInsuranceInfo = (InsuranceInfo) CacheManager.readObject(context,INSURANCE_INFO_CACHE_FILE);
    }

    public void saveInsuranceInfo(InsuranceInfo insuranceInfo)
    {
        mInsuranceInfo = insuranceInfo;
        CacheManager.saveObject(context,insuranceInfo,INSURANCE_INFO_CACHE_FILE);
    }


    public void initTreatmentInfo()
    {
        mTreatmentInfo = (TreatmentInfo) CacheManager.readObject(context,TREATMENT_INFO_CACHE_FILE);
    }

    public void saveTreatmentInfo(TreatmentInfo treatmentInfo)
    {
        mTreatmentInfo = treatmentInfo;
        CacheManager.saveObject(context,treatmentInfo,TREATMENT_INFO_CACHE_FILE);
    }


    public void initEducationInfo()
    {
        mEducationInfo = (EducationInfo) CacheManager.readObject(context,EDUCATION_INFO_CACHE_FILE);
    }

    public void saveEducationInfo(EducationInfo educationInfo)
    {
        mEducationInfo = educationInfo;
        CacheManager.saveObject(context,educationInfo,EDUCATION_INFO_CACHE_FILE);
    }

    public void initVocationInfo()
    {
        mVocationInfo = (VocationInfo) CacheManager.readObject(context,VOCATION_INFO_CACHE_FILE);
    }

    public void saveVocationInfo(VocationInfo vocationInfo)
    {
        mVocationInfo= vocationInfo;
        CacheManager.saveObject(context,vocationInfo,VOCATION_INFO_CACHE_FILE);
    }

    public void initRentInfo()
    {
        mRentInfo = (RentInfo) CacheManager.readObject(context,RENT_INFO_CACHE_FILE);
    }

    public void saveRentInfo(RentInfo rentInfo)
    {
        mRentInfo= rentInfo;
        CacheManager.saveObject(context,rentInfo,RENT_INFO_CACHE_FILE);
    }


    public void initSalaryInfo()
    {
        mSalaryInfo = (SalaryInfo) CacheManager.readObject(context,SALARY_INFO_CACHE_FILE);
    }

    public void saveSalaryInfo(SalaryInfo salaryInfo)
    {
        mSalaryInfo= salaryInfo;
        CacheManager.saveObject(context,salaryInfo,SALARY_INFO_CACHE_FILE);
    }

    public BaseInfo getmBaseInfo() {
        return mBaseInfo;
    }

    public void setmBaseInfo(BaseInfo mBaseInfo) {
        this.mBaseInfo = mBaseInfo;
    }

    public ParentInfo getmFatherInfo() {
        return mFatherInfo;
    }

    public void setmFatherInfo(ParentInfo mFatherInfo) {
        this.mFatherInfo = mFatherInfo;
    }

    public ParentInfo getmMontherInfo() {
        return mMontherInfo;
    }

    public void setmMontherInfo(ParentInfo mMontherInfo) {
        this.mMontherInfo = mMontherInfo;
    }

    public ChildrenInfo getmFirstChildInfo() {
        return mFirstChildInfo;
    }

    public void setmFirstChildInfo(ChildrenInfo mFirstChildInfo) {
        this.mFirstChildInfo = mFirstChildInfo;
    }

    public ChildrenInfo getmSecondChildInfo() {
        return mSecondChildInfo;
    }

    public void setmSecondChildInfo(ChildrenInfo mSecondChildInfo) {
        this.mSecondChildInfo = mSecondChildInfo;
    }

    public SupportParentInfo getmSupportParentInfo() {
        return mSupportParentInfo;
    }

    public void setmSupportParentInfo(SupportParentInfo mSupportParentInfo) {
        this.mSupportParentInfo = mSupportParentInfo;
    }

    public RaiseChildrenInfo getmRaiseChildrenInfo() {
        return mRaiseChildrenInfo;
    }

    public void setmRaiseChildrenInfo(RaiseChildrenInfo mRaiseChildrenInfo) {
        this.mRaiseChildrenInfo = mRaiseChildrenInfo;
    }

    public RoanInterestInfo getmRoanInterestInfo() {
        return mRoanInterestInfo;
    }

    public void setmRoanInterestInfo(RoanInterestInfo mRoanInterestInfo) {
        this.mRoanInterestInfo = mRoanInterestInfo;
    }

    public InsuranceInfo getmInsuranceInfo() {
        return mInsuranceInfo;
    }

    public void setmInsuranceInfo(InsuranceInfo mInsuranceInfo) {
        this.mInsuranceInfo = mInsuranceInfo;
    }

    public TreatmentInfo getmTreatmentInfo() {
        return mTreatmentInfo;
    }

    public void setmTreatmentInfo(TreatmentInfo mTreatmentInfo) {
        this.mTreatmentInfo = mTreatmentInfo;
    }

    public EducationInfo getmEducationInfo() {
        return mEducationInfo;
    }

    public void setmEducationInfo(EducationInfo mEducationInfo) {
        this.mEducationInfo = mEducationInfo;
    }

    public VocationInfo getmVocationInfo() {
        return mVocationInfo;
    }

    public void setmVocationInfo(VocationInfo mVocationInfo) {
        this.mVocationInfo = mVocationInfo;
    }

    public RentInfo getmRentInfo() {
        return mRentInfo;
    }

    public void setmRentInfo(RentInfo mRentInfo) {
        this.mRentInfo = mRentInfo;
    }

    public SalaryInfo getmSalaryInfo() {
        return mSalaryInfo;
    }

    public void setmSalaryInfo(SalaryInfo mSalaryInfo) {
        this.mSalaryInfo = mSalaryInfo;
    }


    public Cursor rawQuery(String sql)
    {
        return DtDatabaseHelper.getInstance(context).rawQuery(sql);
    }

}
