package com.businessapplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Product {
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
			ResultSet rs = DBConnection.getData(" SELECT id_prod FROM allproducts");
			while(rs.next())
			{
				idProduct = rs.getInt("id_prod");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return idProduct;
	}

	public void setId() {		
		try {
			ResultSet rs = DBConnection.getData(" SELECT id_prod FROM allproducts");
					
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
			
			ResultSet rs = DBConnection.getData("SELECT quantity FROM allproducts WHERE id_prod = " + ch);
			
			int oldQ = 0;
			
			while(rs.next())
			{
				oldQ = rs.getInt("quantity");
			}
			
			int newQ = oldQ + q;

			DBConnection.updateData("UPDATE allproducts SET quantity = "+ newQ +" WHERE id_prod = " + ch);
			
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
			
			ResultSet rs = DBConnection.getData("SELECT quantity FROM allproducts WHERE id_prod = " + ch);
			
			int oldQ = 0;
			
			while(rs.next())
			{
				oldQ = rs.getInt("quantity");
			}
			
			int newQ = oldQ - q;
			
			if(newQ <= 0)
			{

				DBConnection.updateData("DELETE FROM allproducts WHERE id_prod = " + ch);
				
				System.out.println("Product deleted. (quantity <= 0)");
			}
			else
			{
				
				DBConnection.updateData("UPDATE allproducts SET quantity = "+ newQ +" WHERE id_prod = " + ch);
				
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
	
}
