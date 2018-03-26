package DAO;

import java.util.Collection;

import Corpus.Company;
import Corpus.Coupon;
import Exceptions.CouponSystemExceptions;

public interface CompanyDAO {

	public void createCompany(Company c) throws CouponSystemExceptions;

	void removeCompany(Company c) throws CouponSystemExceptions;

	public void updateCompany(Company c) throws CouponSystemExceptions;

	public Company getCompany(long id) throws CouponSystemExceptions;

	public Collection<Company> getAllCompanies() throws CouponSystemExceptions;

	public Collection<Coupon> getCompanyCoupons(long compId);

	public boolean login(String compName, String password) throws CouponSystemExceptions;
}
