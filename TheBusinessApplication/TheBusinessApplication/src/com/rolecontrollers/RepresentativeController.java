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
import com.businessapplication.Sale;
import com.businessapplication.Client.Builder;
import com.businessapplication.Representative;
import com.databaseconnection.DBConnection;
import com.emailconnection.EmailDriver;

import java.util.ArrayList;
import java.util.Locale;

public class RepresentativeController {
	
	public static int idRepresentative = 100;
	
	//public abstract void addSale(Client newClient);
	
	public boolean validateEmail(Client clientToVarify)
	{
		return clientToVarify.getEmail().matches("^(.+)@(.+)$");
	}
	
	public void addSale(Client newClient, Representative rep)
	{	
		try(Connection con = DBConnection.getCon()) {  
			String productName = newClient.getProductBought();
			
	        ResultSet rs = DBConnection.getData("SELECT * FROM allproducts WHERE name = '" + productName + "'");
			
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
					DBConnection.updateData( "DELETE FROM allproducts WHERE name = '" + productName + "'");
					
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
					
					DBConnection.updateData("UPDATE allproducts SET quantity = "+ newQuantity +" WHERE name = '"+ productName + "'");
					
					if(newQuantity <= 10) {
						
						Thread th = new Thread( new Runnable(){

							public void run() {
								EmailDriver.sendEmail("thebusinessapp2021@gmail.com", "Product running low!", "'" + productName + "' is running out of stock!\n\nYou might consider restocking.\n\nTheBusinessApp2021");
							}	
						});
						
						th.start();
							
					}
					
				}
				
				PreparedStatement pstmt = DBConnection.insertData
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
					
					pstmt = DBConnection.insertData
							(" INSERT INTO allclients (id_client, name, email)" + " VALUES (?, ?, ?)");
					pstmt.setInt(1, newClient.getIdOfClient());
					pstmt.setString(2, newClient.getName());
					pstmt.setString(3, newClient.getEmail());

					pstmt.execute();
				}
				
				int newNumOfSales = rep.getNumberOfSales() + 1;
				rep.setNumOfSales(newNumOfSales);
				
				DBConnection.updateData("UPDATE allrepresentatives SET numberofsales = " + rep.getNumberOfSales() + " WHERE id_rep = " + rep.getId());
				
				double newProfit = rep.getProfit() + fullPriceOfSale;
				rep.setProfit(newProfit);
				
				DBConnection.updateData("UPDATE allrepresentatives SET profit = " + rep.getProfit() + " WHERE id_rep = " + rep.getId());

			} 
		} catch(SQLException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void checkIfFirstTimeClient(Client clientToCheck) 
	{
		try (Connection con = DBConnection.getCon()){		
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
		try (Connection con = DBConnection.getCon()){		
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
		try (Connection con = DBConnection.getCon()){
			ResultSet rs = DBConnection.getData(" SELECT id_sale FROM allsales ORDER BY id_sale DESC LIMIT 1");
			
			rs.next();
			Client.idSale = rs.getInt("id_sale");
			
			clientToSetId.setIdOfSale(++Client.idSale);
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	public void editClient(Client clientToEdit, String fieldToEdit, int idClient, int idSale)
	{
		try(Connection con = DBConnection.getCon()) {     
				if(fieldToEdit.equals("name")) {
					DBConnection.updateData("UPDATE allclients SET " + fieldToEdit + " = '" + clientToEdit.getName() + "'" +"WHERE id_client = " + idClient);
					DBConnection.updateData("UPDATE allsales SET " + fieldToEdit + " = '" +  clientToEdit.getName() + "'" +"WHERE id_sale = " + idSale);
					
					System.out.println("new name set");
				}
				else if(fieldToEdit.equals("email")) {
					
					if(validateEmail(clientToEdit)) {
						DBConnection.updateData("UPDATE allclients SET  " + fieldToEdit + " = '" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + idClient);
						DBConnection.updateData("UPDATE allsales SET " + fieldToEdit + " = '" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + idSale);
						
						System.out.println("new email set");
					}

				}
				
				else if(fieldToEdit.equals("name & e-mail")) {
					
					if(validateEmail(clientToEdit)) {
						DBConnection.updateData("UPDATE allclients SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_client = " + idClient);
						DBConnection.updateData("UPDATE allsales SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_sale = " + idSale);
						
						System.out.println("new name set");
						
						DBConnection.updateData("UPDATE allclients SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + idClient);
						DBConnection.updateData("UPDATE allsales SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + idSale);

						System.out.println("new email set");
					}
					
				}
      
	    } catch(SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void deleteClient(Client clientToDelete)
	{
		try(Connection con = DBConnection.getCon()) {		
			DBConnection.updateData("DELETE FROM allclients WHERE id_client = " + clientToDelete.getIdOfClient());
			DBConnection.updateData("DELETE FROM allsales WHERE email = '" + clientToDelete.getEmail() + "'");
			
			System.out.println("client deleted");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
	}	
		
	public void addClient(Client clientToAdd)
	{
		try(Connection con = DBConnection.getCon()) {		
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
	
	public ArrayList<Sale> removeRepeatingClients(ArrayList<Sale> catalog) {
		
		try(Connection con = DBConnection.getCon()) {
			for (int i = 0; i < catalog.size(); i++) {
				for (int j = i+1; j < catalog.size(); j++) {
					
					if(catalog.get(i).equals(catalog.get(j))) {
						catalog.remove(j);
					}
				}
			
				ResultSet rs = DBConnection.getData("SELECT id_client FROM allclients WHERE email = '" + catalog.get(i).getEmail() + "'");
				
				while(rs.next())
				{
					catalog.get(i).setId(rs.getInt("id_client"));
				}
			
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return catalog;

	}
}
