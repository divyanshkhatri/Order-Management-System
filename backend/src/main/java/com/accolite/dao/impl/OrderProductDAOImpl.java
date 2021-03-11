package com.accolite.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.accolite.dao.OrderProductDAO;
import com.accolite.model.Customer;
import com.accolite.model.CustomerOrder;
import com.accolite.model.OrderProduct;
import com.accolite.model.OrderProductDetails;
import com.accolite.model.Product;
import com.accolite.utils.Connection;

public class OrderProductDAOImpl implements OrderProductDAO {
	
	@Override
	public OrderProduct save(OrderProductDetails orderProductdetails) {
		
		Session session = Connection.getSession();
		
		Transaction tx = session.beginTransaction();
		
		OrderProduct orderProductData = new OrderProduct();
		
		int productId= orderProductdetails.getProductId();
		int productQuantity = orderProductdetails.getProductQuantity();
		
		Query query1 = session.createSQLQuery("select * from Product WHERE productId = :id");
		query1.setParameter("id", productId);
		@SuppressWarnings("unchecked")
		List<Object[]> obj2 = query1.getResultList();
		Product product=new Product();
		
		product.setProductId((int)obj2.get(0)[0]);
		product.setProductAvailable((int)obj2.get(0)[1]);
		product.setProductcostPrice((int)obj2.get(0)[7]);
		product.setProductIncoming((int)obj2.get(0)[2]);
		product.setProductName((String)obj2.get(0)[3]);
		product.setProductOnHand((int)obj2.get(0)[4]);
		product.setProductOutgoing((int)obj2.get(0)[5]);

		product.setProductSellingPrice((int)obj2.get(0)[6]);
		
		Query checkQuery = session.createSQLQuery("Select * FROM ORDERPRODUCT WHERE orderId = :orderId and productId = :productId");
		checkQuery.setParameter("productId", orderProductdetails.getProductId()); 
		checkQuery.setParameter("orderId", orderProductdetails.getOrderId()); 
		
		@SuppressWarnings("unchecked")
		List<Object[]> checkProduct = checkQuery.getResultList();
		
		if(checkProduct.size() == 0) {
			
			
			
			@SuppressWarnings({ "unchecked", "deprecation" })
			List<CustomerOrder> cr = session.createCriteria(CustomerOrder.class).add(Restrictions.eq("orderId", orderProductdetails.getOrderId())).list();
			
			CustomerOrder customerOrder = new CustomerOrder();
			
			
			Query query2 = session.createSQLQuery("select * from Customer WHERE customerId = :id");
			query2.setParameter("id", cr.get(0).getCustomer().getCustomerId());
			@SuppressWarnings("unchecked")
			List<Object[]> obj1 = query2.getResultList();
			Customer customer = new Customer();
			customer.setCustomerId((int) obj1.get(0)[0]);
			customer.setCustomerName((String) obj1.get(0)[3]);
			customer.setCustomerPhone((String) obj1.get(0)[4]);
			customer.setCustomerEmail((String) obj1.get(0)[2]);
			customer.setCustomerAddress((String) obj1.get(0)[1]);
			customer.setCustomerPincode((String) obj1.get(0)[5]);
			
		
		
			customerOrder.setOrderStatus((String)cr.get(0).getOrderStatus());
			customerOrder.setOrderId((int)cr.get(0).getOrderId());
			customerOrder.setCustomer(customer);
			customerOrder.setOrderProfit((int) cr.get(0).getOrderProfit());
			customerOrder.setOrderCost((int) cr.get(0).getOrderCost());
			

			//////////////////////////////////////////////////////////
			
			Query query3 = session.createSQLQuery("select * from Product WHERE productId = :id");
			query3.setParameter("id", productId);
			@SuppressWarnings("unchecked")
			List<Object[]> obj3 = query1.getResultList();
			Product product1=new Product();
			
			product1.setProductId((int)obj3.get(0)[0]);
			product1.setProductAvailable((int)obj3.get(0)[1]);
			product1.setProductcostPrice((int)obj3.get(0)[7]);
			product1.setProductIncoming((int)obj3.get(0)[2]);
			product1.setProductName((String)obj3.get(0)[3]);
			product1.setProductOnHand((int)obj3.get(0)[4]);
			product1.setProductOutgoing((int)obj3.get(0)[5]);
	
			product1.setProductSellingPrice((int)obj3.get(0)[6]);
			
			Query orderCostUpdateQuery = session.createSQLQuery("UPDATE CUSTOMERORDER SET orderProfit = orderProfit + :newOrderProfit,"
					+ "orderCost = orderCost + :newOrderCost "
					+ "WHERE ORDERID = :orderId ");
			orderCostUpdateQuery.setParameter("newOrderCost", productQuantity * product1.getProductSellingPrice());
			orderCostUpdateQuery.setParameter("newOrderProfit", productQuantity * (product1.getProductSellingPrice() - product1.getProductcostPrice()));
			orderCostUpdateQuery.setParameter("orderId", orderProductdetails.getOrderId());
			
			int count3 = orderCostUpdateQuery.executeUpdate();
			System.out.println(count3);
			
			orderProductData.setProduct(product1);
			orderProductData.setProductQuantity(orderProductdetails.getProductQuantity());		
			orderProductData.setCustomerOrder(customerOrder);
			
			session.save(orderProductData);
			tx.commit();
			return orderProductData;
			
		} else {
			
			return orderProductData;
			
		}	
		
	}
	
	@Override
	public List<OrderProductDetails> getProductsByOrderId(int orderId) {
		
		Session session = Connection.getSession();
		
		Transaction tx = session.beginTransaction();
		
		List<OrderProductDetails> orderProductsDetails = new ArrayList<>();
		
		Query query = session.createSQLQuery("select * from OrderProduct WHERE orderId = :id");
		
		query.setParameter("id", orderId);
		
		@SuppressWarnings("unchecked")
		List<Object[]> obj2 = query.getResultList();
	
		
		for(Object[] o : obj2) {
			
			OrderProductDetails orderProduct = new OrderProductDetails();
				
			orderProduct.setOrderId((int) o[2]);
			orderProduct.setProductId((int) o[3]);
			orderProduct.setProductQuantity((int) o[1]);
			
			orderProductsDetails.add(orderProduct);
			
		}
		
		tx.commit();
		
		return orderProductsDetails;
		
	}


}
