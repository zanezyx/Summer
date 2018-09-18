package com.zyx.jshop.entity;

import java.util.ArrayList;

public class Products {

	public ArrayList<Product> productList;
	private static Products products;
	
	Products()
	{
		productList = new ArrayList<Product>();
	}
	
	
	public static Products getInstance()
	{
		if(products==null)
		{
			products = new Products();
		}	
		return products;
	}
	
}
