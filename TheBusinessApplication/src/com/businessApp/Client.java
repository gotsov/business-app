package com.businessApp;

import java.sql.Connection;


import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.NumberFormatter;

public class Client {
	public static int idSale = 100;
	public static int idClient = 100;
	private int idOfSale;
	private int idOfClient;
	private String name;
	private String email;
	private String category;
	private String product;
	private int quantity;
	private String date;
	private Date sqlDate;

	private boolean firstTimeClient;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	public Client(Builder builder)
	{
		this.name = builder.name;
		this.email = builder.email;
		this.category = builder.category;
		this.product = builder.product;
		this.category = builder.category;
		this.quantity = builder.quantity;
		this.date = builder.date;
		
		checkForUniqueEmail();
		setIdSale();
		setIdClient();
		setSqlDate();	
	}
	
	public static class Builder
	{
		private String name;
		private String email;
		private String category;
		private String product;
		private int quantity;
		private String date;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder category(String category) {
			this.category = category;
			return this;
		}
		
		public Builder product(String product) {
			this.product = product;
			return this;
		}
		
		public Builder quantity(int quantity) {
			this.quantity = quantity;
			return this;
		}
		
		public Builder date(String date) {
			this.date = date;
			return this;
		}
		
		public Client build() {
			return new Client(this);
		}
	}
	
	public void setSqlDate()
	{
		try {
			java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			Date sqlDate = new Date(utilDate.getTime());
			
			this.sqlDate = sqlDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	public void checkForUniqueEmail()
	{
		try {		
			ResultSet rs = DBConnection.getData(" SELECT * FROM allclients WHERE email = '" + this.email + "'");
			
			boolean newEmail = true;
			
			while(rs.next())
			{
				newEmail = false;
			}

			if(newEmail)
				firstTimeClient = true;
			else
				firstTimeClient = false;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getIdOfClient() {
		return idOfClient;
	}
	
	public int getIdOfSale() {
		return idOfSale;
	}

	public void setIdSale() {
		try {
			ResultSet rs = DBConnection.getData(" SELECT id_sale FROM allsales ORDER BY id_sale DESC LIMIT 1");
			
			rs.next();
			idSale = rs.getInt("id_sale");
			
			this.idOfSale = ++idSale;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setIdClient() {
		try {		
			ResultSet rs = DBConnection.getData(" SELECT id_client FROM allclients ORDER BY id_client DESC LIMIT 1");
					
			rs.next();
			idClient = rs.getInt("id_client");

			idClient += 1;
			
			if(firstTimeClient) {
				this.idOfClient = idClient;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public boolean validateOrder()
	{
		String regex = "^(.+)@(.+)$";	 
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(this.email);
		
		if(matcher.matches())
		{
			return true;
		}
		else
			return false;
		
	}
	
	public void buyProduct()
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
	        
	        rs = DBConnection.getData("SELECT * FROM allproducts WHERE name = '" + product + "'");
			
			int q = 0;
			int newQ;

			String categoryProduct = null;
			double priceProduct = 0;
			
			while(rs.next())
			{
				q = rs.getInt("quantity");
				category = rs.getString("category");
				product = rs.getString("name");
				priceProduct = rs.getDouble("price");
				
			}
			
			double fullPrice = quantity * priceProduct;
			
			NumberFormat formatter = new DecimalFormat("#0.00");
			String formatedFullPrice = formatter.format(fullPrice);
			NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
			Number number = format.parse(formatedFullPrice);
			fullPrice = number.doubleValue();
			
			boolean isValid = validateOrder();
			
			if(isValid)
			{
				if(quantity == q)
				{		
					DBConnection.updateData( "DELETE FROM allproducts WHERE name = '" + product + "'");
					
					System.out.println("This client bougth the last copy (product has been deleted).");
				}
				else
				{
					newQ = q - quantity;
					
					DBConnection.updateData("UPDATE allproducts SET quantity = "+ newQ +" WHERE name = '"+ product + "'");
					
				}
				
				PreparedStatement pstmt = DBConnection.getDataWithPrepStatement
						(" INSERT INTO allsales (id_sale, name, email, category, product, quantity, price, date)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				
				pstmt.setInt(1, this.idOfSale);
				pstmt.setString(2, this.name);
				pstmt.setString(3, this.email);
				pstmt.setString(4, category);
				pstmt.setString(5, product);
				pstmt.setInt(6, quantity);
				pstmt.setDouble(7, fullPrice);
				pstmt.setDate(8, sqlDate);
				pstmt.execute();
				
				if(firstTimeClient)
				{
					
					pstmt = DBConnection.getDataWithPrepStatement
							(" INSERT INTO allclients (id_client, name, email)" + " VALUES (?, ?, ?)");
					pstmt.setInt(1, this.idOfClient);
					pstmt.setString(2, this.name);
					pstmt.setString(3, this.email);

					pstmt.execute();
				}
				
			}
	        
		} 
		catch(SQLException | ParseException e){
			e.printStackTrace();
		}
	}

}
