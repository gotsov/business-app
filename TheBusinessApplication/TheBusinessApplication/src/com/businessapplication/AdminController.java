package com.businessapplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	
	public void addProduct(Product newProduct) {
		try {		
			PreparedStatement pstmt = DBConnection.insertData
									(" INSERT INTO allproducts (id_prod, name, category, price, quantity)" + " VALUES (?, ?, ?, ?, ?)");
			pstmt.setInt(1, newProduct.getId());
			pstmt.setString(2, newProduct.getName());
			pstmt.setString(3, newProduct.getCategory());
			pstmt.setDouble(4, newProduct.getPrice());
			pstmt.setInt(5, newProduct.getQuantity());
				
			pstmt.execute();
	        
		} catch (SQLException e) {
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
	
	//haven't implemented this yet
	public void getSalesInAPeriodOfTime(java.sql.Date startDate, java.sql.Date endDate)
	{
		try {			
			ResultSet rs = DBConnection.getData("SELECT * FROM allsales");
	        
	        java.sql.Date dateToCompare;
	        
	        String name, email, product;
	        int id, quantity;
	        double price;
	        
	        while(rs.next())
	        {
	        	id = rs.getInt("id_sale");
	        	name = rs.getString("name");
	        	email = rs.getString("email");
	        	product = rs.getString("product");
	        	quantity = rs.getInt("quantity");
	        	price = rs.getDouble("price");
	        	dateToCompare = rs.getDate("date");
	        	
	        	if(dateToCompare.compareTo(startDate) >= 0 && dateToCompare.compareTo(endDate) <= 0)
	        	{
	        		System.out.println("id, name, email, product, quantity, price, date");
	        		System.out.println(id + "\t" + name + "\t" + email +"\t" + product + "\t" + quantity + "\t" + price + "\t" + dateToCompare);
	        	}
	        }
     
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	Date startDate, endDate;
//	
//try {
//java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
//java.sql.Date sqlStartDate = new java.sql.Date(utilDate.getTime());
//		
//System.out.println("Enter start of period: (yyyy-MM-dd)");
//stringDate = scan.next();
//		
//utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
//java.sql.Date sqlEndDate = new java.sql.Date(utilDate.getTime());
//		
//getSalesInAPeriodOfTime(sqlStartDate, sqlEndDate);
//} catch (ParseException e1) {
//e1.printStackTrace();
//}
	
}

