package com.businessApp;

import java.sql.Connection;
import java.util.Date;
import java.util.Locale;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;


public class Order {
	static DBConnection cDB = new DBConnection();
	static Connection con = cDB.createConnection();
	
	private int id;
	private String name;
	private String email;
	private String product;
	private int quantity;
	private Double price;
	private java.sql.Date date;
	
	public Order(int id, String name, String email, String product, int quantity, Double price, java.sql.Date date) {
		super();
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

	@Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + ", email=" + email + ", product=" + product
				+ ", quantity=" + quantity + ", price=" + price + ", date=" + date + "]";
	}
	
	
}
