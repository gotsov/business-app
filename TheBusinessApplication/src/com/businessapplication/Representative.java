package com.businessapplication;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale.Category;
import java.util.Locale;
import java.util.Scanner;

public class Representative {

	static Scanner scan = new Scanner(System.in);
	
	public static int idRepresentative = 100;
	private int id;
	private String name;
	private String username;
	private String password;
	private String category;
	
	
	public Representative()
	{
		setId();
		setName();
		setUsername();
		setCategory();
		setPassword();
		
//		createRepTableByCategory();
	}
	
	public Representative(String category)
	{
		setId();
		setName();
		setUsername();
		setPassword();
		this.category = category;
		
//		createRepTableByCategory();
	}
	
	public Representative(int id, String name, String password, String category) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName() {
		System.out.println("name: ");
		String name = scan.nextLine();
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername() {
		System.out.println("username: ");
		String username = scan.nextLine();
		this.username = username;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory() {
		System.out.println("category: ");
		String category = scan.nextLine();
		this.category = category;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword()
	{
		System.out.println("password: ");
		String password = scan.nextLine();
		this.password = password;
	}
	
	public int getId() {
		return id;
	}

	public void setId() {		
		try {	
			ResultSet rs = DBConnection.getData(" SELECT id_rep FROM allrepresentatives");
					
			while(rs.next())
			{
				idRepresentative = rs.getInt("id_rep");
			}
			this.id = ++idRepresentative;
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		this.id = idRepresentative;
	}
	
	public static int getIdRepresentativeOfLast()
	{
		try {
			ResultSet rs = DBConnection.getData(" SELECT id_rep FROM allrepresentatives");
					
			while(rs.next())
			{
				idRepresentative = rs.getInt("id_rep");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return idRepresentative;
	}	
	
//	public static void addOrder()              //asks for client name, client email, id_prod, quantity, date
//	{
//		System.out.println("Enter information for the order...");
//		Client c = new Client();
//		c.buyProduct();
//	}
	
	
	public static void addSale(Client newClient)
	{
		System.out.println("What was the order (choose product by id)");
		int ch;
		
		try{	
			ResultSet rs = DBConnection.getData("SELECT * FROM allproducts");
			
	        System.out.println("[id, name, category, price, quantity]");
	         
	        while (rs.next()) {
	           int id = rs.getInt("id_prod");
	           String name = rs.getString("name");
	           String category = rs.getString("category");
	           double price = rs.getDouble("price");
	           int quantity = rs.getInt("quantity");
	           System.out.println(id + "\t" + name + "\t" + category + "\t" + price + "\t" + quantity);
	        }
	        
	        rs = DBConnection.getData("SELECT * FROM allproducts WHERE name = '" + newClient.getProductBought() + "'");
			
			int quantityOfProduct = 0;
			int newQuantity;

			String categoryProduct = null;
			double priceProduct = 0;
			
			
			
			while(rs.next())
			{
				quantityOfProduct = rs.getInt("quantity");
				priceProduct = rs.getDouble("price");		
			}
			
			double fullPrice = newClient.getQuantityBought() * priceProduct;
			
			NumberFormat formatter = new DecimalFormat("#0.00");
			String formatedFullPrice = formatter.format(fullPrice);
			NumberFormat format = NumberFormat.getInstance(Locale.ROOT);
			Number number = format.parse(formatedFullPrice);
			fullPrice = number.doubleValue();
			
			boolean isValid = newClient.isFirstTimeClient() && validateSale(newClient);
			
			if(isValid)
			{
				if(newClient.getQuantityBought() == quantityOfProduct)
				{		
					DBConnection.updateData( "DELETE FROM allproducts WHERE name = '" + newClient.getProductBought() + "'");
					
					System.out.println("This client bougth the last copy (product has been deleted).");
				}
				else
				{
					newQuantity = quantityOfProduct - newClient.getQuantityBought();
					
					DBConnection.updateData("UPDATE allproducts SET quantity = "+ newQuantity +" WHERE name = '"+ newClient.getProductBought() + "'");
					
				}
				
				PreparedStatement pstmt = DBConnection.insertData
						(" INSERT INTO allsales (id_sale, name, email, category, product, quantity, price, date)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				
				pstmt.setInt(1, newClient.getIdOfSale());
				pstmt.setString(2, newClient.getName());
				pstmt.setString(3, newClient.getEmail());
				pstmt.setString(4, newClient.getCategoryOfProductBought());
				pstmt.setString(5, newClient.getProductBought());
				pstmt.setInt(6, newClient.getQuantityBought());
				pstmt.setDouble(7, fullPrice);
				pstmt.setDate(8, newClient.getSqlDateOfSale());
				pstmt.execute();
				
				if(newClient.isFirstTimeClient())
				{
					
					pstmt = DBConnection.insertData
							(" INSERT INTO allclients (id_client, name, email)" + " VALUES (?, ?, ?)");
					pstmt.setInt(1, newClient.getIdOfClient());
					pstmt.setString(2, newClient.getName());
					pstmt.setString(3, newClient.getEmail());

					pstmt.execute();
				}
				
			}
	        
		} 
		catch(SQLException | ParseException e){
			e.printStackTrace();
		}
	}
	
	public static boolean validateSale(Client clientToVarify) {
		return clientToVarify.getEmail().matches("^(.+)@(.+)$");
	}
	
	public static void checkIfFirstTimeClient(Client clientToCheck)
	{
		try {		
			ResultSet rs = DBConnection.getData("SELECT * FROM allclients WHERE email = '" + clientToCheck.getEmail() + "'");
			
			boolean newEmail = true;
			
			while(rs.next())
			{
				System.out.println("im in checkIfFirstTimeClient");
				newEmail = false;
			}

			if(newEmail)
				clientToCheck.setFirstTimeClient(true);
			else
				clientToCheck.setFirstTimeClient(false);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setIdClient(Client clientToSetId)
	{
		try {		
			ResultSet rs = DBConnection.getData(" SELECT id_client FROM allclients ORDER BY id_client DESC LIMIT 1");
					
			rs.next();
			Client.idClient = rs.getInt("id_client");

			Client.idClient += 1;
			
			if(clientToSetId.isFirstTimeClient()) {
				clientToSetId.setIdOfClient(Client.idClient);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setIdSale(Client clientToSetId)
	{
		try {
			ResultSet rs = DBConnection.getData(" SELECT id_sale FROM allsales ORDER BY id_sale DESC LIMIT 1");
			
			rs.next();
			Client.idSale = rs.getInt("id_sale");
			
			clientToSetId.setIdOfSale(++Client.idSale);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void editClient(Client clientToEdit)
	{
		try{     
			String choice = null;
			
			if(clientToEdit.getEmail() == null) {
				choice = "name";
			}
			else if(clientToEdit.getName() == null) {
				choice = "e-mail";
			}
			else if(clientToEdit.getName() != null && clientToEdit.getEmail() != null) {
				choice = "both";
			}
			
			switch (choice) {
			case "name":
				
				DBConnection.updateData("UPDATE allclients SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_client = " + clientToEdit.getIdOfClient());
				DBConnection.updateData("UPDATE allsales SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_sale = " + clientToEdit.getIdOfClient());
				
				System.out.println("new name set");
				break;
			case "e-mail":
				DBConnection.updateData("UPDATE allclients SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + clientToEdit.getIdOfClient());
				DBConnection.updateData("UPDATE allsales SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + clientToEdit.getIdOfClient());
				
				System.out.println("new email set");
				break;
			case "both":

				DBConnection.updateData("UPDATE allclients SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_client = " + clientToEdit.getIdOfClient());
				DBConnection.updateData("UPDATE allsales SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_sale = " + clientToEdit.getIdOfClient());
				
				System.out.println("new name set");
				
				DBConnection.updateData("UPDATE allclients SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + clientToEdit.getIdOfClient());
				DBConnection.updateData("UPDATE allsales SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + clientToEdit.getIdOfClient());

				System.out.println("new email set");
				break;
			default:
				System.out.println("Invalid choice");
				break;
			}
      
	    } 
		catch(SQLException e) {
			e.printStackTrace();
		}

	}
	
	public static void deleteClient(Client clientToDelete)
	{
		try {		
			DBConnection.updateData("DELETE FROM allclients WHERE id_client = " + clientToDelete.getIdOfClient());
			DBConnection.updateData("DELETE FROM allsales WHERE email = '" + clientToDelete.getEmail() + "'");
			
			System.out.println("client deleted");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
	}	
		
	public static void addClient(Client clientToAdd)
	{
		try {		
			PreparedStatement pstmt = DBConnection.insertData
					(" INSERT INTO allclients (id_client, name, email)" + " VALUES (?, ?, ?)");
			pstmt.setInt(1, clientToAdd.getIdOfClient());
			pstmt.setString(2, clientToAdd.getName());
			pstmt.setString(3, clientToAdd.getEmail());

			pstmt.execute();
			
			System.out.println("client deleted");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}
