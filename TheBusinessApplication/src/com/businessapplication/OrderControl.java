package com.businessapplication;

import java.sql.Connection;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Locale;

import com.businessapplication.Representative.Builder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;


public class OrderControl {
	private int id;
	private String name;
	private String email;
	private String product;
	private int quantity;
	private Double price;
	private Date date;
	
	public OrderControl(Builder builder)
	{
		this.id = builder.id;
		this.name = builder.name;
		this.email = builder.email;
		this.product = builder.product;
		this.quantity = builder.quantity;
		this.price = builder.price;
		this.date =builder.date;
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
		
		public OrderControl build() {
			return new OrderControl(this);
		}
	
	}
	

	public int getId() {
		return id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getProduct() {
		return product;
	}

	public int getQuantity() {
		return quantity;
	}

	public Double getPrice() {
		return price;
	}

	public Date getDate() {
		return date;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OrderControl other = (OrderControl) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}


	public static ArrayList<OrderControl> removeRepeatingClients(ArrayList<OrderControl> catalog) {
		
		try {
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


	@Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + ", email=" + email + "]";
	}
	
	
	
	
	
	
}
