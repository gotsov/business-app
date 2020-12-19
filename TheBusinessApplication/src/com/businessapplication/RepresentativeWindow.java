package com.businessapplication;

import java.awt.BorderLayout;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import com.toedter.calendar.JDateChooser;

import com.mysql.cj.protocol.Resultset;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.sql.Date;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import com.toedter.calendar.JDateChooser;

public class RepresentativeWindow extends JFrame {
	
	private JTable tableCatalog;
	private String category;
	private String username;
	private JTable tableClients;
	private JTextField txtFieldClientNameEdit;
	private JTextField txtFieldClientEmailEdit;
	private JTextField txtFieldClientNameAdd;
	private JTextField txtFieldClientEmailAdd;
	
	private ArrayList<OrderControl> clientsFromCatalog  = new ArrayList<>();
	private JTextField txtFieldClientName;
	private JTextField txtFieldClientEmail;

	/**
	 * Create the frame.
	 */
	public RepresentativeWindow(String username, String category) {
		this.category = category;
		this.username = username;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 847, 626);
		
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
		scrollPane.setBounds(53, 49, 733, 162);
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
		columnModel2 = tableClients.getColumnModel();
		columnModel2.getColumn(0).setPreferredWidth(40);
		columnModel2.getColumn(1).setPreferredWidth(120);
		columnModel2.getColumn(2).setPreferredWidth(200);
		tableClients.setRowHeight(30);
		
		JLabel lblClients = new JLabel("Your clients (" + category + "):");
		lblClients.setFont(new Font("Arial", Font.PLAIN, 22));
		lblClients.setBounds(53, 224, 381, 23);
		getContentPane().add(lblClients);
		
		JButton btnRefreshTable = new JButton("Refresh tables");
		btnRefreshTable.setFont(new Font("Arial", Font.PLAIN, 19));
		btnRefreshTable.setBounds(625, 9, 161, 32);
		getContentPane().add(btnRefreshTable);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(479, 224, 307, 307);
		getContentPane().add(tabbedPane);
		
		JPanel panel3 = new JPanel();
		tabbedPane.addTab("Add order", null, panel3, null);
		panel3.setLayout(null);
		
		JLabel label = new JLabel("Client name:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		label.setBounds(35, 13, 110, 30);
		panel3.add(label);
		
		txtFieldClientName = new JTextField();
		txtFieldClientName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientName.setColumns(10);
		txtFieldClientName.setBounds(157, 15, 133, 26);
		panel3.add(txtFieldClientName);
		
		txtFieldClientEmail = new JTextField();
		txtFieldClientEmail.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientEmail.setColumns(10);
		txtFieldClientEmail.setBounds(157, 58, 133, 26);
		panel3.add(txtFieldClientEmail);
		
		JLabel label_1 = new JLabel("Client e-mail:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Arial", Font.PLAIN, 18));
		label_1.setBounds(35, 56, 110, 30);
		panel3.add(label_1);
		
		JLabel label_2 = new JLabel("Bought product:");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setFont(new Font("Arial", Font.PLAIN, 18));
		label_2.setBounds(-8, 99, 153, 30);
		panel3.add(label_2);
		
		JComboBox<String> comboBoxBoughtProduct = new JComboBox();
		comboBoxBoughtProduct.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxBoughtProduct.setBounds(157, 99, 133, 26);
		panel3.add(comboBoxBoughtProduct);
		
		JComboBox<String> comboBoxQuantity = new JComboBox();
		comboBoxQuantity.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxQuantity.setBounds(157, 144, 133, 26);
		panel3.add(comboBoxQuantity);
		
		JLabel label_3 = new JLabel("Quantity bought:");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setFont(new Font("Arial", Font.PLAIN, 18));
		label_3.setBounds(-8, 139, 153, 30);
		panel3.add(label_3);
		
		JLabel label_4 = new JLabel("Date of order:");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setFont(new Font("Arial", Font.PLAIN, 18));
		label_4.setBounds(-8, 182, 153, 30);
		panel3.add(label_4);
		
		JDateChooser dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(157, 186, 133, 26);
		panel3.add(dateChooser);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Arial", Font.PLAIN, 20));
		btnAdd.setBounds(91, 222, 133, 38);
		panel3.add(btnAdd);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Edit Client", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblIdOfClient = new JLabel("ID of client to edit: ");
		lblIdOfClient.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfClient.setBounds(0, 31, 167, 26);
		lblIdOfClient.setFont(new Font("Arial", Font.PLAIN, 19));
		panel.add(lblIdOfClient);
		
