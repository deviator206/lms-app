package service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import repository.entity.Customer;
import util.ExcelUtils;

@Service
public class FileServices {

	// @Autowired
	// CustomerRepository customerRepository;

	// Load Data to Excel File
	public ByteArrayInputStream loadFile() {
		// List<Customer> customers = (List<Customer>) customerRepository.findAll();
		List<Customer> customers = new ArrayList<Customer>();
		Customer cust = new Customer();
		cust.setId(11111);
		cust.setName("1111");
		cust.setAge(1111);
		cust.setAddress("1111");
		customers.add(cust);
		try {
			ByteArrayInputStream in = ExcelUtils.customersToExcel(customers);
			return in;
		} catch (IOException e) {
		}

		return null;
	}
}
