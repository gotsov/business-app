package com.businessapplication;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;

public class AdminWindow extends JFrame {

	private JPanel contentPane;
	private static JTable tableProducts;
	private static JTable tableRepresentatives;
	private JTextField textFieldProductName;
	private JTextField textFieldNewCategory;
	private JTextField textFieldQuantity;
	private JTextField textFieldPrice;
	private JTextField textFieldEdit;
	
	private JTextField textFieldNameRep;
	private JTextField textFieldCategoryRep;
	private JTextField textFieldUsernameRep;
	private JTextField textFieldEditRep;
	
	static ArrayList<Product> listProducts = new ArrayList<>();
	static ArrayList<Representative> listRepresentatives = new ArrayList<>();
	static ArrayList<String> allCategories = new ArrayList<>();
	static ArrayList<String> categoriesWithoutRepresentative = new ArrayList<>();
	static ArrayList<String> categoriesWithRepresentative = new ArrayList<>();
	
	/**
	 * Create the frame.
	 */
	public AdminWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 902, 558);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("User");
		mnNewMenu.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		menuBar.add(mnNewMenu);
		
		JMenuItem mnLogout = new JMenuItem("Log out");
		mnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		mnNewMenu.add(mnLogout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panelProducts = new JPanel();
		tabbedPane.addTab("Products", null, panelProducts, null);
		panelProducts.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Representatives", null, panel_1, null);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(23, 13, 552, 122);
		panel_1.add(scrollPane_1);
		
		tableRepresentatives = new JTable();
		scrollPane_1.setViewportView(tableRepresentatives);
		tableRepresentatives.setFont(new Font("Arial", Font.PLAIN, 16));
		tableRepresentatives.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id", "name", "username", "category", "sales", "profit (lv.)"
			}
		));
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Analize", null, panel_2, null);
		panel_2.setLayout(null);
		
		TableColumnModel columnModel2 = tableRepresentatives.getColumnModel();
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		tableRepresentatives.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.RIGHT );
		tableRepresentatives.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tableRepresentatives.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		
		columnModel2.getColumn(0).setPreferredWidth(40);
		columnModel2.getColumn(1).setPreferredWidth(120);
		columnModel2.getColumn(2).setPreferredWidth(120);
		columnModel2.getColumn(3).setPreferredWidth(80);
		columnModel2.getColumn(4).setPreferredWidth(40);
		columnModel2.getColumn(5).setPreferredWidth(60);
		tableRepresentatives.setRowHeight(30);
		
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Add representative", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_5.setBounds(23, 150, 270, 272);
		panel_1.add(panel_5);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setFont(new Font("Arial", Font.PLAIN, 18));
		lblName.setBounds(0, 15, 122, 30);
		panel_5.add(lblName);
		
		textFieldNameRep = new JTextField();
		textFieldNameRep.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldNameRep.setColumns(10);
		textFieldNameRep.setBounds(122, 17, 133, 26);
		panel_5.add(textFieldNameRep);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setFont(new Font("Arial", Font.PLAIN, 18));
		lblUsername.setBounds(10, 57, 112, 30);
		panel_5.add(lblUsername);
		
		JButton btnAddRepresentative = new JButton("Add");
		btnAddRepresentative.setFont(new Font("Arial", Font.PLAIN, 20));
		btnAddRepresentative.setBounds(55, 221, 133, 38);
		panel_5.add(btnAddRepresentative);
		
		JLabel lblCategory_1 = new JLabel("Category:");
		lblCategory_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCategory_1.setFont(new Font("Arial", Font.PLAIN, 18));
		lblCategory_1.setBounds(0, 100, 122, 30);
		panel_5.add(lblCategory_1);
		
		textFieldCategoryRep = new JTextField();
		textFieldCategoryRep.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldCategoryRep.setColumns(10);
		textFieldCategoryRep.setBounds(122, 100, 133, 26);
		panel_5.add(textFieldCategoryRep);
		
		textFieldUsernameRep = new JTextField();
		textFieldUsernameRep.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldUsernameRep.setColumns(10);
		textFieldUsernameRep.setBounds(122, 60, 133, 26);
		panel_5.add(textFieldUsernameRep);
		
		JPanel panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Delete representative", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_6.setBounds(305, 150, 270, 272);
		panel_1.add(panel_6);
		
		JButton btnDeleteRepresentative = new JButton("Delete");
		btnDeleteRepresentative.setFont(new Font("Arial", Font.PLAIN, 20));
		btnDeleteRepresentative.setBounds(67, 221, 133, 38);
		panel_6.add(btnDeleteRepresentative);
		
		JLabel lblIdOfRep_1 = new JLabel("ID of rep. to delete: ");
		lblIdOfRep_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfRep_1.setFont(new Font("Arial", Font.PLAIN, 19));
		lblIdOfRep_1.setBounds(-17, 103, 200, 26);
		panel_6.add(lblIdOfRep_1);
		
		JComboBox<String> comboBoxIdRepresentativeToDelete = new JComboBox();
		comboBoxIdRepresentativeToDelete.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdRepresentativeToDelete.setBounds(179, 104, 55, 26);
		panel_6.add(comboBoxIdRepresentativeToDelete);
		
		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Edit representative", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_7.setBounds(585, 150, 270, 272);
		panel_1.add(panel_7);
		
		JButton btnEditRepresentative = new JButton("Edit");
		btnEditRepresentative.setFont(new Font("Arial", Font.PLAIN, 20));
		btnEditRepresentative.setBounds(75, 221, 133, 38);
		panel_7.add(btnEditRepresentative);
		
		JLabel lblIdOfRep = new JLabel("ID of rep. to edit: ");
		lblIdOfRep.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfRep.setFont(new Font("Arial", Font.PLAIN, 19));
		lblIdOfRep.setBounds(0, 27, 200, 26);
		panel_7.add(lblIdOfRep);
		
		JComboBox<String> comboBoxIdRepresentative = new JComboBox();
		comboBoxIdRepresentative.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdRepresentative.setBounds(194, 28, 55, 26);
		panel_7.add(comboBoxIdRepresentative);
		
		JLabel label_7 = new JLabel("Edit:");
		label_7.setHorizontalAlignment(SwingConstants.RIGHT);
		label_7.setFont(new Font("Arial", Font.PLAIN, 19));
		label_7.setBounds(44, 66, 61, 26);
		panel_7.add(label_7);
		
		JComboBox<String> comboBoxEditRepresentative = new JComboBox();
		comboBoxEditRepresentative.setModel(new DefaultComboBoxModel(new String[] {"name", "username", "category"}));
		comboBoxEditRepresentative.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxEditRepresentative.setBounds(112, 67, 111, 26);
		panel_7.add(comboBoxEditRepresentative);
		
		JLabel lblEditTextRep = new JLabel("New name:");
		lblEditTextRep.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEditTextRep.setFont(new Font("Arial", Font.PLAIN, 18));
		lblEditTextRep.setBounds(-12, 105, 146, 30);
		panel_7.add(lblEditTextRep);
		
		textFieldEditRep = new JTextField();
		textFieldEditRep.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldEditRep.setColumns(10);
		textFieldEditRep.setBounds(134, 105, 124, 26);
		panel_7.add(textFieldEditRep);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(587, 43, 268, 92);
		panel_1.add(scrollPane_2);
		
		DefaultListModel<String> model = new DefaultListModel<>();
		JList listNoRep = new JList<>(model);
		listNoRep.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		scrollPane_2.setViewportView(listNoRep);
		
		JLabel lblCategoriesWithNo = new JLabel("Categories with no representatives:");
		lblCategoriesWithNo.setHorizontalAlignment(SwingConstants.LEFT);
		lblCategoriesWithNo.setFont(new Font("Arial", Font.PLAIN, 17));
		lblCategoriesWithNo.setBounds(587, 13, 255, 30);
		panel_1.add(lblCategoriesWithNo);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(186, 13, 470, 124);
		panelProducts.add(scrollPane);
		
		tableProducts = new JTable();
		scrollPane.setViewportView(tableProducts);
		tableProducts.setFont(new Font("Arial", Font.PLAIN, 16));
		tableProducts.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id", "name", "category", "quantity", "price"
			}
		));
		
		centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		tableProducts.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		
		centerRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment( JLabel.RIGHT );
		tableProducts.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tableProducts.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		
		TableColumnModel columnModel1 = tableProducts.getColumnModel();
		columnModel1.getColumn(0).setPreferredWidth(40);
		columnModel1.getColumn(1).setPreferredWidth(120);
		columnModel1.getColumn(2).setPreferredWidth(120);
		columnModel1.getColumn(3).setPreferredWidth(80);
		columnModel1.getColumn(4).setPreferredWidth(40);
		tableProducts.setRowHeight(30);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Add product", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(23, 150, 270, 272);
		panelProducts.add(panel);
		panel.setLayout(null);
		
		JLabel lblProductName = new JLabel("Product name:");
		lblProductName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProductName.setFont(new Font("Arial", Font.PLAIN, 18));
		lblProductName.setBounds(0, 15, 122, 30);
		panel.add(lblProductName);
		
		textFieldProductName = new JTextField();
		textFieldProductName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldProductName.setColumns(10);
		textFieldProductName.setBounds(122, 17, 133, 26);
		panel.add(textFieldProductName);
		
		JLabel lblCategory = new JLabel("Category:");
		lblCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCategory.setFont(new Font("Arial", Font.PLAIN, 18));
		lblCategory.setBounds(10, 58, 112, 30);
		panel.add(lblCategory);
		
		JButton btnAddProduct = new JButton("Add");
		btnAddProduct.setFont(new Font("Arial", Font.PLAIN, 20));
		btnAddProduct.setBounds(55, 221, 133, 38);
		panel.add(btnAddProduct);
		
		JLabel lblNewCategory = new JLabel("New category:");
		lblNewCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewCategory.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewCategory.setBounds(0, 100, 122, 30);
		panel.add(lblNewCategory);
		
		textFieldNewCategory = new JTextField();
		textFieldNewCategory.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldNewCategory.setColumns(10);
		textFieldNewCategory.setBounds(122, 100, 133, 26);
		panel.add(textFieldNewCategory);
		
		JComboBox comboBoxCategory = new JComboBox();
		comboBoxCategory.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxCategory.setBounds(122, 58, 133, 26);
		panel.add(comboBoxCategory);
		
		JLabel lblQuantity = new JLabel("Quantity:");
		lblQuantity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQuantity.setFont(new Font("Arial", Font.PLAIN, 18));
		lblQuantity.setBounds(0, 139, 122, 30);
		panel.add(lblQuantity);
		
		textFieldQuantity = new JTextField();
		textFieldQuantity.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldQuantity.setColumns(10);
		textFieldQuantity.setBounds(122, 139, 133, 26);
		panel.add(textFieldQuantity);
		
		textFieldPrice = new JTextField();
		textFieldPrice.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldPrice.setColumns(10);
		textFieldPrice.setBounds(122, 178, 133, 26);
		panel.add(textFieldPrice);
		
		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPrice.setFont(new Font("Arial", Font.PLAIN, 18));
		lblPrice.setBounds(0, 178, 122, 30);
		panel.add(lblPrice);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Delete product", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(305, 150, 270, 272);
		panelProducts.add(panel_3);
		panel_3.setLayout(null);
		
		JButton btnDeleteClient = new JButton("Delete");
		btnDeleteClient.setFont(new Font("Arial", Font.PLAIN, 20));
		btnDeleteClient.setBounds(67, 221, 133, 38);
		panel_3.add(btnDeleteClient);
		
		JLabel lblIdOfProduct = new JLabel("ID of product to delete: ");
		lblIdOfProduct.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfProduct.setFont(new Font("Arial", Font.PLAIN, 19));
		lblIdOfProduct.setBounds(9, 104, 200, 26);
		panel_3.add(lblIdOfProduct);
		
		JComboBox comboBoxIdProductToDelete = new JComboBox();
		comboBoxIdProductToDelete.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdProductToDelete.setBounds(203, 105, 55, 26);
		panel_3.add(comboBoxIdProductToDelete);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Edit product", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.setBounds(585, 150, 270, 272);
		panelProducts.add(panel_4);
		panel_4.setLayout(null);
		
		JButton btnEditProduct = new JButton("Edit");
		btnEditProduct.setFont(new Font("Arial", Font.PLAIN, 20));
		btnEditProduct.setBounds(75, 221, 133, 38);
		panel_4.add(btnEditProduct);
		
		JLabel lblIdOfProduct_1 = new JLabel("ID of product to edit: ");
		lblIdOfProduct_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfProduct_1.setFont(new Font("Arial", Font.PLAIN, 19));
		lblIdOfProduct_1.setBounds(0, 27, 200, 26);
		panel_4.add(lblIdOfProduct_1);
		
		JComboBox comboBoxIdProduct = new JComboBox();
		comboBoxIdProduct.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdProduct.setBounds(194, 28, 55, 26);
		panel_4.add(comboBoxIdProduct);
		
		JLabel lblEdit = new JLabel("Edit:");
		lblEdit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEdit.setFont(new Font("Arial", Font.PLAIN, 19));
		lblEdit.setBounds(44, 66, 61, 26);
		panel_4.add(lblEdit);
		
		JComboBox comboBoxEdit = new JComboBox();
		comboBoxEdit.setModel(new DefaultComboBoxModel(new String[] {"name", "category", "quantity", "price"}));
		comboBoxEdit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxEdit.setBounds(112, 67, 111, 26);
		panel_4.add(comboBoxEdit);
		
		JLabel lblEditText = new JLabel("New name:");
		lblEditText.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEditText.setFont(new Font("Arial", Font.PLAIN, 18));
		lblEditText.setBounds(-12, 105, 146, 30);
		panel_4.add(lblEditText);
		
		textFieldEdit = new JTextField();
		textFieldEdit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldEdit.setColumns(10);
		textFieldEdit.setBounds(134, 105, 124, 26);
		panel_4.add(textFieldEdit);
		
		
		mnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		comboBoxEdit.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboBoxEdit.getSelectedItem().equals("name")) {
					lblEditText.setText("New name:");
				}
				else if(comboBoxEdit.getSelectedItem().equals("category")) {
					lblEditText.setText("New category:");
				}
				else if(comboBoxEdit.getSelectedItem().equals("quantity")) {
					lblEditText.setText("New quantity:");
				}
				else if(comboBoxEdit.getSelectedItem().equals("price")) {
					lblEditText.setText("New price:");
				}
			}
		});
		
		comboBoxEditRepresentative.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				if(comboBoxEditRepresentative.getSelectedItem().equals("name")) {
					lblEditTextRep.setText("New name:");
				}
				else if(comboBoxEditRepresentative.getSelectedItem().equals("category")) {
					lblEditTextRep.setText("New category:");
				}
				else if(comboBoxEditRepresentative.getSelectedItem().equals("username")) {
					lblEditTextRep.setText("New username:");
				}
			}
		});
		
		btnAddProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Product newProduct;
					
					if(comboBoxCategory.getSelectedItem().equals("new category")) {
						
						newProduct = new Product.Builder().name(textFieldProductName.getText())
														  .category(textFieldNewCategory.getText())
														  .quantity(Integer.parseInt((String) textFieldQuantity.getText()))
														  .price(Double.parseDouble((String) textFieldPrice.getText()))
														  .build();
					}
					else {
						newProduct = new Product.Builder().name(textFieldProductName.getText())
													      .category((String)comboBoxCategory.getSelectedItem())
													      .quantity(Integer.parseInt((String) textFieldQuantity.getText()))
													      .price(Double.parseDouble((String) textFieldPrice.getText()))
													      .build();
					}
					AdminController.addProduct(newProduct);
					
					JOptionPane.showMessageDialog(null, "Added [" + newProduct.getName() + ", x" + newProduct.getQuantity() + "]");
					
				} catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Incorrect input");
				}

				textFieldNewCategory.setText("");
				textFieldQuantity.setText("");
				textFieldProductName.setText("");
				textFieldPrice.setText("");
						
				updateTablesAndComboboxes(comboBoxIdProduct, comboBoxIdProductToDelete, comboBoxIdRepresentative, 
											comboBoxIdRepresentativeToDelete, comboBoxCategory, model);
			}
		});
		

		btnDeleteClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int id = Integer.parseInt((String)comboBoxIdProductToDelete.getSelectedItem());
				
				//throws null pointer exception when using builder
				
