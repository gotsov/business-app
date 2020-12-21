package com.businessapplication;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
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

import com.mysql.cj.protocol.x.ContinuousOutputStream;

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

public class AdminWindow extends JFrame {

	private JPanel contentPane;
	private static JTable tableProducts;
	private static JTable tableRepresentatives;
	private JTextField textFieldProductName;
	private JTextField textFieldNewCategory;
	private JTextField textFieldQuantity;
	private JTextField textFieldPrice;
	private JTextField textField_4;
	
	static ArrayList<Product> listProducts = new ArrayList<>();
	static ArrayList<Representative> listRepresentatives = new ArrayList<>();
	static ArrayList<String> allCategories = new ArrayList<>();
	private JTextField textField_1;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;

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
		scrollPane_1.setBounds(174, 13, 462, 122);
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
		
		JLabel label = new JLabel("Product name:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setFont(new Font("Arial", Font.PLAIN, 18));
		label.setBounds(0, 15, 122, 30);
		panel_5.add(label);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_1.setColumns(10);
		textField_1.setBounds(122, 17, 133, 26);
		panel_5.add(textField_1);
		
		JLabel label_1 = new JLabel("Category:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Arial", Font.PLAIN, 18));
		label_1.setBounds(10, 58, 112, 30);
		panel_5.add(label_1);
		
		JButton button_1 = new JButton("Add");
		button_1.setFont(new Font("Arial", Font.PLAIN, 20));
		button_1.setBounds(55, 221, 133, 38);
		panel_5.add(button_1);
		
		JLabel label_2 = new JLabel("New category:");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setFont(new Font("Arial", Font.PLAIN, 18));
		label_2.setBounds(0, 100, 122, 30);
		panel_5.add(label_2);
		
		textField_5 = new JTextField();
		textField_5.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_5.setEnabled(false);
		textField_5.setColumns(10);
		textField_5.setBounds(122, 100, 133, 26);
		panel_5.add(textField_5);
		
		JComboBox<String> comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox.setBounds(122, 58, 133, 26);
		panel_5.add(comboBox);
		
		JLabel label_3 = new JLabel("Quantity:");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setFont(new Font("Arial", Font.PLAIN, 18));
		label_3.setBounds(0, 139, 122, 30);
		panel_5.add(label_3);
		
		textField_6 = new JTextField();
		textField_6.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_6.setColumns(10);
		textField_6.setBounds(122, 139, 133, 26);
		panel_5.add(textField_6);
		
		textField_7 = new JTextField();
		textField_7.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_7.setColumns(10);
		textField_7.setBounds(122, 178, 133, 26);
		panel_5.add(textField_7);
		
		JLabel label_4 = new JLabel("Price:");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setFont(new Font("Arial", Font.PLAIN, 18));
		label_4.setBounds(0, 178, 122, 30);
		panel_5.add(label_4);
		
		JPanel panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Delete representative", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_6.setBounds(305, 150, 270, 272);
		panel_1.add(panel_6);
		
		JButton button_2 = new JButton("Delete");
		button_2.setFont(new Font("Arial", Font.PLAIN, 20));
		button_2.setBounds(67, 221, 133, 38);
		panel_6.add(button_2);
		
		JLabel label_5 = new JLabel("ID of product to delete: ");
		label_5.setHorizontalAlignment(SwingConstants.RIGHT);
		label_5.setFont(new Font("Arial", Font.PLAIN, 19));
		label_5.setBounds(9, 104, 200, 26);
		panel_6.add(label_5);
		
		JComboBox<String> comboBox_1 = new JComboBox();
		comboBox_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox_1.setBounds(203, 105, 55, 26);
		panel_6.add(comboBox_1);
		
		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Edit representative", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_7.setBounds(585, 150, 270, 272);
		panel_1.add(panel_7);
		
		JButton button_3 = new JButton("Edit");
		button_3.setFont(new Font("Arial", Font.PLAIN, 20));
		button_3.setBounds(75, 221, 133, 38);
		panel_7.add(button_3);
		
		JLabel label_6 = new JLabel("ID of product to edit: ");
		label_6.setHorizontalAlignment(SwingConstants.RIGHT);
		label_6.setFont(new Font("Arial", Font.PLAIN, 19));
		label_6.setBounds(0, 27, 200, 26);
		panel_7.add(label_6);
		
		JComboBox<String> comboBox_2 = new JComboBox();
		comboBox_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox_2.setBounds(194, 28, 55, 26);
		panel_7.add(comboBox_2);
		
		JLabel label_7 = new JLabel("Edit:");
		label_7.setHorizontalAlignment(SwingConstants.RIGHT);
		label_7.setFont(new Font("Arial", Font.PLAIN, 19));
		label_7.setBounds(44, 66, 61, 26);
		panel_7.add(label_7);
		
		JComboBox<String> comboBox_3 = new JComboBox();
		comboBox_3.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBox_3.setBounds(112, 67, 111, 26);
		panel_7.add(comboBox_3);
		
		JLabel label_8 = new JLabel("New name:");
		label_8.setHorizontalAlignment(SwingConstants.RIGHT);
		label_8.setFont(new Font("Arial", Font.PLAIN, 18));
		label_8.setBounds(-12, 105, 146, 30);
		panel_7.add(label_8);
		
		textField_8 = new JTextField();
		textField_8.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_8.setColumns(10);
		textField_8.setBounds(134, 105, 124, 26);
		panel_7.add(textField_8);
		
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
		
		JButton button = new JButton("Add");
		button.setFont(new Font("Arial", Font.PLAIN, 20));
		button.setBounds(55, 221, 133, 38);
		panel.add(button);
		
