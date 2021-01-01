<<<<<<< HEAD:TheBusinessApplication/TheBusinessApplication/src/com/gui/RepresentativeWindow.java
package com.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.businessapplication.Client;
import com.businessapplication.Representative;
import com.businessapplication.Sale;
import com.businessapplication.Client.Builder;
import com.databaseconnection.DBConnection;
import com.toedter.calendar.JDateChooser;

import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.sql.Date;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

public class RepresentativeWindow extends JFrame {
	
	private JTable tableCatalog;
	
	private JTable tableClients;
	private JTextField txtFieldClientNameEdit;
	private JTextField txtFieldClientEmailEdit;
	private JTextField txtFieldClientNameAdd;
	private JTextField txtFieldClientEmailAdd;
	private JTextField txtFieldClientName;
	private JTextField txtFieldClientEmail;
	
	private String name;
	private String category;
	private String username;
	private String password;
	private int id;
	private ArrayList<Sale> clientsFromCatalog  = new ArrayList<>();
	Representative thisRepresentative;
	
	/**
	 * Create the frame.
	 */
	public RepresentativeWindow(int id, String name, String username, String password, String category) {
		
		setResizable(false);
		this.id = id;
		this.name  = name;
		this.category = category;
		this.username = username;	
		this.password = password;
		
		Representative thisRepresentative = new Representative.Builder().id(id)
																		.name(name)
																		.username(username)
																		.category(category)
																		.password(password)
																		.numberOfSales(getNumberOfSales())
        																.profit(getProfit())
																		.build();
		
		this.thisRepresentative = thisRepresentative;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 866, 626);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu userMenu = new JMenu("User");
		userMenu.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		menuBar.add(userMenu);
		
		JMenuItem mnLogout = new JMenuItem("Log out");
		mnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		userMenu.add(mnLogout);
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(53, 49, 776, 162);
		getContentPane().add(scrollPane);
		
		tableCatalog = new JTable();
		scrollPane.setViewportView(tableCatalog);
		tableCatalog.setFont(new Font("Arial", Font.PLAIN, 16));
		tableCatalog.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id_sale", "name", "e-mail", "bought product", "quantity", "price (lv.)", "date"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class, Integer.class, Double.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		tableCatalog.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tableCatalog.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		
		TableColumnModel columnModel = tableCatalog.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(120);
		columnModel.getColumn(2).setPreferredWidth(200);
		columnModel.getColumn(3).setPreferredWidth(80);
		columnModel.getColumn(4).setPreferredWidth(40);
		columnModel.getColumn(5).setPreferredWidth(60);
		columnModel.getColumn(6).setPreferredWidth(100);
		tableCatalog.setRowHeight(30);
		
		JLabel lblCatalog = new JLabel("Your catalog (" + category + "):");
		lblCatalog.setFont(new Font("Arial", Font.PLAIN, 22));
		lblCatalog.setBounds(53, 13, 381, 23);
		getContentPane().add(lblCatalog);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(53, 260, 406, 271);
		getContentPane().add(scrollPane_1);
		
		tableClients = new JTable();
		scrollPane_1.setViewportView(tableClients);
		tableClients.setFont(new Font("Arial", Font.PLAIN, 16));
		tableClients.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id_client", "name", "e-mail"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
		TableColumnModel columnModel2 = tableClients.getColumnModel();
		
		tableClients.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		columnModel2 = tableClients.getColumnModel();
		columnModel2.getColumn(0).setPreferredWidth(40);
		columnModel2.getColumn(1).setPreferredWidth(120);
		columnModel2.getColumn(2).setPreferredWidth(200);
		tableClients.setRowHeight(30);
		
		JLabel lblClients = new JLabel("Your clients (" + category + "):");
		lblClients.setFont(new Font("Arial", Font.PLAIN, 22));
		lblClients.setBounds(53, 224, 381, 23);
		getContentPane().add(lblClients);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		tabbedPane.setBounds(479, 224, 350, 307);
		getContentPane().add(tabbedPane);
		
		JPanel panel3 = new JPanel();
		tabbedPane.addTab("Add order", null, panel3, null);
		panel3.setLayout(null);
		
		JLabel label = new JLabel("Client name:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		label.setBounds(55, 13, 110, 30);
		panel3.add(label);
		
		txtFieldClientName = new JTextField();
		txtFieldClientName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientName.setColumns(10);
		txtFieldClientName.setBounds(177, 15, 133, 26);
		panel3.add(txtFieldClientName);
		
		txtFieldClientEmail = new JTextField();
		txtFieldClientEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientEmail.setColumns(10);
		txtFieldClientEmail.setBounds(177, 58, 133, 26);
		panel3.add(txtFieldClientEmail);
		
		JLabel label_1 = new JLabel("Client e-mail:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Arial", Font.PLAIN, 18));
		label_1.setBounds(55, 56, 110, 30);
		panel3.add(label_1);
		
		JLabel label_2 = new JLabel("Bought product:");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setFont(new Font("Arial", Font.PLAIN, 18));
		label_2.setBounds(12, 99, 153, 30);
		panel3.add(label_2);
		
		JComboBox<String> comboBoxBoughtProduct = new JComboBox();
		comboBoxBoughtProduct.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxBoughtProduct.setBounds(177, 99, 133, 26);
		panel3.add(comboBoxBoughtProduct);
		
		JComboBox<String> comboBoxQuantity = new JComboBox();
		comboBoxQuantity.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxQuantity.setBounds(177, 144, 133, 26);
		panel3.add(comboBoxQuantity);
		
