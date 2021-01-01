package com.businessapplication;

import java.sql.Date;
import java.util.ArrayList;

import com.databaseconnection.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Sale {
	private int id;
	private String name;
	private String email;
	private String product;
	private int quantity;
	private Double price;
	private Date date;
	private String representativeUsername;
	private String category;
	
	public Sale(Builder builder)
	{
		this.id = builder.id;
		this.name = builder.name;
		this.email = builder.email;
		this.product = builder.product;
		this.quantity = builder.quantity;
		this.price = builder.price;
		this.date =builder.date;
		this.representativeUsername = builder.representativeUsername;
		this.category = builder.category;
	}
	
	public static class Builder
	{
		private int id;
		private String name;
		private String email;
		private String product;
		private int quantity;
		private Double price;
		private Date date;
		private String representativeUsername;
		private String category;
		
		public Builder id(int id) {
			this.id = id;
			return this;
		}
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder email(String email) {
			this.email = email;
			return this;
		}
		
		public Builder product(String product) {
			this.product = product;
			return this;
		}
		
		public Builder category(String category) {
			this.category = category;
			return this;
		}
		
		public Builder quantity(int quantity) {
			this.quantity = quantity;
			return this;
		}
		
		public Builder price(double price) {
			this.price = price;
			return this;
		}
		
		public Builder date(Date date) {
			this.date = date;
			return this;
		}
		
		public Builder representativeUsername(String representativeUsername) {
			this.representativeUsername = representativeUsername;
			return this;
		}
		
		public Sale build() {
			return new Sale(this);
		}
	
	}
	

	public int getId() {
		return this.id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public String getEmail() {
		return this.email;
	}

	public String getProduct() {
		return this.product;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public String getRepresentativeUsername() {
		return this.representativeUsername;
	}
	
	public String getCategory() {
		return this.category;
	}

	public Double getPrice() {
		return this.price;
	}

	public Date getDate() {
		return this.date;
	}

}
