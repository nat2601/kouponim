package DBDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Corpus.ConnectionPool;
import Corpus.Coupon;
import Corpus.CouponType;
import Corpus.Customer;
import DAO.CustomerDAO;
import Exceptions.CouponSystemExceptions;

public class CustomerDBDAO implements CustomerDAO {
	ConnectionPool pool;
static long id ;

	public CustomerDBDAO(ConnectionPool pool) {
		super();
		this.pool = pool;
	}

	public CustomerDBDAO() {
	}

	@Override
	public void createCustomer(Customer c) throws CouponSystemExceptions {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "INSERT INTO CUSTOMER VALUES (?,?,?)";
		try (PreparedStatement pmst = connect.prepareStatement(sql);) {
			pmst.setLong(1, c.getId());
			pmst.setString(2, c.getCustName());
			pmst.setString(3, c.getPassword());
			pmst.executeUpdate();

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new CouponSystemExceptions("This customer also exist");
		} catch (SQLException e) {
			System.out.println(sql);
			throw new CouponSystemExceptions("This customer was not added");
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		System.out.println("The Customer has been added");
	}

	@Override
	public void removeCustomer(Customer c) {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "DELETE FROM CUSTOMER WHERE ID =" + c.getId();
		String sql2 = "DELETE FROM CUSTOMER_COUPONS WHERE CUST_ID=" + c.getId();

		try (Statement stmt = connect.createStatement();) {
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql2);
			System.out.println("Action sent to the database");
		} catch (SQLException e) {
			System.out.println(sql);
			e.printStackTrace();
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		System.out.println("This customer has been removed");
	}

	@Override
	// Simple Update
	public void updateCustomer(Customer c) throws CouponSystemExceptions {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = " UPDATE CUSTOMER SET CUST_NAME = ? , PASSWORD = ? WHERE ID =" + c.getId();
		try (PreparedStatement pmst = connect.prepareStatement(sql);) {
			pmst.setString(1, c.getCustName());
			pmst.setString(2, c.getPassword());
			pmst.executeUpdate();
		} catch (SQLException e) {
			System.out.println(sql);
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		System.out.println("The customer was updated");
	}

	public void updatecustomer2(Customer c) {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "UPDATE CUSTOMER SET PASSWORD =" + "'" + c.getPassword() + "'" + " WHERE ID = " + c.getId();
		try {
			Statement stmt = connect.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Customer getCustomer(int id) throws CouponSystemExceptions {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT*FROM CUSTOMER WHERE ID =" + id;
		long id2 = 0;
		String custName = null;
		String pass = null;
		try (Statement stmt = connect.createStatement();) {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				id2 = id;
				custName = rs.getString(2);
				pass = rs.getString(3);

			}
			if (id != id2) {
				throw new CouponSystemExceptions(" This id" + " :" + id + " does not exist");
			}
		} catch (SQLException e) {

		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		Customer c = new Customer(id2, custName, pass, null);
		return c;

	}

	@Override
	public Collection<Customer> getAllCustomer() {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT*FROM CUSTOMER";
		Collection<Customer> liste = new ArrayList<>();
		try (Statement stmt = connect.createStatement();) {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long id = rs.getLong(1);
				String custName = rs.getString(2);
				String password = rs.getString(3);
				Customer c = new Customer(id, custName, password, null);
				liste.add(c);
			}

		} catch (SQLException e) {
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		return liste;
	}

	@Override
	public Collection<Coupon> getCustomerCoupons(Customer c) {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT COUPON_ID FROM CUSTOMER_COUPONS WHERE CUST_ID= ?";
		String sql2 = "SELECT*FROM COUPON WHERE ID = ?";
		Collection<Coupon> coupons = new HashSet<>();
		try (PreparedStatement pmst = connect.prepareStatement(sql);
				PreparedStatement pmst2 = connect.prepareStatement(sql2);) {
			pmst.setLong(1, c.getId());
			ResultSet rs = pmst.executeQuery();
			Set<Long> couponids = new HashSet<>();

			while (rs.next()) {
				couponids.add(rs.getLong(1));
			}
			for (Long couponID : couponids) {
				pmst2.setLong(1, couponID);
				ResultSet rs2 = pmst2.executeQuery();
				Coupon c1 = new Coupon();
				rs2.next();
				c1.setId(rs2.getLong(1));
				c1.setTitle(rs2.getString(2));
				c1.setSd(rs2.getString(8));
				c1.setEd(rs2.getString(9));
				c1.setAmount(rs2.getInt(3));
				c1.setType(CouponType.valueOf(rs2.getString(4)));
				c1.setMessage(rs2.getString(5));
				c1.setPrice(rs.getDouble(6));
				c1.setImage(rs2.getString(7));
				coupons.add(c1);

			}

		} catch (SQLException e) {
			System.out.println(sql);
			System.out.println(e.getMessage());
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		return coupons;
	}

	@Override
	public boolean login(String custName, String password) throws CouponSystemExceptions {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT ID, CUST_NAME,PASSWORD FROM CUSTOMER";
		long custID = 0 ;
		String customer_name = null;
		String pass = null;

		try {
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				custID= rs.getLong(1);
				customer_name = rs.getString(2);
				pass = rs.getString(3);
				if (custName.equalsIgnoreCase(customer_name) && password.equalsIgnoreCase(pass)) {
					System.out.println("Entry Successfuly");
			  id = custID;
				System.out.println(id);
					return true;
				}
			}
			throw new CouponSystemExceptions("Conection failed password or/and cust_Name");

		}

		catch (SQLException e) {
			System.out.println(sql);
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		return false;
	}

	// To remove Coupon from table Join
	public void removeCouponFromJoinCustomer(Coupon c) {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "DELETE FROM CUSTOMER_COUPONS WHERE COUPON_ID=" + c.getId();
		try (Statement stmt = connect.createStatement();) {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
	}

	// To have the customer id with the cust name
	public long getIdCustomer(String name) {
		long custId = 0;
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT ID FROM CUSTOMER WHERE CUST_NAME=" + "'" + name + "'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				custId = rs.getLong(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return custId;
	}

	public Collection<Coupon> getallcustcoupon() throws CouponSystemExceptions {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = " SELECT* FROM COUPON INNER JOIN CUSTOMER_COUPONS on COUPON.ID =CUSTOMER_COUPONS.COUPON_ID ";
		Collection<Coupon> coupons = new ArrayList<>();
		try {
			PreparedStatement pmst = connect.prepareStatement(sql);
			ResultSet rs = pmst.executeQuery();
			while (rs.next()) {
				Coupon coup = new Coupon();
				coup.setId(rs.getLong(1));
				coup.setTitle(rs.getString(2));
				coup.setAmount(rs.getInt(3));
				coup.setType(CouponType.valueOf(rs.getString(4)));
				coup.setMessage(rs.getString(5));
				coup.setPrice(rs.getDouble(6));
				coup.setImage(rs.getString(7));
				coup.setSd(rs.getString(8));
				coup.setEd(rs.getString(9));
				coupons.add(coup);

			}
		} catch (SQLException e) {
			throw new CouponSystemExceptions();
			

		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		System.out.println(coupons.toString());
		return coupons;
	}

	public Collection<Long> readcustcoupon() {
		Collection<Long> custcoupons = new HashSet<>();
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "Select* from customer_coupons";
		try {
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long cust_id = rs.getLong(1);
				long coup_id = rs.getLong(2);
				custcoupons.add(coup_id);
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		return custcoupons;
	}

	public Collection<Coupon> couponfree() throws CouponSystemExceptions {
		CustomerDBDAO cdb = new CustomerDBDAO();
		CouponDBDAO coupondb = new CouponDBDAO();
		Collection<Coupon> couponsfree = new ArrayList<>();
		Collection<Coupon> coupons = new ArrayList<>();
		coupons = cdb.getallcustcoupon();
		try {
			couponsfree = coupondb.getAllCoupon();
		} catch (CouponSystemExceptions e) {
			e.printStackTrace();
		}
		couponsfree.removeAll(coupons);
		return coupons;

	}
	public static void main(String[] args) {
	
	}

}
