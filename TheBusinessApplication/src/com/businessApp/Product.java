package com.businessApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Product {
	static DBConnection cDB = new DBConnection();
	static Connection con = cDB.createConnection();
	static Scanner scan = new Scanner(System.in);
	
	public static int idProduct = 100;
	protected int id;
	protected String name;
	protected String category;
	protected int quantity;
	protected double price;
	
	public Product()
	{
		setId();
		setName();
		setCategory();
		setPrice();
		setQuantity();
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory() {
		System.out.println("category: ");
		String category = scan.nextLine();
		this.category = category;
	}
	
	public int getId() {
		return id;
	}
	
	public static int getIdProductOfLast()
	{
		try {
			Statement stmt = con.createStatement();	
			ResultSet rs = stmt.executeQuery(" SELECT id_prod FROM allproducts");
					
			while(rs.next())
			{
				idProduct = rs.getInt("id_prod");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return idProduct;
	}

	public void setId() {		
		try {
			Statement stmt = con.createStatement();	
			ResultSet rs = stmt.executeQuery(" SELECT id_prod FROM allproducts");
					
			while(rs.next())
			{
				idProduct = rs.getInt("id_prod");
			}
			this.id = ++idProduct;
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
		this.id = idProduct;
	}
	
	public String getName() {
		return name;
	}

	public void setName() {
		System.out.println("name: ");
		String name = scan.nextLine();
		this.name = name;
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity() {
		System.out.println("quantity: ");
		
		try {
			int quantity = scan.nextInt();
			
			while(true)
			{
				if(quantity > 0)
				{
					this.quantity = quantity;
					break;
				}			
				else
				{
					System.out.println("quantity(over 0): ");
					quantity = scan.nextInt();
				}
					
			}
		}
		catch(InputMismatchException e){
			System.out.println("Invalid quantity");
			setQuantity();
		}
	}
	
	public double getPrice() {
		return price;
	}

	public void setPrice() {
		System.out.println("price: ");
		try {
			double price = scan.nextDouble();
			this.price = price;
		}
		catch(InputMismatchException e)
		{
			System.out.println("Incorrect imput");
			setPrice();
		}
		
	}
	
	public static void quantityToAdd(int ch)
	{
		try
		{
			System.out.println("how much to add: ");
			int q = scan.nextInt();

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT quantity FROM allproducts WHERE id_prod = " + ch);
			int oldQ = 0;
			
			while(rs.next())
			{
				oldQ = rs.getInt("quantity");
			}
			
			int newQ = oldQ + q;
			
			String query = "UPDATE allproducts SET quantity = "+ newQ +" WHERE id_prod = " + ch;
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.execute();
			System.out.println("Quantity changed to " + newQ);
		}
		catch(InputMismatchException e){
			System.out.println("Invalid  quantity");
			quantityToAdd(ch);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void quantityToRemove(int ch)
	{
		Scanner scan = new Scanner(System.in);
		try
		{
			System.out.println("how much to remove: ");
			int q = scan.nextInt();

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT quantity FROM allproducts WHERE id_prod = " + ch);
			int oldQ = 0;
			
			while(rs.next())
			{
				oldQ = rs.getInt("quantity");
			}
			
			int newQ = oldQ - q;
			
			if(newQ <= 0)
			{
				String query = "DELETE FROM allproducts WHERE id_prod = " + ch;
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.execute();
				System.out.println("Product deleted. (quantity <= 0)");
			}
			else
			{
				String query = "UPDATE allproducts SET quantity = "+ newQ +" WHERE id_prod = " + ch;
				PreparedStatement pstmt = con.prepareStatement(query);
				pstmt.execute();
				System.out.println("Quantity changed to " + newQ);
			}
			
		}
		catch (InputMismatchException e){
			System.out.println("Invalid  quantity");
			quantityToRemove(ch);
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setQuantityPlus(int quantity)
	{
		int newQ = this.quantity + quantity;
		this.quantity = newQ;
	}
	
	public void setQuantityMinus(int quantity)
	{
		int newQ = this.quantity - quantity;
		this.quantity = newQ;
		
		if(this.quantity < 0)
		{
			this.quantity = 0;
		}
	}
	
	public String toString() {
		return id + " " + name + " " + category + " " + price + " " + quantity;
	}
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Product other = (Product) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
}
