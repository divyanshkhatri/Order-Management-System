import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.accolite.controller.CustomerController;
import com.accolite.dao.impl.CustomerDAOImpl;
import com.accolite.model.Customer;
import com.accolite.model.CustomerData;
import com.accolite.service.CustomerService;

//@RunWith(SpringRunner.class)
//@WebMvcTest(value = CustomerController.class)
//@SpringBootTest(classes= {com.accolite.controller.CustomerController.class})
@ExtendWith(MockitoExtension.class)
public class CustomerRestControllerIntegrationTest {
	
//	@Autowired
//	private MockMvc mockMvc;
	
	@Mock
	CustomerService customerService;
	
	@InjectMocks
	CustomerController customerController = new CustomerController();
	
	@InjectMocks
	CustomerDAOImpl customerDaoImpl = new CustomerDAOImpl();
	
//	@Autowired
//	private CustomerService customerService;
    	
//	CustomerData mockCustomer = new CustomerData(Arrays.asList(customers), 1);
	
//	CustomerService customerService = new CustomerService();
	
	@Test
	public void getCustomerDetails() throws Exception{
		
		Customer customer = new Customer(1, "Divyansh", "9868228970", "kh@gmail.com", "UGH-9, Ajnara", "201011");
		
		List<Customer> customers = new ArrayList<>();
		
		customers.add(customer);
		
		CustomerData mockCustomer = new CustomerData(customers, 1);
		
		CustomerDAOImpl mock = org.mockito.Mockito.mock(CustomerDAOImpl.class);
		
		when(mock.list(0,1)).thenReturn(mockCustomer);
		
//		when(customerService.getCustomers(0, 1)).thenReturn(mockCustomer);
		
		ResponseEntity<CustomerData> customerData = customerController.getCustomer(0, 1);
		
		assertEquals(mockCustomer.getCustomers().get(0).getCustomerId(), customerData.getBody().getCustomers().get(0).getCustomerId());
		
	}

	@Test
	public void createCustomer() throws Exception{
		
		Customer mockCustomer = new Customer(1, "Divyansh", "9868228970", "kh@gmail.com", "UGH-9, Ajnara", "201011");
		
		
		CustomerDAOImpl mock = org.mockito.Mockito.mock(CustomerDAOImpl.class);
		
		when(mock.save(mockCustomer)).thenReturn(mockCustomer);
		
//		when(customerService.getCustomers(0, 1)).thenReturn(mockCustomer);
		
		ResponseEntity<Customer> customerData = customerController.createCustomer(mockCustomer);

		assertEquals(mockCustomer.getCustomerId(), customerData.getBody().getCustomerId());
		assertEquals(mockCustomer.getCustomerName(), customerData.getBody().getCustomerName());
		assertEquals(mockCustomer.getCustomerEmail(), customerData.getBody().getCustomerEmail());
		assertEquals(mockCustomer.getCustomerAddress(), customerData.getBody().getCustomerAddress());
		assertEquals(mockCustomer.getCustomerPincode(), customerData.getBody().getCustomerPincode());

	}

	
	
}