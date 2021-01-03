package com.businessapplication;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import com.businessapplication.Representative.Builder;
import com.databaseconnection.DBConnection;
import com.emailconnection.EmailDriver;
import com.rolecontrollers.RepresentativeController;

public class Representative extends RepresentativeController{
	
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
	
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getNumberOfSales() {
		return this.numOfSales;
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
	
	@Override
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
	
	@Override
	public void addSale(Client newClient)
	{
		System.out.println("What was the order (choose product by id)");
		int ch;
		
		try{	    
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
					
					EmailDriver.sendEmail("thebusinessapp2021@gmail.com", "Product out of stock!", "'" + productName + "' is out of stock!\n\nThe last amounts where bought on "
																		+ newClient.getDateOfSale() + ".\nYou might consider restocking.\n\nTheBusinessApp2021");
				}
				else
				{
					newQuantity = quantityOfProduct - newClient.getQuantityBought();
					
					DBConnection.updateData("UPDATE allproducts SET quantity = "+ newQuantity +" WHERE name = '"+ productName + "'");
					
					if(newQuantity <= 10) {
						EmailDriver.sendEmail("thebusinessapp2021@gmail.com", "Product running low!", "'" + productName + "' is running out of stock!\n\nYou might consider restocking.\n\nTheBusinessApp2021");
					}
					
				}
				
				PreparedStatement pstmt = DBConnection.insertData
						(" INSERT INTO allsales (id_sale, name, email, representative_username, category, product, quantity, price, date)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
				
				pstmt.setInt(1, newClient.getIdOfSale());
				pstmt.setString(2, newClient.getName());
				pstmt.setString(3, newClient.getEmail());
				pstmt.setString(4, this.username);
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
}
