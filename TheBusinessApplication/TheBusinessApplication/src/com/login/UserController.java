package com.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Base64;

import com.databaseconnection.DBConnection;

public abstract class UserController {
																					  //1234 by default
	public static void addUser(int id, String usertype, String name, String username, String password, String category) {
		
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
			PreparedStatement pstmt = DBConnection.insertData
					(" INSERT INTO users (id_user, usertype, name, username, password, category) VALUES (?, ?, ?, ?, ?, ?)");
			
			pstmt.setInt(1, id);
			pstmt.setString(2, usertype);
			pstmt.setString(3, name);
			pstmt.setString(4, username);
			pstmt.setString(5, hashedPassword + " " + encodedSalt);                  //sets default password to 1234 (will be salted hash of "1234")
			pstmt.setString(6, category);
			
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}
	
	public abstract void changePassword(String password);

}
