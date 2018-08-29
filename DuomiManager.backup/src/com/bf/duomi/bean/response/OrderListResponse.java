package com.bf.duomi.bean.response;

import java.util.List;

import com.bf.duomi.commication.BaseResponse;
import com.bf.duomi.entity.Cart;
import com.bf.duomi.entity.Custom;
import com.bf.duomi.entity.ReceiveAddress;
import com.bf.duomi.entity.TOrderToJson;

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
