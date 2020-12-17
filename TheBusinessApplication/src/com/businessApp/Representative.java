package com.businessApp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale.Category;
import java.util.Scanner;

public class Representative {

	static Scanner scan = new Scanner(System.in);
	
	public static int idRepresentative = 100;
	private int id;
	private String name;
	private String username;
	private String password;
	private String category;
	
	
	public Representative()
	{
		setId();
		setName();
		setUsername();
		setCategory();
		setPassword();
		
//		createRepTableByCategory();
	}
	
	public Representative(String category)
	{
		setId();
		setName();
		setUsername();
		setPassword();
		this.category = category;
		
//		createRepTableByCategory();
	}
	
	public Representative(int id, String name, String password, String category) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName() {
		System.out.println("name: ");
		String name = scan.nextLine();
		this.name = name;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername() {
		System.out.println("username: ");
		String username = scan.nextLine();
		this.username = username;
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory() {
		System.out.println("category: ");
		String category = scan.nextLine();
		this.category = category;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public void setPassword()
	{
		System.out.println("password: ");
		String password = scan.nextLine();
		this.password = password;
	}
	
	public int getId() {
		return id;
	}

	public void setId() {		
		try {	
			ResultSet rs = DBConnection.getData(" SELECT id_rep FROM allrepresentatives");
					
			while(rs.next())
			{
				idRepresentative = rs.getInt("id_rep");
			}
			this.id = ++idRepresentative;
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		this.id = idRepresentative;
	}
	
	public static int getIdRepresentativeOfLast()
	{
		try {
			ResultSet rs = DBConnection.getData(" SELECT id_rep FROM allrepresentatives");
					
			while(rs.next())
			{
				idRepresentative = rs.getInt("id_rep");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return idRepresentative;
	}	
	
//	public static void addOrder()              //asks for client name, client email, id_prod, quantity, date
//	{
//		System.out.println("Enter information for the order...");
//		Client c = new Client();
//		c.buyProduct();
//	}
	
	public void menuRep()
	{
		System.out.println("What do you want to do: ");
		System.out.println("a. Enter information of new sale");
		System.out.println("b. Edit clients");
		System.out.println("c. View catalog(made orders)");
		System.out.println("d. Log out");
		
		System.out.println("choose: ");
		String choice = scan.next();
		
		while(!choice.equals("d"))
		{
			switch (choice) {
			case "a":
//				addOrder();
				break;
			case "b":
				menuEditClient(this.category);
				break;
			case "c":
				viewCatalog(this.category);
				break;
			default:
				System.out.println("Invalid input");
				break;
			}
			
			System.out.println("What do you want to do: ");
			System.out.println("a. Enter information of new sale");
			System.out.println("b. Edit clients");
			System.out.println("c. View catalog(made orders)");
			System.out.println("d. Log out");
			
			System.out.println("choose: ");
			choice = scan.next();
		}
	}
	
	public void menuEditClient(String categoryRep)
	{
		int ch;
		System.out.println("Choose id of client to change: ");
		
		try{
			ResultSet rs = DBConnection.getData("SELECT * FROM clients" + categoryRep);
	        
	        String name, email, product;
	        int id, quantity;
	        double price;
	        Date date;
	        
	        while (rs.next()) {
	        	id = rs.getInt("id_client");
	        	name = rs.getString("name");
	        	email = rs.getString("email");
	        	product = rs.getString("product");
	        	quantity = rs.getInt("quantity");
	        	price = rs.getDouble("price");
	        	date = rs.getDate("date");
	        	
	        	System.out.println(id + "\t" + name + "\t" + email +"\t" + product + "\t" + quantity + "\t" + price + "\t" + date);
		    }
	        
	        while(true)
			{
				System.out.println("choose id: ");
				ch = scan.nextInt();
				if(ch > 0 && ch <= Representative.getIdRepresentativeOfLast())
				{
					break;
				}
				else
				{
					System.out.println("Invalid id");
				}
			}
	        
	        System.out.println("What do you want to change: ");
			System.out.println("a. name");
			System.out.println("b. email");
			System.out.println("choose: ");
			String choice = scan.next();
			
			switch (choice) {
			case "a":
				System.out.println("name: ");
				String newName = scan.next();
				
				DBConnection.updateData("UPDATE allsales SET name = "+ "'" + newName + "'" +"WHERE id_client = "+ ch);
				
				System.out.println("new name set");
				break;
			case "b":
				System.out.println("new email: ");
				String newEmail = scan.next();
			
				DBConnection.updateData("UPDATE allsales SET email = "+ "'" + newEmail + "'" +"WHERE id_client = "+ ch);

				System.out.println("new email set");
				break;
			default:
				System.out.println("Invalid choice");
				break;
			}
			
	        
	    } 
		catch(SQLException e) {
			e.printStackTrace();
		}

	}
	
	
	public void viewCatalog(String categoryRep)
	{
		try
		{
			System.out.println("catalog: ");
			
			ResultSet rs = DBConnection.getData("SELECT * FROM clients" + categoryRep);
	        
	        String name, email, product;
	        int id, quantity;
	        double price;
	        Date date;
	        
	        while (rs.next()) {
	        	id = rs.getInt("id_client");
	        	name = rs.getString("name");
	        	email = rs.getString("email");
	        	product = rs.getString("product");
	        	quantity = rs.getInt("quantity");
	        	price = rs.getDouble("price");
	        	date = rs.getDate("date");
	        	
	        	System.out.println(id + "\t" + name + "\t" + email +"\t" + product + "\t" + quantity + "\t" + price + "\t" + date);
		    }
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
		
	}
}
