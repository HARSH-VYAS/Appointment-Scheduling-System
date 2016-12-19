package databaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	Connection con = null;
	String DB_URL = "jdbc:mysql://localhost:3306/Appointment";
	String username = "root";
	String password = "root";

	public DBConnection() {

	}

	public Connection getConnection() {

		try {
			// Register JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			// Open connection
			con = DriverManager.getConnection(DB_URL, username, password);
			System.out.println("connection created successfully.....!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;

	}

}