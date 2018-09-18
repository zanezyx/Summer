package com.zyx.jshop.entity;

import java.util.ArrayList;

public class ShopCartManager {

	
	public ArrayList<Cart> allCartList;
	public ArrayList<Cart> selectCartList;
	private static ShopCartManager shopCartManager;
	
	ShopCartManager()
	{
		allCartList = new ArrayList<Cart>();
		selectCartList = new ArrayList<Cart>();
	}
	
	
	public static ShopCartManager getInstance()
	{
		if(shopCartManager==null)
		{
			shopCartManager = new ShopCartManager();
		}	
		return shopCartManager;
	}
	
}
