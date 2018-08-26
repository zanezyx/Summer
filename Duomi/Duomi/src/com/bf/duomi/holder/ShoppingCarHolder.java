package com.bf.duomi.holder;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public final class ShoppingCarHolder{
	public int position;
    public ImageView producePicture;
    public TextView name;
    public TextView number;//商品数量
    public TextView state;
    public EditText total;//商品数量，编辑模式
    public TextView price;
    public Button reduction;
    public Button add;
    public CheckBox selected;
    public Button deleteBtn;
    
}