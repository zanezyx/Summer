package com.bf.duomi.bean.request;

import com.bf.duomi.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion search
 */
public class SearchProductRequest extends BaseRequest {

	private String metaKeywords;
	
	public String getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public SearchProductRequest() {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
	}

	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}

}
