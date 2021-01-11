package com.representative;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.businessapplication.Client;
import com.businessapplication.Sale;
import com.databaseconnection.DBConnection;

public class ClientService {
	public boolean validateEmail(Client clientToVarify)
	{
		return clientToVarify.getEmail().matches("^(.+)@(.+)$");
	}
	
	public void checkIfFirstTimeClient(Client clientToCheck) throws SQLException 
	{
		try (Connection con = DBConnection.getCon()){		
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allclients WHERE email = '" + clientToCheck.getEmail() + "'");
			
			boolean newEmail = true;
			
			while(rs.next())
			{
				newEmail = false;
			}

			if(newEmail)
				clientToCheck.setFirstTimeClient(true);
			else
				clientToCheck.setFirstTimeClient(false);
			
		} 
		
	}
	
	
	public void setIdClient(Client clientToSetId) throws SQLException
	{
		try (Connection con = DBConnection.getCon()){	
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData(" SELECT id_client FROM allclients ORDER BY id_client DESC LIMIT 1");
					
			rs.next();
			Client.idClient = rs.getInt("id_client");

			Client.idClient += 1;
			
			if(clientToSetId.isFirstTimeClient()) {
				clientToSetId.setIdOfClient(Client.idClient);
			}
			
		} 
	}
	
	public void editClient(Client clientToEdit, String fieldToEdit, int idClient, int idSaleByClient) throws SQLException
	{
		try(Connection con = DBConnection.getCon()) {     
			DBConnection dbConnection = new DBConnection();
			
				if(fieldToEdit.equals("name")) {
					dbConnection.updateData("UPDATE allclients SET " + fieldToEdit + " = '" + clientToEdit.getName() + "'" +"WHERE id_client = " + idClient);
					dbConnection.updateData("UPDATE allsales SET " + fieldToEdit + " = '" +  clientToEdit.getName() + "'" +"WHERE id_sale = " + idSaleByClient);
					
					System.out.println("new name set");
				}
				else if(fieldToEdit.equals("email")) {
					
					if(validateEmail(clientToEdit)) {
						dbConnection.updateData("UPDATE allclients SET  " + fieldToEdit + " = '" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + idClient);
						dbConnection.updateData("UPDATE allsales SET " + fieldToEdit + " = '" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + idSaleByClient);
						
						System.out.println("new email set");
					}

				}
				
				else if(fieldToEdit.equals("name & e-mail")) {
					
					if(validateEmail(clientToEdit)) {
						dbConnection.updateData("UPDATE allclients SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_client = " + idClient);
						dbConnection.updateData("UPDATE allsales SET name = "+ "'" + clientToEdit.getName() + "'" +"WHERE id_sale = " + idSaleByClient);
						
						System.out.println("new name set");
						
						dbConnection.updateData("UPDATE allclients SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_client = " + idClient);
						dbConnection.updateData("UPDATE allsales SET email = "+ "'" + clientToEdit.getEmail() + "'" +"WHERE id_sale = " + idSaleByClient);

						System.out.println("new email set");
					}
					
				}
      
	    } 

	}
	
	public ArrayList<Sale> removeRepeatingClients(ArrayList<Sale> catalog) throws SQLException {
		ArrayList<Sale> clients = new ArrayList<>();
		
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			for (int i = 0; i < catalog.size(); i++) 
			{
				for (int j = i+1; j < catalog.size(); j++) {
					
					if(catalog.get(i).equals(catalog.get(j))) {
						catalog.remove(j);
					}
				}
				
				ResultSet rs = dbConnection.getData("SELECT * FROM allclients WHERE email = '" + catalog.get(i).getEmail() + "'");	
				rs.next();
				catalog.get(i).setId(rs.getInt("id_client"));
			
			}
			
		}
		return catalog;

	}
}
