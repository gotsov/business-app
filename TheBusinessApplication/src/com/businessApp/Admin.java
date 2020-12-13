package com.businessApp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Admin {
	
	static DBConnection cDB = new DBConnection();
	static Connection con = cDB.createConnection();
	static Scanner scan = new Scanner(System.in);
	
	public Admin()
	{
		
	}
	
	public static void menu()
	{
		Scanner scan = new Scanner(System.in);
		System.out.println("What do you want to work with: ");
		System.out.println("a. Products");
		System.out.println("b. Representatives");
		System.out.println("c. See all sales");
		System.out.println("d. See all sales in a period of time");
		System.out.println("e. See sales by a representative");	
		System.out.println("f. Log out");
		
		System.out.println("choose: ");
		String choice = scan.next();
		
		while(!choice.equals("f"))
		{
			switch (choice) {
			case "a":
				menuProducts();
				break;
			case "b":
				menuRepresentatives();
				break;
			case "c":
				System.out.println("List of all sales: ");
				
				try{
					Statement stmt = con.createStatement();
			        ResultSet rs = stmt.executeQuery("SELECT * FROM allclients");
			        System.out.println("[id, name, email, bought category, bought product, quantity, price, date]");
			         
			        while (rs.next()) {
			           int id = rs.getInt("id_client");
			           String name = rs.getString("name");
			           String email = rs.getString("email");
			           String boughtCategory = rs.getString("boughtcategory");
			           String boughtProduct = rs.getString("boughtproduct");
			           int quantity = rs.getInt("quantity");
			           Double price = rs.getDouble("price");
			           String date = rs.getString("date");
			           System.out.println(id + "\t" + name + "\t" + email + "\t" + boughtCategory +"\t" + boughtProduct + "\t" + quantity + "\t" + price + "\t" + date);
			        }
			      } 
				catch(SQLException e){
					e.printStackTrace();
				}
				break;
			case "d":
				Date startDate, endDate;
				
				try {
					System.out.println("Enter start of period: (yyyy-MM-dd)");
					String stringDate = scan.next();
					
					java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
					java.sql.Date sqlStartDate = new java.sql.Date(utilDate.getTime());
					
					System.out.println("Enter start of period: (yyyy-MM-dd)");
					stringDate = scan.next();
					
					utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(stringDate);
					java.sql.Date sqlEndDate = new java.sql.Date(utilDate.getTime());
					
					getSalesInAPeriodOfTime(sqlStartDate, sqlEndDate);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				
				
				break;
			case "e":
				//printSoldItemsByRep();
				System.out.println("Choose a representative to see their sales: ");
				int ch;
				
				try{
					Statement stmt = con.createStatement();
			        ResultSet rs = stmt.executeQuery("SELECT * FROM allrepresentatives");
			        System.out.println("[id, name, category]");
			         
			        String name, category = null, email, boughtProduct;
			        int id, quantity;
			        double price;
			        Date date;
			        while (rs.next()) {
			           id = rs.getInt("id_rep");
			           name = rs.getString("name");
			           category = rs.getString("category");
			           System.out.println(id + "\t" + name + "\t" + category);
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
					

					stmt = con.createStatement();
					rs = stmt.executeQuery("SELECT * FROM allrepresentatives WHERE id_rep = " + ch);
						
					while (rs.next()) {
						category = rs.getString("category");
					}
						
					rs = stmt.executeQuery("SELECT * FROM clients" + category);
				    System.out.println("[id, name, email, bought product, quantity, price, date]");
				        
				    while (rs.next()) {
				    	id = rs.getInt("id_client");
				        name = rs.getString("name");
				        email = rs.getString("email");
				        boughtProduct = rs.getString("boughtproduct");
				        quantity = rs.getInt("quantity");
				        price = rs.getDouble("price");
				        date = rs.getDate("date");
				        System.out.println(id + "\t" + name + "\t" + email +"\t" + boughtProduct + "\t" + quantity + "\t" + price + "\t" + date);
				    }
			    } 
				catch(SQLException e){
					e.printStackTrace();
				}	
				break;
			default:
				System.out.println("Invalid input");
				break;
			}
			
			System.out.println("What do you want to work with: ");
			System.out.println("a. Products");
			System.out.println("b. Representatives");
			System.out.println("c. See all sales");
			System.out.println("d. See all sales in a period of time");
			System.out.println("e. See sales by a representative");	
			System.out.println("f. Log out");
			
			System.out.println("choose: ");
			choice = scan.next();
		}

	}
		
	public static void getSalesInAPeriodOfTime(java.sql.Date startDate, java.sql.Date endDate)
	{
		try {
			
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM allclients");
	        
	        java.sql.Date dateToCompare;
	        
	        String name, email, boughtProduct;
	        int id, quantity;
	        double price;
	        
	        while(rs.next())
	        {
	        	id = rs.getInt("id_client");
	        	name = rs.getString("name");
	        	email = rs.getString("email");
	        	boughtProduct = rs.getString("boughtproduct");
	        	quantity = rs.getInt("quantity");
	        	price = rs.getDouble("price");
	        	dateToCompare = rs.getDate("date");
	        	
	        	if(dateToCompare.compareTo(startDate) >= 0 && dateToCompare.compareTo(endDate) <= 0)
	        	{
	        		System.out.println(id + "\t" + name + "\t" + email +"\t" + boughtProduct + "\t" + quantity + "\t" + price + "\t" + dateToCompare);
	        	}
	        }
     
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void menuProducts() {

		System.out.println("a. Enter new product");
		System.out.println("b. Edit product");
		System.out.println("c. Delete product");
		System.out.println("d. Exit");
		
		String choice = scan.next();
		int ch, flag;
		
		while(!choice.equals("d"))
		{
			switch (choice) {
			case "a":
				System.out.println("Enter product...");
				flag = 0;
				Product p = new Product();
		
				try {
					
					Statement stmt = con.createStatement();
			        ResultSet rs = stmt.executeQuery("SELECT * FROM allrepresentatives");
			        String categoryRep;
			        while(rs.next())
			        {
			        	categoryRep = rs.getString("category");
			        	
			        	if(p.getCategory().equals(categoryRep))
			        	{
			        		flag = 1;
			        		break;
			        	}
			        	
			        }
			        
			        if(flag == 0)
					{
						System.out.println("There is NO representative with this category");
						System.out.println("We suggest you add representative for this category");
							
						createRepresentativeByCategory(p.getCategory());
					}
			        
			        String query = " INSERT INTO allproducts (id_prod, name, category, price, quantity)" + " VALUES (?, ?, ?, ?, ?)";
					PreparedStatement pstmt = con.prepareStatement(query);
						
					pstmt.setInt(1, p.getId());
					pstmt.setString(2, p.getName());
					pstmt.setString(3, p.getCategory());
					pstmt.setDouble(4, p.getPrice());
					pstmt.setInt(5, p.getQuantity());
						
					pstmt.execute();
			        
				} catch (SQLException e) {
					e.printStackTrace();
				}	

				System.out.println("product added");		
				break;
				
			case "b":
				System.out.println("Choose id of product to change: ");		
				
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
				
				System.out.println("What do you want to change: ");
				System.out.println("a. name");
				System.out.println("b. price");
				System.out.println("c. quantity to add");
				System.out.println("d. quantity to remove");
				System.out.println("choose: ");
				choice = scan.next();
				
				switch (choice) {
				case "a":
					System.out.println("name: ");
					String newName = scan.next();

					try {
						Statement stmt = con.createStatement();
						stmt.execute("SELECT * FROM allproducts");
						String query = "UPDATE allproducts SET name = "+ "'"+newName+"'" +"WHERE id_prod = "+ ch;

						PreparedStatement pstmt = con.prepareStatement(query);

						pstmt.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					System.out.println("new name set");
					break;
				case "b":
					System.out.println("price: ");
					double newPrice = scan.nextDouble();
					
					try {
						Statement stmt = con.createStatement();
						stmt.execute("SELECT * FROM allproducts");
						String query = "UPDATE allproducts SET price = "+ newPrice +"WHERE id_prod = "+ ch;

						PreparedStatement pstmt = con.prepareStatement(query);

						pstmt.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.out.println("new price set");
					break;
				case "c":
					Product.quantityToAdd(ch);
					break;
				case "d":
					Product.quantityToRemove(ch);
					break;
				default:
					System.out.println("Invalid choice");
					break;
				}
				break;
			case "c":
				System.out.println("Choose id of product to delete: ");
				
				try{
					Statement stmt = con.createStatement();
			        ResultSet rs = stmt.executeQuery("SELECT * FROM allproducts");
			        System.out.println("[id, name, category, price, quantity]");
			         
			        while (rs.next()) 
			        {
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
				
				try
				{
					String query = "DELETE FROM allproducts WHERE id_prod = " + ch;
					PreparedStatement pstmt = con.prepareStatement(query);
					pstmt.execute();
					System.out.println("Product has been deleted");
				}catch(SQLException e)
				{
					e.printStackTrace();
				}
					
				System.out.println("Product deleted");				
				break;
			default:
				System.out.println("Invalid input");
				break;
			}
			
			System.out.println("a. Enter new product");
			System.out.println("b. Edit product");
			System.out.println("c. Delete product");
			System.out.println("d. Exit");
			choice = scan.next();
			
		}
	}

	public static void menuRepresentatives()
	{	
		System.out.println("What do you want to do: ");
		System.out.println("a. Enter representative");
		System.out.println("b. Edit representative");
		System.out.println("c. Delete representative");
		System.out.println("d. Exit");
		
		System.out.println("choose: ");
		String choice = scan.next();
		int ch;
		
		while(!choice.equals("d"))
		{
			switch (choice) {
			case "a":			
				createRepresentative();
				break;
			case "b":
				System.out.println("Choose id of representative to change: ");
				
				try{
					Statement stmt = con.createStatement();
			        ResultSet rs = stmt.executeQuery("SELECT * FROM allrepresentatives");
			        System.out.println("[id, name, brand]");
			         
			        while (rs.next()) {
			           int id = rs.getInt("id_rep");
			           String name = rs.getString("name");
			           String password = rs.getString("password");
			           String category = rs.getString("category");
			           System.out.println(id + "\t" + name + "\t" + password +"\t" + category);
			        }
			    } 
				catch(SQLException e) {
					e.printStackTrace();
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
				System.out.println("b. password");
				System.out.println("c. category");
				System.out.println("choose: ");
				choice = scan.next();
				
				switch (choice) {
				case "a":
					System.out.println("name: ");
					String newName = scan.next();

					try {
						Statement stmt = con.createStatement();
						stmt.execute("SELECT * FROM allrepresentatives");
						String query = "UPDATE allrepresentatives SET name = "+ "'" + newName + "'" +"WHERE id_rep = "+ ch;

						PreparedStatement pstmt = con.prepareStatement(query);

						pstmt.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					System.out.println("new name set");
					break;
				case "b":
					System.out.println("new password: ");
					String newPass = scan.next();

					try {
						Statement stmt = con.createStatement();
						stmt.execute("SELECT * FROM allrepresentatives");
						String query = "UPDATE allrepresentatives SET password = "+ "'" + newPass + "'" +"WHERE id_rep = "+ ch;

						PreparedStatement pstmt = con.prepareStatement(query);

						pstmt.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					System.out.println("new password set");
					break;
				case "c":
					System.out.println("category: ");
					String newCategory = scan.next();

					try {
						Statement stmt = con.createStatement();
						stmt.execute("SELECT * FROM allrepresentatives");
						String query = "UPDATE allrepresentatives SET category = "+ "'" + newCategory + "'" +"WHERE id_rep = "+ ch;

						PreparedStatement pstmt = con.prepareStatement(query);

						pstmt.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					System.out.println("new category set");
					break;
				default:
					System.out.println("Invalid choice");
					break;
				}
					
				break;
			case "c":
				System.out.println("Choose id of representative to delete: ");

				try{
					Statement stmt = con.createStatement();
			        ResultSet rs = stmt.executeQuery("SELECT * FROM allrepresentatives");
			        System.out.println("[id, name, category]");
			         
			        while (rs.next()) {
			           int id = rs.getInt("id_rep");
			           String name = rs.getString("name");
			           String category = rs.getString("category");
			           System.out.println(id + "\t" + name + "\t" + category);
			        }
			    } 
				catch(SQLException e){
					e.printStackTrace();
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
				
				try
				{
					String query = "DELETE FROM allrepresentatives WHERE id_rep = " + ch;
					PreparedStatement pstmt = con.prepareStatement(query);
					pstmt.execute();		
				}catch(SQLException e)
				{
					e.printStackTrace();
				}
				
				System.out.println("Product has been deleted");
				
				break;
			default:
				System.out.println("Invalid input");
				break;
			}
			
			System.out.println("What do you want to do: ");
			System.out.println("a. Enter representative");
			System.out.println("b. Edit representative");
			System.out.println("c. Delete representative");
			System.out.println("d. Exit");
			
			System.out.println("choose: ");
			choice = scan.next();
		}
		
	}
	
	public static void createRepresentative()
	{
		Representative r = new Representative();			
		try {
			String query = " INSERT INTO allrepresentatives (id_rep, name, password, category)" + " VALUES (?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(query);
						
			pstmt.setInt(1, r.getId());
			pstmt.setString(2, r.getName());
			pstmt.setString(3, r.getPassword());		
			pstmt.setString(4, r.getCategory());
					
			pstmt.execute();
		} catch (SQLException e) {
				
			e.printStackTrace();
		}

	}
	
	public static void createRepresentativeByCategory(String brand)
	{
		Representative r = new Representative(brand);		

		try {
			String query = " INSERT INTO allrepresentatives (id_rep, name, password, category)" + " VALUES (?, ?, ?, ?)";
			PreparedStatement pstmt = con.prepareStatement(query);

					
			pstmt.setInt(1, r.getId());
			pstmt.setString(2, r.getName());
			pstmt.setString(3, r.getPassword());
			pstmt.setString(4, r.getCategory());
					
			pstmt.execute();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	
}

