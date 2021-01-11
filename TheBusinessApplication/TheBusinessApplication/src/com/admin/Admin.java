package com.admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.admin.Admin.Builder;
import com.databaseconnection.DBConnection;

public class Admin{
	private static int idAdmin = 1;
	
	private int id;
	private String name;
	private String username;
	private String password;
	
	public Admin(Builder builder)
	{
		this.id = builder.id;
		this.name = builder.name;
		this.username = builder.username;
		this.password = builder.password;
		
		setIdForDB();

	}
	
	public static class Builder
	{
		private String name;
		private int id;
		private String username;
		private String password;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
	
		public Builder id(int id) {
			this.id = id;
			return this;
		}	
		
		public Admin build() {
			return new Admin(this);
		}

	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public void setIdForDB() {		
		try(Connection con = DBConnection.getCon()) {	
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT id_user FROM users WHERE usertype = 'admin' ORDER BY id_user DESC LIMIT 1");
					
			rs.next();
			idAdmin = rs.getInt("id_user");

			this.id = ++idAdmin;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		this.id = idAdmin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
