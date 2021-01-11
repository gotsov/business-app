package com.login;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.admin.AdminWindow;
import com.databaseconnection.DBConnection;
import com.exceptions.InvalidUserException;
import com.representative.RepresentativeWindow;

public class LoginController {
	
	private boolean loggedIn;
	
	public void checkLogin(JPasswordField passwordField, JTextField userNameTxtField) throws SQLException, InvalidUserException
	{
		try (Connection con = DBConnection.getCon()){
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM users");
			
	        String username, name, password, usertype, category;
	        int id;
	        
	        String passwordEnteredByUser = new String(passwordField.getPassword());
	        
	        while(rs.next())
	        {
	        	username = rs.getString("username");
	        	
	        	if(username.equals(userNameTxtField.getText())) {
	        		
	        		String[] passwordFiled = rs.getString("password").split(" ");
	        		
	     	        String saltedHashStringDB = passwordFiled[0];
	     	        String saltStringDB = passwordFiled[1];	       
	     	        
	     	        String saltedHashBytesToCheck = Hasher.getSaltedHash(passwordEnteredByUser, saltStringDB);
	     	        
	     			System.out.println("saltedHashToCheck = " + saltedHashBytesToCheck);
	     			System.out.println("saltedHash(in db)= " + saltedHashStringDB);
	     	        	
	        		if(saltedHashStringDB.equals(saltedHashBytesToCheck))
	        		{	
	        			id = rs.getInt("id_user");
		        		usertype = rs.getString("usertype");
		        		name = rs.getString("name");
		        		category = rs.getString("category");
		        		
	        			if(usertype.equals("representative")) {
	        				RepresentativeWindow repWin = new RepresentativeWindow(id, name, username, saltedHashStringDB, category);
							repWin.setVisible(true);
							repWin.setTitle("Sales Representative - " + name + " (" + username + ") / " + category);
							setLoggedIn(true);
							break;
	        			}
	        			else if(usertype.equals("admin")) {
		        			AdminWindow adminWin = new AdminWindow(id, name, username, saltedHashStringDB);
							adminWin.setVisible(true);
							adminWin.setTitle("Admin - " + name + " (" + username + ")");
							setLoggedIn(true);
							break;
	        			}
	        				
	        		} 
	        	} 
	        	
	        }
	        
	        if(!loggedIn) {
	        	throw new InvalidUserException("Invalid user");
	        }
   
		}
	}
	
	private void setLoggedIn(boolean b) {
		this.loggedIn = b;
	}
}
