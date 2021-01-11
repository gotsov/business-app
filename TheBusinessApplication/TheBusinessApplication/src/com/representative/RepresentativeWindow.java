package com.representative;

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
import com.businessapplication.Sale;
import com.businessapplication.Client.Builder;
import com.businessapplication.Product;
import com.databaseconnection.DBConnection;
import com.exceptions.NegativePriceException;
import com.exceptions.NegativeQuantityException;
import com.login.ChangePasswordWindow;
import com.login.LoginWindow;
import com.toedter.calendar.JDateChooser;

import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.sql.Connection;
import java.sql.Date;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import java.awt.BorderLayout;

public class RepresentativeWindow extends JFrame {
	
	private static JTable tableCatalog;
	private static JTable tableClients;
	private static JTable tableProducts;
	
	private static JTextField txtFieldClientNameEdit;
	private static JTextField txtFieldClientEmailEdit;
	private static JTextField txtFieldClientNameAdd;
	private static JTextField txtFieldClientEmailAdd;
	private static JTextField txtFieldClientName;
	private static JTextField txtFieldClientEmail;
	
	private static JComboBox comboBoxIdClient = new JComboBox();
	private static JComboBox comboBoxIdClientToDelete = new JComboBox();
	private static JComboBox<String> comboBoxBoughtProduct = new JComboBox();
	private static JLabel lblMaxQuantity = new JLabel("...........");
	
	private ArrayList<Sale> clientsFromCatalog  = new ArrayList<>();
	private ArrayList<Product> yourProducts = new ArrayList<>();
	private ArrayList<Sale> catalog = new ArrayList<>();
	private Representative thisRepresentative;
	private static SalesService salesService = new SalesService();
	private static ClientService clientService = new ClientService();
	private JTextField textFieldQuantityBought;
	
