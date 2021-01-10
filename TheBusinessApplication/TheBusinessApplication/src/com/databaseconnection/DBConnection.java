package com.databaseconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
	
	private static Connection con;
	private static final String url = "jdbc:mysql://localhost:3306/businessapp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	private static final String username = "root";
	private static final String password = "root";
	
	public DBConnection(){
		
	}
	
	public static Connection getCon()
	{
		try {
			
			Class.forName("com.mysql.jdbc.Driver");		
			con = DriverManager.getConnection(url, username, password);
			
			return con;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void setData(String s) throws SQLException
	{
		con.createStatement().executeQuery(s);
	}
	
	public ResultSet getData(String s) throws SQLException
	{
		return con.createStatement().executeQuery(s);
	}
	
	public PreparedStatement insertData(String s) throws SQLException
	{
		PreparedStatement pstmt = con.prepareStatement(s);
		
		return pstmt;
	}
	
	public void updateData(String s) throws SQLException
	{
		PreparedStatement pstmt = con.prepareStatement(s);
		
		pstmt.execute();
	}
}
