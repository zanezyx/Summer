package com.bf.duomi.tools;

import java.util.WeakHashMap;

import android.graphics.Bitmap;

public class ImageCache extends WeakHashMap<String, Bitmap> {

	private static final long serialVersionUID = 1L;
	
	public boolean isCached(String url){
		return containsKey(url) && get(url) != null;
	}

}
