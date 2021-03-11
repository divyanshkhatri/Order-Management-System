package com.accolite.model;

public class Order {
	
	int customerId;
	
	int orderId;
	
	String orderStatus;

	int orderProfit;
	
	int orderCost;
	
	public Order(int customerId, int orderId, String orderStatus, int orderCost) {
		super();
		this.customerId = customerId;
		this.orderId = orderId;
		this.orderStatus = orderStatus;
		this.orderCost = orderCost;
	}
	

	public int getOrderCost() {
		return orderCost;
	}


	public void setOrderCost(int orderCost) {
		this.orderCost = orderCost;
	}
	
	
	public int getOrderProfit() {
		return orderProfit;
	}


	public void setOrderProfit(int orderProfit) {
		this.orderProfit = orderProfit;
	}


	public Order() {
		// TODO Auto-generated constructor stub
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
