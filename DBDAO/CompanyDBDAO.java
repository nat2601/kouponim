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
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

import Corpus.Company;
import Corpus.ConnectionPool;
import Corpus.Coupon;
import Corpus.CouponType;
import DAO.CompanyDAO;
import Exceptions.CouponSystemExceptions;
import Facade.CustomerFacade;

public class CompanyDBDAO implements CompanyDAO {
	ConnectionPool pool;

	public CompanyDBDAO(ConnectionPool pool) {
		this.pool = pool;

	}

	public CompanyDBDAO() {

	}

	@Override
	public void createCompany(Company c) throws CouponSystemExceptions {
		Connection connect = ConnectionPool.getinstance().getConnection();
		System.out.println("connexion prises");
		// Insert the company
		String sql = "INSERT INTO COMPANY VALUES (?,?,?,?)";
		try (PreparedStatement pmst = connect.prepareStatement(sql);) {
			pmst.setLong(1, c.getId());
			pmst.setString(2, c.getCompName());
			pmst.setString(3, c.getPassword());
			pmst.setString(4, c.getEmail());
			pmst.executeUpdate();
			// Insert into Table Join the collection of Coupon of this company
			Collection<Coupon> coupons = c.getCoupons();
			long coupID = 0;
			if (coupons != null) {
				for (Coupon coupon : coupons) {
					coupID = coupon.getId();
					String sql2 = "INSERT INTO COMPANY_COUPON VALUES(" + c.getId() + "," + coupID + ")";
					Statement stmt = connect.createStatement();
					stmt.executeUpdate(sql2);
				}
			}

		} catch (SQLIntegrityConstraintViolationException e) {
			throw new CouponSystemExceptions("This company also exist");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(sql);
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
	}

	@Override
	public void removeCompany(Company c) throws CouponSystemExceptions {
		String sql = "DELETE FROM COMPANY WHERE ID = " + c.getId();
		String sql2 = "DELETE FROM COMPANY_COUPON WHERE COMPANY_ID=" + c.getId();
		Connection connect = ConnectionPool.getinstance().getConnection();
		CompanyDBDAO cm = new CompanyDBDAO();
		int size = cm.getAllCompanies().size();

		try (Statement stmt = connect.createStatement();) {
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql2);
			System.out.println("Action sent to the Database ");
			CompanyDBDAO cm2 = new CompanyDBDAO();
			int size2 = cm2.getAllCompanies().size();
			if (size == size2)
				throw new CouponSystemExceptions("This Company dosn't exist");

		} catch (SQLException e) {
			throw new CouponSystemExceptions("Problem encountred");

		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}

	}

	@Override
	// Advanced Updated with the JVM , Utilisation of Scanner
	public void updateCompany(Company c) throws CouponSystemExceptions {

		String sql = "UPDATE COMPANY SET COMP_NAME = ?, PASSWORD = ?, EMAIL = ? WHERE ID =" + c.getId();
		Connection connect = ConnectionPool.getinstance().getConnection();
		Scanner sc = new Scanner(System.in);
		try (PreparedStatement pmst = connect.prepareStatement(sql)) {
			CompanyDBDAO cm = new CompanyDBDAO();

			if (cm.getAllCompanies().contains(c)) {
				System.out.println("What do you want do update : 1-COMP_Name 2- PASSWORD 3- EMAIL 4 - ALL Column ? ");

				int rep = sc.nextInt();
				sc.nextLine();
				switch (rep) {
				case 1:
					System.out.println("What is the new name of the company ?");
					String newNameOfcompany = sc.nextLine();
					pmst.setString(1, newNameOfcompany);
					pmst.setString(2, c.getPassword());
					pmst.setString(3, c.getEmail());
					pmst.executeUpdate();
					System.out.println("Name of Company changed");
					break;
				case 2:
					System.out.println("What is your new Password");
					String newPass = sc.nextLine();
					System.out.println("Reenter youre new password");
					String newPass2 = sc.nextLine();
					if (newPass.equals(newPass2)) {
						pmst.setString(1, c.getCompName());
						pmst.setString(2, newPass);
						pmst.setString(3, c.getEmail());
						pmst.executeUpdate();
						System.out.println("The Password has been updated");
						sc.close();
						break;
					} else {
						System.out.println("Eror");

					}
					break;
				case 3:
					System.out.println("What is you're new email");
					String newEmail = sc.nextLine();
					if (newEmail.contains("@")) {
						pmst.setString(1, c.getCompName());
						pmst.setString(2, c.getPassword());
						pmst.setString(3, newEmail);
						pmst.executeUpdate();
						System.out.println("The email has been update");
						sc.close();
						break;
					} else {
						System.out.println("Error");

					}

					break;
				default:
					System.out.println("Enter the new name");
					String newName = sc.nextLine();
					pmst.setString(1, newName);
					pmst.setString(2, c.getPassword());
					pmst.setString(3, c.getEmail());
					pmst.executeUpdate();
					System.out.println("Enter youre new password");
					String newPassword = sc.nextLine();
					System.out.println("Reenter");
					String newPassword2 = sc.nextLine();
					if (newPassword.equals(newPassword2)) {
						pmst.setString(1, newName);
						pmst.setString(2, newPassword);
						pmst.setString(3, c.getEmail());
						pmst.executeUpdate();

					}
					System.out.println("Enter youre new Email ");
					String newEM = sc.nextLine();
					if (newEM.contains("@")) {
						pmst.setString(1, newName);
						pmst.setString(2, newPassword);
						pmst.setString(3, newEM);
						pmst.executeUpdate();
					}
					System.out.println("The company has been completely removed ");
					break;
				}

			} else
				throw new CouponSystemExceptions("This company doesn't exist");
		} catch (SQLException e) {
			throw new CouponSystemExceptions("This company wasn't updated");
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
			sc.close();
		}

	}

