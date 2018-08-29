package com.zyx.duomimanager.bean.response;

import com.zyx.duomimanager.commication.BaseResponse;
import com.zyx.duomimanager.entity.Cart;
import com.zyx.duomimanager.entity.Custom;
import com.zyx.duomimanager.entity.ReceiveAddress;
import com.zyx.duomimanager.entity.TOrderToJson;

import java.util.List;



public class OrderListResponse extends BaseResponse {

	private TOrderToJson order;
	private ReceiveAddress receiveAddress;
	private Custom custom;
	private List<Cart> carts;

	public OrderListResponse() {
		super();
	}



	public TOrderToJson getOrder() {
		return order;
	}



	public void setOrder(TOrderToJson order) {
		this.order = order;
	}



	public ReceiveAddress getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(ReceiveAddress receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public Custom getCustom() {
		return custom;
	}

	public void setCustom(Custom custom) {
		this.custom = custom;
	}

	public List<Cart> getCarts() {
		return carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

}
