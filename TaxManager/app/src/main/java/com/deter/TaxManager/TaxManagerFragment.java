package com.deter.TaxManager;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TaxManagerFragment extends android.app.Fragment {

    public TaxManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tax_manage, container, false);
        ((TextView) v.findViewById(R.id.title)).setText(R.string.tax_manager);
        return v;
    }


    private void init() {
    }


    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
