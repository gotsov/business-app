package com.databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
	
	static private Connection con;
	private static final String url = "jdbc:mysql://localhost:3306/businessapp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String username = "root";
	private static final String password = "root";
	
	public static Connection getCon()
	{
		try {
			if(con == null) {
				Class.forName("com.mysql.jdbc.Driver");		
				con = DriverManager.getConnection(url, username, password);
			}
			return con;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void setData(String s) throws SQLException
	{
		DBConnection.getCon().createStatement().executeQuery(s);
	}
	
	public static ResultSet getData(String s) throws SQLException
	{
		return DBConnection.getCon().createStatement().executeQuery(s);
	}
	
	public static PreparedStatement insertData(String s) throws SQLException
	{
		PreparedStatement pstmt = con.prepareStatement(s);
		
		return pstmt;
	}
	
	public static void updateData(String s) throws SQLException
	{
		PreparedStatement pstmt = con.prepareStatement(s);
		
		pstmt.execute();
	}
}
