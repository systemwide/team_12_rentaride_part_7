package edu.uga.cs.rentaride.presentation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseAccess {
	
	static final String DRIVE_NAME = "com.mysql.jdbc.Driver";
	
	static final String CONNECTION_URL = "jdbc:mysql://uml.cs.uga.edu:3306/team12";
	
	static final String DB_CONNECTION_USERNAME = "team12";
	
	static final String DB_CONNECTION_PASSWORD = "oodesign";
	
	public static Connection connect() {
		java.sql.Connection con = null;
		try {
			Class.forName(DRIVE_NAME);
			con = DriverManager.getConnection(CONNECTION_URL, DB_CONNECTION_USERNAME, DB_CONNECTION_PASSWORD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	} // end of connect
	
	public static ResultSet retrieve (Connection con, String query) {
		ResultSet rset = null;
		Statement stmt = null;
		try 
		{
			stmt = con.createStatement();
			rset = stmt.executeQuery(query);
			return rset;
		} 
			catch (SQLException e) 
		{
			e.printStackTrace();
		}   
		return rset;
	}// end of retrieve
	
	public static void closeConnection(Connection con) {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	} // end of closeConnection

}
