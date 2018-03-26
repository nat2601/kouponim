package DBDAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Corpus.Coupon;
import Corpus.CouponType;

public class test {
	public static void main(String[] args) {
		// MYSQL IDENTIFIANTS
		String dbname = "sql2222452";
		String username = "sql2222452";
		String password = "tQ9%25cM2%25";
		String hostname = "sql2.freemysqlhosting.net";
		String port = "3306";
		String connexionurl = "jdbc:mysql://" + hostname + ":" + port + "/" + dbname + "?user=" + username
				+ "&password=" + password;

		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver);
			System.out.println("driver loaded");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		Connection connect;
		try {
			connect = DriverManager.getConnection(connexionurl);
			java.sql.Statement stmt = connect.createStatement();
			String query = "Insert into Company values(200,'Company2','company2','comps2@mail.com')";
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("connecter");
		System.out.println("questy executer");
	}
}
