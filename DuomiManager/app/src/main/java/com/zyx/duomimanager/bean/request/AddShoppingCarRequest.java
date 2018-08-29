package com.zyx.duomimanager.bean.request;

import com.zyx.duomimanager.commication.BaseRequest;

/**
 * 
 * @author zyx
 * @anotion 添加购物车项
 */
public class AddShoppingCarRequest extends BaseRequest {

	private int customId;
	private int productId;
	private int amout;
	public String customMobile;
	public String productName;
	public double price;
	public String productLogo;

	public AddShoppingCarRequest() {
		super(REQUESTTYPE.GET, REQUESTSHOWTYPE.DISPLAY);
	}

	@Override
	public String getUrl() {
		return getSERVIER_URL() + getPath() + toParams();
	}

	public int getCustomId() {
		return customId;
	}

	public void setCustomId(int customId) {
		this.customId = customId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}


	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getAmout() {
		return amout;
	}

	public void setAmout(int amout) {
		this.amout = amout;
	}

	public String getCustomMobile() {
		return customMobile;
	}

	public void setCustomMobile(String customMobile) {
		this.customMobile = customMobile;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductLogo() {
		return productLogo;
	}

	public void setProductLogo(String productLogo) {
		this.productLogo = productLogo;
	}

}
