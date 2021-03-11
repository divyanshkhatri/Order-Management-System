import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


import com.accolite.controller.CustomerController;
import com.accolite.controller.OrderController;
import com.accolite.dao.impl.CustomerOrderDAOImpl;
import com.accolite.model.Order;
import com.accolite.service.CustomerOrderService;

@ExtendWith(MockitoExtension.class)
public class OrderRestControllerIntegrationTest {
	
	
	@InjectMocks
	CustomerOrderService orderService;
	
	@InjectMocks
	OrderController orderController = new OrderController();
	
	@InjectMocks
	CustomerController customerController = new CustomerController();
	
	@InjectMocks
	CustomerOrderDAOImpl customerOrderDaoImpl = new CustomerOrderDAOImpl();
	

	
	@Test
	public void getOrders() throws Exception{
		
		Order mockOrder = new Order(2, 2, "NotConfirmed", 0);

		List<Order> orders = new ArrayList<>();
		
		orders.add(mockOrder);
		
		CustomerOrderDAOImpl mock = org.mockito.Mockito.mock(CustomerOrderDAOImpl.class);
		
		when(mock.list()).thenReturn(orders);
		
		ResponseEntity<List<Order>> customerOrderData = orderController.getOrder();
		
		assertEquals(orders.get(0).getOrderProfit(), customerOrderData.getBody().get(0).getOrderProfit());
		
	}
	

//	@Test
//	public void createOrder() throws Exception{
//		
//		Customer mockCustomer = new Customer(3, "Divyansh", "9868228970", "kh@gmail.com", "UGH-9, Ajnara", "201011");
//		
//		
//		Order mockOrder = new Order(3, 2, "NotConfirmed");
//
//		CustomerOrder mockCustomerOrder = new CustomerOrder();
//		
//		mockCustomerOrder.setCustomer(mockCustomer);
//		mockCustomerOrder.setOrderStatus("NotConfirmed");
//		mockCustomerOrder.setOrderId(2);
//		mockCustomerOrder.setOrderProfit(0);
//		
//		
//		CustomerOrderDAOImpl mock = org.mockito.Mockito.mock(CustomerOrderDAOImpl.class);
//		
//		when(mock.save(mockOrder)).thenReturn(mockCustomerOrder);
		
//		CustomerOrderService mockService = org.mockito.Mockito.mock(CustomerOrderService.class);
//		
//		when(mockService.createOrder(mockOrder)).thenReturn(mockCustomerOrder);
		
//		ResponseEntity<Customer> customer = customerController.createCustomer(mockCustomer);
//		
//		CustomerOrderService customerOrderService = new CustomerOrderService();
//		
//		CustomerOrder customerOrderData = customerOrderService.createOrder(mockOrder);
//		
//		System.out.println("CustomerId: " + customerOrderData.getCustomer().getCustomerId());
//		assertEquals(mockCustomerOrder.getOrderStatus(), customerOrderData.getOrderStatus());
//
//	}
	
}