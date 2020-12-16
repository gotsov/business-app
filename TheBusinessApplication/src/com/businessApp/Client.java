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
	static DBConnection cDB = new DBConnection();
	static Connection con = cDB.createConnection();
	static Scanner scan = new Scanner(System.in);
	
	public static int idSale = 100;
	public static int idClient = 100;
	private int idOfSale;
	private int idOfClient;
	private String name;
	private String email;
	private String category;
	private String product;
	private int quantityBought;
	private java.sql.Date sqlDate;

	private boolean firstTimeClient;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	public Client()
	{
		setIdSale();
		setName();
		setEmail();
		checkForUniqueEmail();
		setIdClient();
	}
	
	public Client(String name, String email, String product, int quantity, String stringDate) {
		setIdSale();
		this.name = name;
		this.email = email;
		checkForUniqueEmail();
		this.product = product;
		this.quantityBought = quantity;
		setIdClient();

		try {
			java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			
			this.sqlDate = sqlDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void checkForUniqueEmail()
	{
		try {
			Statement stmt = con.createStatement();	
			ResultSet rs = stmt.executeQuery(" SELECT * FROM allclients WHERE email = '" + this.email + "'");
			int count = 0;
			
			while(rs.next())
			{
				count++;
			}

			if(count == 0)
				firstTimeClient = true;
			else
				firstTimeClient = false;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
			Statement stmt = con.createStatement();	
			ResultSet rs = stmt.executeQuery(" SELECT id_sale FROM allsales");
					
			while(rs.next())
			{
				idSale = rs.getInt("id_sale");
			}
			this.idOfSale = ++idSale;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		//this.id = idSale;
	}
	
	public void setIdClient() {
		try {
			Statement stmt = con.createStatement();	
			ResultSet rs = stmt.executeQuery(" SELECT id_client FROM allclients");
					
			while(rs.next())
			{
				idClient = rs.getInt("id_client");
			}
			idClient += 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if(firstTimeClient)
		{
			this.idOfClient = idClient;
		}

	}
	
	public String getName() {
		return name;
	}

	public void setName() {
		System.out.println("name: ");
		String name = scan.nextLine();
		this.name = name;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public void setEmail()
	{
		System.out.println("email: ");
		String email = scan.nextLine();
		this.email = email;
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
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM allproducts");
	        System.out.println("[id, name, category, price, quantity]");
	         
	        while (rs.next()) {
	           int id = rs.getInt("id_prod");
	           String name = rs.getString("name");
	           String category = rs.getString("category");
	           double price = rs.getDouble("price");
	           int quantity = rs.getInt("quantity");
	           System.out.println(id + "\t" + name + "\t" + category + "\t" + price + "\t" + quantity);
	        }
	      } 
		catch(SQLException e){
			e.printStackTrace();
		}
		
//		while(true)
//		{
//			System.out.println("choose id: ");
//			ch = scan.nextInt();
//			if(ch > 0 && ch <= Product.getIdProductOfLast())
//			{
//				break;
//			}
//			else
//			{
//				System.out.println("Invalid id");
//			}
//		}
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM allproducts WHERE name = '" + product + "'");
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
			
//			System.out.println("There are [" + q + "] copies of this product available." );
//			System.out.println("How much did he buy: ");
//			int qToBuy = scan.nextInt();
			
//			if(qToBuy > q)
//			{
//				System.out.println("There are only [" + q + "] copies!");
//			}
			
			
//			System.out.println("Date of order (yyyy-MM-dd): ");
//			String stringDate = scan.next();
			
//			java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
//			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			
			double fullPrice = quantityBought * priceProduct;
			
			NumberFormat formatter = new DecimalFormat("#0.00");
			String formatedFullPrice = formatter.format(fullPrice);
			NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
			Number number = format.parse(formatedFullPrice);
			fullPrice = number.doubleValue();
			
			boolean isValid = validateOrder();
			
			if(isValid)
			{
				if(quantityBought == q)
				{
					String query = "DELETE FROM allproducts WHERE name = '" + product + "'";
					PreparedStatement pstmt = con.prepareStatement(query);
					pstmt.execute();
					System.out.println("This client bougth the last copy (product has been deleted).");
				}
				else
				{
					newQ = q - quantityBought;
					String query = "UPDATE allproducts SET quantity = "+ newQ +" WHERE name = '"+ product + "'";
					PreparedStatement pstmt = con.prepareStatement(query);
					pstmt.execute();
				}
				
				String query = " INSERT INTO allsales (id_sale, name, email, category, product, quantity, price, date)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.setInt(1, this.idOfSale);
				pstmt.setString(2, this.name);
				pstmt.setString(3, this.email);
				pstmt.setString(4, category);
				pstmt.setString(5, product);
				pstmt.setInt(6, quantityBought);
				pstmt.setDouble(7, fullPrice);
				pstmt.setDate(8, sqlDate);
				pstmt.execute();
				
				if(firstTimeClient)
				{
					query = " INSERT INTO allclients (id_client, name, email)" + " VALUES (?, ?, ?)";
					pstmt = con.prepareStatement(query);
					pstmt.setInt(1, this.idOfClient);
					pstmt.setString(2, this.name);
					pstmt.setString(3, this.email);

					pstmt.execute();
				}
				
				
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		catch (ParseException e) {
			e.printStackTrace();
		}

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
		Client other = (Client) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
	
	
}
