package Facade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import Corpus.ConnectionPool;
import Corpus.Coupon;
import Corpus.CouponType;
import Corpus.Customer;
import DBDAO.CouponDBDAO;
import DBDAO.CustomerDBDAO;
import Exceptions.CouponSystemExceptions;

public class CustomerFacade implements CouponClientFacade {
	CustomerDBDAO cust = new CustomerDBDAO();
	CouponDBDAO coupond = new CouponDBDAO();
static long  id ;
	public CustomerFacade() {
		

	}

	public void purchaseCoupon(Coupon co) throws CouponSystemExceptions {
		// Date of the day
		Date d = new Date();
		// Verification if the coupon is also purchased
		Collection<Long> customerjoin = cust.readcustcoupon();
		for (Long long1 : customerjoin) {
			if (long1 == co.getId()) {
				System.out.println("im here");
				CouponSystemExceptions e = new CouponSystemExceptions();
				e.setMessage("This Coupon was also Purchased ");
				throw e;
			}
		}
		// verify if the coupon can be purchased
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date d1 = new java.util.Date();

		try {
			d1 = sdf.parse(co.getEd());
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (co.getAmount() >= 1) {
			
			Connection connect = ConnectionPool.getinstance().getConnection();
			String sql2 = "INSERT INTO CUSTOMER_COUPONS VALUES(" + this.id + " ," + co.getId() + ")";
			try (Statement stmt = connect.createStatement();) {
				stmt.executeUpdate(sql2);
				// Remove the coupon purchased if he was the last one
				if (co.getAmount() - 1 == 0) {
					coupond.removeCoupon(co);
				} else {

					// Change the coupon quantity
					co.setAmount(co.getAmount() - 1);

					coupond.updateCoupon(co);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				ConnectionPool.getinstance().returnConnection(connect);
			}
		}
		System.out.println("Coupon purchased");
	}

	public Collection<Coupon> getAllPurchasedCoupons() throws CouponSystemExceptions {
		return cust.getallcustcoupon();
	}

	public Collection<Coupon> getAllPurchasedCouponsByType(CouponType type) throws CouponSystemExceptions {
		Collection<Coupon> coupons = cust.getallcustcoupon();
		Collection<Coupon> couponBytype = new ArrayList<>();
		for (Coupon coupon : coupons) {
			if (coupon.getType().equals(type)) {
				couponBytype.add(coupon);

			}

		}
		return couponBytype;
	}

	public Collection<Long> getAllPurchasedCouponByPrice(double price) {
		Collection<Long> purchasedCouponByPrice = new ArrayList<>();
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT*FROM COUPON WHERE PRICE <=" + price;
		try (Statement stmt = connect.createStatement();) {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long id = rs.getLong(1);
				String title = rs.getString(2);
				Date start = rs.getDate(3);
				Date end = rs.getDate(4);
				int amount = rs.getInt(5);
				CouponType type = CouponType.valueOf(rs.getString(6));
				String message = rs.getString(7);
				double price2 = price;
				String image = rs.getString(9);
				String sql2 = "SELECT*FROM CUSTOMER_COUPONS WHERE COUPON_ID=" + id;
				try (Statement stmt2 = connect.createStatement();) {
					ResultSet rs2 = stmt2.executeQuery(sql2);
					while (rs2.next()) {
						long cust_id = rs2.getLong(1);
						long coup_id = id;
						purchasedCouponByPrice.add(coup_id);
					}
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return purchasedCouponByPrice;
	}

	@Override
	public CouponClientFacade login(String uname, String password, ClientType clientType)
			throws CouponSystemExceptions {
		if (cust.login(uname, password) == true && clientType == ClientType.CUSTOMER) {
			CustomerFacade cf = new CustomerFacade();
			 id = this.getidfromlogin(uname, password);
			return cf;
		} else
			throw new CouponSystemExceptions("Not identified");
	}

	public long getidfromlogin(String uname, String password) {
		long id = 0;
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT ID FROM CUSTOMER WHERE CUST_NAME= " + "'" + uname + "'" + "and PASSWORD=" + "'" + password
				+ "'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				id = rs.getLong(1);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		return id;

	}



}