		JLabel label_3 = new JLabel("Quantity bought:");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setFont(new Font("Arial", Font.PLAIN, 18));
		label_3.setBounds(12, 139, 153, 30);
		panel3.add(label_3);
		
		JLabel label_4 = new JLabel("Date of order:");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setFont(new Font("Arial", Font.PLAIN, 18));
		label_4.setBounds(12, 182, 153, 30);
		panel3.add(label_4);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(177, 186, 133, 26);
		panel3.add(dateChooser);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Arial", Font.PLAIN, 20));
		btnAdd.setBounds(111, 222, 133, 38);
		panel3.add(btnAdd);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Edit Client", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblIdOfClient = new JLabel("ID of client to edit: ");
		lblIdOfClient.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfClient.setBounds(30, 28, 167, 26);
		lblIdOfClient.setFont(new Font("Arial", Font.PLAIN, 19));
		panel.add(lblIdOfClient);
		
		JLabel lblWhatDoYou = new JLabel("Change client's:");
		lblWhatDoYou.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWhatDoYou.setFont(new Font("Arial", Font.PLAIN, 19));
		lblWhatDoYou.setBounds(12, 69, 179, 26);
		panel.add(lblWhatDoYou);
		
		JComboBox comboBoxChange = new JComboBox();
		comboBoxChange.setModel(new DefaultComboBoxModel(new String[] {"", "name", "email", "name & e-mail"}));
		comboBoxChange.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxChange.setBounds(203, 70, 111, 26);
		panel.add(comboBoxChange);
		
		JComboBox comboBoxIdClient = new JComboBox();
		comboBoxIdClient.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdClient.setBounds(203, 29, 55, 26);
		panel.add(comboBoxIdClient);
		
		JLabel lblNewName = new JLabel("New name:");
		lblNewName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewName.setFont(new Font("Arial", Font.PLAIN, 19));
		lblNewName.setBounds(21, 121, 111, 26);
		panel.add(lblNewName);
		
		JLabel lblNewEmail = new JLabel("New e-mail:");
		lblNewEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewEmail.setFont(new Font("Arial", Font.PLAIN, 19));
		lblNewEmail.setBounds(28, 160, 104, 26);
		panel.add(lblNewEmail);
		
		txtFieldClientNameEdit = new JTextField();
		txtFieldClientNameEdit.setEnabled(false);
		txtFieldClientNameEdit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientNameEdit.setBounds(144, 125, 170, 22);
		panel.add(txtFieldClientNameEdit);
		txtFieldClientNameEdit.setColumns(10);
		
		txtFieldClientEmailEdit = new JTextField();
		txtFieldClientEmailEdit.setEnabled(false);
		txtFieldClientEmailEdit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientEmailEdit.setColumns(10);
		txtFieldClientEmailEdit.setBounds(144, 164, 170, 22);
		panel.add(txtFieldClientEmailEdit);
		
		JButton btnEditClient = new JButton("Edit Client");
		btnEditClient.setEnabled(false);
		btnEditClient.setBounds(102, 199, 144, 48);
		panel.add(btnEditClient);
		btnEditClient.setFont(new Font("Arial", Font.PLAIN, 20));
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Delete Client", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblIdOfClient_1 = new JLabel("ID of client to delete: ");
		lblIdOfClient_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfClient_1.setFont(new Font("Arial", Font.PLAIN, 19));
		lblIdOfClient_1.setBounds(40, 98, 199, 26);
		panel_1.add(lblIdOfClient_1);
		
		JComboBox comboBoxIdClientToDelete = new JComboBox();
		comboBoxIdClientToDelete.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdClientToDelete.setBounds(243, 99, 55, 26);
		panel_1.add(comboBoxIdClientToDelete);
		
		JButton btnDeleteClient = new JButton("Delete Client");
		btnDeleteClient.setFont(new Font("Arial", Font.PLAIN, 20));
		btnDeleteClient.setBounds(98, 171, 171, 48);
		panel_1.add(btnDeleteClient);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Add client", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblClientsName = new JLabel("Client's name:");
		lblClientsName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClientsName.setFont(new Font("Arial", Font.PLAIN, 19));
		lblClientsName.setBounds(26, 74, 130, 26);
		panel_2.add(lblClientsName);
		
		JLabel lblClientsEmail = new JLabel("Client's e-mail:");
		lblClientsEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClientsEmail.setFont(new Font("Arial", Font.PLAIN, 19));
		lblClientsEmail.setBounds(33, 113, 123, 26);
		panel_2.add(lblClientsEmail);
		
		txtFieldClientNameAdd = new JTextField();
		txtFieldClientNameAdd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientNameAdd.setColumns(10);
		txtFieldClientNameAdd.setBounds(168, 81, 148, 22);
		panel_2.add(txtFieldClientNameAdd);
		
		txtFieldClientEmailAdd = new JTextField();
		txtFieldClientEmailAdd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientEmailAdd.setColumns(10);
		txtFieldClientEmailAdd.setBounds(168, 116, 148, 22);
		panel_2.add(txtFieldClientEmailAdd);
		
		JButton btnAddClient = new JButton("Add client");
		btnAddClient.setFont(new Font("Arial", Font.PLAIN, 20));
		btnAddClient.setBounds(119, 166, 144, 48);
		panel_2.add(btnAddClient);

		
		mnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
		
