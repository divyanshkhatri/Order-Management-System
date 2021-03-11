package com.accolite.dao.impl;

import java.util.ArrayList;

import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.accolite.dao.CustomerOrderDAO;
import com.accolite.model.Customer;
import com.accolite.model.CustomerOrder;
import com.accolite.model.CustomerOrderDetails;
import com.accolite.model.Order;

import com.accolite.utils.Connection;

public class CustomerOrderDAOImpl implements CustomerOrderDAO {

	@Override
	public CustomerOrder save(Order order) {
		
		Session session = Connection.getSession();
		
		Transaction tx = session.beginTransaction();
		
		CustomerOrder customerOrderData = new CustomerOrder();
		
		int orderId = order.getCustomerId();
		
		Query query = session.createSQLQuery("select * from Customer WHERE customerId = :id");
		query.setParameter("id", orderId);
		@SuppressWarnings("unchecked")
		List<Object[]> obj = query.getResultList();
		Customer customer = new Customer();
		customer.setCustomerId((int) obj.get(0)[0]);
		customer.setCustomerName((String) obj.get(0)[3]);
		customer.setCustomerPhone((String) obj.get(0)[4]);
		customer.setCustomerEmail((String) obj.get(0)[2]);
		customer.setCustomerAddress((String) obj.get(0)[1]);
		customer.setCustomerPincode((String) obj.get(0)[5]);

		customerOrderData.setCustomer(customer);
		customerOrderData.setOrderId(order.getOrderId());
		customerOrderData.setOrderStatus("NotConfirmed");
		customerOrderData.setOrderProfit(0);
		customerOrderData.setOrderCost(0);
	
		session.save(customerOrderData);
		tx.commit();
		
		return customerOrderData;
	}

	@Override
	public List<Order> list() {
         Session session = Connection.getSession();
		
		Transaction tx = session.beginTransaction();

		
		@SuppressWarnings({ "unchecked", "deprecation" })
		List<CustomerOrder> customerOrderList = session.createCriteria(CustomerOrder.class).list();

	
		List<Order> orders = new ArrayList<>();
		
		for(CustomerOrder customerOrder : customerOrderList) {
			
			Order order = new Order();
			
			order.setCustomerId(customerOrder.getCustomer().getCustomerId());
	
			order.setOrderId(customerOrder.getOrderId());
			order.setOrderStatus(customerOrder.getOrderStatus());
			order.setOrderProfit(customerOrder.getOrderProfit());
			order.setOrderCost(customerOrder.getOrderCost());
			
			orders.add(order);
			
		}
		
		tx.commit();
		
		return orders;
		
	}
	
	@Override
	public List<Order> getOrdersByCustomerId(int customerId) {
		Session session = Connection.getSession();
		 
		Transaction tx = session.beginTransaction();
		
		Query query = session.createSQLQuery("select * from CustomerOrder where customerId = :customerId");
		query.setParameter("customerId", customerId);
		
		@SuppressWarnings({ "unchecked", "deprecation" })
		List<Customer> customerList = session.createCriteria(Customer.class).add(Restrictions.eq("customerId", customerId)).list();
		

		@SuppressWarnings({ "unchecked", "deprecation" })
		List<CustomerOrder> customerOrderList = session.createCriteria(CustomerOrder.class).add(Restrictions.eq("customer", customerList.get(0))).list();

	
		List<Order> orders = new ArrayList<>();
		
		for(CustomerOrder customerOrder : customerOrderList) {
			
			Order order = new Order();
			
			order.setCustomerId(customerOrder.getCustomer().getCustomerId());
	
			order.setOrderId(customerOrder.getOrderId());
			order.setOrderStatus(customerOrder.getOrderStatus());
			order.setOrderProfit(customerOrder.getOrderProfit());
			order.setOrderCost(customerOrder.getOrderCost());
			
			orders.add(order);
			
		}
		tx.commit();
		
		return orders;
	}
	
