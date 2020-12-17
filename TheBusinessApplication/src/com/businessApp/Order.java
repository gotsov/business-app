package com.businessApp;

import java.sql.Connection;

import java.sql.Date;
import java.util.Locale;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;


public class Order {
	private int id;
	private String name;
	private String email;
	private String product;
	private int quantity;
	private Double price;
	private Date date;
	
	public Order(int id, String name, String email, String product, int quantity, Double price, Date date) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.product = product;
		this.quantity = quantity;
		this.price = price;
		this.date = date;
	}
	

	public int getId() {
		return id;
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
	
}
