package Corpus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ConnectionPool {
	private static ConnectionPool instance = new ConnectionPool();
	private boolean shutdown = false;
	private Set<Connection> list = new HashSet<>();

	// MYSQL Amazon IDENTIFIANTS
	String dbname = "db1";
	String username = "nathan";
	String password = "root2601";
	String hostname = "35.198.178.175";
	String port = "3306";

	private ConnectionPool() {
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver);
			System.out.println("driver loaded");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public static ConnectionPool getinstance() {
		return instance;

	}

	public synchronized Connection getConnection() {
		try {
			Connection connect = DriverManager.getConnection(
					"jdbc:mysql://" + hostname + ":" + port + "/" + dbname + "?useSSL=false", "nathan", "root2601");
			return connect;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void returnConnection(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized void closeAllConnection() {
		for (Connection connection : list) {
			try {
				wait(6000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		System.out.println(list.size() + " " + "connexions have been closed ");
	}

}
