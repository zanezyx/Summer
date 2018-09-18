package com.zyx.jshop.entity;

import java.util.ArrayList;

public class News {

	public ArrayList<IndexNew> newsList;
	private static News news;
	
	News()
	{
		newsList = new ArrayList<IndexNew>();
	}
	
	
	public static News getInstance()
	{
		if(news==null)
		{
			news = new News();
		}	
		return news;
	}
	
	

	
}
