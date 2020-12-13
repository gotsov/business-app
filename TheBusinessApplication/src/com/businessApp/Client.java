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

import javax.swing.text.NumberFormatter;

public class Client {
	static DBConnection cDB = new DBConnection();
	static Connection con = cDB.createConnection();
	static Scanner scan = new Scanner(System.in);
	
	public static int idClient = 100;
	private int id;
	private String name;
	private String email;
	private String boughtCategory;
	private String boughtProduct;

	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	public Client()
	{
		setId();
		setName();
		setEmail();
	}
	
	public int getId() {
		return id;
	}

	public void setId() {
		try {
			Statement stmt = con.createStatement();	
			ResultSet rs = stmt.executeQuery(" SELECT id_client FROM allclients");
					
			while(rs.next())
			{
				idClient = rs.getInt("id_client");
			}
			this.id = ++idClient;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		this.id = idClient;
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
		
		while(true)
		{
			System.out.println("choose id: ");
			ch = scan.nextInt();
			if(ch > 0 && ch <= Product.getIdProductOfLast())
			{
				break;
			}
			else
			{
				System.out.println("Invalid id");
			}
		}
		
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM allproducts WHERE id_prod = " + ch);
			int q = 0;
			int newQ;

			String categoryProduct = null;
			double priceProduct = 0;
			
			while(rs.next())
			{
				q = rs.getInt("quantity");
				boughtCategory = rs.getString("category");
				boughtProduct = rs.getString("name");
				priceProduct = rs.getDouble("price");
				
			}
			
			System.out.println("There are [" + q + "] copies of this product available." );
			System.out.println("How much did he buy: ");
			int qToBuy = scan.nextInt();
			
			if(qToBuy > q)
			{
				System.out.println("There are only [" + q + "] copies!");
			}
			else if(qToBuy == q)
			{
				String query = "DELETE FROM allproducts WHERE id_prod = " + ch;
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.execute();
				System.out.println("This client bougth the last copy (product has been deleted).");
			}
			else
			{
				newQ = q - qToBuy;
				String query = "UPDATE allproducts SET quantity = "+ newQ +" WHERE id_prod = "+ ch;
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.execute();
			}
			
			System.out.println("Date of order (yyyy-MM-dd): ");
			String stringDate = scan.next();
			
			java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
			java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
			
			double fullPrice = qToBuy * priceProduct;
			
			NumberFormat formatter = new DecimalFormat("#0.00");
			String formatedFullPrice = formatter.format(fullPrice);
			NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
			Number number = format.parse(formatedFullPrice);
			fullPrice = number.doubleValue();
			
			String query = " INSERT INTO allclients (id_client, name, email, boughtcategory, boughtproduct, quantity, price, date)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, this.id);
			pstmt.setString(2, this.name);
			pstmt.setString(3, this.email);
			pstmt.setString(4, boughtCategory);
			pstmt.setString(5, boughtProduct);
			pstmt.setInt(6, qToBuy);
			pstmt.setDouble(7, fullPrice);
			pstmt.setDate(8, sqlDate);
			pstmt.execute();
			
			//insert into representative/brand table
			query = " INSERT INTO clients"+ boughtCategory +" (id_client, name, email, boughtproduct, quantity, price, date)" + " VALUES (?, ?, ?, ?, ?, ?, ?)";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, this.id);
			pstmt.setString(2, this.name);
			pstmt.setString(3, this.email);
			pstmt.setString(4, boughtProduct);
			pstmt.setInt(5, qToBuy);
			pstmt.setDouble(6, fullPrice);
			pstmt.setDate(7, sqlDate);
			pstmt.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		catch (ParseException e) {
			e.printStackTrace();
		}

	}
}
