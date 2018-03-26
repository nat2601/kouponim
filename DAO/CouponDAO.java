package DAO;

import java.util.Collection;



import Corpus.Coupon;
import Corpus.CouponType;
import Exceptions.CouponSystemExceptions;

public interface CouponDAO {

	public void createCoupon(Coupon c) throws CouponSystemExceptions;

	public void removeCoupon(Coupon c) throws CouponSystemExceptions;

	public void updateCoupon(Coupon c);

	public Coupon getCoupon(int id) throws CouponSystemExceptions;

	public Collection<Coupon> getAllCoupon() throws CouponSystemExceptions;

	public Collection<Coupon> getCouponByType(CouponType x) throws CouponSystemExceptions;
}
