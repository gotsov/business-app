package com.businessApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	public DBConnection()
	{
		
	}
	
	public Connection createConnection()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver");		
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/businessapp?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","root");
			
			System.out.println("Connected successfully");
			return con;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
