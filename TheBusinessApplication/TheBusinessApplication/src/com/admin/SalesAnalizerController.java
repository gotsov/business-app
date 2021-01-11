package com.admin;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;

import com.businessapplication.Product;
import com.businessapplication.Sale;
import com.databaseconnection.DBConnection;

public class SalesAnalizerController {
	public HashMap<String, Integer> categoriesHash = new HashMap<>();

	public ArrayList<Sale> filterSalesByDate(String startDate, String endDate, ArrayList<Sale> listAllSales)
	{	
		ArrayList<Sale> filteredByDateList = new ArrayList<>();
		
		try {					
			java.util.Date utilStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);			
			java.util.Date utilEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);			
	        Date dateToCompare;
	        	
			for(Sale sale : listAllSales)
			{
				dateToCompare = sale.getDate();
						
				if(dateToCompare.compareTo(utilStartDate) >= 0 && dateToCompare.compareTo(utilEndDate) <= 0) {
					filteredByDateList.add(sale);    
				}
			}
		} catch ( ParseException e) {
			e.printStackTrace();
		}
		
		return filteredByDateList;
	}

	public ArrayList<Sale> filterSalesByCriteria(ArrayList<Sale> listAllSales, String criteria, String variable)
	{
		ArrayList<Sale> filteredList = new ArrayList<>();
		
		if(criteria.equals("username")) {
			for(Sale sale : listAllSales)
			{
				if(sale.getRepresentativeUsername().equals(variable)) {
	        		filteredList.add(sale);
	        	}
			}
		}
		else if(criteria.equals("category")) {
			System.out.println("equals category");
			for(Sale sale : listAllSales)
			{
				if(sale.getCategory().equals(variable)) {
					System.out.println("added");
	        		filteredList.add(sale);
	        	}
			}
		}
		
		return filteredList;
	}
	
	public HashMap<String, Integer> getHashMapSalesByCategory()
	{
		categoriesHash.clear();
		String mostFrequentCriteria = "";
		int numSales = 0;

		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allsales");
			String result;
			while(rs.next())
			{
				result = rs.getString("category");
				
				if(categoriesHash.containsKey(result)) {
					categoriesHash.put(result, categoriesHash.get(result) + 1);
				}
				else {
					categoriesHash.put(result, 1);
				}
			}
			
			Set<Map.Entry<String, Integer> > set = categoriesHash.entrySet();
			
			
			for(Map.Entry<String, Integer> me : set)
			{
				if(me.getValue() > numSales)	{
					numSales = me.getValue();
					mostFrequentCriteria = me.getKey();
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.categoriesHash;
	}
	
	public String getMostSalesByCriteria(String criteria)
	{
		categoriesHash.clear();
		String mostFrequentCriteria = "";
		int numSales = 0;

		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allsales");
			String result;
			while(rs.next())
			{
				result = rs.getString(criteria);
				
				if(categoriesHash.containsKey(result)) {
					categoriesHash.put(result, categoriesHash.get(result) + 1);
				}
				else {
					categoriesHash.put(result, 1);
				}
			}
			
			Set<Map.Entry<String, Integer> > set = categoriesHash.entrySet();
			
			
			for(Map.Entry<String, Integer> me : set)
			{
				if(me.getValue() > numSales)	{
					numSales = me.getValue();
					mostFrequentCriteria = me.getKey();
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mostFrequentCriteria + " - " + numSales + " sales";
	}
	
	public int getNumberOfClients()
	{
		int numOfClients = 0;
		
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT COUNT(*) from allclients");
			rs.next();
			
			numOfClients = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return numOfClients;
	}
	
	public String getTotalProfit()
	{
		double totalProfit = 0;
		
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * from allsales");
			
			while(rs.next())
			{
				totalProfit += rs.getDouble("price");
			}
			
			String strDouble = String.format("%.2f", totalProfit);
			
			return strDouble;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
	}
	
	public void findCategoriesWithNoRepresentative(DefaultListModel<String> model, ArrayList<String> categoriesWithRepresentative, 
											ArrayList<String> categoriesWithoutRepresentative, ArrayList<Product> listProducts) throws SQLException{
		
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allrepresentatives");
			
			while(rs.next())
			{
				if(!categoriesWithRepresentative.contains(rs.getString("category")) && !rs.getString("category").equals("all"))
				{
					categoriesWithRepresentative.add(rs.getString("category"));
				}
				
			}			
			
			for(Product p : listProducts)
			{
				if(!categoriesWithRepresentative.contains(p.getCategory()) && !categoriesWithoutRepresentative.contains(p.getCategory())) {
					categoriesWithoutRepresentative.add(p.getCategory());
				}
			}
			
			for(String category : categoriesWithoutRepresentative) {
				model.addElement(category);
			}
		}
			
	}
}