		JLabel lblNewCategory = new JLabel("New category:");
		lblNewCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewCategory.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewCategory.setBounds(0, 100, 122, 30);
		panel.add(lblNewCategory);
		
		textFieldNewCategory = new JTextField();
		textFieldNewCategory.setEnabled(false);
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
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Arial", Font.PLAIN, 20));
		btnDelete.setBounds(67, 221, 133, 38);
		panel_3.add(btnDelete);
		
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
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.setFont(new Font("Arial", Font.PLAIN, 20));
		btnEdit.setBounds(75, 221, 133, 38);
		panel_4.add(btnEdit);
		
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
		comboBoxEdit.setModel(new DefaultComboBoxModel(new String[] {"name", "category", "add quantity", "remove quantity", "price"}));
		comboBoxEdit.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxEdit.setBounds(112, 67, 111, 26);
		panel_4.add(comboBoxEdit);
		
		JLabel lblEditText = new JLabel("New name:");
		lblEditText.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEditText.setFont(new Font("Arial", Font.PLAIN, 18));
		lblEditText.setBounds(-12, 105, 146, 30);
		panel_4.add(lblEditText);
		
		textField_4 = new JTextField();
		textField_4.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textField_4.setColumns(10);
		textField_4.setBounds(134, 105, 124, 26);
		panel_4.add(textField_4);
		
		
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
				else if(comboBoxEdit.getSelectedItem().equals("add quantity")) {
					lblEditText.setText("Add quantity:");
				}
				else if(comboBoxEdit.getSelectedItem().equals("remove quantity")) {
					lblEditText.setText("Remove quantity");
				}
				else if(comboBoxEdit.getSelectedItem().equals("price")) {
					lblEditText.setText("New price:");
				}
				else {

				}
			}
		});
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
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
				
				Admin.addProduct(newProduct);
				textFieldNewCategory.setText("");
				textFieldQuantity.setText("");
				textFieldProductName.setText("");
				textFieldPrice.setText("");
				
				JOptionPane.showMessageDialog(null, "Added [" + newProduct.getName() + ", x" + newProduct.getQuantity() + "]");
				
				updateTablesAndComboboxes(comboBoxCategory, comboBoxIdProduct, comboBoxIdProductToDelete);
				
				comboBoxCategory.addActionListener(new ActionListener() {		
					@Override
					public void actionPerformed(ActionEvent e) {
						if(comboBoxCategory.getSelectedItem().equals("new category")) {
							textFieldNewCategory.setEnabled(true);
						}
						else
							textFieldNewCategory.setEnabled(false);
					}
				});
			}
		});
		

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int id = Integer.parseInt((String)comboBoxIdProductToDelete.getSelectedItem());
				
//				Product productToDelete = new Product.Builder().id(id)
//															   .build();
				
				Product productToDelete = new Product(id);
				
				Admin.deleteProduct(productToDelete);
				
				JOptionPane.showMessageDialog(null, "Product with [id = " + productToDelete.getId() + "] has been deleted");
				
				updateTablesAndComboboxes(comboBoxCategory, comboBoxIdProduct, comboBoxIdProductToDelete);
			}
		});
		
		updateTablesAndComboboxes(comboBoxCategory, comboBoxIdProduct, comboBoxIdProductToDelete);	
	}
	
	public static void updateTablesAndComboboxes(JComboBox<String> comboBoxCategory,JComboBox<String> comboBoxIdProduct,JComboBox<String> comboBoxIdProductToDelete)
	{
		comboBoxCategory.removeAllItems();
		loadTablesAdmin();
		getAllCategories(comboBoxCategory);
		fillComboBoxProductsId(comboBoxIdProduct);
		fillComboBoxProductsId(comboBoxIdProductToDelete);
		fillComboBoxCategory(comboBoxCategory);
	}
	public static void loadTablesAdmin()
	{
		try
		{
			DefaultTableModel modelProducts = (DefaultTableModel)tableProducts.getModel(); 
			DefaultTableModel modelRepresentatives = (DefaultTableModel)tableRepresentatives.getModel(); 
			
			if(modelProducts.getRowCount() > 0)
			{
				modelProducts.setRowCount(0);
			}
			
			if(modelRepresentatives.getRowCount() > 0)
			{
				modelRepresentatives.setRowCount(0);
			}
			
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
	        	
	        	Representative r = new Representative.Builder().idOfRepresentative(rs.getInt("id_rep"))
	        												   .name(rs.getString("name"))
	        												   .username(rs.getString("username"))
	        												   .category(rs.getString("category"))
	        												   .numberOfSales(rs.getInt("numberofsales"))
	        												   .profit(rs.getDouble("profit"))
	        												   .build();
	        	
	        	listRepresentatives.add(r);
	        }
	        
			Object[] row2 = new Object[7];
			
	        System.out.println("listProducts.size() = " +listProducts.size());
	        
	        for (int i = 0; i < listRepresentatives.size(); i++) {
				row2[0] = listRepresentatives.get(i).getIdOfRepresentative();
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
	
	public static void getAllCategories(JComboBox<String> ComboBoxIdProduct )
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
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void fillComboBoxProductsId(JComboBox<String> comboBoxIdProduct)
	{		
		comboBoxIdProduct.removeAllItems();
		for(Product product : listProducts)
		{
			comboBoxIdProduct.addItem(""+product.getId());
		}
		
	}
	
	public static void fillComboBoxCategory(JComboBox<String> comboBoxCategory)
	{	
		
		for(String category : allCategories)
		{
			comboBoxCategory.addItem(category);
		}
		
		comboBoxCategory.addItem("new category");
		
	}
}
