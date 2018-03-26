package DBDAO;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;


import Corpus.ConnectionPool;
import Corpus.Coupon;
import Corpus.CouponType;
import DAO.CouponDAO;
import Exceptions.CouponSystemExceptions;

public class CouponDBDAO implements CouponDAO {
	ConnectionPool pool;

	public CouponDBDAO(ConnectionPool pool) {
		this.pool = pool;
	}

	public CouponDBDAO() {

	}

	@Override
	public void createCoupon(Coupon c) throws CouponSystemExceptions {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "INSERT INTO COUPON VALUES(?,?,?,?,?,?,?,?,?)";
		try (PreparedStatement pmst = connect.prepareStatement(sql);) {
			pmst.setLong(1, c.getId());
			pmst.setString(2, c.getTitle());
			pmst.setInt(3, c.getAmount());
			pmst.setString(4, String.valueOf(c.getType()));
			pmst.setString(5, c.getMessage());
			pmst.setDouble(6, c.getPrice());
			pmst.setString(7, c.getImage());
			pmst.setString(8, c.getSd());
			pmst.setString(9, c.getEd());

			pmst.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			CouponSystemExceptions e2 = new CouponSystemExceptions();
			e2.setMessage("Duplicate Id ");
			throw e2;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(sql);
		}

		finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		System.out.println("Coupons created");
	}

	@Override
	public void removeCoupon(Coupon c) throws CouponSystemExceptions {
		Connection connect = ConnectionPool.getinstance().getConnection();
		CouponDBDAO cd = new CouponDBDAO();
		Collection<Coupon> coupons = new ArrayList<>();
		coupons = cd.getAllCoupon();
		int taille1 = coupons.size();
		String sql = " DELETE FROM COUPON WHERE ID =" + c.getId();
		// String sql3 = "DELETE FROM CUSTOMER_COUPONS WHERE COUPON_ID =" + c.getId();
		try (Statement stmt = connect.createStatement();) {
			stmt.executeUpdate(sql);
			int taille2 = cd.getAllCoupon().size();
			if (taille1 == taille2) {
				CouponSystemExceptions ex = new CouponSystemExceptions();
				ex.setMessage("No Coupon with this id");
				throw ex;
			}

		}

		catch (CouponSystemExceptions e) {
			throw e;
		} catch (Exception e) {
			System.out.println(sql);
			e.printStackTrace();
		}

		finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}

	}

	/*
	 * Classic Updated ,Impossible to change : _the startDate of the coupon
	 */
	@Override
	public void updateCoupon(Coupon c) {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "UPDATE COUPON SET Amount = ?  Where id =" + c.getId();
		try (PreparedStatement pmst = connect.prepareStatement(sql);) {
			pmst.setInt(1, c.getAmount());
			pmst.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			System.out.println(sql);
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		System.out.println("Coupon modified ");
	}

	@Override
	public Coupon getCoupon(int id) throws CouponSystemExceptions {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT*FROM COUPON WHERE ID =" + id;
		long id2 = 0;
		String title = null;
		String startDate = null;
		String endDate = null;
		int amount = 0;
		CouponType type = null;
		String message = null;
		double price = 0;
		String image = null;
		try (Statement stmt = connect.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				id2 = id;
				title = rs.getString(2);
				startDate = rs.getString(8);
				endDate = rs.getString(9);
				amount = rs.getInt(3);
				type = CouponType.valueOf(rs.getString(4));
				message = rs.getString(5);
				price = rs.getDouble(6);
				image = rs.getString(7);

			}
		}

		catch (SQLException e) {
			System.out.println(sql);
		}

		finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		Coupon c = new Coupon(id2, title, amount, type, message, price, image, startDate, endDate);
		if (c.getId() == 0 && c.getTitle() == null) {
			CouponSystemExceptions e = new CouponSystemExceptions();
			e.setMessage("No coupon with this Id");
			throw e;
		}
		return c;
	}

	@Override
	public Collection<Coupon> getAllCoupon() throws CouponSystemExceptions {
		Collection<Coupon> list = new ArrayList<>();
		long id = 0;
		String title = null;
		String start = null;
		String end = null;
		int amount = 0;
		CouponType type = null;
		String message = null;
		double price = 0;
		String image = null;
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT*FROM COUPON";
		try (Statement stmt = connect.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				id = rs.getLong(1);
				title = rs.getString(2);
				start = rs.getString(8);
				end = rs.getString(9);
				amount = rs.getInt(3);
				type = CouponType.valueOf(rs.getString(4));
				message = rs.getString(5);
				price = rs.getDouble(6);
				image = rs.getString(7);
				Coupon c1 = new Coupon(id, title, amount, type, message, price, image, start, end);
				list.add(c1);
			}
		} catch (SQLException e) {
			System.out.println(sql);
			System.out.println(e.getMessage());
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		
		}
		return list;
	}

	@Override
	public Collection<Coupon> getCouponByType(CouponType x) throws CouponSystemExceptions {
		String cType = String.valueOf(x);
		Collection<Coupon> list = new ArrayList<>();
		long id = 0;
		String title = null;
		String start = null;
		String end = null;
		int amount = 0;
		CouponType type = null;
		String message = null;
		double price = 0;
		String image = null;
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT*FROM COUPON WHERE ENUM = " + "'" + String.valueOf(x) + "'";
		try (Statement stmt = connect.createStatement();) {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				id = rs.getLong(1);
				title = rs.getString(2);
				start = rs.getString(8);
				end = rs.getString(9);
				amount = rs.getInt(3);
				type = CouponType.valueOf(rs.getString(4));
				message = rs.getString(5);
				price = rs.getDouble(6);
				image = rs.getString(7);
				Coupon c1 = new Coupon(id, title, amount, type, message, price, image, start, end);
				list.add(c1);

			}
			if (list.isEmpty()) {
				CouponSystemExceptions e = new CouponSystemExceptions();
				e.setMessage("No coupon with this Type ");
				throw e;
			}

		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}

		return list;
	}

}