		comboBoxChange.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboBoxChange.getSelectedItem().equals("name")) {
					txtFieldClientNameEdit.setEnabled(true);
					txtFieldClientEmailEdit.setEnabled(false);
					btnEditClient.setEnabled(true);
				}
				else if(comboBoxChange.getSelectedItem().equals("email")) {
					txtFieldClientNameEdit.setEnabled(false);
					txtFieldClientEmailEdit.setEnabled(true);
					btnEditClient.setEnabled(true);
				}
				else if(comboBoxChange.getSelectedItem().equals("name & e-mail")) {
					txtFieldClientNameEdit.setEnabled(true);
					txtFieldClientEmailEdit.setEnabled(true);
					btnEditClient.setEnabled(true);
				}
				else {
					txtFieldClientNameEdit.setEnabled(false);
					txtFieldClientEmailEdit.setEnabled(false);
					btnEditClient.setEnabled(false);
				}
			}
		});
		
		comboBoxBoughtProduct.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				fillComboBoxQuantity(comboBoxBoughtProduct, comboBoxQuantity);
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				java.util.Date selectedDate = dateChooser.getDate();
	            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	            String date = dateFormat.format(selectedDate);      
				
	            Client newClient = new Client.Builder().name(txtFieldClientName.getText())
	            									   .email(txtFieldClientEmail.getText())
	            									   .productBought((String)comboBoxBoughtProduct.getSelectedItem())
	            									   .categoryOfProductBought(category)
	            									   .quantityBought(Integer.parseInt((String) comboBoxQuantity.getSelectedItem()))
	            									   .dateOfSale(date)
	            									   .build();
	
	             
	            thisRepresentative.addSale(newClient);		
				
				if(newClient.isFirstTimeClient())
				{
					JOptionPane.showMessageDialog(null, "Client [" + newClient.getName() + ", " + newClient.getEmail() 
					                                   + "] bought [" + newClient.getQuantityBought() + "x " + newClient.getProductBought() + "].");
					updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);

				}
				else
				{
					JOptionPane.showMessageDialog(null, "Invalid information");
				}
			}
		});
		//
		
		btnEditClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				
				Client clientToEdit;
				int idClient, idSale;
				
				if(comboBoxChange.getSelectedItem().equals("name")) {
					clientToEdit = new Client.Builder()
							.idOfClient(Integer.parseInt((String) comboBoxIdClient.getSelectedItem()))
							.name(txtFieldClientNameEdit.getText())
							.email(null)
							.build();
					
					System.out.println("Integer.parseInt((String) comboBoxIdClient.getSelectedItem()) = " + Integer.parseInt((String) comboBoxIdClient.getSelectedItem()));
					
					idClient = clientToEdit.getIdOfClient();
					idSale = getIdOfSale();
					
					thisRepresentative.editClient(clientToEdit, "name", idClient, idSale);
					updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
				}
				
				else if(comboBoxChange.getSelectedItem().equals("email")) {
					clientToEdit = new Client.Builder()
							.idOfClient(Integer.parseInt((String) comboBoxIdClient.getSelectedItem()))
							.name(null)
							.email(txtFieldClientEmailEdit.getText())
							.build();
					
					
					if(clientToEdit.isFirstTimeClient()) {
						idClient = clientToEdit.getIdOfClient();
						idSale = getIdOfSale();
						
						thisRepresentative.editClient(clientToEdit, "email", idClient, idSale);
						updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
					}
					else {
						JOptionPane.showMessageDialog(getContentPane(), "Not unique e-mail");
					}
			
				}
				else if(comboBoxChange.getSelectedItem().equals("name & e-mail")) {
					clientToEdit = new Client.Builder()
							.idOfClient(Integer.parseInt((String) comboBoxIdClient.getSelectedItem()))
							.name(txtFieldClientNameEdit.getText())
							.email(txtFieldClientEmailEdit.getText())
							.build();
					
					System.out.println(clientToEdit.isFirstTimeClient());
					if(clientToEdit.isFirstTimeClient()) {
						idClient = clientToEdit.getIdOfClient();
						idSale = getIdOfSale();
						
						thisRepresentative.editClient(clientToEdit, "name & e-mail", idClient, idSale);
						
						updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
					}
					else {
						JOptionPane.showMessageDialog(null, "Not unique e-mail");
					}

				}
				
			}
		});
		
		btnDeleteClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = Integer.parseInt((String) comboBoxIdClientToDelete.getSelectedItem());	
				String email = getInfoOfClientById("email", id);
				String name = getInfoOfClientById("name", id);
				
				Client clientToDelete = new Client.Builder()
											.idOfClient(id)
											.name(name)
											.email(email)
											.build();
					
				thisRepresentative.deleteClient(clientToDelete);
				JOptionPane.showMessageDialog(null, "Client ["+ clientToDelete.getName() + ", " + clientToDelete.getEmail() + "] has been deleted.");
				
				updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
			}
		});
		
		btnAddClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = Integer.parseInt((String) comboBoxIdClientToDelete.getSelectedItem());	
				
				Client clientToAdd = new Client.Builder()
											.idOfClient(id)
											.name(txtFieldClientNameAdd.getText())
											.email(txtFieldClientEmailAdd.getText())
											.build();
				
				System.out.println(clientToAdd.isFirstTimeClient());
				if(clientToAdd.isFirstTimeClient()) {
					thisRepresentative.addClient(clientToAdd);
					updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
				}
				else {
					JOptionPane.showMessageDialog(null, "Not unique e-mail");
				}
				thisRepresentative.addClient(clientToAdd);
			}
		});
	}
	
	public void updateTablesAndComboBoxes(JComboBox<String> comboBoxIdClient,JComboBox<String> comboBoxIdClientToDelete,
								JComboBox<String> comboBoxBoughtProduct,JComboBox<String> comboBoxQuantity) {
		loadTables();
		fillComboBoxClient(comboBoxIdClient);
		fillComboBoxClient(comboBoxIdClientToDelete);
		fillComboBoxProducts(comboBoxBoughtProduct);
		fillComboBoxQuantity(comboBoxBoughtProduct, comboBoxQuantity);
	}
	
	public String getInfoOfClientById(String toChange, int id)
	{
		
		try {
			ResultSet rs = DBConnection.getData("SELECT " + toChange + " FROM allclients WHERE id_client = " + id);
			
			while(rs.next())
			{
				String info =  rs.getString(toChange);
				return info;
			}
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	public void loadTables()
	{
		try
		{
			DefaultTableModel model = (DefaultTableModel)tableCatalog.getModel(); 
			DefaultTableModel modelClients = (DefaultTableModel)tableClients.getModel(); 
			
			if(model.getRowCount() > 0)
			{
				model.setRowCount(0);
			}
			
			if(modelClients.getRowCount() > 0)
			{
				modelClients.setRowCount(0);
			}
			
			ArrayList<Sale> catalog = new ArrayList<>();
			
			ResultSet rs = DBConnection.getData("SELECT * FROM allsales WHERE representative_username = '" + this.username + "'");
			
	        while(rs.next())
	        {  	      	
	        	Sale o = new Sale.Builder().id(rs.getInt("id_sale"))
	        											   .name(rs.getString("name"))
	        											   .email(rs.getString("email"))
	        											   .product(rs.getString("product"))
	        											   .quantity(rs.getInt("quantity"))
	        											   .price(rs.getDouble("price"))
	        											   .date(rs.getDate("date"))
	        											   .build();	
	        	catalog.add(o);
	        }
	        
			Object[] row = new Object[7];
	        
	        for (int i = 0; i < catalog.size(); i++) {
				row[0] = catalog.get(i).getId();
				row[1] = catalog.get(i).getName();
				row[2] = catalog.get(i).getEmail();
				row[3] = catalog.get(i).getProduct();
				row[4] = catalog.get(i).getQuantity();
				row[5] = catalog.get(i).getPrice();
				row[6] = catalog.get(i).getDate();
				model.addRow(row);
			}
	        
	        clientsFromCatalog = thisRepresentative.removeRepeatingClients(catalog);
	        
	        int id;
	        String name, email;
	        
	        for (int i = 0; i < clientsFromCatalog.size(); i++) {
				id = clientsFromCatalog.get(i).getId();
				name = clientsFromCatalog.get(i).getName();
				email = clientsFromCatalog.get(i).getEmail();
				
				Object[] data = {id, name, email};
				
				modelClients.addRow(data);
			}
	        
	        
		} catch(SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	public void fillComboBoxClient(JComboBox<String> ComboBoxIdClient)
	{		
		ComboBoxIdClient.removeAllItems();
		
		for(Sale clientInCatalog : clientsFromCatalog)
		{
			ComboBoxIdClient.addItem(""+clientInCatalog.getId());
		}
		
	}
	
	public void fillComboBoxProducts(JComboBox<String> comboBoxBoughtProduct)
	{
		
		// clears comboBox before updating it
		comboBoxBoughtProduct.removeAllItems();
		try
		{     
			if(this.category.equals("all")) {
				ResultSet rs = DBConnection.getData("SELECT * FROM allproducts");
				
				 while(rs.next())
				 {
			        	comboBoxBoughtProduct.addItem(rs.getString("name"));
				 }
			}
			else {
				ResultSet rs = DBConnection.getData("SELECT * FROM allproducts WHERE category = '" + this.category + "'");
				
				 while(rs.next())
				 {
			        	comboBoxBoughtProduct.addItem(rs.getString("name"));
				 }
			}
			
	       
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void fillComboBoxQuantity(JComboBox<String> comboBoxBoughtProduct, JComboBox<String> comboBoxQuantity)
	{
		
		// clears comboBox before updating it
		comboBoxQuantity.removeAllItems();
		
		try
		{
			ResultSet rs = DBConnection.getData("SELECT * FROM allproducts WHERE name = '" + comboBoxBoughtProduct.getSelectedItem() + "'");
	        
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
	
	public int getIdOfRepresentative()
	{
		try {
			ResultSet rs = DBConnection.getData("SELECT * FROM allrepresentatives WHERE username = '" + this.username + "'");
			rs.next();
			
			return rs.getInt("id_rep");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getIdOfSale()
	{
		try {
			ResultSet rs = DBConnection.getData("SELECT * FROM allsales WHERE representative_username = '" + this.username + "'");
			rs.next();
			
			return rs.getInt("id_sale");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getNumberOfSales()
	{
		try {
			ResultSet rs = DBConnection.getData("SELECT * FROM allrepresentatives WHERE username = '" + this.username + "'");
			rs.next();
			
			return rs.getInt("numberofsales");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public double getProfit()
	{
		try {
			ResultSet rs = DBConnection.getData("SELECT * FROM allrepresentatives WHERE username = '" + this.username + "'");
			rs.next();
			
			System.out.println("rs.getDouble(\"profit\") = " + rs.getDouble("profit"));
			return rs.getDouble("profit");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

}
=======
package com.businessapplication;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import com.toedter.calendar.JDateChooser;

import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.sql.Date;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

public class RepresentativeWindow extends JFrame {
	
	private JTable tableCatalog;
	
	private JTable tableClients;
	private JTextField txtFieldClientNameEdit;
	private JTextField txtFieldClientEmailEdit;
	private JTextField txtFieldClientNameAdd;
	private JTextField txtFieldClientEmailAdd;
	private JTextField txtFieldClientName;
	private JTextField txtFieldClientEmail;
	
	private String name;
	private String category;
	private String username;
	private String password;
	private int id;
	private ArrayList<OrderControl> clientsFromCatalog  = new ArrayList<>();
	
	/**
	 * Create the frame.
	 */
	public RepresentativeWindow(int id, String name, String username, String password, String category) {
		
		setResizable(false);
		this.id = id;
		this.name  = name;
		this.category = category;
		this.username = username;	
		this.password = password;
		
		Representative thisRepresentative = new Representative.Builder().id(id)
																		.name(name)
																		.username(username)
																		.category(category)
																		.password(password)
																		.numberOfSales(getNumberOfSales())
        																.profit(getProfit())
																		.build();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 866, 626);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu userMenu = new JMenu("User");
		userMenu.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		menuBar.add(userMenu);
		
		JMenuItem mnLogout = new JMenuItem("Log out");
		mnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		userMenu.add(mnLogout);
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(53, 49, 776, 162);
		getContentPane().add(scrollPane);
		
		tableCatalog = new JTable();
		scrollPane.setViewportView(tableCatalog);
		tableCatalog.setFont(new Font("Arial", Font.PLAIN, 16));
		tableCatalog.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id_sale", "name", "e-mail", "bought product", "quantity", "price (lv.)", "date"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class, Integer.class, Double.class, Object.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		tableCatalog.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tableCatalog.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		
		TableColumnModel columnModel = tableCatalog.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(40);
		columnModel.getColumn(1).setPreferredWidth(120);
		columnModel.getColumn(2).setPreferredWidth(200);
		columnModel.getColumn(3).setPreferredWidth(80);
		columnModel.getColumn(4).setPreferredWidth(40);
		columnModel.getColumn(5).setPreferredWidth(60);
		columnModel.getColumn(6).setPreferredWidth(100);
		tableCatalog.setRowHeight(30);
		
		JLabel lblCatalog = new JLabel("Your catalog (" + category + "):");
		lblCatalog.setFont(new Font("Arial", Font.PLAIN, 22));
		lblCatalog.setBounds(53, 13, 381, 23);
		getContentPane().add(lblCatalog);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(53, 260, 406, 271);
		getContentPane().add(scrollPane_1);
		
		tableClients = new JTable();
		scrollPane_1.setViewportView(tableClients);
		tableClients.setFont(new Font("Arial", Font.PLAIN, 16));
		tableClients.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id_client", "name", "e-mail"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
		TableColumnModel columnModel2 = tableClients.getColumnModel();
		
		tableClients.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		columnModel2 = tableClients.getColumnModel();
		columnModel2.getColumn(0).setPreferredWidth(40);
		columnModel2.getColumn(1).setPreferredWidth(120);
		columnModel2.getColumn(2).setPreferredWidth(200);
		tableClients.setRowHeight(30);
		
		JLabel lblClients = new JLabel("Your clients (" + category + "):");
		lblClients.setFont(new Font("Arial", Font.PLAIN, 22));
		lblClients.setBounds(53, 224, 381, 23);
		getContentPane().add(lblClients);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		tabbedPane.setBounds(479, 224, 350, 307);
		getContentPane().add(tabbedPane);
		
		JPanel panel3 = new JPanel();
		tabbedPane.addTab("Add order", null, panel3, null);
		panel3.setLayout(null);
		
		JLabel label = new JLabel("Client name:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		label.setBounds(55, 13, 110, 30);
		panel3.add(label);
		
		txtFieldClientName = new JTextField();
		txtFieldClientName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientName.setColumns(10);
		txtFieldClientName.setBounds(177, 15, 133, 26);
		panel3.add(txtFieldClientName);
		
		txtFieldClientEmail = new JTextField();
		txtFieldClientEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientEmail.setColumns(10);
		txtFieldClientEmail.setBounds(177, 58, 133, 26);
		panel3.add(txtFieldClientEmail);
		
		JLabel label_1 = new JLabel("Client e-mail:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Arial", Font.PLAIN, 18));
		label_1.setBounds(55, 56, 110, 30);
		panel3.add(label_1);
		
		JLabel label_2 = new JLabel("Bought product:");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setFont(new Font("Arial", Font.PLAIN, 18));
		label_2.setBounds(12, 99, 153, 30);
		panel3.add(label_2);
		
		JComboBox<String> comboBoxBoughtProduct = new JComboBox();
		comboBoxBoughtProduct.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxBoughtProduct.setBounds(177, 99, 133, 26);
		panel3.add(comboBoxBoughtProduct);
		
		JComboBox<String> comboBoxQuantity = new JComboBox();
		comboBoxQuantity.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxQuantity.setBounds(177, 144, 133, 26);
		panel3.add(comboBoxQuantity);
		
		JLabel label_3 = new JLabel("Quantity bought:");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setFont(new Font("Arial", Font.PLAIN, 18));
		label_3.setBounds(12, 139, 153, 30);
		panel3.add(label_3);
		
		JLabel label_4 = new JLabel("Date of order:");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setFont(new Font("Arial", Font.PLAIN, 18));
		label_4.setBounds(12, 182, 153, 30);
		panel3.add(label_4);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(177, 186, 133, 26);
		panel3.add(dateChooser);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Arial", Font.PLAIN, 20));
		btnAdd.setBounds(111, 222, 133, 38);
		panel3.add(btnAdd);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Edit Client", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblIdOfClient = new JLabel("ID of client to edit: ");
		lblIdOfClient.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfClient.setBounds(30, 28, 167, 26);
		lblIdOfClient.setFont(new Font("Arial", Font.PLAIN, 19));
		panel.add(lblIdOfClient);
		
		JLabel lblWhatDoYou = new JLabel("Change client's:");
		lblWhatDoYou.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWhatDoYou.setFont(new Font("Arial", Font.PLAIN, 19));
		lblWhatDoYou.setBounds(12, 69, 179, 26);
		panel.add(lblWhatDoYou);
		
		JComboBox comboBoxChange = new JComboBox();
		comboBoxChange.setModel(new DefaultComboBoxModel(new String[] {"", "name", "email", "name & e-mail"}));
		comboBoxChange.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxChange.setBounds(203, 70, 111, 26);
		panel.add(comboBoxChange);
		
		JComboBox comboBoxIdClient = new JComboBox();
		comboBoxIdClient.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdClient.setBounds(203, 29, 55, 26);
		panel.add(comboBoxIdClient);
		
		JLabel lblNewName = new JLabel("New name:");
		lblNewName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewName.setFont(new Font("Arial", Font.PLAIN, 19));
		lblNewName.setBounds(21, 121, 111, 26);
		panel.add(lblNewName);
		
		JLabel lblNewEmail = new JLabel("New e-mail:");
		lblNewEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewEmail.setFont(new Font("Arial", Font.PLAIN, 19));
		lblNewEmail.setBounds(28, 160, 104, 26);
		panel.add(lblNewEmail);
		
		txtFieldClientNameEdit = new JTextField();
		txtFieldClientNameEdit.setEnabled(false);
		txtFieldClientNameEdit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientNameEdit.setBounds(144, 125, 170, 22);
		panel.add(txtFieldClientNameEdit);
		txtFieldClientNameEdit.setColumns(10);
		
		txtFieldClientEmailEdit = new JTextField();
		txtFieldClientEmailEdit.setEnabled(false);
		txtFieldClientEmailEdit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientEmailEdit.setColumns(10);
		txtFieldClientEmailEdit.setBounds(144, 164, 170, 22);
		panel.add(txtFieldClientEmailEdit);
		
		JButton btnEditClient = new JButton("Edit Client");
		btnEditClient.setEnabled(false);
		btnEditClient.setBounds(102, 199, 144, 48);
		panel.add(btnEditClient);
		btnEditClient.setFont(new Font("Arial", Font.PLAIN, 20));
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Delete Client", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblIdOfClient_1 = new JLabel("ID of client to delete: ");
		lblIdOfClient_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfClient_1.setFont(new Font("Arial", Font.PLAIN, 19));
		lblIdOfClient_1.setBounds(40, 98, 199, 26);
		panel_1.add(lblIdOfClient_1);
		
		JComboBox comboBoxIdClientToDelete = new JComboBox();
		comboBoxIdClientToDelete.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdClientToDelete.setBounds(243, 99, 55, 26);
		panel_1.add(comboBoxIdClientToDelete);
		
		JButton btnDeleteClient = new JButton("Delete Client");
		btnDeleteClient.setFont(new Font("Arial", Font.PLAIN, 20));
		btnDeleteClient.setBounds(98, 171, 171, 48);
		panel_1.add(btnDeleteClient);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Add client", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblClientsName = new JLabel("Client's name:");
		lblClientsName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClientsName.setFont(new Font("Arial", Font.PLAIN, 19));
		lblClientsName.setBounds(26, 74, 130, 26);
		panel_2.add(lblClientsName);
		
		JLabel lblClientsEmail = new JLabel("Client's e-mail:");
		lblClientsEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClientsEmail.setFont(new Font("Arial", Font.PLAIN, 19));
		lblClientsEmail.setBounds(33, 113, 123, 26);
		panel_2.add(lblClientsEmail);
		
		txtFieldClientNameAdd = new JTextField();
		txtFieldClientNameAdd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientNameAdd.setColumns(10);
		txtFieldClientNameAdd.setBounds(168, 81, 148, 22);
		panel_2.add(txtFieldClientNameAdd);
		
		txtFieldClientEmailAdd = new JTextField();
		txtFieldClientEmailAdd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientEmailAdd.setColumns(10);
		txtFieldClientEmailAdd.setBounds(168, 116, 148, 22);
		panel_2.add(txtFieldClientEmailAdd);
		
		JButton btnAddClient = new JButton("Add client");
		btnAddClient.setFont(new Font("Arial", Font.PLAIN, 20));
		btnAddClient.setBounds(119, 166, 144, 48);
		panel_2.add(btnAddClient);

		
		mnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
		
		comboBoxChange.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboBoxChange.getSelectedItem().equals("name")) {
					txtFieldClientNameEdit.setEnabled(true);
					txtFieldClientEmailEdit.setEnabled(false);
					btnEditClient.setEnabled(true);
				}
				else if(comboBoxChange.getSelectedItem().equals("email")) {
					txtFieldClientNameEdit.setEnabled(false);
					txtFieldClientEmailEdit.setEnabled(true);
					btnEditClient.setEnabled(true);
				}
				else if(comboBoxChange.getSelectedItem().equals("name & e-mail")) {
					txtFieldClientNameEdit.setEnabled(true);
					txtFieldClientEmailEdit.setEnabled(true);
					btnEditClient.setEnabled(true);
				}
				else {
					txtFieldClientNameEdit.setEnabled(false);
					txtFieldClientEmailEdit.setEnabled(false);
					btnEditClient.setEnabled(false);
				}
			}
		});
		
		comboBoxBoughtProduct.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				fillComboBoxQuantity(comboBoxBoughtProduct, comboBoxQuantity);
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				java.util.Date selectedDate = dateChooser.getDate();
	            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	            String date = dateFormat.format(selectedDate);      
				
	            Client newClient = new Client.Builder().name(txtFieldClientName.getText())
	            									   .email(txtFieldClientEmail.getText())
	            									   .productBought((String)comboBoxBoughtProduct.getSelectedItem())
	            									   .categoryOfProductBought(category)
	            									   .quantityBought(Integer.parseInt((String) comboBoxQuantity.getSelectedItem()))
	            									   .dateOfSale(date)
	            									   .build();
	
	             
	            thisRepresentative.addSale(newClient);		
				
				if(newClient.isFirstTimeClient())
				{
					JOptionPane.showMessageDialog(null, "Client [" + newClient.getName() + ", " + newClient.getEmail() 
					                                   + "] bought [" + newClient.getQuantityBought() + "x " + newClient.getProductBought() + "].");
					updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);

				}
				else
				{
					JOptionPane.showMessageDialog(null, "Invalid information");
				}
			}
		});
		//
		
		btnEditClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				
				Client clientToEdit;
				int idClient, idSale;
				
				if(comboBoxChange.getSelectedItem().equals("name")) {
					clientToEdit = new Client.Builder()
							.idOfClient(Integer.parseInt((String) comboBoxIdClient.getSelectedItem()))
							.name(txtFieldClientNameEdit.getText())
							.email(null)
							.build();
					
					System.out.println("Integer.parseInt((String) comboBoxIdClient.getSelectedItem()) = " + Integer.parseInt((String) comboBoxIdClient.getSelectedItem()));
					
					idClient = clientToEdit.getIdOfClient();
					idSale = getIdOfSale();
					
					thisRepresentative.editClient(clientToEdit, "name", idClient, idSale);
					updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
				}
				
				else if(comboBoxChange.getSelectedItem().equals("email")) {
					clientToEdit = new Client.Builder()
							.idOfClient(Integer.parseInt((String) comboBoxIdClient.getSelectedItem()))
							.name(null)
							.email(txtFieldClientEmailEdit.getText())
							.build();
					
					
					if(clientToEdit.isFirstTimeClient()) {
						idClient = clientToEdit.getIdOfClient();
						idSale = getIdOfSale();
						
						thisRepresentative.editClient(clientToEdit, "email", idClient, idSale);
						updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
					}
					else {
						JOptionPane.showMessageDialog(getContentPane(), "Not unique e-mail");
					}
			
				}
				else if(comboBoxChange.getSelectedItem().equals("name & e-mail")) {
					clientToEdit = new Client.Builder()
							.idOfClient(Integer.parseInt((String) comboBoxIdClient.getSelectedItem()))
							.name(txtFieldClientNameEdit.getText())
							.email(txtFieldClientEmailEdit.getText())
							.build();
					
					System.out.println(clientToEdit.isFirstTimeClient());
					if(clientToEdit.isFirstTimeClient()) {
						idClient = clientToEdit.getIdOfClient();
						idSale = getIdOfSale();
						
						thisRepresentative.editClient(clientToEdit, "name & e-mail", idClient, idSale);
						
						updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
					}
					else {
						JOptionPane.showMessageDialog(null, "Not unique e-mail");
					}

				}
				
			}
		});
		
		btnDeleteClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = Integer.parseInt((String) comboBoxIdClientToDelete.getSelectedItem());	
				String email = getInfoOfClientById("email", id);
				String name = getInfoOfClientById("name", id);
				
				Client clientToDelete = new Client.Builder()
											.idOfClient(id)
											.name(name)
											.email(email)
											.build();
					
				thisRepresentative.deleteClient(clientToDelete);
				JOptionPane.showMessageDialog(null, "Client ["+ clientToDelete.getName() + ", " + clientToDelete.getEmail() + "] has been deleted.");
				
				updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
			}
		});
		
		btnAddClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = Integer.parseInt((String) comboBoxIdClientToDelete.getSelectedItem());	
				
				Client clientToAdd = new Client.Builder()
											.idOfClient(id)
											.name(txtFieldClientNameAdd.getText())
											.email(txtFieldClientEmailAdd.getText())
											.build();
				
				System.out.println(clientToAdd.isFirstTimeClient());
				if(clientToAdd.isFirstTimeClient()) {
					thisRepresentative.addClient(clientToAdd);
					updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct,comboBoxQuantity);
				}
				else {
					JOptionPane.showMessageDialog(null, "Not unique e-mail");
				}
				thisRepresentative.addClient(clientToAdd);
			}
		});
	}
	
	public void updateTablesAndComboBoxes(JComboBox<String> comboBoxIdClient,JComboBox<String> comboBoxIdClientToDelete,
								JComboBox<String> comboBoxBoughtProduct,JComboBox<String> comboBoxQuantity) {
		loadTables();
		fillComboBoxClient(comboBoxIdClient);
		fillComboBoxClient(comboBoxIdClientToDelete);
		fillComboBoxProducts(comboBoxBoughtProduct);
		fillComboBoxQuantity(comboBoxBoughtProduct, comboBoxQuantity);
	}
	
	public String getInfoOfClientById(String toChange, int id)
	{
		
		try {
			ResultSet rs = DBConnection.getData("SELECT " + toChange + " FROM allclients WHERE id_client = " + id);
			
			while(rs.next())
			{
				String info =  rs.getString(toChange);
				return info;
			}
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	public void loadTables()
	{
		try
		{
			DefaultTableModel model = (DefaultTableModel)tableCatalog.getModel(); 
			DefaultTableModel modelClients = (DefaultTableModel)tableClients.getModel(); 
			
			if(model.getRowCount() > 0)
			{
				model.setRowCount(0);
			}
			
			if(modelClients.getRowCount() > 0)
			{
				modelClients.setRowCount(0);
			}
			
			ArrayList<OrderControl> catalog = new ArrayList<>();
			
			ResultSet rs = DBConnection.getData("SELECT * FROM allsales WHERE representative_username = '" + this.username + "'");
			
	        while(rs.next())
	        {  	      	
	        	OrderControl o = new OrderControl.Builder().id(rs.getInt("id_sale"))
	        											   .name(rs.getString("name"))
	        											   .email(rs.getString("email"))
	        											   .product(rs.getString("product"))
	        											   .quantity(rs.getInt("quantity"))
	        											   .price(rs.getDouble("price"))
	        											   .date(rs.getDate("date"))
	        											   .build();	
	        	catalog.add(o);
	        }
	        
			Object[] row = new Object[7];
	        
	        for (int i = 0; i < catalog.size(); i++) {
				row[0] = catalog.get(i).getId();
				row[1] = catalog.get(i).getName();
				row[2] = catalog.get(i).getEmail();
				row[3] = catalog.get(i).getProduct();
				row[4] = catalog.get(i).getQuantity();
				row[5] = catalog.get(i).getPrice();
				row[6] = catalog.get(i).getDate();
				model.addRow(row);
			}
	        
	        clientsFromCatalog = OrderControl.removeRepeatingClients(catalog);
	        
	        int id;
	        String name, email;
	        
	        for (int i = 0; i < clientsFromCatalog.size(); i++) {
				id = clientsFromCatalog.get(i).getId();
				name = clientsFromCatalog.get(i).getName();
				email = clientsFromCatalog.get(i).getEmail();
				
				Object[] data = {id, name, email};
				
				modelClients.addRow(data);
			}
	        
	        
		} catch(SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	public void fillComboBoxClient(JComboBox<String> ComboBoxIdClient)
	{		
		ComboBoxIdClient.removeAllItems();
		
		for(OrderControl clientInCatalog : clientsFromCatalog)
		{
			ComboBoxIdClient.addItem(""+clientInCatalog.getId());
		}
		
	}
	
	public void fillComboBoxProducts(JComboBox<String> comboBoxBoughtProduct)
	{
		
		// clears comboBox before updating it
		comboBoxBoughtProduct.removeAllItems();
		try
		{     
			if(this.category.equals("all")) {
				ResultSet rs = DBConnection.getData("SELECT * FROM allproducts");
				
				 while(rs.next())
				 {
			        	comboBoxBoughtProduct.addItem(rs.getString("name"));
				 }
			}
			else {
				ResultSet rs = DBConnection.getData("SELECT * FROM allproducts WHERE category = '" + this.category + "'");
				
				 while(rs.next())
				 {
			        	comboBoxBoughtProduct.addItem(rs.getString("name"));
				 }
			}
			
	       
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void fillComboBoxQuantity(JComboBox<String> comboBoxBoughtProduct, JComboBox<String> comboBoxQuantity)
	{
		
		// clears comboBox before updating it
		comboBoxQuantity.removeAllItems();
		
		try
		{
			ResultSet rs = DBConnection.getData("SELECT * FROM allproducts WHERE name = '" + comboBoxBoughtProduct.getSelectedItem() + "'");
	        
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
	
	public int getIdOfRepresentative()
	{
		try {
			ResultSet rs = DBConnection.getData("SELECT * FROM allrepresentatives WHERE username = '" + this.username + "'");
			rs.next();
			
			return rs.getInt("id_rep");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getIdOfSale()
	{
		try {
			ResultSet rs = DBConnection.getData("SELECT * FROM allsales WHERE representative_username = '" + this.username + "'");
			rs.next();
			
			return rs.getInt("id_sale");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public int getNumberOfSales()
	{
		try {
			ResultSet rs = DBConnection.getData("SELECT * FROM allrepresentatives WHERE username = '" + this.username + "'");
			rs.next();
			
			return rs.getInt("numberofsales");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public double getProfit()
	{
		try {
			ResultSet rs = DBConnection.getData("SELECT * FROM allrepresentatives WHERE username = '" + this.username + "'");
			rs.next();
			
			System.out.println("rs.getDouble(\"profit\") = " + rs.getDouble("profit"));
			return rs.getDouble("profit");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

}
>>>>>>> b34b0072d621633b360bfa41a214ce1009287cca:TheBusinessApplication/TheBusinessApplication/src/com/businessapplication/RepresentativeWindow.java