	@Override
	public String updateOrderStatus(CustomerOrderDetails customerOrderDetails) {
		
		Session session = Connection.getSession();
		
		Transaction tx = session.beginTransaction();
		
		System.out.println(customerOrderDetails.getOrderStatus());
		
		if(customerOrderDetails.getOrderStatus().equalsIgnoreCase("Confirmed")) {					
			
			Query selectProducts = session.createSQLQuery("SELECT * FROM ORDERPRODUCT WHERE orderId = :orderId");
			selectProducts.setParameter("orderId", customerOrderDetails.getOrderId());
			
			@SuppressWarnings("unchecked")
			List<Object[]> objList = selectProducts.getResultList();
			
			
			int updatedCount = 0;
			
			
			for(int i = 0; i < objList.size(); i++) {
				
				Query checkQuantities = session.createSQLQuery("SELECT * FROM PRODUCT WHERE PRODUCTID = :productId");
				
				checkQuantities.setParameter("productId", objList.get(i)[3]);
				
				@SuppressWarnings("unchecked")
				List<Object[]> checkQuantityList = checkQuantities.getResultList();
				
				if((int) checkQuantityList.get(0)[1] - (int) objList.get(i)[1] < 0) {
					
					return "Order Status cannot be modified due to insufficient available quantity";
					
				}
					
				
			}
			
			Query updateStatusQuery = session.createSQLQuery("UPDATE CUSTOMERORDER SET orderStatus = :status "
					+ "WHERE orderId = :orderId");
			updateStatusQuery.setParameter("status", customerOrderDetails.getOrderStatus());	
			updateStatusQuery.setParameter("orderId", customerOrderDetails.getOrderId());
				
			updateStatusQuery.executeUpdate();
			
			for(int i = 0; i < objList.size(); i++) {
		
				Query updateProductQuantites = session.createSQLQuery("UPDATE PRODUCT SET PRODUCTOUTGOING = PRODUCTOUTGOING + :productValue,"
						+ " PRODUCTAVAILABLE = PRODUCTAVAILABLE - :productValue WHERE productId = :productId");
	
				updateProductQuantites.setParameter("productValue", objList.get(i)[1]);
				updateProductQuantites.setParameter("productId", objList.get(i)[3]);
				
				@SuppressWarnings("unused")
				int count = updateProductQuantites.executeUpdate();
				
				updatedCount += 1;	
					
			}
			
			
			
			
			tx.commit();
			
			return "Updated Count for Not Confirmed -> Confirmed is : "  + Integer.toString(updatedCount);
		
		}
		
		if(customerOrderDetails.getOrderStatus().equalsIgnoreCase("Shipped")) {
			
			Query selectProducts = session.createSQLQuery("SELECT * FROM ORDERPRODUCT WHERE orderId = :orderId");
			selectProducts.setParameter("orderId", customerOrderDetails.getOrderId());
			
			@SuppressWarnings("unchecked")
			List<Object[]> objList = selectProducts.getResultList();
			
			
			int updatedCount = 0;
			
			
			for(int i = 0; i < objList.size(); i++) {
				
				Query checkQuantities = session.createSQLQuery("SELECT * FROM PRODUCT WHERE PRODUCTID = :productId");
				
				checkQuantities.setParameter("productId", objList.get(i)[3]);
				
				@SuppressWarnings("unchecked")
				List<Object[]> checkQuantityList = checkQuantities.getResultList();
				
				if((int) checkQuantityList.get(0)[4] - (int) objList.get(i)[1] < 0) {
					
					return "Order Status cannot be modified due to insufficient onhand quantity";
					
				}
				
				if((int) checkQuantityList.get(0)[5] - (int) objList.get(i)[1] < 0) {
					
					return "Order Status cannot be modified due to insufficient outgoing quantity";
					
				}
					
				
			}
			
			Query updateStatusQuery = session.createSQLQuery("UPDATE CUSTOMERORDER SET orderStatus = :status "
					+ "WHERE orderId = :orderId");
			updateStatusQuery.setParameter("status", customerOrderDetails.getOrderStatus());	
			updateStatusQuery.setParameter("orderId", customerOrderDetails.getOrderId());
				
			updateStatusQuery.executeUpdate();
			
			for(int i = 0; i < objList.size(); i++) {
		
				Query updateProductQuantites = session.createSQLQuery("UPDATE PRODUCT SET PRODUCTOUTGOING = PRODUCTOUTGOING - :productValue,"
						+ " PRODUCTONHAND= PRODUCTONHAND - :productValue WHERE productId = :productId");
	
				updateProductQuantites.setParameter("productValue", objList.get(i)[1]);
				updateProductQuantites.setParameter("productId", objList.get(i)[3]);
				
				@SuppressWarnings("unused")
				int count = updateProductQuantites.executeUpdate();
				
				updatedCount += 1;	
					
			}
			
			tx.commit();
			
			return "Updated Count for Confirmed -> Shipped is : "  + Integer.toString(updatedCount);
		
		}
		
		
		
		if(customerOrderDetails.getOrderStatus().equalsIgnoreCase("Completed")) {
			
			Query updateStatusQuery = session.createSQLQuery("UPDATE CUSTOMERORDER SET orderStatus = :status "
					+ "WHERE orderId = :orderId");
			updateStatusQuery.setParameter("status", customerOrderDetails.getOrderStatus());	
			updateStatusQuery.setParameter("orderId", customerOrderDetails.getOrderId());	
			
			updateStatusQuery.executeUpdate();
			
			tx.commit();
			
			return "Status changed from Shipped to Confirmed.";
		
		}
		
		if(customerOrderDetails.getOrderStatus().equalsIgnoreCase("Cancelled")) {
			
			
			Query getOldStatus = session.createSQLQuery("SELECT * FROM CUSTOMERORDER WHERE ORDERID = :orderId");
			
			getOldStatus.setParameter("orderId", customerOrderDetails.getOrderId());
			
			@SuppressWarnings("unchecked")
			List<Object[]> getOldStatusList = getOldStatus.getResultList();
			
			String oldStatus = (String) getOldStatusList.get(0)[2];
			
			System.out.println(oldStatus);
			
			if(oldStatus.equalsIgnoreCase("Confirmed")){
				
				Query updateStatusQuery = session.createSQLQuery("UPDATE CUSTOMERORDER SET orderStatus = :status "
						+ "WHERE orderId = :orderId");
				updateStatusQuery.setParameter("status", customerOrderDetails.getOrderStatus());	
				updateStatusQuery.setParameter("orderId", customerOrderDetails.getOrderId());	
						
				
				Query selectProducts = session.createSQLQuery("SELECT * FROM ORDERPRODUCT WHERE orderId = :orderId");
				selectProducts.setParameter("orderId", customerOrderDetails.getOrderId());
				
				@SuppressWarnings("unchecked")
				List<Object[]> objList = selectProducts.getResultList();
				
				updateStatusQuery.executeUpdate();
				
				@SuppressWarnings("unused")
				int updatedCount = 0;
				
				for(int i = 0; i < objList.size(); i++) {
						
					Query updateProductQuantites = session.createSQLQuery("UPDATE PRODUCT SET PRODUCTOUTGOING = PRODUCTOUTGOING - :productValue,"
							+ " PRODUCTAVAILABLE = PRODUCTAVAILABLE + :productValue WHERE productId = :productId");
					
					updateProductQuantites.setParameter("productValue", objList.get(i)[1]);
					updateProductQuantites.setParameter("productId", objList.get(i)[3]);
					
					@SuppressWarnings("unused")
					int count = updateProductQuantites.executeUpdate();
					
					updatedCount += 1;	
					
				}
				
				tx.commit();
				
				return "Changed status from Confirmed -> Cancelled.";
			
			} else {	
				return "You cannot cancel the product once it is shipped";
				
			}
		}
				
		return null;
	}
	
}