	public void updateCompany2(Company c) {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "UPDATE COMPANY SET PASSWORD=" + "'" + c.getPassword() + "'" + ", EMAIL =" + "'" + c.getEmail()
				+ "'" + "WHERE ID =" + c.getId();
		try {
			Statement stmt = connect.createStatement();
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Company getCompany(long id) throws CouponSystemExceptions {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT*FROM COMPANY WHERE ID =" + id;
		String NameC = null;
		String Pass = null;
		String Email = null;
		long id2 = 0;
		try (Statement stmt = connect.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				id2 = id;
				NameC = rs.getString(2);
				Pass = rs.getString(3);
				Email = rs.getString(4);

			}

		} catch (SQLException e) {
			System.out.println(sql);
			System.out.println(e.getMessage());
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		Company c = new Company(id2, NameC, Pass, Email, null);
		if (c.getId() == 0 && c.getCompName().equals(null)) {
			CouponSystemExceptions e = new CouponSystemExceptions();
			e.setMessage("No Comapny existing for this id" + id);
			throw e;
		}
		return c;

	}

	@Override
	public Collection<Company> getAllCompanies() {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT*FROM COMPANY";
		Collection<Company> list = new ArrayList<>();
		Company c = new Company();
		try (Statement stmt = connect.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long id2 = rs.getLong(1);
				String NameC = rs.getString(2);
				String Pass = rs.getString(3);
				String Email = rs.getString(4);
				c = new Company(id2, NameC, Pass, Email, null);
				list.add(c);

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
	public boolean login(String compName, String password) throws CouponSystemExceptions {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT COMP_NAME,PASSWORD FROM COMPANY";
		String companyname = null;
		String pass = null;

		try {
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				companyname = rs.getString(1);
				pass = rs.getString(2);
				if (compName.equalsIgnoreCase(companyname) && password.equals(pass)) {
					System.out.println("Entry Successfuly");
					return true;
				}

			}
			throw new CouponSystemExceptions("Conection failed password or/and CompName ");

		} catch (Exception e) {
			throw new CouponSystemExceptions("Conection failed password or/and CompName false ");
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}

	}

	// Method added by me
	public Company getCompany(String name) {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT*FROM COMPANY WHERE COMP_NAME =" + name;
		long id = 0;
		String NameC = null;
		String Pass = null;
		String Email = null;
		long id2 = 0;
		try (Statement stmt = connect.createStatement()) {
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				id = rs.getLong(1);
				NameC = name;
				Pass = rs.getString(3);
				Email = rs.getString(4);

			}

		} catch (SQLException e) {
			System.out.println(sql);
			System.out.println(e.getMessage());
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		Company c = new Company(id2, NameC, Pass, Email, null);
		System.out.println(c.toString());
		return c;

	}

	// To remove Coupon from table Join
	public void removeCouponFromJoinCompany(Coupon c) {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "DELETE FROM COMPANY_COUPON WHERE COUPON_ID=" + c.getId();
		try (Statement stmt = connect.createStatement();) {

			stmt.executeUpdate(sql);
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
	}

	@Override
	public Collection<Coupon> getCompanyCoupons(long compId) {
		Connection connect = ConnectionPool.getinstance().getConnection();
		String sql = "SELECT COUPON_ID FROM COMPANY_COUPON WHERE COMPANY_ID=" + compId;
		Collection<Coupon> coupons = new ArrayList<>();
		Collection<Long> couponId = new ArrayList<>();
		try {
			Statement stmt = connect.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				long coup_id = rs.getLong(1);
				couponId.add(coup_id);
			}
			for (Long long1 : couponId) {
				String sql2 = "SELECT * FROM COUPON WHERE ID = " + long1;
				ResultSet rs2 = stmt.executeQuery(sql2);
				while (rs2.next()) {
					long id2 = long1;
					String title = rs2.getString(2);
					String startDate = rs2.getString(8);
					String endDate = rs2.getString(9);
					int amount = rs2.getInt(3);
					CouponType type = CouponType.valueOf(rs2.getString(4));
					String message = rs2.getString(5);
					double price = rs2.getDouble(6);
					String image = rs2.getString(7);
					Coupon coupon = new Coupon(id2, title, amount, type, message, price, image, startDate, endDate);
					coupons.add(coupon);
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ConnectionPool.getinstance().returnConnection(connect);
		}
		return coupons;
	}
}
