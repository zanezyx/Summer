package com.zyx.jshop.entity;

import java.util.ArrayList;

public class RecommendProducts {

	public ArrayList<Product> newList;
	public ArrayList<Product> hotSalesList;
	private static RecommendProducts recommendProducts;
	
	RecommendProducts()
	{
		newList = new ArrayList<Product>();
		hotSalesList = new ArrayList<Product>();
	}
	
	
	public static RecommendProducts getInstance()
	{
		if(recommendProducts==null)
		{
			recommendProducts = new RecommendProducts();
		}	
		return recommendProducts;
	}
	
}