		JLabel lblWhatDoYou = new JLabel("Change client's:");
		lblWhatDoYou.setHorizontalAlignment(SwingConstants.RIGHT);
		lblWhatDoYou.setFont(new Font("Arial", Font.PLAIN, 19));
		lblWhatDoYou.setBounds(-18, 72, 179, 26);
		panel.add(lblWhatDoYou);
		
		JComboBox comboBoxChange = new JComboBox();
		comboBoxChange.setModel(new DefaultComboBoxModel(new String[] {"", "name", "e-mail", "name & e-mail"}));
		comboBoxChange.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxChange.setBounds(173, 73, 111, 26);
		panel.add(comboBoxChange);
		
		JComboBox comboBoxIdClient = new JComboBox();
		comboBoxIdClient.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdClient.setBounds(173, 32, 55, 26);
		panel.add(comboBoxIdClient);
		
		JLabel lblNewName = new JLabel("New name:");
		lblNewName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewName.setFont(new Font("Arial", Font.PLAIN, 19));
		lblNewName.setBounds(-9, 124, 111, 26);
		panel.add(lblNewName);
		
		JLabel lblNewEmail = new JLabel("New e-mail:");
		lblNewEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewEmail.setFont(new Font("Arial", Font.PLAIN, 19));
		lblNewEmail.setBounds(-2, 163, 104, 26);
		panel.add(lblNewEmail);
		
		txtFieldClientNameEdit = new JTextField();
		txtFieldClientNameEdit.setEnabled(false);
		txtFieldClientNameEdit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientNameEdit.setBounds(114, 128, 170, 22);
		panel.add(txtFieldClientNameEdit);
		txtFieldClientNameEdit.setColumns(10);
		
		txtFieldClientEmailEdit = new JTextField();
		txtFieldClientEmailEdit.setEnabled(false);
		txtFieldClientEmailEdit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientEmailEdit.setColumns(10);
		txtFieldClientEmailEdit.setBounds(114, 167, 170, 22);
		panel.add(txtFieldClientEmailEdit);
		
		JButton btnEditClient = new JButton("Edit Client");
		btnEditClient.setEnabled(false);
		btnEditClient.setBounds(84, 202, 144, 48);
		panel.add(btnEditClient);
		btnEditClient.setFont(new Font("Arial", Font.PLAIN, 20));
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Delete Client", null, panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblIdOfClient_1 = new JLabel("ID of client to delete: ");
		lblIdOfClient_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfClient_1.setFont(new Font("Arial", Font.PLAIN, 19));
		lblIdOfClient_1.setBounds(12, 94, 199, 26);
		panel_1.add(lblIdOfClient_1);
		
		JComboBox comboBoxIdClientToDelete = new JComboBox();
		comboBoxIdClientToDelete.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdClientToDelete.setBounds(215, 95, 55, 26);
		panel_1.add(comboBoxIdClientToDelete);
		
		JButton btnDeleteClient = new JButton("Delete Client");
		btnDeleteClient.setFont(new Font("Arial", Font.PLAIN, 20));
		btnDeleteClient.setBounds(70, 167, 171, 48);
		panel_1.add(btnDeleteClient);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Add client", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblClientsName = new JLabel("Client's name:");
		lblClientsName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClientsName.setFont(new Font("Arial", Font.PLAIN, 19));
		lblClientsName.setBounds(0, 74, 130, 26);
		panel_2.add(lblClientsName);
		
		JLabel lblClientsEmail = new JLabel("Client's e-mail:");
		lblClientsEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		lblClientsEmail.setFont(new Font("Arial", Font.PLAIN, 19));
		lblClientsEmail.setBounds(7, 113, 123, 26);
		panel_2.add(lblClientsEmail);
		
		txtFieldClientNameAdd = new JTextField();
		txtFieldClientNameAdd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientNameAdd.setColumns(10);
		txtFieldClientNameAdd.setBounds(142, 81, 148, 22);
		panel_2.add(txtFieldClientNameAdd);
		
		txtFieldClientEmailAdd = new JTextField();
		txtFieldClientEmailAdd.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtFieldClientEmailAdd.setColumns(10);
		txtFieldClientEmailAdd.setBounds(142, 116, 148, 22);
		panel_2.add(txtFieldClientEmailAdd);
		
		JButton btnAddClient = new JButton("Add client");
		btnAddClient.setFont(new Font("Arial", Font.PLAIN, 20));
		btnAddClient.setBounds(93, 166, 144, 48);
		panel_2.add(btnAddClient);

		
		mnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		showCatalog();
		fillComboBoxIdClient(comboBoxIdClient);
		fillComboBoxIdClient(comboBoxIdClientToDelete);
		fillProductsComboBox(comboBoxBoughtProduct);
		fillQuantityComboBox(comboBoxBoughtProduct, comboBoxQuantity);
		
