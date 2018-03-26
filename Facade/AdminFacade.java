package Facade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import Corpus.Company;
import Corpus.ConnectionPool;
import Corpus.Coupon;
import Corpus.Customer;
import DAO.CompanyDAO;
import DAO.CustomerDAO;
import DBDAO.CompanyDBDAO;
import DBDAO.CustomerDBDAO;
import Exceptions.CouponSystemExceptions;

public class AdminFacade implements CouponClientFacade {

	CompanyDBDAO cd = new CompanyDBDAO();

	public AdminFacade(CompanyDBDAO cd, CustomerDBDAO cust) {
		super();
		this.cd = cd;
		this.cust = cust;
	}

	public void createCompany(Company c) throws CouponSystemExceptions {
		Collection<Company> companies = cd.getAllCompanies();
		for (Company company : companies) {
			if (company.getCompName().equals(c.getCompName())) {
				CouponSystemExceptions e = new CouponSystemExceptions();
				e.setMessage("Duplicate name");
				throw e;
			}
		}
		cd.createCompany(c);
		System.out.println("The Company has been added");

	}

	public void removeCompany(Company c) throws CouponSystemExceptions {
		cd.removeCompany(c);

	}

	public void updateCompany(Company c) {

		cd.updateCompany2(c);
	}

	public Company getCompany(long id) throws CouponSystemExceptions {
		Company comp = cd.getCompany(id);
		return comp;
	}

	public Collection<Company> getAllCompanies() {
		Collection<Company> companies = new HashSet<>();
		companies = cd.getAllCompanies();
		return companies;
	}

	public AdminFacade() {
	}

	@Override
	public CouponClientFacade login(String name, String password, ClientType clientType) throws CouponSystemExceptions {
		if (name.equals("admin") && password.equals("1234") && clientType.equals(ClientType.ADMINISTRATOR)) {
			System.out.println("Success");
			return this;
		} else
			throw new CouponSystemExceptions("Not identified");

	};

	CustomerDBDAO cust = new CustomerDBDAO();

	public void createCustomer(Customer c1) throws CouponSystemExceptions {
		Collection<Customer> customers = cust.getAllCustomer();
		for (Customer customer : customers) {
			if (customer.getCustName().equals(c1.getCustName())) {
				CouponSystemExceptions e = new CouponSystemExceptions();
				e.setMessage("Duplicate name");
				throw e;
			}

		}
		cust.createCustomer(c1);

	}

	public void removeCustomer(Customer c) {
		cust.removeCustomer(c);
	}

	public void updateCustomer(Customer c) {
		cust.updatecustomer2(c);
		System.out.println("Customer updated");
	}

	public Customer getCustomer(int id) throws CouponSystemExceptions {
		Customer c = cust.getCustomer(id);
		return c;

	}

	public Collection<Customer> getAllCustomer() {
		Collection<Customer> customers = cust.getAllCustomer();
		return customers;
	}
};
