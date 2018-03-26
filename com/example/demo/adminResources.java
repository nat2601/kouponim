package com.example.demo;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import Corpus.Company;
import Corpus.Coupon;
import Corpus.Customer;
import Exceptions.CouponSystemExceptions;
import Facade.AdminFacade;

@Controller
@CrossOrigin(origins = "*")
@RestController
public class adminResources {
	/**
	 * Method handling HTTP GET requests. The returned object will be sent to the
	 * client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */

	AdminFacade admin = new AdminFacade();

	// Get All Company
	@RequestMapping(value = "/getallcompanies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Company> getallCompanies() {
		System.out.println("im here getallcomps");
		Collection<Company> companies = admin.getAllCompanies();
		return companies;
	}

	// Get company By id
	@RequestMapping(value = "/getallcompanies/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Company> getcompanybyid(@PathVariable("id") long id) throws CouponSystemExceptions {
		Company c = new Company();
		try {

			c = admin.getCompany(id);
		} catch (CouponSystemExceptions e) {
			throw e;
		}

		return ResponseEntity.ok().body(c);
	}

	// Create company
	@RequestMapping(value = "/createcompany", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public ResponseEntity createCompany(@RequestBody Company c) throws CouponSystemExceptions {
		try {
			admin.createCompany(c);
		} catch (CouponSystemExceptions e) {
			throw e;
		}
		return new ResponseEntity<>("Success", HttpStatus.OK);
	}

	// Remove company
	@RequestMapping(value = "/removecompany", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)

	public void removeCompany(@RequestBody Company c) throws CouponSystemExceptions {
		try {
			admin.removeCompany(c);
		} catch (CouponSystemExceptions e) {

		}
	}

	// Update company
	@RequestMapping(value = "/updatecompany", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)

	public void updateCompany(@RequestBody Company c) {
		admin.updateCompany(c);
	}

	// Create Customer
	@RequestMapping(value = "/createcustomer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public void createcustomer(@RequestBody Customer cust) throws CouponSystemExceptions {
		try {
			admin.createCustomer(cust);
		} catch (CouponSystemExceptions e) {
			e.setMessage("Duplicate name");
			throw e;
		}
	}

	@RequestMapping(value = "/removecustomer", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)

	public void removeCustomer(@RequestBody Customer cust) {
		admin.removeCustomer(cust);
	}

	// Update Customer
	@RequestMapping(value = "/updatecustomer", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)

	public void updateCustomer(@RequestBody Customer cust) throws CouponSystemExceptions {

		admin.updateCustomer(cust);

	}

	// Get Customer by ID
	@RequestMapping(value = "/getcustomer/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

	public Customer getCustomer(@PathVariable("id") int id) throws CouponSystemExceptions {
		Customer cust = new Customer();
		try {
			cust = admin.getCustomer(id);
		} catch (CouponSystemExceptions e) {
			e.setMessage("No Customer with this id ");
			throw e ;
		}
		return cust;

	}

	// Get All Customer
	@RequestMapping(value = "/getallcustomers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Customer> getAllcustomer() {
		Collection<Customer> customers = admin.getAllCustomer();
		return customers;
	}


}
