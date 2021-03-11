package com.accolite.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.accolite.dao.PurchaseProductDAO;
import com.accolite.model.OrderProduct;
import com.accolite.model.OrderProductDetails;
import com.accolite.model.Product;
import com.accolite.model.PurchaseOrder;
import com.accolite.model.PurchaseProduct;
import com.accolite.model.PurchaseProductDetails;
import com.accolite.model.Supplier;
import com.accolite.model.UpdatePurchaseStatus;
import com.accolite.utils.Connection;

public class PurchaseProductDAOImpl implements PurchaseProductDAO{
	
	@Override
	public PurchaseProduct save(PurchaseProductDetails purchaseProductdetails) {
		
		Session session = Connection.getSession();
		
		Transaction tx = session.beginTransaction();
		
		PurchaseProduct purchaseProductData = new PurchaseProduct();
		
		int purchaseId = purchaseProductdetails.getPurchaseId();
		System.out.println(purchaseId);
		int productId= purchaseProductdetails.getProductId();
		@SuppressWarnings({ "unchecked", "deprecation" })
		List<PurchaseOrder> list = session.createCriteria(PurchaseOrder.class).add(Restrictions.eq("purchaseId", purchaseId)).list();
		for(int i = 0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
		
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		System.out.println("Supplier Id is: " + list.get(0).getSupplier().getSupplierId());
		Query query2 = session.createSQLQuery("select * from Supplier WHERE supplierId = :id");
		query2.setParameter("id", list.get(0).getSupplier().getSupplierId());
		@SuppressWarnings("unchecked")
		List<Object[]> obj1 = query2.getResultList();
		Supplier supplier = new Supplier();
		supplier.setSupplierId((int)obj1.get(0)[0]);

		supplier.setSupplierAddress((String)obj1.get(0)[1]);

		supplier.setSupplierEmail((String)obj1.get(0)[2]);

		supplier.setSupplierName((String)obj1.get(0)[3]);
		supplier.setSupplierPhone((String)obj1.get(0)[4]);

		supplier.setSupplierPincode((String)obj1.get(0)[5]);
		
		
		purchaseOrder.setPurchaseId((int)list.get(0).getPurchaseId());

		purchaseOrder.setPurchaseStatus((String)list.get(0).getPurchaseStatus());

		purchaseOrder.setSupplier(supplier);

	   purchaseOrder.setPurchaseCost((int) list.get(0).getPurchaseCost());
	
		
		Query query1 = session.createSQLQuery("select * from Product WHERE productId = :id");
		query1.setParameter("id", productId);
		@SuppressWarnings("unchecked")
		List<Object[]> obj2 = query1.getResultList();
		Product product=new Product();
		
		product.setProductId((int)obj2.get(0)[0]);
		product.setProductAvailable((int) obj2.get(0)[1]);
		product.setProductcostPrice((int) obj2.get(0)[7]);
		product.setProductIncoming((int) obj2.get(0)[2]);
		product.setProductName((String) obj2.get(0)[3]);
		product.setProductOnHand((int) obj2.get(0)[4]);
		product.setProductOutgoing((int) obj2.get(0)[5]);

		product.setProductSellingPrice((int) obj2.get(0)[6]);
		
		purchaseProductData.setProduct(product);
		purchaseProductData.setProductQuantity(purchaseProductdetails.getProductQuantity());
		purchaseProductData.setPurchaseOrder(purchaseOrder);
		

		session.save(purchaseProductData);
		tx.commit();

		
		return purchaseProductData;
	}
	
	@Override
	public String updatePurchaseStatus(UpdatePurchaseStatus updatePurchaseStatus) {
		
		Session session = Connection.getSession();
		
		Transaction tx = session.beginTransaction();
		
		System.out.println(updatePurchaseStatus.getPurchaseStatus());
		
		if(updatePurchaseStatus.getPurchaseStatus().equalsIgnoreCase("Raised")) {
			
			Query updateStatusQuery = session.createSQLQuery("UPDATE PURCHASEORDER SET status = :status "
					+ "WHERE purchaseId = :purchaseId");
			updateStatusQuery.setParameter("status", updatePurchaseStatus.getPurchaseStatus());	
			updateStatusQuery.setParameter("purchaseId", updatePurchaseStatus.getPurchaseId());	
			
					
//			List<PurchaseProduct> list = session.createCriteria(PurchaseProduct.class).add(Restrictions.eq("purchaseOrder.getpurchaseId()", updatePurchaseStatus.getPurchaseId())).list();
			
			Query selectProducts = session.createSQLQuery("SELECT * FROM PURCHASEPRODUCT WHERE purchaseId = :purchaseId");
			selectProducts.setParameter("purchaseId", updatePurchaseStatus.getPurchaseId());
			
			int newPurchaseCost = 0;
			
			Query calculatePurchaseCost = session.createSQLQuery("SELECT * FROM PURCHASEPRODUCT where purchaseId = :purchaseId");
			calculatePurchaseCost.setParameter("purchaseId", updatePurchaseStatus.getPurchaseId());
			
			@SuppressWarnings("unchecked")
			List<Object[]> calculatePurchaseCostList = calculatePurchaseCost.getResultList();
			
			for(int i = 0; i < calculatePurchaseCostList.size(); i++ ) {
				
				Query calculateFromProduct = session.createSQLQuery("SELECT * FROM PRODUCT WHERE productId = :productId");
				
				calculateFromProduct.setParameter("productId", calculatePurchaseCostList.get(i)[2]);
				
				@SuppressWarnings("unchecked")
				List<Object[]> productObj = calculateFromProduct.getResultList();
				
				newPurchaseCost += ((int) productObj.get(0)[7] * (int) calculatePurchaseCostList.get(i)[1]);
				
			}
			
			
			Query updatePurchaseCost = session.createSQLQuery("UPDATE PURCHASEORDER SET purchaseCost = :newPurchase WHERE purchaseId = :purchaseId");
			
			updatePurchaseCost.setParameter("newPurchase", newPurchaseCost);
			
			updatePurchaseCost.setParameter("purchaseId", updatePurchaseStatus.getPurchaseId());
			
			updatePurchaseCost.executeUpdate();
			
			@SuppressWarnings("unchecked")
			List<Object[]> objList = selectProducts.getResultList();
			
			
			
			updateStatusQuery.executeUpdate();
			
			int updatedCount = 0;
			
			for(int i = 0; i < objList.size(); i++) {
					
				Query updateProductQuantites = session.createSQLQuery("UPDATE PRODUCT SET PRODUCTINCOMING = PRODUCTINCOMING + :productValue "
						+ "WHERE productId = :productId");
				
				updateProductQuantites.setParameter("productValue", objList.get(i)[1]);
				updateProductQuantites.setParameter("productId", objList.get(i)[2]);
				
				@SuppressWarnings("unused")
				int count = updateProductQuantites.executeUpdate();
				
				updatedCount += 1;	
				
			}
			
			tx.commit();
			
			return "Updated Count for Draft -> Raised is : "  + Integer.toString(updatedCount);
		
		}
		
		if(updatePurchaseStatus.getPurchaseStatus().equalsIgnoreCase("Received")) {
			
			Query updateStatusQuery = session.createSQLQuery("UPDATE PURCHASEORDER SET status = :status "
					+ "WHERE purchaseId = :purchaseId");
			updateStatusQuery.setParameter("status", updatePurchaseStatus.getPurchaseStatus());	
			updateStatusQuery.setParameter("purchaseId", updatePurchaseStatus.getPurchaseId());	
					
//			List<PurchaseProduct> list = session.createCriteria(PurchaseProduct.class).add(Restrictions.eq("purchaseOrder.getpurchaseId()", updatePurchaseStatus.getPurchaseId())).list();
			
			Query selectProducts = session.createSQLQuery("SELECT * FROM PURCHASEPRODUCT WHERE purchaseId = :purchaseId");
			selectProducts.setParameter("purchaseId", updatePurchaseStatus.getPurchaseId());
			
			@SuppressWarnings("unchecked")
			List<Object[]> objList = selectProducts.getResultList();
			
			updateStatusQuery.executeUpdate();
			
			int updatedCount = 0;
			
			for(int i = 0; i < objList.size(); i++) {
					
				Query updateProductQuantites = session.createSQLQuery("UPDATE PRODUCT SET PRODUCTINCOMING = PRODUCTINCOMING - :productValue,"
						+ "PRODUCTONHAND = PRODUCTONHAND + :productValue "
						+ "WHERE productId = :productId");
				
				updateProductQuantites.setParameter("productValue", objList.get(i)[1]);
				updateProductQuantites.setParameter("productId", objList.get(i)[2]);
				
				@SuppressWarnings("unused")
				int count = updateProductQuantites.executeUpdate();
				
				updatedCount += 1;	
				
			}
			
			
			
			tx.commit();
			
			return "Updated Count for Raised -> Received is : "  + Integer.toString(updatedCount);
		
		}
		
		if(updatePurchaseStatus.getPurchaseStatus().equalsIgnoreCase("Closed")) {
			
			Query updateStatusQuery = session.createSQLQuery("UPDATE PURCHASEORDER SET status = :status "
					+ "WHERE purchaseId = :purchaseId");
			updateStatusQuery.setParameter("status", updatePurchaseStatus.getPurchaseStatus());	
			updateStatusQuery.setParameter("purchaseId", updatePurchaseStatus.getPurchaseId());	
			
			updateStatusQuery.executeUpdate();
			tx.commit();
			
			return "Changed from Received -> Completed.";
		
		}
		
		if(updatePurchaseStatus.getPurchaseStatus().equalsIgnoreCase("Cancelled")) {

			@SuppressWarnings({ "unchecked", "deprecation" })
			List<PurchaseOrder> fetchStatusList = session.createCriteria(PurchaseOrder.class).add(Restrictions.eq("purchaseId", updatePurchaseStatus.getPurchaseId())).list();
			
			
			String oldStatus = (String) fetchStatusList.get(0).getPurchaseStatus();
			
			if(oldStatus.equalsIgnoreCase("Raised")) {
			
				Query updateStatusQuery = session.createSQLQuery("UPDATE PURCHASEORDER SET status = :status "
						+ "WHERE purchaseId = :purchaseId");
				updateStatusQuery.setParameter("status", updatePurchaseStatus.getPurchaseStatus());	
				updateStatusQuery.setParameter("purchaseId", updatePurchaseStatus.getPurchaseId());	
						
	//			List<PurchaseProduct> list = session.createCriteria(PurchaseProduct.class).add(Restrictions.eq("purchaseOrder.getpurchaseId()", updatePurchaseStatus.getPurchaseId())).list();
				
				Query selectProducts = session.createSQLQuery("SELECT * FROM PURCHASEPRODUCT WHERE purchaseId = :purchaseId");
				selectProducts.setParameter("purchaseId", updatePurchaseStatus.getPurchaseId());
				
				@SuppressWarnings("unchecked")
				List<Object[]> objList = selectProducts.getResultList();
				
				updateStatusQuery.executeUpdate();
				
				@SuppressWarnings("unused")
				int updatedCount = 0;
				
				for(int i = 0; i < objList.size(); i++) {
						
					Query updateProductQuantites = session.createSQLQuery("UPDATE PRODUCT SET PRODUCTINCOMING = PRODUCTINCOMING - :productValue "
							+ "WHERE productId = :productId");
					
					updateProductQuantites.setParameter("productValue", objList.get(i)[1]);
					updateProductQuantites.setParameter("productId", objList.get(i)[2]);
					
					@SuppressWarnings("unused")
					int count = updateProductQuantites.executeUpdate();
					
					updatedCount += 1;	
					
				}
				

				tx.commit();
				
				return "Changed from raised to cancelled";
				
				
			} else {
				return "You can't change once the status is above raised";
			}
		}	
		return null;
	}

	@Override
	public List<PurchaseProductDetails> getProducts(int purchaseId) {
		
		Session session = Connection.getSession();
		
		System.out.println(purchaseId);
		
		@SuppressWarnings({ "unchecked", "deprecation" })
		List<PurchaseOrder> purchaseList = session.createCriteria(PurchaseOrder.class).add(Restrictions.eq("purchaseId", purchaseId)).list();
		
		System.out.println(purchaseList.get(0).getPurchaseId());
		@SuppressWarnings({ "unchecked", "deprecation" })
		List<PurchaseProduct> purchaseProductList = session.createCriteria(PurchaseProduct.class).add(Restrictions.eq("purchaseOrder", purchaseList.get(0))).list();
		
		List<PurchaseProductDetails> purchaseProductDetails = new ArrayList<>();
		
		for(int i = 0; i<purchaseProductList.size(); i++) {
			
			PurchaseProductDetails purchaseProductDetail = new PurchaseProductDetails();
			
			purchaseProductDetail.setProductId(purchaseProductList.get(i).getProduct().getProductId());
			purchaseProductDetail.setPurchaseId(purchaseProductList.get(i).getPurchaseOrder().getPurchaseId());
			purchaseProductDetail.setProductQuantity(purchaseProductList.get(i).getProductQuantity());
			
			purchaseProductDetails.add(purchaseProductDetail);
			
		}
		
		return purchaseProductDetails;
		
	}

	@Override
	public List<OrderProductDetails> getSoldProducts() {
		
		Session session = Connection.getSession();

		@SuppressWarnings({ "unchecked", "deprecation" })
		List<OrderProduct> orderProductList = session.createCriteria(OrderProduct.class).list();
		
		List<OrderProductDetails> orderProductDetails = new ArrayList<>();
		
		for(int i = 0; i<orderProductList.size(); i++) {
			
			OrderProductDetails orderProductDetail = new OrderProductDetails();
			
			orderProductDetail.setProductId(orderProductList.get(i).getProduct().getProductId());
			orderProductDetail.setProductQuantity(orderProductList.get(i).getProductQuantity());
			orderProductDetail.setOrderId(orderProductList.get(i).getCustomerOrder().getOrderId());
			
			orderProductDetails.add(orderProductDetail);
			
		}
		
		return orderProductDetails;
		
	}
	
	


}


