package com.businessapplication;

import java.sql.Date;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.exceptions.NegativeQuantityException;
import com.representative.ClientService;
import com.representative.SalesService;

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
	
	public Client(Builder builder) throws SQLException,  NegativeQuantityException
	{
		ClientService clientService = new ClientService();
		SalesService salesService = new SalesService();
		
		this.name = builder.name;
		this.email = builder.email;
		this.categoryOfProductBought = builder.categoryOfProductBought;
		this.productBought = builder.ProductBought;
		this.quantityBought = builder.quantityBought;
		this.dateOfSale = builder.dateOfSale;
		this.idOfClient = builder.idOfClient;
		
		clientService.checkIfFirstTimeClient(this);
		
		if(this.categoryOfProductBought != null)   
		{
			salesService.setIdSale(this);
			clientService.setIdClient(this);
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
		
		public Builder quantityBought(int quantityBought) throws NegativeQuantityException{
			
			if(quantityBought <= 0) {
				throw new NegativeQuantityException("Negative quantity");
			}
			
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
		
		public Client build() throws SQLException,  NegativeQuantityException{
			return new Client(this);
		}

		
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

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCategoryOfProductBought(String categoryOfProductBought) {
		this.categoryOfProductBought = categoryOfProductBought;
	}

	public void setProductBought(String productBought) {
		this.productBought = productBought;
	}

	public void setQuantityBought(int quantityBought) {
		this.quantityBought = quantityBought;
	}

	public void setDateOfSale(String dateOfSale) {
		this.dateOfSale = dateOfSale;
	}
	
}
