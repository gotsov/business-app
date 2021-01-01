package com.businessapplication;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.businessapplication.Client.Builder;

import java.util.Locale;

public abstract class RepresentativeController {
	
	public static int idRepresentative = 100;

	public abstract void setId();
	
	public abstract void addSale(Client newClient);
	
	public boolean validateEmail(Client clientToVarify)
	{
		return clientToVarify.getEmail().matches("^(.+)@(.+)$");
	}
	
	public static void checkIfFirstTimeClient(Client clientToCheck) 
	{
		try {		
			ResultSet rs = DBConnection.getData("SELECT * FROM allclients WHERE email = '" + clientToCheck.getEmail() + "'");
			
			boolean newEmail = true;
			
			while(rs.next())
			{
				System.out.println("im in checkIfFirstTimeClient");
				newEmail = false;
			}

			if(newEmail)
				clientToCheck.setFirstTimeClient(true);
			else
				clientToCheck.setFirstTimeClient(false);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setIdClient(Client clientToSetId)
	{
		try {		
			ResultSet rs = DBConnection.getData(" SELECT id_client FROM allclients ORDER BY id_client DESC LIMIT 1");
					
			rs.next();
			Client.idClient = rs.getInt("id_client");

			Client.idClient += 1;
			
			if(clientToSetId.isFirstTimeClient()) {
				clientToSetId.setIdOfClient(Client.idClient);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setIdSale(Client clientToSetId)
	{
		try {
			ResultSet rs = DBConnection.getData(" SELECT id_sale FROM allsales ORDER BY id_sale DESC LIMIT 1");
			
			rs.next();
			Client.idSale = rs.getInt("id_sale");
			
			clientToSetId.setIdOfSale(++Client.idSale);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void setSqlDate(Client clientToSetSqlDate)
	{
		try {
			java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(clientToSetSqlDate.getDateOfSale());
			Date sqlDate = new Date(utilDate.getTime());
			
			clientToSetSqlDate.setSqlDateOfSale(sqlDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void editClient(Client clientToEdit, String fieldToEdit, int idClient, int idSale)
	{
		try{     
				if(fieldToEdit.equals("name")) {
					DBConnection.updateData("UPDATE allclients SET " + fieldToEdit + " = '" + clientToEdit.getName() + "'" +"WHERE id_client = " + idClient);
					DBConnection.updateData("UPDATE allsales SET " + fieldToEdit + " = '" +  clientToEdit.getName() + "'" +"WHERE id_sale = " + idSale);
					
					System.out.println("new name set");
				}
				else if(fieldToEdit.equals("email")) {
					
					if(validateEmail(clientToEdit)) {
						DBConnection.updateData("UPDATE allclients SET  " + fieldToEdit + "'" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + idClient);
						DBConnection.updateData("UPDATE allsales SET " + fieldToEdit + "'" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + idSale);
						
						System.out.println("new email set");
					}

				}
				
				else if(fieldToEdit.equals("name & e-mail")) {
					
					if(validateEmail(clientToEdit)) {
						DBConnection.updateData("UPDATE allclients SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_client = " + idClient);
						DBConnection.updateData("UPDATE allsales SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_sale = " + idSale);
						
						System.out.println("new name set");
						
						DBConnection.updateData("UPDATE allclients SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + idClient);
						DBConnection.updateData("UPDATE allsales SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + idSale);

						System.out.println("new email set");
					}
					
				}
      
	    } catch(SQLException e) {
			e.printStackTrace();
		}

	}
	
	public void deleteClient(Client clientToDelete)
	{
		try {		
			DBConnection.updateData("DELETE FROM allclients WHERE id_client = " + clientToDelete.getIdOfClient());
			DBConnection.updateData("DELETE FROM allsales WHERE email = '" + clientToDelete.getEmail() + "'");
			
			System.out.println("client deleted");
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
	}	
		
	public void addClient(Client clientToAdd)
	{
		try {		
			PreparedStatement pstmt = DBConnection.insertData
					(" INSERT INTO allclients (id_client, name, email)" + " VALUES (?, ?, ?)");
			pstmt.setInt(1, clientToAdd.getIdOfClient());
			pstmt.setString(2, clientToAdd.getName());
			pstmt.setString(3, clientToAdd.getEmail());

			pstmt.execute();
			
			System.out.println("client deleted");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}
