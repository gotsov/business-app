package com.businessapplication;

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
	private String categoryOfProductBought;
	private String productBought;
	private int quantityBought;
	private String dateOfSale;
	private Date sqlDateOfSale;

	private boolean firstTimeClient;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	public Client(Builder builder)
	{
		this.name = builder.name;
		this.email = builder.email;
		this.categoryOfProductBought = builder.categoryOfProductBought;
		this.productBought = builder.ProductBought;
		this.quantityBought = builder.quantityBought;
		this.dateOfSale = builder.dateOfSale;
		this.idOfClient = builder.idOfClient;
		
		checkForUniqueEmail();
		
		//check if a field that's is not idOdClient, name or email is null, 
		//because when editing a sale only these three are used with the builder, 
		//therefore it needn't procede to SetIdSale(), setIdClient(), setSqlDate()
		if(this.categoryOfProductBought != null)   
		{
			setIdSale();
			setIdClient();
			setSqlDate();	
		}

	}
	
	public static class Builder
	{
		private String name;
		private String email;
		private String categoryOfProductBought;
		private String ProductBought;
		private int quantityBought;
		private String dateOfSale;
		private int idOfClient;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder categoryOfProductBought(String categoryOfProductBought) {
			this.categoryOfProductBought = categoryOfProductBought;
			return this;
		}
		
		public Builder productBought(String ProductBought) {
			this.ProductBought = ProductBought;
			return this;
		}
		
		public Builder quantityBought(int quantityBought) {
			this.quantityBought = quantityBought;
			return this;
		}
		
		public Builder idOfClient(int idOfClient) {
			this.idOfClient = idOfClient;
			return this;
		}
		
		public Builder dateOfSale(String dateOfSale) {
			this.dateOfSale = dateOfSale;
			return this;
		}
		
		public Client build() {
			return new Client(this);
		}

		
	}
	
	public void setIdSale() {
		Representative.setIdSale(this);
	}
	
	public void setIdClient() {
		Representative.setIdClient(this);
	}
	
	public void setSqlDate()
	{
		try {
			java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateOfSale);
			Date sqlDate = new Date(utilDate.getTime());
			
			this.sqlDateOfSale = sqlDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	
	public void checkForUniqueEmail()
	{
		Representative.checkIfFirstTimeClient(this);
	}
	
	public boolean isFirstTimeClient() {
		return firstTimeClient;
	}
	
	public void setFirstTimeClient(boolean b)
	{
		this.firstTimeClient = b;
	}

	public int getIdOfClient() {
		return idOfClient;
	}
	
	public void setIdOfClient(int idClient) {
		this.idOfClient = idClient;
	}
	
	public int getIdOfSale() {
		return idOfSale;
	}
	
	public void setIdOfSale(int idSale) {
		this.idOfSale = idSale;
	}

	public String getCategoryOfProductBought() {
		return categoryOfProductBought;
	}


	public String getProductBought() {
		return productBought;
	}


	public int getQuantityBought() {
		return quantityBought;
	}


	public String getDateOfSale() {
		return dateOfSale;
	}


	public Date getSqlDateOfSale() {
		return sqlDateOfSale;
	}
	
	public void setSqlDateOfSale(Date sqlDateOfSale) {
		this.sqlDateOfSale = sqlDateOfSale;
	}
	
	public String getName() {
		return name;
	}
	
	public String getEmail()
	{
		return email;
	}
}
