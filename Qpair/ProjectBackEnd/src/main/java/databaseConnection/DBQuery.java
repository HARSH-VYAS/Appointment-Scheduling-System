package databaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBQuery {
	
	Statement stmt=null;
	ResultSet resultSet=null;
	
	public ResultSet doSelect(String sql){
		
		DBConnection dbcon = new DBConnection();
		Connection con = dbcon.getConnection();
		
			//create statement
			try {
				stmt = con.createStatement();
				resultSet = stmt.executeQuery(sql);
				stmt.close(); //close statement
			    con.close();  // close connection	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(Exception e){
			      //Handle errors for Class.forName
			      e.printStackTrace();
			   }finally{
			      //finally block used to close resources
			      try{
			         if(stmt!=null)
			            stmt.close();
			      }catch(SQLException se2){
			      }// nothing we can do
			      try{
			         if(con!=null)
			            con.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }//end finally try
			   }//end try
			   System.out.println("Done!");		
		    return resultSet;
	}
	
	
	public void doAction(String sql){
		
		DBConnection dbcon = new DBConnection();
		Connection con = dbcon.getConnection();
		try {
			stmt = con.createStatement();
			stmt.executeUpdate(sql);
			stmt.close(); // Close statement
		    con.close();  //Close Connection
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();
		   }finally{
		      //finally block used to close resources
		      try{
		         if(stmt!=null)
		            stmt.close();
		      }catch(SQLException se2){
		      }// nothing we can do
		      try{
		         if(con!=null)
		            con.close();
		      }catch(SQLException se){
		         se.printStackTrace();
		      }//end finally try
		   }//end try
		   System.out.println("Done!");
	}
}
