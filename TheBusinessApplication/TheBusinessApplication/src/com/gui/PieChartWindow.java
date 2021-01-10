package com.gui;

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
import com.rolecontrollers.SalesAnalizerController;
 
public class PieChartWindow extends ApplicationFrame {
   
   public PieChartWindow(String title) {
	   super(title);
	   this.setSize( 777 , 530 );    
	   RefineryUtilities.centerFrameOnScreen(this);    
	   this.setVisible( true ); 
	   
	   SalesAnalizerController analizer = new SalesAnalizerController();
	   HashMap<String, Integer> mostSalesByCategory = analizer.getHashMapSalesByCategory();
	   
	   setContentPane(createDemoPanel(mostSalesByCategory));
   }
   
   private static PieDataset createDataset(HashMap<String, Integer> mostSalesByCategory) {
      DefaultPieDataset dataset = new DefaultPieDataset( );
     
      for(Map.Entry<String, Integer> entry : mostSalesByCategory.entrySet()) {
    	  dataset.setValue(entry.getKey(), entry.getValue()); 
      }
      
      return dataset;         
   }
   
   private static JFreeChart createChart(PieDataset dataset) {
      JFreeChart chart = ChartFactory.createPieChart(      
         "Sales by categories",  
         dataset,          		     
         true,             		    
         true, 
         false);

      return chart;
   }
   
   public static JPanel createDemoPanel(HashMap<String, Integer> mostSalesByCategory) {
      JFreeChart chart = createChart(createDataset(mostSalesByCategory));  
      return new ChartPanel( chart ); 
   }
   
}