		JButton btnNewOrder = new JButton("Add new order");
		btnNewOrder.setBounds(351, 205, 172, 42);
		getContentPane().add(btnNewOrder);
		
		btnNewOrder.setFont(new Font("Arial", Font.PLAIN, 20));
		
		btnNewOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewOrderWindow newOrder = new NewOrderWindow(username, category);
				newOrder.setVisible(true);
				
			}
		});	
		
		btnRefreshTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showCatalog();
				fillComboBoxIdClient(comboBoxIdClient);
				fillComboBoxIdClient(comboBoxIdClientToDelete);
			}
		});
		
		comboBoxChange.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboBoxChange.getSelectedItem().equals("name")) {
					txtFieldClientNameEdit.setEnabled(true);
					txtFieldClientEmailEdit.setEnabled(false);
					btnEditClient.setEnabled(true);
				}
				else if(comboBoxChange.getSelectedItem().equals("e-mail")) {
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
	
	             
	            Representative.addSale(newClient);		
				
				if(newClient.isFirstTimeClient())
				{
					JOptionPane.showMessageDialog(null, "Order has been added");
					fillProductsComboBox(comboBoxBoughtProduct);
					fillQuantityComboBox(comboBoxBoughtProduct, comboBoxQuantity);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Invalid information");
				}
			}
		});
		
		btnEditClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				
				Client clientToEdit;
				
				if(comboBoxChange.getSelectedItem().equals("name")) {
					clientToEdit = new Client.Builder()
							.idOfClient(Integer.parseInt((String) comboBoxIdClient.getSelectedItem()))
							.name(txtFieldClientNameEdit.getText())
							.email(null)
							.build();
					
					Representative.editClient(clientToEdit);
				}
				
				else if(comboBoxChange.getSelectedItem().equals("e-mail")) {
					clientToEdit = new Client.Builder()
							.idOfClient(Integer.parseInt((String) comboBoxIdClient.getSelectedItem()))
							.name(null)
							.email(txtFieldClientEmailEdit.getText())
							.build();
					
					
					System.out.println(clientToEdit.isFirstTimeClient());
					if(clientToEdit.isFirstTimeClient()) {
						Representative.editClient(clientToEdit);
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
						Representative.editClient(clientToEdit);
					}
					else {
						JOptionPane.showMessageDialog(getContentPane(), "Not unique e-mail");
					}

				}
			}
		});
		
		btnDeleteClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = Integer.parseInt((String) comboBoxIdClientToDelete.getSelectedItem());	
				String email = getEmailOfClientById(id);
				
				Client clientToDelete = new Client.Builder()
											.idOfClient(id)
											.name(txtFieldClientNameEdit.getText())
											.email(email)
											.build();
					
				Representative.deleteClient(clientToDelete);
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
					Representative.addClient(clientToAdd);
				}
				else {
					JOptionPane.showMessageDialog(getContentPane(), "Not unique e-mail");
				}
				Representative.addClient(clientToAdd);
			}
		});
	}
	
	
	
	public String getEmailOfClientById(int id)
	{
		
		try {
			ResultSet rs = DBConnection.getData("SELECT email FROM allclients WHERE id_client = " + id);
			
			while(rs.next())
			{
				String email =  rs.getString("email");
				System.out.println("EMAIL IN RS: " + email);
				return email;
			}
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
		
	public void showCatalog()
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
			
			ResultSet rs = DBConnection.getData("SELECT * FROM allsales WHERE category = '" + this.category + "'");
			
	        while(rs.next())
	        {  	
	        	
	        	//it throws nullpoint exception when I do it with Builder design pattern
	        	OrderControl o = new OrderControl(
	        				rs.getInt("id_sale"), 
	        				rs.getString("name"),
	        				rs.getString("email"),
	        				rs.getString("product"), 
	        				rs.getInt("quantity"), 
	        				rs.getDouble("price"), 
	        				rs.getDate("date")
	        				);
	        	
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
	        
	        
		}catch(SQLException e){
			e.printStackTrace();
		}
	
	}
	
	public void fillComboBoxIdClient(JComboBox<String> ComboBoxIdClient)
	{		
		for(OrderControl clientInCatalog : clientsFromCatalog)
		{
			ComboBoxIdClient.addItem(""+clientInCatalog.getId());
		}
		
	}
	
	public void fillProductsComboBox(JComboBox<String> comboBoxBoughtProduct)
	{
		try
		{     
			ResultSet rs = DBConnection.getData("SELECT * FROM allproducts WHERE category = '" + this.category + "'");
			
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
}
