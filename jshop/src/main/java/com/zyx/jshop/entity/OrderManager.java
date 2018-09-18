package com.zyx.jshop.entity;

import java.util.ArrayList;

public class OrderManager {


	private static OrderManager orderManager;
	
	public TOrderToJson currOrder;
	
	
	OrderManager()
	{
		currOrder = new TOrderToJson();
		currOrder.id = -1;
	}
	
	
	public static OrderManager getInstance()
	{
		if(orderManager==null)
		{
			orderManager = new OrderManager();
		}	
		return orderManager;
	}
	
	
}
