package com.zyx.jshop.entity;


public class Cart implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	public static final long serialVersionUID = 4219005963249188154L;
	public Integer id;
	public Integer productId;
	public Integer amout;
	public Integer customId;
	public String customMobile;
	public String productName;
	public Integer price;
	public String productLogo;

	public Cart() {
	}

	public Cart(Integer orderId, Integer productId, Integer number,
			Integer amout, Integer customId, String customName,
			String productName, Integer price, String productLogo) {
		this.productId = productId;
		this.amout = amout;
		this.customId = customId;
		this.customMobile = customName;
		this.productName = productName;
		this.price = price;
		this.productLogo = productLogo;
	}


}