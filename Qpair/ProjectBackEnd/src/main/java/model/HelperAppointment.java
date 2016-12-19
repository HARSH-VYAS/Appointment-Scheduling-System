package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import databaseConnection.DBConnection;

public class HelperAppointment {

	
	DBConnection dbCon;
	Connection conn;
	Statement stmt;

	public int getDocIdFromName(String docName) {

		int doc_id = -1;
		dbCon = new DBConnection();
		conn = dbCon.getConnection();
		Statement stmt = null;

		try {

			stmt = conn.createStatement();
			String sql;
			sql = "SELECT doc_id FROM doctor where name='" + docName + "'";
			ResultSet rs = stmt.executeQuery(sql);
			System.out.println(sql);
			while (rs.next()) {
				// Retrieve by column name
				System.out.println(">>> getDocIDFromName returned: " + rs.getInt("doc_id"));
				return rs.getInt("doc_id");
			}

			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se1) {
				se1.printStackTrace();
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
			} // end finally try
		} // end try

		return doc_id;
	}

	public int getPatIdFromName(String patName) {

		int pat_id = -1;
		dbCon = new DBConnection();
		conn = dbCon.getConnection();
		Statement stmt = null;

		try {
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT pat_id FROM patient where name='" + patName + "'";
			ResultSet rs = stmt.executeQuery(sql);
			
			System.out.println(sql);
			
			while (rs.next()) {
				// Retrieve by column name
				System.out.println(">>> getPatIdFromName returned: " + rs.getInt("pat_id"));
				return rs.getInt("pat_id");
			}

			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se1) {
				se1.printStackTrace();
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
			} // end finally try
		} // end try

		return pat_id;
	}

	public String getPatNameFromId(int id) {

		String pat_id = "";
		System.out.println("PatId"+id);
		dbCon = new DBConnection();
		conn = dbCon.getConnection();
		Statement stmt = null;

		try {
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT name FROM patient where pat_id='" + id + "'";
			ResultSet rs = stmt.executeQuery(sql);
			
			System.out.println(sql);
			
			while (rs.next()) {
				// Retrieve by column name
				//System.out.println(">>> getPatIdFromName returned: " + rs.getInt("pat_id"));
				System.out.println("PatName"+rs.getString("name"));
				return rs.getString("name");
			}

			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se1) {
				se1.printStackTrace();
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
			} // end finally try
		} // end try

		return pat_id;
	}

	public String getDocNameFromId(int id) {

		System.out.println("DocId"+id);
		String doc_id = "";
		dbCon = new DBConnection();
		conn = dbCon.getConnection();
		Statement stmt = null;

		try {
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT name FROM doctor where doc_id='" + id + "'";
			ResultSet rs = stmt.executeQuery(sql);
			
			System.out.println(sql);
			
			while (rs.next()) {
				// Retrieve by column name
				//System.out.println(">>> getPatIdFromName returned: " + rs.getInt("doc_id"));
				System.out.println("DocName"+rs.getString("name"));
				return rs.getString("name");
			}

			rs.close();
			stmt.close();
			conn.close();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se1) {
				se1.printStackTrace();
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
				se2.printStackTrace();
			} // end finally try
		} // end try

		return doc_id;
	}	
}
