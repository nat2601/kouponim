package Facade;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import Corpus.Company;
import Corpus.ConnectionPool;
import Corpus.Coupon;
import Corpus.CouponType;
import DAO.CouponDAO;
import DBDAO.CompanyDBDAO;
import DBDAO.CouponDBDAO;
import Exceptions.CouponSystemExceptions;

public class CompanyFacade implements CouponClientFacade {
	CouponDBDAO coupond = new CouponDBDAO();
	CompanyDBDAO comp = new CompanyDBDAO();

	public CompanyFacade(CouponDBDAO coupond, CompanyDBDAO comp) {
		super();
		this.coupond = coupond;
		this.comp = comp;
	}

	public CompanyFacade() {
	}

	public void createCoupons(Coupon co) throws CouponSystemExceptions {
		Collection<Coupon> coupons = coupond.getAllCoupon();
		for (Coupon coupon : coupons) {
			if (coupon.getTitle().equals(co.getTitle())) {
				CouponSystemExceptions e = new CouponSystemExceptions();
				e.setMessage("Duplicate Title");
				throw e;
			}
		}
		coupond.createCoupon(co);
	}

	public void removeCoupon(Coupon c) throws CouponSystemExceptions {
		try {
			coupond.removeCoupon(c);
		} catch (CouponSystemExceptions e) {
			throw e;
		}
		System.out.println("Coupon removed");
	}

	// Impossible to Change the Id , the Title , the startDate (logic)
	public void updateCoupon(Coupon co) {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "UPDATE COUPON SET ED =" + "'" + co.getEd() + "'" + ", PRICE =" + co.getPrice() + "WHERE ID = "
				+ co.getId();
		try (Statement stmt = connect.createStatement();) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		System.out.println("Coupon updated");
	}

	public Coupon getCoupon(int id) throws CouponSystemExceptions {
		Coupon coup;
		try {
			coup = coupond.getCoupon(id);
		} catch (CouponSystemExceptions e) {
			throw e;
		}

		return coup;
	}

	public Collection<Coupon> getCouponByType(CouponType a) throws CouponSystemExceptions {
		Collection<Coupon> couponsType;
		try {
			couponsType = coupond.getCouponByType(a);
		} catch (CouponSystemExceptions e) {
			throw e;
		}
		return couponsType;
	}

	public Collection<Coupon> getCouponByprice(double maxprice) throws CouponSystemExceptions {
		Collection<Coupon> coupons = coupond.getAllCoupon();
		Collection<Coupon> couponsbyprice = new ArrayList<Coupon>();
		for (Coupon coupon : coupons) {
			System.out.println(coupon.getPrice());
			if (coupon.getPrice() <= maxprice) {
				couponsbyprice.add(coupon);
			}

		}
		if (couponsbyprice.size() == 0 || coupons.size() == 0) {
			CouponSystemExceptions e = new CouponSystemExceptions();
			e.setMessage("No coupon under this price " + "" + maxprice);
			throw e;
		}
		System.out.println(couponsbyprice.toString());
		return couponsbyprice;
	}

	public Collection<Coupon> getCouponByTheDateEnd(String d) throws CouponSystemExceptions {
		// Parsing
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d1 = new java.util.Date();
		try {
			d1 = sdf.parse(d);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Collection<Coupon> coupons = coupond.getAllCoupon();
		Collection<Coupon> couponBytheDate = new HashSet<>();
		for (Coupon coupon : coupons) {
			// Parsing 2
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date d2 = new java.util.Date();
			try {
				if (coupon.getEd() != null) {
					d2 = sdf2.parse(coupon.getEd());
					System.out.println(d2);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (d2.after(d1)) {
				couponBytheDate.add(coupon);
			}

		}
		if (couponBytheDate.isEmpty()) {
			CouponSystemExceptions e = new CouponSystemExceptions();
			e.setMessage("No coupon After this Date");
			throw e;
		}
		return couponBytheDate;
	}

	public Collection<Coupon> getCompanyCoupons(long compId) {
		Collection<Coupon> coupons = comp.getCompanyCoupons(compId);
		return coupons;
	}

	@Override
	public CompanyFacade login(String name, String password, ClientType clientType) throws CouponSystemExceptions {
		if (comp.login(name, password) == true && clientType == ClientType.COMPANY) {
			return this;
		} else
			throw new CouponSystemExceptions("Not identified");

	};
}
