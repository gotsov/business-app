package com.businessApp;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class App {

	public static Connection con;
	static Scanner scan = new Scanner(System.in);
	
	public static void main(String[] args) {
		
		DBConnection cDB = new DBConnection();
		con = cDB.createConnection();
		
		
		
		while(true) {
			System.out.println("Continue as:");
			System.out.println("a. Admin");
			System.out.println("b. Representative");
			System.out.println("choose: ");
			
			String choice = scan.next();
			
			switch (choice) {
			case "a":
				System.out.println("Logged in as Admin");
				Admin.menu();
				break;
			case "b":
				//Representative r = Admin.chooseRepresentative();
				//System.out.println("Logged in as: " + r.toString());      //uses arrayList version
				//r.menuRep();
				
				Representative r = chooseRepresentative();
				r.menuRep();
				break;
			default:
				System.out.println("Invalid input");
				break;		
			}
		
		}
	}
	
	public static Representative chooseRepresentative()
	{
		int ch;
		System.out.println("Choose id of representative to login as (for testing): ");
		
		try{
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM allrepresentatives");
	        System.out.println("[id, name, brand]");
	        
	        String name = "";
	        String password = "";
	        String category = "";
	        int id = 0;
	        
	        while (rs.next()) {
	           id = rs.getInt("id_rep");
	           name = rs.getString("name");
	           password = rs.getString("password");
	           category = rs.getString("category");
	           System.out.println(id + "\t" + name + "\t" + password +"\t" + category);
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
			
			rs = stmt.executeQuery("SELECT * FROM allrepresentatives WHERE id_rep = " + ch);
			while(rs.next())
			{
				id = rs.getInt("id_rep");
				name = rs.getString("name");
		        password = rs.getString("password");
		        category = rs.getString("category");
			}
			Representative r = new Representative(id, name, password, category);
			return r;
	    } 
		catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
