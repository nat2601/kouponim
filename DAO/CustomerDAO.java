package DAO;

import java.util.Collection;

import Corpus.Coupon;
import Corpus.Customer;
import Exceptions.CouponSystemExceptions;

public interface CustomerDAO {
	public void createCustomer(Customer c) throws CouponSystemExceptions;

	public void removeCustomer(Customer c) throws CouponSystemExceptions;

	public void updateCustomer(Customer c) throws CouponSystemExceptions;

	public Customer getCustomer(int id) throws CouponSystemExceptions;

	public Collection<Customer> getAllCustomer();

	public Collection<Coupon> getCustomerCoupons(Customer c);

	public boolean login(String custName, String password) throws CouponSystemExceptions;

}
