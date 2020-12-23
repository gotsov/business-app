package com.businessapplication;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.businessapplication.Client.Builder;

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
	private Double profit;
	private int numOfSales;
	
	public Representative(Builder builder)
	{
		this.name = builder.name;
		this.id = builder.id;
		this.username = builder.username;
		this.password = builder.password;
		this.category = builder.category;
		this.numOfSales = builder.numberOfSales;
		this.profit = builder.profit;
		
		if(this.id == 0)
		{
			setId();
		}
	}
	
	public static class Builder
	{
		private String name;
		private int id;
		private String username;
		private String password;
		private String category;
		private int numberOfSales;
		private double profit;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		public Builder category(String category) {
			this.category = category;
			return this;
		}
	
		public Builder id(int id) {
			this.id = id;
			return this;
		}
		
		public Builder numberOfSales(int numberOfSales) {
			this.numberOfSales = numberOfSales;
			return this;
		}
		
		public Builder profit(double profit)
		{
			this.profit = profit;
			return this;
		}
		
		public Representative build() {
			return new Representative(this);
		}

		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
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
		return this.id;
	}
	
	public int getNumberOfSales() {
		return this.numOfSales;
	}

	public void setId() {		
		try {	
			ResultSet rs = DBConnection.getData(" SELECT id_rep FROM allrepresentatives ORDER BY id_rep DESC LIMIT 1");
					
			rs.next();
			idRepresentative = rs.getInt("id_rep");

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
	
	public void setProfit(Double newProfit)
	{
		this.profit = newProfit;
	}
	
	public double getProfit()
	{
		return this.profit;
	}
	
	public void setNumOfSales(int newNumOfSales)
	{
		this.numOfSales = newNumOfSales;
	}	
	
	public void addSale(Client newClient, String representativeUsername)
	{
		System.out.println("What was the order (choose product by id)");
		int ch;
		
		try{	        
	        ResultSet rs = DBConnection.getData("SELECT * FROM allproducts WHERE name = '" + newClient.getProductBought() + "'");
			
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
			
			//using FRANCE because when using ROOT or getDefault the results are incorrect
			NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
			Number number = format.parse(formatedFullPrice);
			fullPriceOfSale = number.doubleValue();
			
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
						(" INSERT INTO allsales (id_sale, name, email, representative_username, category, product, quantity, price, date)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
				
				pstmt.setInt(1, newClient.getIdOfSale());
				pstmt.setString(2, newClient.getName());
				pstmt.setString(3, newClient.getEmail());
				pstmt.setString(4, representativeUsername);
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
				
				int newNumOfSales = this.numOfSales + 1;
				setNumOfSales(newNumOfSales);
				
				DBConnection.updateData("UPDATE allrepresentatives SET numberofsales = " + this.numOfSales + " WHERE id_rep = " + this.id);
				
				double newProfit = getProfit() + fullPriceOfSale;
				setProfit(newProfit);
				
				DBConnection.updateData("UPDATE allrepresentatives SET profit = " + this.profit + " WHERE id_rep = " + this.id);

			} 
		} catch(SQLException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean validateSale(Client clientToVarify)
	{
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
	
	public void setSqlDate(Client clientToSetSqlDate)
	{
		try {
			java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(clientToSetSqlDate.getDateOfSale());
			Date sqlDate = new Date(utilDate.getTime());
			
			clientToSetSqlDate.setSqlDateOfSale(sqlDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public static void editClient(Client clientToEdit, String fieldToEdit, int idClient, int idSale)
	{
		try{     
				if(fieldToEdit.equals("name")) {
					DBConnection.updateData("UPDATE allclients SET " + fieldToEdit + " = '" + clientToEdit.getName() + "'" +"WHERE id_client = " + idClient);
					DBConnection.updateData("UPDATE allsales SET " + fieldToEdit + " = '" +  clientToEdit.getName() + "'" +"WHERE id_sale = " + idSale);
					
					System.out.println("new name set");
				}
				else if(fieldToEdit.equals("email")) {
					DBConnection.updateData("UPDATE allclients SET  " + fieldToEdit + "'" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + idClient);
					DBConnection.updateData("UPDATE allsales SET " + fieldToEdit + "'" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + idSale);
					
					System.out.println("new email set");
				}
				
				else if(fieldToEdit.equals("name & e-mail")) {
					DBConnection.updateData("UPDATE allclients SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_client = " + idClient);
					DBConnection.updateData("UPDATE allsales SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_sale = " + idSale);
					
					System.out.println("new name set");
					
					DBConnection.updateData("UPDATE allclients SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + idClient);
					DBConnection.updateData("UPDATE allsales SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + idSale);

					System.out.println("new email set");
				}
      
	    } catch(SQLException e) {
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
