package com.rolecontrollers;

import java.io.IOException;
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

import com.businessapplication.Product;
import com.businessapplication.Representative;
import com.businessapplication.Sale;
import com.twitterconnection.TwitterDriver;
import com.databaseconnection.DBConnection;

import twitter4j.TwitterException;

public abstract class AdminController {	
	public static int idUser = 1;
	
	//will be used when adding new admins to users table
	public int getIdUser()
	{
		try {	
			ResultSet rs = DBConnection.getData(" SELECT id_user FROM users");
					
			while(rs.next())
			{
				//with 100 start the id's of representatives 
				if(rs.getInt("id_user") > 100)
				{
					break;
				}
				
				idUser = rs.getInt("id_user");
			}
			return ++idUser;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public void addProduct(Product newProduct, boolean postOnSocial) {
		try {		
			
			String name = newProduct.getName();
			PreparedStatement pstmt = DBConnection.insertData
									(" INSERT INTO allproducts (id_prod, name, category, price, quantity)" + " VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, newProduct.getId());
			pstmt.setString(2, name);
			pstmt.setString(3, newProduct.getCategory());
			pstmt.setDouble(4, newProduct.getPrice());
			pstmt.setInt(5, newProduct.getQuantity());
				
			pstmt.execute();
			
			if(postOnSocial) {
				TwitterDriver.tweetOut("" + name + " now available in our stores! #newoffer #" + name.replaceAll("\\s", "").toLowerCase(), "newProduct");
			}
			
	        
		} catch (SQLException | IOException | TwitterException e) {
			e.printStackTrace();
		}	

		System.out.println("product added");
	}
	
	public void deleteProduct(Product productToDelete)
	{
		try {		
			DBConnection.updateData("DELETE FROM allproducts WHERE id_prod = " + productToDelete.getId());
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}	

		System.out.println("product deleted");
	}
	
	public void editFieldProduct(Product productToEdit, String fieldToEdit) {
		
		try {			
			if(fieldToEdit.equals("name")) {
				DBConnection.updateData("UPDATE allproducts SET " + fieldToEdit + " = '" + productToEdit.getName() + "' WHERE id_prod = " + productToEdit.getId());
			}
			else if(fieldToEdit.equals("category"))
			{
				DBConnection.updateData("UPDATE allproducts SET " + fieldToEdit + " = '" + productToEdit.getCategory() + "' WHERE id_prod = " + productToEdit.getId());
			}
			else if(fieldToEdit.equals("price"))
			{
				DBConnection.updateData("UPDATE allproducts SET " + fieldToEdit + " = " + productToEdit.getPrice() + " WHERE id_prod = " + productToEdit.getId());
			}
			else if(fieldToEdit.equals("quantity"))
			{
				DBConnection.updateData("UPDATE allproducts SET " + fieldToEdit + " = " + productToEdit.getQuantity() + " WHERE id_prod = " + productToEdit.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addRepresentative(Representative representativeToAdd)
	{
		try {		
			PreparedStatement pstmt = DBConnection.insertData
									(" INSERT INTO allrepresentatives (id_rep, name, username, category, numberofsales, profit) VALUES (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, representativeToAdd.getId());
			pstmt.setString(2, representativeToAdd.getName());
			pstmt.setString(3, representativeToAdd.getUsername());
			pstmt.setString(4, representativeToAdd.getCategory());
			pstmt.setInt(5, representativeToAdd.getNumberOfSales());
			pstmt.setDouble(6, representativeToAdd.getProfit());
				
			pstmt.execute();
			
			pstmt = DBConnection.insertData
					(" INSERT INTO users (id_user, usertype, name, username, password, category) VALUES (?, ?, ?, ?, ?, ?)");
			
			pstmt.setInt(1, representativeToAdd.getId());
			pstmt.setString(2, "representative");
			pstmt.setString(3, representativeToAdd.getName());
			pstmt.setString(4, representativeToAdd.getUsername());
			pstmt.setString(5, "1234");                                     //sets default password to 1234
			pstmt.setString(6, representativeToAdd.getCategory());
			
			pstmt.execute();
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		System.out.println("representative added");
	}
	
	public void deleteRepresentative(Representative representativeToDelete)
	{
		try {		
			DBConnection.updateData("DELETE FROM allrepresentatives WHERE id_rep = " + representativeToDelete.getId());
			DBConnection.updateData("DELETE FROM users WHERE id_user = " + representativeToDelete.getId());
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}	

		System.out.println("representatives deleted");
	}
	
	public void editFieldRepresentative(Representative representativeToEdit, String fieldToEdit)
	{
		System.out.println("representativeToEdit = " + representativeToEdit);
		System.out.println("fieldToEdit = " + fieldToEdit);
		
		try {			
			if(fieldToEdit.equals("name")) {
				DBConnection.updateData("UPDATE allrepresentatives SET " + fieldToEdit + " = '" + representativeToEdit.getName() + "' WHERE id_rep = " + representativeToEdit.getId());
				DBConnection.updateData("UPDATE users SET " + fieldToEdit + " = '" + representativeToEdit.getName() + "' WHERE id_user = " + representativeToEdit.getId());
			}
			else if(fieldToEdit.equals("username"))
			{
				DBConnection.updateData("UPDATE allrepresentatives SET " + fieldToEdit + " = '" + representativeToEdit.getUsername() + "' WHERE id_rep = " + representativeToEdit.getId());
				DBConnection.updateData("UPDATE users SET " + fieldToEdit + " = '" + representativeToEdit.getUsername() + "' WHERE id_user = " + representativeToEdit.getId());

			}
			else if(fieldToEdit.equals("category"))
			{
				DBConnection.updateData("UPDATE allrepresentatives SET " + fieldToEdit + " = '" + representativeToEdit.getCategory() + "' WHERE id_rep = " + representativeToEdit.getId());
				DBConnection.updateData("UPDATE users SET " + fieldToEdit + " = '" + representativeToEdit.getCategory() + "' WHERE id_user = " + representativeToEdit.getId());

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<Sale> filterSalesByDate(String startDate, String endDate, ArrayList<Sale> listAllSales)
	{
		
		ArrayList<Sale> filteredByDateList = new ArrayList<>();
		
		try {			
			ResultSet rs = DBConnection.getData("SELECT * FROM allsales");
			
			java.util.Date utilStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
			
			java.util.Date utilEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
			
	        Date dateToCompare;
	        
	        String name, email, product, representativeUsername, category;
	        int id, quantity;
	        double price;
	        
	        while(rs.next())
	        {     	
	        	id = rs.getInt("id_sale");
	        	email = rs.getString("email");
	        	product = rs.getString("product");
	        	category = rs.getString("category");
	        	quantity = rs.getInt("quantity");
	        	price = rs.getDouble("price");
	        	representativeUsername = rs.getString("representative_username");
	        	dateToCompare = rs.getDate("date");
	        	
	        	if(dateToCompare.compareTo(utilStartDate) >= 0 && dateToCompare.compareTo(utilEndDate) <= 0)
	        	{
	        		Sale sale = new Sale.Builder().id(id)
	        													  .email(email)
	        													  .representativeUsername(representativeUsername)
	        													  .category(category)
	        													  .product(product)
	        													  .quantity(quantity)
	        													  .price(price)
	        													  .date(dateToCompare)
	        													  .build();
	        		filteredByDateList.add(sale);
	        	}
	        }
     
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		
		return filteredByDateList;
	}

	public ArrayList<Sale> filterSalesByRepresentativeUsername(ArrayList<Sale> listAllSales, String username)
	{
		ArrayList<Sale> filteredByUsernameList = new ArrayList<>();
		
		try {			
			ResultSet rs = DBConnection.getData("SELECT * FROM allsales");
					
	        Date date;
	        String name, email, product, representativeUsername, category;
	        int id, quantity;
	        double price;
	        
			while(rs.next())
	        {     	
	        	id = rs.getInt("id_sale");
	        	email = rs.getString("email");
	        	product = rs.getString("product");
	        	category = rs.getString("category");
	        	quantity = rs.getInt("quantity");
	        	price = rs.getDouble("price");
	        	representativeUsername = rs.getString("representative_username");
	        	date = rs.getDate("date");
	        	
	        	if(representativeUsername.equals(username))
	        	{
	        		Sale sale = new Sale.Builder().id(id)
	        													  .email(email)
	        													  .representativeUsername(representativeUsername)
	        													  .category(category)
	        													  .product(product)
	        													  .quantity(quantity)
	        													  .price(price)
	        													  .date(date)
	        													  .build();
	        		filteredByUsernameList.add(sale);
	        	}
	        }
 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return filteredByUsernameList;
	}
	
	
	public String getMostSalesByCriteria(String criteria)
	{
		String mostFrequentCriteria = "";
		int numSales = 0;
		
		HashMap<String, Integer> categoriesHash = new HashMap<>();
		
		try {
			ResultSet rs = DBConnection.getData("SELECT * FROM allsales");
			String result;
			while(rs.next())
			{
				result = rs.getString(criteria);
				
				if(categoriesHash.containsKey(result)) {
					categoriesHash.put(result, categoriesHash.get(result) + 1);
				}
				else {
					categoriesHash.put(result, 1);
				}
			}
			
			Set<Map.Entry<String, Integer> > set = categoriesHash.entrySet();
			
			
			for(Map.Entry<String, Integer> me : set)
			{
				if(me.getValue() > numSales)	{
					numSales = me.getValue();
					mostFrequentCriteria = me.getKey();
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mostFrequentCriteria + " - " + numSales + " sales";
	}
	
	public int getNumberOfClients()
	{
		int numOfClients = 0;
		
		try {
			ResultSet rs = DBConnection.getData("SELECT COUNT(*) from allclients");
			rs.next();
			
			numOfClients = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return numOfClients;
	}
	
	public double getTotalProfit()
	{
		double totalProfit = 0;
		
		try {
			ResultSet rs = DBConnection.getData("SELECT * from allsales");
			
			while(rs.next())
			{
				totalProfit += rs.getDouble("price");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return totalProfit;
	}
}

