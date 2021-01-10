package com.businessapplication;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.rolecontrollers.AdminController;
 
public class PieChart extends ApplicationFrame {
   
   public PieChart(String title) {
	   super(title);
	   this.setSize( 777 , 530 );    
	   RefineryUtilities.centerFrameOnScreen(this);    
	   this.setVisible( true ); 
	   
	   AdminController adminController = new AdminController();
	   HashMap<String, Integer> mostSalesByCategory = adminController.getHashMapSalesByCategory();
	   
	   setContentPane(createDemoPanel(mostSalesByCategory));
   }
   
   private static PieDataset createDataset(HashMap<String, Integer> mostSalesByCategory) {
      DefaultPieDataset dataset = new DefaultPieDataset( );
//      dataset.setValue( "IPhone 5s" , new Double( 20 ) );  
//      dataset.setValue( "SamSung Grand" , new Double( 20 ) );   
//      dataset.setValue( "MotoG" , new Double( 40 ) );    
//      dataset.setValue( "Nokia Lumia" , new Double( 80 ) );  
      
      for(Map.Entry<String, Integer> entry : mostSalesByCategory.entrySet()) {
    	  dataset.setValue(entry.getKey(), entry.getValue()); 
      }
      
      return dataset;         
   }
   
   private static JFreeChart createChart(PieDataset dataset) {
      JFreeChart chart = ChartFactory.createPieChart(      
         "Sales by categories",   // chart title 
         dataset,          		  // data    
         true,             		  // include legend   
         true, 
         false);

      return chart;
   }
   
   public static JPanel createDemoPanel(HashMap<String, Integer> mostSalesByCategory) {
      JFreeChart chart = createChart(createDataset(mostSalesByCategory));  
      return new ChartPanel( chart ); 
   }

//   public static void main( String[ ] args ) {
//	  PieChart demo = new PieChart("Sales by categories");  
//      demo.setSize( 560 , 367 );    
//      RefineryUtilities.centerFrameOnScreen( demo );    
//      demo.setVisible( true ); 
//   }
   
}
	
//	DefaultPieDataset pieDataset = new DefaultPieDataset();
//	
//	HashMap<String, Integer> data = adminController.getHashMapSalesByCategory();
//	
//	for (HashMap.Entry<String, Integer> entry : data.entrySet()) {
//		//pieDataset.setValue(entry.getKey(), entry.getValue());
//		
//		System.out.println(entry.getKey() + " - " + entry.getValue());
//	}
//}
