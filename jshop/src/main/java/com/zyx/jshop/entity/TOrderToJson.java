package com.zyx.jshop.entity;

import java.util.ArrayList;
import java.util.Date;

public class TOrderToJson implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 4512401368206610260L;
	public Integer id;
	public String deliverySN;
	public Integer orderStatus;
	public Integer paymentStatus;
	public String deliveryTypeName;
	public Integer paymentType;
	public Integer deliveryFee;
	public String orderRemark;
	public Integer productPrice;
	public Integer totalPrice;
	public Integer customId;
	public Integer addressId;
	public String publishDate;
	public Integer deliveryState;
	//订单人姓名
	public String name;
	//订单人手机号码
	public String mobile;
	//收货人姓名
	public String receiverName;
	//收货人地址
	public String receiverAddress;
	//收货人手机号码
	public String receiverMobile;
	public ArrayList<Cart> cartList;
	// Constructors

	
	/** default constructor */
	public TOrderToJson() {
		cartList = new ArrayList<Cart>();
	}

	/** full constructor */
	public TOrderToJson(String orderSn, Integer orderStatus, Integer paymentStatus,
			String deliveryTypeName, Integer paymentType,
			Integer deliveryFee, String orderRemark, Integer totalPrice,
			Integer customId, Integer addressId, String publishDate,
			Date deliveryDate) {
		this.deliverySN = orderSn;
		this.orderStatus = orderStatus;
		this.paymentStatus = paymentStatus;
		this.deliveryTypeName = deliveryTypeName;
		this.paymentType = paymentType;
		this.deliveryFee = deliveryFee;
		this.orderRemark = orderRemark;
		this.totalPrice = totalPrice;
		this.customId = customId;
		this.addressId = addressId;
		this.publishDate = publishDate;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeliverySN() {
		return this.deliverySN;
	}

	public void setDeliverySN(String sn) {
		this.deliverySN = sn;
	}

	public Integer getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getPaymentStatus() {
		return this.paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getDeliveryTypeName() {
		return this.deliveryTypeName;
	}

	public void setDeliveryTypeName(String deliveryTypeName) {
		this.deliveryTypeName = deliveryTypeName;
	}

	public Integer getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentTypeName(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public double getDeliveryFee() {
		return this.deliveryFee;
	}

	public void setDeliveryFee(Integer deliveryFee) {
		this.deliveryFee = deliveryFee;
	}

	public String getOrderRemark() {
		return this.orderRemark;
	}

	public void setOrderRemark(String orderRemark) {
		this.orderRemark = orderRemark;
	}

	public Integer getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(Integer totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer getCustomId() {
		return this.customId;
	}

	public void setCustomId(Integer customId) {
		this.customId = customId;
	}

	public Integer getAddressId() {
		return this.addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
}