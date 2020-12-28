package com.businessapplication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import com.businessapplication.OrderControl.Builder;

public class Product {
	
	public static int idProduct = 100;
	protected int id;
	protected String name;
	protected String category;
	protected int quantity;
	protected double price;
	
	public Product(Builder builder)
	{
		this.id = builder.id;
		this.name = builder.name;
		this.category = builder.category;
		this.quantity = builder.quantity;
		this.price = builder.price;
		
		if(this.id == 0)
		{
			setId();
		}
		
	}
	
	public Product(int id)
	{
		this.id = id;
	}
	
	public static class Builder
	{
		private int id;
		private String name;
		private String category;
		private int quantity;
		private Double price;
		
		public Builder id(int id) {
			this.id = id;
			return this;
		}
		
		public Builder name(String name) {
			this.name = name;
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
		
		public Product build() {
			return new Product(this);
		}
	
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public int getId() {
		return id;
	}
	
//	public static int getIdProductOfLast()
//	{
//		try {		
//			ResultSet rs = DBConnection.getData(" SELECT id_prod FROM allproducts");
//			while(rs.next())
//			{
//				idProduct = rs.getInt("id_prod");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		return idProduct;
//	}

	public void setId() {		
		try {
			ResultSet rs = DBConnection.getData(" SELECT id_prod FROM allproducts ORDER BY id_prod DESC LIMIT 1");
					
			rs.next();

			idProduct = rs.getInt("id_prod");

			this.id = ++idProduct;
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		this.id = idProduct;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
}
