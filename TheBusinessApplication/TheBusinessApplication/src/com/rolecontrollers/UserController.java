package com.rolecontrollers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

import com.businessapplication.User;
import com.databaseconnection.DBConnection;
import com.login.Hasher;

public class UserController {
																					  //1234 by default
	public void addUser(int id, String usertype, String name, String username, String password, String category) throws SQLException {
		
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
		
		
		System.out.println("Salt: " + saltString);
		System.out.println("saltEncoded: " + encodedSalt);
			
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			PreparedStatement pstmt = dbConnection.insertData
					(" INSERT INTO users (id_user, usertype, name, username, password, category) VALUES (?, ?, ?, ?, ?, ?)");
			
			pstmt.setInt(1, id);
			pstmt.setString(2, usertype);
			pstmt.setString(3, name);
			pstmt.setString(4, username);
			pstmt.setString(5, hashedPassword + " " + encodedSalt);                  //sets default password to 1234 (will be salted hash of "1234")
			pstmt.setString(6, category);
			
			pstmt.execute();
		}

	}
	
	public void changePassword(User user, String newPassword) throws SQLException {

		byte[] salt =  Hasher.createSalt();
		String hashedPassword = Hasher.getSaltedHash(newPassword, salt);
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
			
			dbConnection.updateData("UPDATE users SET password = '" + hashedPassword + " " + encodedSalt + "' WHERE id_user = " + user.getId());
			
		}

	}

}