//				Product productToDelete = new Product.Builder().id(id)
//															   .build();
				
				Product productToDelete = new Product(id);
				
				AdminController.deleteProduct(productToDelete);
				
				JOptionPane.showMessageDialog(null, "Product with [id = " + productToDelete.getId() + "] has been deleted");
				
				updateTablesAndComboboxes(comboBoxIdProduct, comboBoxIdProductToDelete, comboBoxIdRepresentative, 
									comboBoxIdRepresentativeToDelete, comboBoxCategory, model);
			}
		});
		

		btnEditProduct.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					Product productToEdit = null;
					int idOfProductToEdit = Integer.parseInt((String)comboBoxIdProduct.getSelectedItem());
					
					for(Product p : listProducts)
					{
						if(p.getId() == idOfProductToEdit)
						{
							productToEdit = p;
							break;
						}
					}
					
					if(comboBoxEdit.getSelectedItem().equals("name"))
					{
						productToEdit.setName(textFieldEdit.getText());
						AdminController.editFieldProduct(productToEdit, "name");
					}
					else if(comboBoxEdit.getSelectedItem().equals("category"))
					{
						productToEdit.setCategory(textFieldEdit.getText());
						AdminController.editFieldProduct(productToEdit, "category");
					}
					else if(comboBoxEdit.getSelectedItem().equals("quantity"))
					{
						productToEdit.setQuantity(Integer.parseInt(textFieldEdit.getText()));
						AdminController.editFieldProduct(productToEdit, "quantity");
					}
					else if(comboBoxEdit.getSelectedItem().equals("price"))
					{
						productToEdit.setPrice(Double.parseDouble(textFieldEdit.getText()));
						AdminController.editFieldProduct(productToEdit, "price");
					}
					
				} catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Incorrect input");
				}
				
				updateTablesAndComboboxes(comboBoxIdProduct, comboBoxIdProductToDelete, comboBoxIdRepresentative, 
										comboBoxIdRepresentativeToDelete, comboBoxCategory, model);
			}
		});
		
		btnAddRepresentative.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Representative represenatativeToAdd = new Representative.Builder().name(textFieldNameRep.getText())
																				  .username(textFieldUsernameRep.getText())
																				  .category(textFieldCategoryRep.getText())
																				  .numberOfSales(0)
																				  .profit(0)
																				  .build();
				
				AdminController.addRepresentative(represenatativeToAdd);
				
				JOptionPane.showMessageDialog(null, "Added [" + represenatativeToAdd.getName() + " (" + represenatativeToAdd.getUsername() + "), "
																							+ "category: " + represenatativeToAdd.getCategory() + ".");
				
				updateTablesAndComboboxes(comboBoxIdProduct, comboBoxIdProductToDelete, comboBoxIdRepresentative, 
									comboBoxIdRepresentativeToDelete, comboBoxCategory, model);
			}
			
		});
		

		btnDeleteRepresentative.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = Integer.parseInt((String)comboBoxIdRepresentativeToDelete.getSelectedItem());
				
				Representative represenatativeToDelete = new Representative.Builder().id(id)
																					 .build();
				
				AdminController.deleteRepresentative(represenatativeToDelete);
				
				JOptionPane.showMessageDialog(null, "Representative with [id = " + represenatativeToDelete.getId() + "] has been deleted");
				
				updateTablesAndComboboxes(comboBoxIdProduct, comboBoxIdProductToDelete, comboBoxIdRepresentative, 
										comboBoxIdRepresentativeToDelete, comboBoxCategory, model);
				
			}
		});
		

		btnEditRepresentative.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					Representative representativeToEdit = null;
					int idOfProductToEdit = Integer.parseInt((String)comboBoxIdRepresentative.getSelectedItem());
					
					for(Representative rep : listRepresentatives)
					{
						if(rep.getId() == idOfProductToEdit)
						{
							representativeToEdit = rep;
							break;
						}
					}
					
					if(comboBoxEditRepresentative.getSelectedItem().equals("name"))
					{
						representativeToEdit.setName(textFieldEditRep.getText());
						AdminController.editFieldRepresentative(representativeToEdit, "name");
					}
					else if(comboBoxEditRepresentative.getSelectedItem().equals("username"))
					{
						representativeToEdit.setUsername(textFieldEditRep.getText());
						AdminController.editFieldRepresentative(representativeToEdit, "username");
					}
					else if(comboBoxEditRepresentative.getSelectedItem().equals("category"))
					{
						representativeToEdit.setCategory(textFieldEditRep.getText());
						AdminController.editFieldRepresentative(representativeToEdit, "category");
					}

					
				} catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Incorrect input");
				}
				
				updateTablesAndComboboxes(comboBoxIdProduct, comboBoxIdProductToDelete, comboBoxIdRepresentative, 
										comboBoxIdRepresentativeToDelete, comboBoxCategory, model);
			}
		});
		
		
		updateTablesAndComboboxes(comboBoxIdProduct, comboBoxIdProductToDelete, comboBoxIdRepresentative, 
									comboBoxIdRepresentativeToDelete, comboBoxCategory, model);

	}
	
	public static void updateTablesAndComboboxes(JComboBox<String> comboBoxIdProduct, JComboBox<String> comboBoxIdProductToDelete, 
											JComboBox<String> comboBoxIdRepresentative, JComboBox<String> comboBoxIdRepresentativeToDelete,
											JComboBox<String> comboBoxCategory, DefaultListModel<String> model)
	{
		loadTablesAdmin();
		fillComboBoxProductsId(comboBoxIdProduct);
		fillComboBoxProductsId(comboBoxIdProductToDelete);
		fillComboBoxRepresentativeId(comboBoxIdRepresentative);
		fillComboBoxRepresentativeId(comboBoxIdRepresentativeToDelete);
		getAllCategories(comboBoxCategory);
		findCategoriesWithNoRepresentative(model);
		
	}
	public static void loadTablesAdmin()
	{
		try
		{
			DefaultTableModel modelProducts = (DefaultTableModel)tableProducts.getModel(); 
			DefaultTableModel modelRepresentatives = (DefaultTableModel)tableRepresentatives.getModel(); 
			
			modelProducts.setRowCount(0);		
			modelRepresentatives.setRowCount(0);
			listProducts.clear();
			listRepresentatives.clear();
			
			ResultSet rs = DBConnection.getData("SELECT * FROM allproducts");
			
	        while(rs.next())
	        {  	
	        	
	        	Product p = new Product.Builder().id(rs.getInt("id_prod"))
	        									 .name(rs.getString("name"))
	        									 .category(rs.getString("category"))
	        									 .quantity(rs.getInt("quantity"))
	        									 .price(rs.getDouble("price"))
	        									 .build();
	        	
	        	listProducts.add(p);
	        }
	        
			Object[] row = new Object[7];
	        
	        for (int i = 0; i < listProducts.size(); i++) {
				row[0] = listProducts.get(i).getId();
				row[1] = listProducts.get(i).getName();
				row[2] = listProducts.get(i).getCategory();
				row[3] = listProducts.get(i).getQuantity();
				row[4] = listProducts.get(i).getPrice();
	
				modelProducts.addRow(row);
			}
	        
	        
			rs = DBConnection.getData("SELECT * FROM allrepresentatives");
			
	        while(rs.next())
	        {  	
	        	
	        	Representative r = new Representative.Builder().id(rs.getInt("id_rep"))
	        												   .name(rs.getString("name"))
	        												   .username(rs.getString("username"))
	        												   .category(rs.getString("category"))
	        												   .numberOfSales(rs.getInt("numberofsales"))
	        												   .profit(rs.getDouble("profit"))
	        												   .build();
	        	
	        	listRepresentatives.add(r);
	        }
	        
			Object[] row2 = new Object[7];
	        
	        for (int i = 0; i < listRepresentatives.size(); i++) {
				row2[0] = listRepresentatives.get(i).getId();
				row2[1] = listRepresentatives.get(i).getName();
				row2[2] = listRepresentatives.get(i).getUsername();
				row2[3] = listRepresentatives.get(i).getCategory();
				row2[4] = listRepresentatives.get(i).getNumberOfSales();
				row2[5] = listRepresentatives.get(i).getProfit();
	
				modelRepresentatives.addRow(row2);
			}   
	        
		}catch(SQLException e){
			e.printStackTrace();
		}
	
	}
	
	public static void getAllCategories(JComboBox<String>  comboBoxCategory)
	{
		try {
			ResultSet rs = DBConnection.getData("SELECT * FROM allrepresentatives");
				
			String category;
			while(rs.next())
			{
				category = rs.getString("category");
				
				if(!allCategories.contains(category))
				{
					allCategories.add(category);
				}
			}
			
			fillComboBoxCategory(comboBoxCategory);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void fillComboBoxProductsId(JComboBox<String> comboBoxIdProduct)
	{		
		comboBoxIdProduct.removeAllItems();
		allCategories.clear();
		
		for(Product product : listProducts)
		{
			comboBoxIdProduct.addItem(""+product.getId());
		}
		
	}
	
	public static void fillComboBoxRepresentativeId(JComboBox<String> comboBoxIdRepresentative)
	{		
		comboBoxIdRepresentative.removeAllItems();
		
		for(Representative rep : listRepresentatives)
		{
			comboBoxIdRepresentative.addItem(""+rep.getId());
		}
		
	}
	
	public static void fillComboBoxCategory(JComboBox<String> comboBoxCategory)
	{	
		comboBoxCategory.removeAllItems();
		for(String category : allCategories)
		{
			comboBoxCategory.addItem(category);
		}
		
		comboBoxCategory.addItem("new category");
		
	}
	
	public static void findCategoriesWithNoRepresentative(DefaultListModel<String> model)
	{
		model.clear();
		categoriesWithoutRepresentative.clear();
		categoriesWithRepresentative.clear();
		try {
			ResultSet rs = DBConnection.getData("SELECT * FROM allrepresentatives");
			
			while(rs.next())
			{
				categoriesWithRepresentative.add(rs.getString("category"));
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

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}