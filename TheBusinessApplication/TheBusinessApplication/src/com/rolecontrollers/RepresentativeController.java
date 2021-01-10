package com.rolecontrollers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.businessapplication.Client;
import com.businessapplication.Product;
import com.businessapplication.Sale;
import com.businessapplication.Client.Builder;
import com.businessapplication.Representative;
import com.databaseconnection.DBConnection;
import com.emailconnection.EmailDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.JComboBox;

public class RepresentativeController {
	
	public static int idRepresentative = 100;
	public HashMap<String, Integer> clientsOfRepresentative = new HashMap<>();
	
	public boolean validateEmail(Client clientToVarify)
	{
		return clientToVarify.getEmail().matches("^(.+)@(.+)$");
	}
	
	public void addSale(Client newClient, Representative rep) throws SQLException
	{	
		try(Connection con = DBConnection.getCon()) {  
			DBConnection dbConnection = new DBConnection();
			
			String productName = newClient.getProductBought();
			
	        ResultSet rs = dbConnection.getData("SELECT * FROM allproducts WHERE name = '" + productName + "'");
			
			int quantityOfProduct = 0;
			int newQuantity;

			double priceProduct = 0;
			
			while(rs.next())
			{
				quantityOfProduct = rs.getInt("quantity");
				priceProduct = rs.getDouble("price");		
			}
			
			double fullPriceOfSale = newClient.getQuantityBought() * priceProduct;
			
			NumberFormat formatter = new DecimalFormat("#0.00");
			String formatedFullPrice = formatter.format(fullPriceOfSale);
			
			NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
			Number number = format.parse(formatedFullPrice);
			fullPriceOfSale = number.doubleValue();
			
			boolean isValid = newClient.isFirstTimeClient() && validateEmail(newClient);
			
			if(isValid)
			{
				if(newClient.getQuantityBought() == quantityOfProduct)
				{		
					dbConnection.updateData( "DELETE FROM allproducts WHERE name = '" + productName + "'");
					
					System.out.println("This client bougth the last copy (product has been deleted).");
					
					Thread th = new Thread( new Runnable(){

						public void run() {
							EmailDriver.sendEmail("thebusinessapp2021@gmail.com", "Product out of stock!", "'" + productName + "' is out of stock!\n\nThe last amounts where bought on "
									+ newClient.getDateOfSale() + ".\nYou might consider restocking.\n\nTheBusinessApp2021");
						}	
					});
					
					th.start();

				}
				else
				{
					newQuantity = quantityOfProduct - newClient.getQuantityBought();
					
					dbConnection.updateData("UPDATE allproducts SET quantity = "+ newQuantity +" WHERE name = '"+ productName + "'");
					
					if(newQuantity <= 10) {
						
						Thread th = new Thread( new Runnable(){

							public void run() {
								EmailDriver.sendEmail("thebusinessapp2021@gmail.com", "Product running low!", "'" + productName + "' is running out of stock!\n\nYou might consider restocking.\n\nTheBusinessApp2021");
							}	
						});
						
						th.start();
							
					}
					
				}
				
				PreparedStatement pstmt = dbConnection.insertData
						(" INSERT INTO allsales (id_sale, name, email, representative_username, category, product, quantity, price, date)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
				
				pstmt.setInt(1, newClient.getIdOfSale());
				pstmt.setString(2, newClient.getName());
				pstmt.setString(3, newClient.getEmail());
				pstmt.setString(4, rep.getUsername());
				pstmt.setString(5, newClient.getCategoryOfProductBought());
				pstmt.setString(6, newClient.getProductBought());
				pstmt.setInt(7, newClient.getQuantityBought());
				pstmt.setDouble(8, fullPriceOfSale);
				pstmt.setDate(9, newClient.getSqlDateOfSale());
				pstmt.execute();
				
				
				if(newClient.isFirstTimeClient())
				{
					
					pstmt = dbConnection.insertData
							(" INSERT INTO allclients (id_client, name, email)" + " VALUES (?, ?, ?)");
					pstmt.setInt(1, newClient.getIdOfClient());
					pstmt.setString(2, newClient.getName());
					pstmt.setString(3, newClient.getEmail());

					pstmt.execute();
				}
				
				int newNumOfSales = rep.getNumberOfSales() + 1;
				rep.setNumOfSales(newNumOfSales);
				
				dbConnection.updateData("UPDATE allrepresentatives SET numberofsales = " + rep.getNumberOfSales() + " WHERE id_rep = " + rep.getId());
				
				double newProfit = rep.getProfit() + fullPriceOfSale;
				rep.setProfit(newProfit);
				
				dbConnection.updateData("UPDATE allrepresentatives SET profit = " + rep.getProfit() + " WHERE id_rep = " + rep.getId());

			} 
		} catch(ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void checkIfFirstTimeClient(Client clientToCheck) throws SQLException 
	{
		try (Connection con = DBConnection.getCon()){		
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allclients WHERE email = '" + clientToCheck.getEmail() + "'");
			
			boolean newEmail = true;
			
			while(rs.next())
			{
				newEmail = false;
			}

			if(newEmail)
				clientToCheck.setFirstTimeClient(true);
			else
				clientToCheck.setFirstTimeClient(false);
			
		} 
		
	}
	
	public static void setIdClient(Client clientToSetId) throws SQLException
	{
		try (Connection con = DBConnection.getCon()){	
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData(" SELECT id_client FROM allclients ORDER BY id_client DESC LIMIT 1");
					
			rs.next();
			Client.idClient = rs.getInt("id_client");

			Client.idClient += 1;
			
			if(clientToSetId.isFirstTimeClient()) {
				clientToSetId.setIdOfClient(Client.idClient);
			}
			
		} 
	}
	
	public static void setIdSale(Client clientToSetId) throws SQLException
	{
		try (Connection con = DBConnection.getCon()){
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData(" SELECT id_sale FROM allsales ORDER BY id_sale DESC LIMIT 1");
			
			rs.next();
			Client.idSale = rs.getInt("id_sale");
			
			clientToSetId.setIdOfSale(++Client.idSale);
		} 
	}
	
	public static void setSqlDate(Client clientToSetSqlDate)
	{
		try {
			java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(clientToSetSqlDate.getDateOfSale());
			Date sqlDate = new Date(utilDate.getTime());
			
			clientToSetSqlDate.setSqlDateOfSale(sqlDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void editClient(Client clientToEdit, String fieldToEdit, int idClient, int idSale) throws SQLException
	{
		try(Connection con = DBConnection.getCon()) {     
			DBConnection dbConnection = new DBConnection();
			
				if(fieldToEdit.equals("name")) {
					dbConnection.updateData("UPDATE allclients SET " + fieldToEdit + " = '" + clientToEdit.getName() + "'" +"WHERE id_client = " + idClient);
					dbConnection.updateData("UPDATE allsales SET " + fieldToEdit + " = '" +  clientToEdit.getName() + "'" +"WHERE id_sale = " + idSale);
					
					System.out.println("new name set");
				}
				else if(fieldToEdit.equals("email")) {
					
					if(validateEmail(clientToEdit)) {
						dbConnection.updateData("UPDATE allclients SET  " + fieldToEdit + " = '" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + idClient);
						dbConnection.updateData("UPDATE allsales SET " + fieldToEdit + " = '" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + idSale);
						
						System.out.println("new email set");
					}

				}
				
				else if(fieldToEdit.equals("name & e-mail")) {
					
					if(validateEmail(clientToEdit)) {
						dbConnection.updateData("UPDATE allclients SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_client = " + idClient);
						dbConnection.updateData("UPDATE allsales SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_sale = " + idSale);
						
						System.out.println("new name set");
						
						dbConnection.updateData("UPDATE allclients SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + idClient);
						dbConnection.updateData("UPDATE allsales SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + idSale);

						System.out.println("new email set");
					}
					
				}
      
	    } 

	}
	
	public void deleteClient(Client clientToDelete) throws SQLException
	{
		try(Connection con = DBConnection.getCon()) {	
			DBConnection dbConnection = new DBConnection();
			
			dbConnection.updateData("DELETE FROM allclients WHERE id_client = " + clientToDelete.getIdOfClient());
			dbConnection.updateData("DELETE FROM allsales WHERE email = '" + clientToDelete.getEmail() + "'");
			
			System.out.println("client deleted");
		} 		
		
	}	
		
	public void addClient(Client clientToAdd) throws SQLException
	{
		try(Connection con = DBConnection.getCon()) {	
			DBConnection dbConnection = new DBConnection();
			
			PreparedStatement pstmt = dbConnection.insertData
					(" INSERT INTO allclients (id_client, name, email)" + " VALUES (?, ?, ?)");
			pstmt.setInt(1, clientToAdd.getIdOfClient());
			pstmt.setString(2, clientToAdd.getName());
			pstmt.setString(3, clientToAdd.getEmail());

			pstmt.execute();
			
			System.out.println("client deleted");
		}
	}
	
	public ArrayList<Sale> removeRepeatingClients(ArrayList<Sale> catalog) throws SQLException {
		ArrayList<Sale> clients = new ArrayList<>();
		
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			for (int i = 0; i < catalog.size(); i++) 
			{
				for (int j = i+1; j < catalog.size(); j++) {
					
					if(catalog.get(i).equals(catalog.get(j))) {
						catalog.remove(j);
					}
				}
				
				ResultSet rs = dbConnection.getData("SELECT * FROM allclients WHERE email = '" + catalog.get(i).getEmail() + "'");	
				rs.next();
				catalog.get(i).setId(rs.getInt("id_client"));
			
			}
			
		}
		return catalog;

	}
	
	public ArrayList<Product> getProductsFromYourCategory(Representative rep) throws SQLException{
		DBConnection dbConnection = new DBConnection();
		ArrayList<Product> products = new ArrayList<>();
		
		try(Connection con = DBConnection.getCon()) {
			ResultSet rs = dbConnection.getData("SELECT * FROM allproducts WHERE category = '" + rep.getCategory() + "'");
			
			if(rep.getCategory().equals("all")) {
				rs = dbConnection.getData("SELECT * FROM allproducts");
			}
			
			while(rs.next())
	        {  	      	
	        	Product p = new Product.Builder().id(rs.getInt("id_prod"))
	        									 .name(rs.getString("name"))
	        									 .category(rs.getString("category"))
	        									 .quantity(rs.getInt("quantity"))
	        									 .price(rs.getDouble("price"))
	        									 .build();
	        	products.add(p);
	        }
			
		} 
		
		return products;
	}
	
	public ArrayList<Sale> getCatalog(Representative rep) throws SQLException{
		
		ArrayList<Sale> catalog = new ArrayList<>();
		try(Connection con = DBConnection.getCon()){
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allsales WHERE representative_username = '" + rep.getUsername() + "'");
			
	        while(rs.next())
	        {  	      	
	        	Sale s = new Sale.Builder().id(rs.getInt("id_sale"))
	        											   .name(rs.getString("name"))
	        											   .email(rs.getString("email"))
	        											   .product(rs.getString("product"))
	        											   .quantity(rs.getInt("quantity"))
	        											   .price(rs.getDouble("price"))
	        											   .date(rs.getDate("date"))
	        											   .build();	
	        	catalog.add(s);
	        }
		}

        return catalog;
	}

	public String getCategoryOfBoughtProduct(String boughtProduct) throws SQLException {
		
		try(Connection con = DBConnection.getCon()) {
    		DBConnection dbConnection = new DBConnection();
    		
			ResultSet rs = dbConnection.getData("SELECT * FROM allproducts WHERE name = '" + boughtProduct + "'");
			rs.next();
			
			return rs.getString("category");
		} 

	}
	
	
	public int getIdOfSale(Representative rep) throws SQLException
	{
		try (Connection con = DBConnection.getCon()){
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allsales WHERE representative_username = '" + rep.getUsername() + "'");
			rs.next();
			
			return rs.getInt("id_sale");
		} 
		
	}
	
	public String getInfoOfClientById(String toChange, int id) throws SQLException
	{
		
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT " + toChange + " FROM allclients WHERE id_client = " + id);
			
			while(rs.next())
			{
				String info =  rs.getString(toChange);
				return info;
			}
					
		} 
		return null;	
	}
	
	public void fillWithQuantity(JComboBox<String> comboBoxBoughtProduct, JComboBox<String> comboBoxQuantity) throws SQLException{
		try(Connection con = DBConnection.getCon()){
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allproducts WHERE name = '" + comboBoxBoughtProduct.getSelectedItem() + "'");
	        
	        int quantity = 0;
	        while(rs.next())
	        {
	        	quantity = rs.getInt("quantity");
	        }
	        
	        for(int i = 1; i <= quantity; i++)
	        {
	        	comboBoxQuantity.addItem(""+i);
	        }
	        
		} 
	}
}

