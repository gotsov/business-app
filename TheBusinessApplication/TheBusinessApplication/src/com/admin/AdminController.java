package com.admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;

import com.businessapplication.Product;
import com.businessapplication.Sale;
import com.twitterconnection.TwitterDriver;
import com.databaseconnection.DBConnection;
import com.emailconnection.EmailDriver;
import com.exceptions.NotUniqueUsernameException;
import com.login.UserController;
import com.representative.Representative;

import twitter4j.TwitterException;

public class AdminController {	
	
	public void addProduct(Product newProduct, boolean postOnSocial) throws SQLException {
		try (Connection con = DBConnection.getCon()){		
			
			DBConnection dbConnection = new DBConnection();
			
			String name = newProduct.getName();
			PreparedStatement pstmt = dbConnection.insertData
									(" INSERT INTO allproducts (id_prod, name, category, price, quantity)" + " VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, newProduct.getId());
			pstmt.setString(2, name);
			pstmt.setString(3, newProduct.getCategory());
			pstmt.setDouble(4, newProduct.getPrice());
			pstmt.setInt(5, newProduct.getQuantity());
				
			pstmt.execute();
			
			if(postOnSocial) {
				Thread th = new Thread( new Runnable(){

					public void run() {
						try {
							TwitterDriver.tweetOut("" + name + " now available in our stores! #newoffer #" + name.replaceAll("\\s", "").toLowerCase(), "newProduct");
						} catch (IOException | TwitterException e) {
							e.printStackTrace();
						}
	
					}	
				});
				
				th.start();
			}
		}     

		System.out.println("product added");
	}
	
	public void deleteProduct(Product productToDelete) throws SQLException
	{
		try (Connection con = DBConnection.getCon()){		
			DBConnection dbConnection = new DBConnection();
			
			dbConnection.updateData("DELETE FROM allproducts WHERE id_prod = " + productToDelete.getId());
	        
		} 
		System.out.println("product deleted");
	}
	
	public void editFieldProduct(Product productToEdit, String fieldToEdit) throws SQLException 
	{	
		try (Connection con = DBConnection.getCon()){	
			DBConnection dbConnection = new DBConnection();
			
			if(fieldToEdit.equals("name")) {
				dbConnection.updateData("UPDATE allproducts SET " + fieldToEdit + " = '" + productToEdit.getName() + "' WHERE id_prod = " + productToEdit.getId());
			}
			else if(fieldToEdit.equals("category"))
			{
				dbConnection.updateData("UPDATE allproducts SET " + fieldToEdit + " = '" + productToEdit.getCategory() + "' WHERE id_prod = " + productToEdit.getId());
			}
			else if(fieldToEdit.equals("price"))
			{
				dbConnection.updateData("UPDATE allproducts SET " + fieldToEdit + " = " + productToEdit.getPrice() + " WHERE id_prod = " + productToEdit.getId());
			}
			else if(fieldToEdit.equals("quantity"))
			{
				dbConnection.updateData("UPDATE allproducts SET " + fieldToEdit + " = " + productToEdit.getQuantity() + " WHERE id_prod = " + productToEdit.getId());
			}
		} 
	}
	
	public void addRepresentative(Representative representativeToAdd) throws SQLException
	{
		try(Connection con = DBConnection.getCon()) {		
			DBConnection dbConnection = new DBConnection();
			UserController userController = new UserController();
			
			PreparedStatement pstmt = dbConnection.insertData
									(" INSERT INTO allrepresentatives (id_rep, name, username, category, numberofsales, profit) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, representativeToAdd.getId());
			pstmt.setString(2, representativeToAdd.getName());
			pstmt.setString(3, representativeToAdd.getUsername());
			pstmt.setString(4, representativeToAdd.getCategory());
			pstmt.setInt(5, representativeToAdd.getNumberOfSales());
			pstmt.setDouble(6, representativeToAdd.getProfit());
				
			pstmt.execute();
			
			userController.addUser(representativeToAdd.getId(), "representative", representativeToAdd.getName(), representativeToAdd.getUsername(),
									"1234", representativeToAdd.getCategory());
	        
		}	
		
		System.out.println("representative added");
	}
	
	public void deleteRepresentative(Representative representativeToDelete) throws SQLException
	{	
		DBConnection dbConnection = new DBConnection();
		
		try(Connection con = DBConnection.getCon()) {
			dbConnection.updateData("DELETE FROM allrepresentatives WHERE id_rep = " + representativeToDelete.getId());
			dbConnection.updateData("DELETE FROM users WHERE id_user = " + representativeToDelete.getId());
		}
      
		System.out.println("representatives deleted");
	}
	
	public void editFieldRepresentative(Representative representativeToEdit, String fieldToEdit) throws SQLException
	{
		
		try(Connection con = DBConnection.getCon()) {
		DBConnection dbConnection = new DBConnection();
			
			if(fieldToEdit.equals("name")) {
				dbConnection.updateData("UPDATE allrepresentatives SET " + fieldToEdit + " = '" + representativeToEdit.getName() + "' WHERE id_rep = " + representativeToEdit.getId());
				dbConnection.updateData("UPDATE users SET " + fieldToEdit + " = '" + representativeToEdit.getName() + "' WHERE id_user = " + representativeToEdit.getId());
			}
			else if(fieldToEdit.equals("username")) {
				dbConnection.updateData("UPDATE allrepresentatives SET " + fieldToEdit + " = '" + representativeToEdit.getUsername() + "' WHERE id_rep = " + representativeToEdit.getId());
				dbConnection.updateData("UPDATE users SET " + fieldToEdit + " = '" + representativeToEdit.getUsername() + "' WHERE id_user = " + representativeToEdit.getId());
			}
			else if(fieldToEdit.equals("category"))	{
				dbConnection.updateData("UPDATE allrepresentatives SET " + fieldToEdit + " = '" + representativeToEdit.getCategory() + "' WHERE id_rep = " + representativeToEdit.getId());
				dbConnection.updateData("UPDATE users SET " + fieldToEdit + " = '" + representativeToEdit.getCategory() + "' WHERE id_user = " + representativeToEdit.getId());
			}
		}
		
	}
	
	public void addAdmin(Admin adminToAdd) throws SQLException {
		
		UserController userController = new UserController();
		userController.addUser(adminToAdd.getId(), "admin", adminToAdd.getName(), adminToAdd.getUsername(), "1234", "");
		
	}
	
	public ArrayList<String> getListOfAdminUsernames() throws SQLException
	{
		ArrayList<String> listAdminUsernames = new ArrayList<>();
		
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
				
			ResultSet rs = dbConnection.getData("SELECT * from users WHERE id_user < 100");
				
			while(rs.next())
			{
				listAdminUsernames.add(rs.getString("username"));
			}
		}
		return listAdminUsernames;
	}
	
	public void deleteAdmin(Admin adminToDelete) throws SQLException
	{
		
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			ResultSet rs = dbConnection.getData("SELECT * FROM users WHERE username = '" + adminToDelete.getUsername() + "'");
			rs.next();
			adminToDelete.setId(rs.getInt("id_user"));
				
			dbConnection.updateData("DELETE FROM users WHERE id_user = " + adminToDelete.getId());
		}
	        	
		System.out.println("admin deleted");
	}
	
	public void checkUniqueAdminUsername(Admin admin) throws SQLException, NotUniqueUsernameException{
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
				
			ResultSet rs = dbConnection.getData("SELECT * FROM users");
				
			while(rs.next())
			{
				if(rs.getString("username").equals(admin.getUsername())) {
					throw new NotUniqueUsernameException("Username already used.");
				}
			}
		}
	}
	
	public void checkUniqueRepresentativeUsername(Representative rep) throws SQLException, NotUniqueUsernameException{
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
				
			ResultSet rs = dbConnection.getData("SELECT * FROM users");
				
			while(rs.next())
			{
				if(rs.getString("username").equals(rep.getUsername())) {
					throw new NotUniqueUsernameException("Username already used.");
				}
			}
		}
	}
}

