package com.businessApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;

public class NewOrderWindow extends JFrame {

	private JPanel contentPane;
	static DBConnection cDB = new DBConnection();
	static Connection con = cDB.createConnection();
	private JTextField txtFieldClientName;
	private JTextField txtFieldClientEmail;
	private JLabel lblDateOfOrder;

	private String repName;
	private String category;
	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					NewOrderWindow frame = new NewOrderWindow();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public NewOrderWindow(String username, String category) {
		this.repName = username;
		this.category = category;
		
		setTitle("New order");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 386, 334);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Client name:");
		lblName.setFont(new Font("Arial", Font.PLAIN, 18));
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(55, 13, 110, 30);
		contentPane.add(lblName);
		
		txtFieldClientName = new JTextField();
		txtFieldClientName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientName.setBounds(177, 15, 133, 26);
		contentPane.add(txtFieldClientName);
		txtFieldClientName.setColumns(10);
		
		JLabel lblClientEmail = new JLabel("Client e-mail:");
		lblClientEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClientEmail.setFont(new Font("Arial", Font.PLAIN, 18));
		lblClientEmail.setBounds(55, 56, 110, 30);
		contentPane.add(lblClientEmail);
		
		txtFieldClientEmail = new JTextField();
		txtFieldClientEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientEmail.setColumns(10);
		txtFieldClientEmail.setBounds(177, 58, 133, 26);
		contentPane.add(txtFieldClientEmail);
		
		JLabel lblBoughtProduct = new JLabel("Bought product:");
		lblBoughtProduct.setHorizontalAlignment(SwingConstants.RIGHT);
		lblBoughtProduct.setFont(new Font("Arial", Font.PLAIN, 18));
		lblBoughtProduct.setBounds(12, 99, 153, 30);
		contentPane.add(lblBoughtProduct);
		
		JLabel lblQuantityBought = new JLabel("Quantity bought:");
		lblQuantityBought.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQuantityBought.setFont(new Font("Arial", Font.PLAIN, 18));
		lblQuantityBought.setBounds(12, 139, 153, 30);
		contentPane.add(lblQuantityBought);
		
		lblDateOfOrder = new JLabel("Date of order:");
		lblDateOfOrder.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDateOfOrder.setFont(new Font("Arial", Font.PLAIN, 18));
		lblDateOfOrder.setBounds(12, 182, 153, 30);
		contentPane.add(lblDateOfOrder);
		
		JComboBox<String> comboBoxBoughtProduct = new JComboBox();
		comboBoxBoughtProduct.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxBoughtProduct.setBounds(177, 99, 133, 26);
		contentPane.add(comboBoxBoughtProduct);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(177, 186, 133, 26);
		contentPane.add(dateChooser);
		
		JComboBox<String> comboBoxQuantity = new JComboBox();
		comboBoxQuantity.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxQuantity.setBounds(177, 144, 133, 26);
		contentPane.add(comboBoxQuantity);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Arial", Font.PLAIN, 20));
		btnAdd.setBounds(113, 230, 133, 49);
		contentPane.add(btnAdd);
		
		fillProductsComboBox(comboBoxBoughtProduct);
		fillQuantityComboBox(comboBoxBoughtProduct, comboBoxQuantity);
		
		comboBoxBoughtProduct.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				fillQuantityComboBox(comboBoxBoughtProduct, comboBoxQuantity);
				System.out.println("hereeeee");
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				Order newOrder = new Order(txtFieldClientName.getText(), txtFieldClientEmail.getText(), (String)comboBoxBoughtProduct.getSelectedItem(), Integer.parseInt((String) comboBoxQuantity.getSelectedItem()), dateChooser.getDate());
//				System.out.println(newOrder.toString());
				
				 Date date = dateChooser.getDate();
	             DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	             String stringDate = dateFormat.format(date);  
			
				Client newClient  = new Client(txtFieldClientName.getText(), txtFieldClientEmail.getText(), (String)comboBoxBoughtProduct.getSelectedItem(), Integer.parseInt((String) comboBoxQuantity.getSelectedItem()), stringDate);
				newClient.buyProduct();
				boolean isValid = newClient.validateOrder();
				
				if(isValid)
				{
					JOptionPane.showMessageDialog(contentPane, "Order has been added");
					setVisible(false);
				}
				else
				{
					JOptionPane.showMessageDialog(contentPane, "Invalid information");
				}
			}
		});
		

	}
	
	public void fillProductsComboBox(JComboBox<String> comboBoxBoughtProduct)
	{
		try
		{
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM allproducts WHERE category = '" + this.category + "'");
	        
	        while(rs.next())
	        {
	        	comboBoxBoughtProduct.addItem(rs.getString("name"));
	        }
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void fillQuantityComboBox(JComboBox<String> comboBoxBoughtProduct, JComboBox<String> comboBoxQuantity)
	{
		comboBoxQuantity.removeAllItems();
		
		try
		{
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM allproducts WHERE name = '" + comboBoxBoughtProduct.getSelectedItem() + "'");
	        
	        int quantity = 0;
	        while(rs.next())
	        {
	        	quantity = rs.getInt("quantity");
	        }
	        
	        for(int i = 1; i <= quantity; i++)
	        {
	        	comboBoxQuantity.addItem(""+i);
	        }
	        
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
