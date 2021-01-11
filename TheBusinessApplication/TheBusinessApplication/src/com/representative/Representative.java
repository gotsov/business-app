package com.representative;

import java.sql.Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.databaseconnection.DBConnection;

public class Representative {
	
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
	
	
	public void setId() {		
		try(Connection con = DBConnection.getCon()) {	
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData(" SELECT id_rep FROM allrepresentatives ORDER BY id_rep DESC LIMIT 1");
					
			rs.next();
			id = rs.getInt("id_rep");

			this.id = id + 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
	}
	
	public int getNumberOfSalesFromDB()
	{
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allrepresentatives WHERE username = '" + username + "'");
			rs.next();
			
			return rs.getInt("numberofsales");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public double getProfitFromDB()
	{
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allrepresentatives WHERE username = '" + username + "'");
			rs.next();
			
			return rs.getDouble("profit");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
}