	/**
	 * Create the frame.
	 */
	public RepresentativeWindow(int id, String name, String username, String password, String category) {
		
		setResizable(false);
		
		Representative thisRepresentative = new Representative.Builder().id(id)
																		.name(name)
																		.username(username)
																		.category(category)
																		.password(password)
																		.build();
		
		thisRepresentative.setNumOfSales(thisRepresentative.getNumberOfSalesFromDB());
		thisRepresentative.setProfit(thisRepresentative.getProfitFromDB());
		
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
		
		JMenuItem mnChangePassword = new JMenuItem("Change password");
		mnChangePassword.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		userMenu.add(mnChangePassword);
		
				
				
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(53, 260, 406, 271);
		getContentPane().add(scrollPane_1);
			
		JLabel lblClients = new JLabel("Your clients (" + category + "):");
		lblClients.setFont(new Font("Arial", Font.PLAIN, 22));
		lblClients.setBounds(53, 224, 381, 23);
		getContentPane().add(lblClients);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		tabbedPane.setBounds(479, 224, 350, 307);
		getContentPane().add(tabbedPane);
		
		JPanel panel3 = new JPanel();
		tabbedPane.addTab("Add sale", null, panel3, null);
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
		
		
		comboBoxBoughtProduct.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxBoughtProduct.setBounds(177, 99, 133, 26);
		panel3.add(comboBoxBoughtProduct);
		
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
		
		textFieldQuantityBought = new JTextField();
		textFieldQuantityBought.setText("0");
		textFieldQuantityBought.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldQuantityBought.setColumns(10);
		textFieldQuantityBought.setBounds(177, 143, 44, 26);
		panel3.add(textFieldQuantityBought);
		
		lblMaxQuantity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMaxQuantity.setFont(new Font("Arial", Font.PLAIN, 18));
		lblMaxQuantity.setBounds(226, 139, 84, 30);
		panel3.add(lblMaxQuantity);
		
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
		
		JTabbedPane tabYourCatalog = new JTabbedPane(JTabbedPane.TOP);
		tabYourCatalog.setBounds(12, 13, 817, 191);
		getContentPane().add(tabYourCatalog);
		
		JPanel panel_3 = new JPanel();
		tabYourCatalog.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		tabYourCatalog.addTab("Your Catalog", null, panel_3, null);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_3.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel_4 = new JPanel();
		tabYourCatalog.addTab("Your Products", null, panel_4, null);
		panel_4.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(0, 0, 812, 152);
		panel_4.add(scrollPane_2);
		
		//TABLES
		
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
		
		tableProducts = new JTable();
		scrollPane_2.setViewportView(tableProducts);
		tableProducts.setFont(new Font("Arial", Font.PLAIN, 16));
		tableProducts.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id_product", "name", "category", "quantity", "price (lv.)"
			}
		) );
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		
		tableClients.setAutoCreateRowSorter(true);
		tableClients.getTableHeader().setReorderingAllowed(false);
		
		tableCatalog.setAutoCreateRowSorter(true);
		tableCatalog.getTableHeader().setReorderingAllowed(false);
		
		tableProducts.setAutoCreateRowSorter(true);
		tableProducts.getTableHeader().setReorderingAllowed(false);
		
		TableColumnModel columnModelCatalog = tableCatalog.getColumnModel();
		tableCatalog.setRowHeight(30);
	
		columnModelCatalog.getColumn(0).setPreferredWidth(40);
		columnModelCatalog.getColumn(1).setPreferredWidth(120);
		columnModelCatalog.getColumn(2).setPreferredWidth(200);
		columnModelCatalog.getColumn(3).setPreferredWidth(80);
		columnModelCatalog.getColumn(4).setPreferredWidth(40);
		columnModelCatalog.getColumn(5).setPreferredWidth(60);
		columnModelCatalog.getColumn(6).setPreferredWidth(100);
		
		tableCatalog.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tableCatalog.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		
		
		TableColumnModel columnModelClients = tableClients.getColumnModel();
		tableClients.setRowHeight(30);
		
		tableClients.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		columnModelClients = tableClients.getColumnModel();
		columnModelClients.getColumn(0).setPreferredWidth(40);
		columnModelClients.getColumn(1).setPreferredWidth(120);
		columnModelClients.getColumn(2).setPreferredWidth(200);
		
		
		TableColumnModel columnModelProducts = tableProducts.getColumnModel();
		tableProducts.setRowHeight(30);
		
		columnModelProducts.getColumn(0).setPreferredWidth(40);
		columnModelProducts.getColumn(0).setCellRenderer(centerRenderer);
		
		comboBoxBoughtProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String boughtProduct = (String)comboBoxBoughtProduct.getSelectedItem();
				int maxQuantity;
				try {
					maxQuantity = salesService.getQuantityOfProduct(boughtProduct);
					lblMaxQuantity.setText(maxQuantity + " max");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println();
				}
				
			}
		});
		
		mnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				
				LoginWindow loginWindow = new LoginWindow();
				loginWindow.setVisible(true);
			}
		});
		
		mnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChangePasswordWindow changePassWin = new ChangePasswordWindow(thisRepresentative.getId());
				changePassWin.setVisible(true);
				changePassWin.setTitle("Password - " + thisRepresentative.getName() + "(" + thisRepresentative.getUsername() + ")");
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
		

		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				java.util.Date selectedDate = dateChooser.getDate();
	            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	            String date = dateFormat.format(selectedDate); 
	            
	            String boughtProduct = (String)comboBoxBoughtProduct.getSelectedItem();
	            String quantityBoughtString = textFieldQuantityBought.getText();
	            int quantityBought = Integer.parseInt(quantityBoughtString);
				Client newClient = null;
				
				try {
		            if(category.equals("all")) {	            	
		            	String categoryOfBoughtProduct = salesService.getCategoryOfBoughtProduct(boughtProduct);
		            	
		            	newClient = new Client.Builder().name(txtFieldClientName.getText())
								   .email(txtFieldClientEmail.getText())
								   .productBought(boughtProduct)
								   .categoryOfProductBought(categoryOfBoughtProduct)
								   .quantityBought(quantityBought)
								   .dateOfSale(date)
								   .build();
		            	
		            	salesService.addSale(newClient, thisRepresentative);			        	
		            	
		            } else {
		            	newClient = new Client.Builder().name(txtFieldClientName.getText())
								   .email(txtFieldClientEmail.getText())
								   .productBought(boughtProduct)
								   .categoryOfProductBought(category)
								   .quantityBought(quantityBought)
								   .dateOfSale(date)
								   .build();
		            	
		            	salesService.addSale(newClient, thisRepresentative);	
		            }
	
					if(newClient.isFirstTimeClient())
					{
						JOptionPane.showMessageDialog(null, "Client [" + newClient.getName() + ", " + newClient.getEmail() 
						                                   + "] bought [" + newClient.getQuantityBought() + "x " + newClient.getProductBought() + "].");
	
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Invalid information");
					}
					
					updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct);
					
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error with database");
					e.printStackTrace();
				} catch (NegativeQuantityException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
			}
		});
		
		
		btnEditClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				
				Client clientToEdit;
				int idClient, idSaleByClient;
				String email;
				
				try {
					if(comboBoxChange.getSelectedItem().equals("name")) {
						clientToEdit = new Client.Builder()
								.idOfClient(Integer.parseInt((String) comboBoxIdClient.getSelectedItem()))    //in allclients
								.name(txtFieldClientNameEdit.getText())
								.email(null)
								.build();
						
						email = salesService.getEmailOfClient(clientToEdit);
						clientToEdit.setEmail(email);
						
						idClient = clientToEdit.getIdOfClient();
						idSaleByClient = salesService.getIdOfSaleWithEmail(clientToEdit);
						
						System.out.println("idClient = " + idClient);
						System.out.println("idSaleByClient = " + idSaleByClient);
						clientService.editClient(clientToEdit, "name", idClient, idSaleByClient);
						
						updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct);
					}
					
					else if(comboBoxChange.getSelectedItem().equals("email")) {
						
						clientToEdit = new Client.Builder()
								.idOfClient(Integer.parseInt((String) comboBoxIdClient.getSelectedItem()))
								.name(null)
								.email(txtFieldClientEmailEdit.getText())
								.build();
						
						Client originalClient = new Client.Builder().email(salesService.getOldEmailOfClient(clientToEdit))
																	.build();
						
						
						if(clientToEdit.isFirstTimeClient()) {
							idClient = clientToEdit.getIdOfClient();
							idSaleByClient = salesService.getIdOfSaleWithEmail(originalClient);
							
							clientService.editClient(clientToEdit, "email", idClient, idSaleByClient);
							
							updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct);
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
						
						Client originalClient = new Client.Builder().email(salesService.getOldEmailOfClient(clientToEdit))
														.build();
						
						System.out.println(clientToEdit.isFirstTimeClient());
						if(clientToEdit.isFirstTimeClient()) {
							idClient = clientToEdit.getIdOfClient();
							idSaleByClient = salesService.getIdOfSaleWithEmail(originalClient);
							
							
							clientService.editClient(clientToEdit, "name & e-mail", idClient, idSaleByClient);
							
							updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct);
						}
						else {
							JOptionPane.showMessageDialog(null, "Not unique e-mail");
						}
	
					}
				}catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error with database");
					e.printStackTrace();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NegativeQuantityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		btnDeleteClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = Integer.parseInt((String) comboBoxIdClientToDelete.getSelectedItem());	
				try {
					String email = salesService.getInfoOfClientById("email", id);
					String name = salesService.getInfoOfClientById("name", id);
					
					Client clientToDelete = new Client.Builder()
												.idOfClient(id)
												.name(name)
												.email(email)
												.build();
					
					salesService.deleteClient(clientToDelete);
				
				JOptionPane.showMessageDialog(null, "Client ["+ clientToDelete.getName() + ", " + clientToDelete.getEmail() + "] has been deleted.");
				
				updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct);
				
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error with database");
					e.printStackTrace();
				} catch (NegativeQuantityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		btnAddClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
							
				try {
					int id = salesService.getIdOfLastClient();	
					
					Client clientToAdd = new Client.Builder()
												.idOfClient(id)
												.name(txtFieldClientNameAdd.getText())
												.email(txtFieldClientEmailAdd.getText())
												.build();
					
					System.out.println(clientToAdd.isFirstTimeClient());
				
					if(clientToAdd.isFirstTimeClient()) {
						salesService.addClient(clientToAdd);
						updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct);
					}
					else {
						JOptionPane.showMessageDialog(null, "Not unique e-mail");
					}
					
					salesService.addClient(clientToAdd);
				} catch (SQLException e) {
//					JOptionPane.showMessageDialog(null, "Error with database");
//					e.printStackTrace();
				} catch (NegativeQuantityException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		});
		
		try {
			updateTablesAndComboBoxes(comboBoxIdClient, comboBoxIdClientToDelete, comboBoxBoughtProduct);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	public void updateTablesAndComboBoxes(JComboBox<String> comboBoxIdClient,JComboBox<String> comboBoxIdClientToDelete,
								JComboBox<String> comboBoxBoughtProduct) throws SQLException {
		
		loadTables();
		fillComboBoxClient(comboBoxIdClient);
		fillComboBoxClient(comboBoxIdClientToDelete);
		fillComboBoxProducts(comboBoxBoughtProduct);
		
		String boughtProduct = (String)comboBoxBoughtProduct.getSelectedItem();
		int maxQuantity = salesService.getQuantityOfProduct(boughtProduct);
		lblMaxQuantity.setText(maxQuantity + " max");
		
		txtFieldClientEmail.setText("");
		txtFieldClientName.setText("");
	}

	public void loadTables()
	{	
		DefaultTableModel modelCatalog = (DefaultTableModel)tableCatalog.getModel(); 
		DefaultTableModel modelClients = (DefaultTableModel)tableClients.getModel();
		DefaultTableModel modelProducts = (DefaultTableModel)tableProducts.getModel();
		
		int id, quantity;
        String name, email, category, product;
        double price;
        Date date;
		
		modelCatalog.setRowCount(0);		
		modelClients.setRowCount(0);
		modelProducts.setRowCount(0);
		
		catalog.clear();
		clientsFromCatalog.clear();
		yourProducts.clear();
				
		try {
			catalog = salesService.getCatalog(thisRepresentative);
		
        
	        for (int i = 0; i < catalog.size(); i++) {
				id = catalog.get(i).getId();
				name = catalog.get(i).getName();
				email = catalog.get(i).getEmail();
				product = catalog.get(i).getProduct();
				quantity = catalog.get(i).getQuantity();
				price = catalog.get(i).getPrice();
				date = catalog.get(i).getDate();
				
				Object[] data = {id, name, email, product, quantity, price, date};
				
				modelCatalog.addRow(data);
			}
	        
	        clientsFromCatalog = clientService.removeRepeatingClients(catalog);
	        
	        for (int i = 0; i < clientsFromCatalog.size(); i++) {
				id = clientsFromCatalog.get(i).getId();
				name = clientsFromCatalog.get(i).getName();
				email = clientsFromCatalog.get(i).getEmail();
				
				Object[] data = {id, name, email};
				
				modelClients.addRow(data);
			}
	        
	        yourProducts = salesService.getProductsFromYourCategory(thisRepresentative);
	        
	        for (int i = 0; i < yourProducts.size(); i++) {
				id = yourProducts.get(i).getId();
				name = yourProducts.get(i).getName();
				category = yourProducts.get(i).getCategory();
				quantity = yourProducts.get(i).getQuantity();
				price = yourProducts.get(i).getPrice();
				
				Object[] data = {id, name, category, quantity, price};
				
				modelProducts.addRow(data);
			}
        
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NegativePriceException | NegativeQuantityException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
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
		comboBoxBoughtProduct.removeAllItems();
			
		if(thisRepresentative.getCategory().equals("all")) {
			
			for(Product p : yourProducts)
			{
				comboBoxBoughtProduct.addItem(p.getName());
			}
		}
		else {	
			for(Product p : yourProducts)
			{
				if(p.getCategory().equals(thisRepresentative.getCategory()))
					comboBoxBoughtProduct.addItem(p.getName());
			}
				
		}
	}
	
}
