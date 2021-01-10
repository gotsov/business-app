package com.businessapplication;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

import com.databaseconnection.DBConnection;
import com.login.Hasher;
import com.login.UserController;

public class User{
	
	private int id;
	
	public User(int id) {
		this.id = id;
	}


	public int getId() {
		return id;
	}
	
	
	public void changePassword(String password)
	{
		byte[] salt =  Hasher.createSalt();
		String hashedPassword = Hasher.getSaltedHash(password, salt);
		String encodedSalt = Base64.getEncoder().encodeToString(salt);
		
		StringBuilder saltString = new StringBuilder();
		for (int i = 0; i < salt.length; i++) {
	        String hex = Integer.toHexString(0xFF & salt[i]);                 
																			
	        if (hex.length() == 1) {										  
	        	saltString.append('0');										  
																			  
	        }
	        saltString.append(hex);
	    }
		
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			dbConnection.updateData("UPDATE users SET password = '" + hashedPassword + " " + encodedSalt + "' WHERE id_user = " + this.id);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
