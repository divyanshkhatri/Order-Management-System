import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.ResponseEntity;

import com.accolite.controller.CustomerController;
import com.accolite.controller.SupplierController;
import com.accolite.dao.impl.CustomerDAOImpl;
import com.accolite.dao.impl.SupplierDAOImpl;
import com.accolite.model.Customer;
import com.accolite.model.CustomerData;
import com.accolite.model.Supplier;
import com.accolite.service.CustomerService;

//@RunWith(SpringRunner.class)
//@WebMvcTest(value = CustomerController.class)
//@SpringBootTest(classes= {com.accolite.controller.CustomerController.class})
@ExtendWith(MockitoExtension.class)
public class SupplierRestControllerIntegrationTest {
	
//	@Autowired
//	private MockMvc mockMvc;
	
	@Mock
	CustomerService customerService;
	
	@InjectMocks
	SupplierController supplierController = new SupplierController();
	
	@InjectMocks
	SupplierDAOImpl customerDaoImpl = new SupplierDAOImpl();
	
//	@Autowired
//	private CustomerService customerService;
    	
//	CustomerData mockCustomer = new CustomerData(Arrays.asList(customers), 1);
	
//	CustomerService customerService = new CustomerService();
	
	@Test
	public void getSupplierDetails() throws Exception{
		
		Supplier mockSupplier = new Supplier(1, "Divyansh", "9868228970", "kh@gmail.com", "UGH-9, Ajnara", "201011");
		
		List<Supplier> suppliers = new ArrayList<>();

		SupplierDAOImpl mock = org.mockito.Mockito.mock(SupplierDAOImpl.class);
		
		when(mock.list(0,1)).thenReturn(suppliers);
		
//		when(customerService.getCustomers(0, 1)).thenReturn(mockCustomer);
		
		ResponseEntity<List<Supplier>> customerData = supplierController.getSupplier(0, 1);
		
		assertEquals(mockSupplier.getSupplierId(), customerData.getBody().get(0).getSupplierId());
		
	}

	@Test
	public void createSupplier() throws Exception{
		
		Supplier mockSupplier = new Supplier(3, "Divyansh", "9868228970", "kh@gmail.com", "UGH-9, Ajnara", "201011");
		
		SupplierDAOImpl mock = org.mockito.Mockito.mock(SupplierDAOImpl.class);
		
		when(mock.save(mockSupplier)).thenReturn(mockSupplier);
		
		ResponseEntity<Supplier> supplierData = supplierController.createSupplier(mockSupplier);

//		assertEquals(mockSupplier.getSupplierId(), supplierData.getBody().getSupplierId());
		assertEquals(mockSupplier.getSupplierName(), supplierData.getBody().getSupplierName());
		assertEquals(mockSupplier.getSupplierEmail(), supplierData.getBody().getSupplierEmail());
		assertEquals(mockSupplier.getSupplierAddress(), supplierData.getBody().getSupplierAddress());
		assertEquals(mockSupplier.getSupplierPincode(), supplierData.getBody().getSupplierPincode());

	}

	
	
}