package com.admin;

import java.awt.BorderLayout;
import java.lang.Object;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

import com.businessapplication.Product;
import com.businessapplication.Sale;
import com.businessapplication.Product.Builder;
import com.databaseconnection.DBConnection;
import com.exceptions.NegativePriceException;
import com.exceptions.NegativeQuantityException;
import com.exceptions.NotUniqueUsernameException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import net.miginfocom.swing.MigLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.login.ChangePasswordWindow;
import com.login.LoginWindow;
import com.representative.Representative;

public class AdminWindow extends JFrame {

	private JPanel contentPane;
	private static JTable tableProducts;
	private static JTable tableRepresentatives;
	private static JTable tableAllSales;
	private JTextField textFieldProductName;
	private JTextField textFieldNewCategory;
	private JTextField textFieldQuantity;
	private JTextField textFieldPrice;
	private JTextField textFieldEdit;
	
	private JTextField textFieldNameRep;
	private JTextField textFieldCategoryRep;
	private JTextField textFieldUsernameRep;
	private JTextField textFieldEditRep;
	private JTextField textFieldRepUsername;
	
	static JComboBox comboBoxIdProductToDelete = new JComboBox();
	static JComboBox comboBoxCategory = new JComboBox();
	static JComboBox comboBoxIdProduct = new JComboBox();
	static JComboBox comboBoxEdit = new JComboBox();
	static JComboBox<String> comboBoxIdRepresentativeToDelete = new JComboBox();
	static JComboBox<String> comboBoxIdRepresentative = new JComboBox();
	static JComboBox<String> comboBoxEditRepresentative = new JComboBox();
	static JComboBox<String> comboBoxUsernames = new JComboBox();
	static JComboBox<String> comboBoxCategoryFilter = new JComboBox();
	
	static DefaultListModel<String> model = new DefaultListModel<>();
	static JLabel lblNumberOfRepresentatives = new JLabel("Number of representatives: ");
	static JLabel lblNumberOfProducts = new JLabel("Number of products: ");
	static JLabel lblNumberOfClients = new JLabel("Number of clients: ");
	static JLabel lblTotalNumberOfSales = new JLabel("Total number of sales: ");
	static JLabel lblMostSalesByProduct = new JLabel("Most sales by product: ");
	static JLabel lblMostSalesByCategory = new JLabel("Most sales by category: ");
	static JLabel lblRepresentativeWithMostSales = new JLabel("Representative with most sales: ");
	static JLabel lblTotalEarnings = new JLabel("Total earnings: ");
	static JLabel lblNumberOfCategories = new JLabel("Number of categories: ");
	
	static ArrayList<Product> listProducts = new ArrayList<>();
	static ArrayList<Representative> listRepresentatives = new ArrayList<>();
	static ArrayList<Sale> listAllSales = new ArrayList<>();
	static ArrayList<String> allCategories = new ArrayList<>();
	static ArrayList<String> categoriesWithoutRepresentative = new ArrayList<>();
	static ArrayList<String> categoriesWithRepresentative = new ArrayList<>();
	
	private static int numberOfProducts;
	private static int numberOfRepresentatives;
	private static int numberOfSales;
	private JTextField textFieldCategoryFilter;
	
	private AdminController adminController  = new AdminController();
	private static SalesAnalizerController analizer = new SalesAnalizerController();
	
	
	/**
	 * Create the frame.
	 */
	public AdminWindow(int id, String name, String username, String password) {
		
		Admin thisAdmin = new Admin.Builder().id(id)
											 .name(name)
											 .username(username)
											 .password(password)
											 .build();
		
		
		
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
		
		JMenuItem mnManageAdmins = new JMenuItem("Manage admins");
		mnManageAdmins.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		mnNewMenu.add(mnManageAdmins);
		
		JMenuItem mnChangePassword = new JMenuItem("Change password");
		mnChangePassword.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		mnNewMenu.add(mnChangePassword);
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
		scrollPane_1.setBounds(23, 13, 552, 214);
		panel_1.add(scrollPane_1);
		
		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Add representative", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_5.setBounds(23, 233, 270, 189);
		panel_1.add(panel_5);
				
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Analize", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblAllSales = new JLabel("All sales:");
		lblAllSales.setHorizontalAlignment(SwingConstants.LEFT);
		lblAllSales.setFont(new Font("Arial", Font.PLAIN, 20));
		lblAllSales.setBounds(12, 13, 114, 30);
		panel_2.add(lblAllSales);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(12, 49, 845, 161);
		panel_2.add(scrollPane_3);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(128, 13, 587, 124);
		panelProducts.add(scrollPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Add product", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 150, 281, 272);
		panelProducts.add(panel);
		panel.setLayout(null);
		
		JLabel lblProductName = new JLabel("Product name:");
		lblProductName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProductName.setFont(new Font("Arial", Font.PLAIN, 18));
		lblProductName.setBounds(11, 15, 122, 30);
		panel.add(lblProductName);
		
		textFieldProductName = new JTextField();
		textFieldProductName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldProductName.setColumns(10);
		textFieldProductName.setBounds(134, 15, 133, 26);
		panel.add(textFieldProductName);
		
		JLabel lblCategory = new JLabel("Category:");
		lblCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCategory.setFont(new Font("Arial", Font.PLAIN, 18));
		lblCategory.setBounds(21, 58, 112, 30);
		panel.add(lblCategory);
		
		JButton btnAddProduct = new JButton("Add");
		btnAddProduct.setFont(new Font("Arial", Font.PLAIN, 20));
		btnAddProduct.setBounds(134, 221, 133, 38);
		panel.add(btnAddProduct);
		
		JLabel lblNewCategory = new JLabel("\"new category\":");
		lblNewCategory.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewCategory.setFont(new Font("Arial", Font.PLAIN, 18));
		lblNewCategory.setBounds(0, 100, 133, 30);
		panel.add(lblNewCategory);
		
		textFieldNewCategory = new JTextField();
		textFieldNewCategory.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldNewCategory.setColumns(10);
		textFieldNewCategory.setBounds(134, 98, 133, 26);
		panel.add(textFieldNewCategory);
			
		comboBoxCategory.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxCategory.setBounds(134, 56, 133, 26);
		panel.add(comboBoxCategory);
		
		JLabel lblQuantity = new JLabel("Quantity:");
		lblQuantity.setHorizontalAlignment(SwingConstants.RIGHT);
		lblQuantity.setFont(new Font("Arial", Font.PLAIN, 18));
		lblQuantity.setBounds(11, 139, 122, 30);
		panel.add(lblQuantity);
		
		textFieldQuantity = new JTextField();
		textFieldQuantity.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldQuantity.setColumns(10);
		textFieldQuantity.setBounds(134, 137, 133, 26);
		panel.add(textFieldQuantity);
		
		textFieldPrice = new JTextField();
		textFieldPrice.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldPrice.setColumns(10);
		textFieldPrice.setBounds(134, 176, 133, 26);
		panel.add(textFieldPrice);
		
		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPrice.setFont(new Font("Arial", Font.PLAIN, 18));
		lblPrice.setBounds(11, 178, 122, 30);
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
		
		comboBoxIdProduct.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdProduct.setBounds(194, 28, 55, 26);
		panel_4.add(comboBoxIdProduct);
		
		JLabel lblEdit = new JLabel("Edit:");
		lblEdit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEdit.setFont(new Font("Arial", Font.PLAIN, 19));
		lblEdit.setBounds(44, 66, 61, 26);
		panel_4.add(lblEdit);
		
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
		btnAddRepresentative.setBounds(59, 143, 133, 38);
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
		panel_6.setBounds(305, 233, 270, 189);
		panel_1.add(panel_6);
		
		JButton btnDeleteRepresentative = new JButton("Delete");
		btnDeleteRepresentative.setFont(new Font("Arial", Font.PLAIN, 20));
		btnDeleteRepresentative.setBounds(68, 142, 133, 38);
		panel_6.add(btnDeleteRepresentative);
		
		JLabel lblIdOfRep_1 = new JLabel("ID of rep. to delete: ");
		lblIdOfRep_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfRep_1.setFont(new Font("Arial", Font.PLAIN, 19));
		lblIdOfRep_1.setBounds(0, 64, 200, 26);
		panel_6.add(lblIdOfRep_1);
		
		
		comboBoxIdRepresentativeToDelete.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdRepresentativeToDelete.setBounds(196, 65, 55, 26);
		panel_6.add(comboBoxIdRepresentativeToDelete);
		
		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Edit representative", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_7.setBounds(585, 180, 270, 242);
		panel_1.add(panel_7);
		
		JButton btnEditRepresentative = new JButton("Edit");
		btnEditRepresentative.setFont(new Font("Arial", Font.PLAIN, 20));
		btnEditRepresentative.setBounds(77, 191, 133, 38);
		panel_7.add(btnEditRepresentative);
		
		JLabel lblIdOfRep = new JLabel("ID of rep. to edit: ");
		lblIdOfRep.setHorizontalAlignment(SwingConstants.RIGHT);
		lblIdOfRep.setFont(new Font("Arial", Font.PLAIN, 19));
		lblIdOfRep.setBounds(0, 27, 200, 26);
		panel_7.add(lblIdOfRep);
		
		
		comboBoxIdRepresentative.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxIdRepresentative.setBounds(194, 28, 55, 26);
		panel_7.add(comboBoxIdRepresentative);
		
		JLabel label_7 = new JLabel("Edit:");
		label_7.setHorizontalAlignment(SwingConstants.RIGHT);
		label_7.setFont(new Font("Arial", Font.PLAIN, 19));
		label_7.setBounds(44, 66, 61, 26);
		panel_7.add(label_7);
		
		
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
		scrollPane_2.setBounds(587, 75, 268, 92);
		panel_1.add(scrollPane_2);
		JList listNoRep = new JList<>(model);
		scrollPane_2.setViewportView(listNoRep);
		listNoRep.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		
		JLabel lblCategoriesWithNo = new JLabel("Categories with no representatives:");
		lblCategoriesWithNo.setHorizontalAlignment(SwingConstants.LEFT);
		lblCategoriesWithNo.setFont(new Font("Arial", Font.PLAIN, 17));
		lblCategoriesWithNo.setBounds(587, 13, 255, 20);
		panel_1.add(lblCategoriesWithNo);
		
		JLabel lblwillBeSold = new JLabel("(will be sold by representative with ");
		lblwillBeSold.setHorizontalAlignment(SwingConstants.LEFT);
		lblwillBeSold.setFont(new Font("Arial", Font.PLAIN, 17));
		lblwillBeSold.setBounds(587, 33, 255, 20);
		panel_1.add(lblwillBeSold);
		
		JLabel lblCategoryall = new JLabel("category \"all\")");
		lblCategoryall.setHorizontalAlignment(SwingConstants.LEFT);
		lblCategoryall.setFont(new Font("Arial", Font.PLAIN, 17));
		lblCategoryall.setBounds(587, 51, 255, 20);
		panel_1.add(lblCategoryall);
		
		
//		TABLES:
		
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
		
		tableProducts.setAutoCreateRowSorter(true);
		tableProducts.getTableHeader().setReorderingAllowed(false);
		
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
		
		tableRepresentatives.setAutoCreateRowSorter(true);
		tableRepresentatives.getTableHeader().setReorderingAllowed(false);

		tableAllSales = new JTable();
		scrollPane_3.setViewportView(tableAllSales);
		tableAllSales.setFont(new Font("Arial", Font.PLAIN, 16));
		tableAllSales.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id_sale", "email_client", "username_rep", "category", "product", "quantity", "price(lv.)", "date"
			}
		));
		
		tableAllSales.setAutoCreateRowSorter(true);
		tableAllSales.getTableHeader().setReorderingAllowed(false);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		rightRenderer.setHorizontalAlignment( JLabel.RIGHT );
		leftRenderer.setHorizontalAlignment( JLabel.LEFT );
		
		//Table products
		TableColumnModel columnModelProducts = tableProducts.getColumnModel();
		
		columnModelProducts.getColumn(0).setPreferredWidth(40);
		columnModelProducts.getColumn(1).setPreferredWidth(120);
		columnModelProducts.getColumn(2).setPreferredWidth(120);
		columnModelProducts.getColumn(3).setPreferredWidth(80);
		columnModelProducts.getColumn(4).setPreferredWidth(40);
		tableProducts.setRowHeight(30);
		
		tableProducts.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tableProducts.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
		tableProducts.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		
		//Table representatives	
		TableColumnModel columnModelRepresentatives = tableRepresentatives.getColumnModel();
		columnModelRepresentatives.getColumn(0).setPreferredWidth(40);
		columnModelRepresentatives.getColumn(1).setPreferredWidth(120);
		columnModelRepresentatives.getColumn(2).setPreferredWidth(120);
		columnModelRepresentatives.getColumn(3).setPreferredWidth(80);
		columnModelRepresentatives.getColumn(4).setPreferredWidth(40);
		columnModelRepresentatives.getColumn(5).setPreferredWidth(60);
		tableRepresentatives.setRowHeight(30);
				
		tableRepresentatives.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tableRepresentatives.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
		tableRepresentatives.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
		
		//Table allsales
		TableColumnModel columnModelAllSales = tableAllSales.getColumnModel();
		
		columnModelAllSales.getColumn(0).setPreferredWidth(10);
		columnModelAllSales.getColumn(1).setPreferredWidth(150);
		columnModelAllSales.getColumn(2).setPreferredWidth(100);
		columnModelAllSales.getColumn(3).setPreferredWidth(80);
		columnModelAllSales.getColumn(4).setPreferredWidth(100);
		columnModelAllSales.getColumn(5).setPreferredWidth(30);
		columnModelAllSales.getColumn(6).setPreferredWidth(60);
		columnModelAllSales.getColumn(7).setPreferredWidth(80);
		tableAllSales.setRowHeight(30);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(null, "Sales in a period", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_8.setBounds(12, 223, 285, 199);
		panel_2.add(panel_8);
		panel_8.setLayout(null);
		
		JDateChooser dateChooserStartDate = new JDateChooser();
		dateChooserStartDate.setBounds(114, 43, 133, 26);
		panel_8.add(dateChooserStartDate);
		dateChooserStartDate.setDateFormatString("yyyy-MM-dd");
		
		JDateChooser dateChooserEndDate = new JDateChooser();
		dateChooserEndDate.setDateFormatString("yyyy-MM-dd");
		dateChooserEndDate.setBounds(114, 88, 133, 26);
		panel_8.add(dateChooserEndDate);
		
		JLabel lblStartTime = new JLabel("Start date:");
		lblStartTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStartTime.setFont(new Font("Arial", Font.PLAIN, 18));
		lblStartTime.setBounds(25, 39, 89, 30);
		panel_8.add(lblStartTime);
		
		JLabel lblEndDate = new JLabel("End date:");
		lblEndDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEndDate.setFont(new Font("Arial", Font.PLAIN, 18));
		lblEndDate.setBounds(25, 88, 89, 30);
		panel_8.add(lblEndDate);
		
		JButton btnFilterByDate = new JButton("Filter");
		btnFilterByDate.setFont(new Font("Arial", Font.PLAIN, 20));
		btnFilterByDate.setBounds(73, 148, 133, 38);
		panel_8.add(btnFilterByDate);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.setFont(new Font("Arial", Font.PLAIN, 20));
		btnRefresh.setBounds(724, 5, 133, 38);
		panel_2.add(btnRefresh);
		
		JPanel panel_9 = new JPanel();
		panel_9.setLayout(null);
		panel_9.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filter by representative", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_9.setBounds(309, 223, 548, 84);
		panel_2.add(panel_9);
		
		JLabel lblUsernameOfRepresentative = new JLabel("Usename:");
		lblUsernameOfRepresentative.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsernameOfRepresentative.setFont(new Font("Arial", Font.PLAIN, 18));
		lblUsernameOfRepresentative.setBounds(41, 13, 126, 30);
		panel_9.add(lblUsernameOfRepresentative);
		
		
		comboBoxUsernames.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxUsernames.setBounds(175, 15, 139, 26);
		panel_9.add(comboBoxUsernames);
		
		JLabel lblEnterNameif = new JLabel("If selected \"other\":");
		lblEnterNameif.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEnterNameif.setFont(new Font("Arial", Font.PLAIN, 18));
		lblEnterNameif.setBounds(-27, 47, 194, 30);
		panel_9.add(lblEnterNameif);
		
		textFieldRepUsername = new JTextField();
		textFieldRepUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldRepUsername.setColumns(10);
		textFieldRepUsername.setBounds(175, 49, 133, 26);
		panel_9.add(textFieldRepUsername);
		
		JButton btnFilterByRepresentative = new JButton("Filter");
		btnFilterByRepresentative.setBounds(371, 27, 133, 38);
		panel_9.add(btnFilterByRepresentative);
		btnFilterByRepresentative.setFont(new Font("Arial", Font.PLAIN, 20));
		
		tableAllSales.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tableAllSales.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		
		
		JPanel panel_10 = new JPanel();
		tabbedPane.addTab("Stats", null, panel_10, null);
		
		JPanel panel_11 = new JPanel();
		panel_11.setLayout(null);
		panel_11.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Filter by category", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_11.setBounds(309, 338, 548, 84);
		panel_2.add(panel_11);
		
		JLabel lblCategory_2 = new JLabel("Category:");
		lblCategory_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCategory_2.setFont(new Font("Arial", Font.PLAIN, 18));
		lblCategory_2.setBounds(41, 13, 126, 30);
		panel_11.add(lblCategory_2);
		
		
		comboBoxCategoryFilter.setFont(new Font("Tahoma", Font.PLAIN, 16));
		comboBoxCategoryFilter.setBounds(175, 15, 139, 26);
		panel_11.add(comboBoxCategoryFilter);
		
		JLabel label_1 = new JLabel("If selected \"other\":");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Arial", Font.PLAIN, 18));
		label_1.setBounds(-27, 47, 194, 30);
		panel_11.add(label_1);
		
		textFieldCategoryFilter = new JTextField();
		textFieldCategoryFilter.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldCategoryFilter.setColumns(10);
		textFieldCategoryFilter.setBounds(175, 49, 133, 26);
		panel_11.add(textFieldCategoryFilter);
		
		JButton btnFilterByCategory = new JButton("Filter");
		btnFilterByCategory.setFont(new Font("Arial", Font.PLAIN, 20));
		btnFilterByCategory.setBounds(371, 27, 133, 38);
		panel_11.add(btnFilterByCategory);
		
		JLabel lblTweet = new JLabel("Tweet:");
		lblTweet.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTweet.setFont(new Font("Arial", Font.PLAIN, 18));
		lblTweet.setBounds(0, 226, 71, 30);
		panel.add(lblTweet);
		
		JCheckBox checkBoxTweet = new JCheckBox("");
		checkBoxTweet.setFont(new Font("Sylfaen", Font.PLAIN, 46));
		checkBoxTweet.setBounds(71, 226, 29, 33);
		panel.add(checkBoxTweet);
		GridBagLayout gbl_panel_10 = new GridBagLayout();
		gbl_panel_10.columnWidths = new int[]{42, 553, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_10.rowHeights = new int[]{20, 40, 40, 40, 40, 40, 40, 40, 40, 72, 0};
		gbl_panel_10.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_10.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_10.setLayout(gbl_panel_10);
								
													
		lblNumberOfProducts.setHorizontalAlignment(SwingConstants.LEFT);
		lblNumberOfProducts.setFont(new Font("Arial", Font.PLAIN, 20));
		GridBagConstraints gbc_lblNumberOfProducts = new GridBagConstraints();
		gbc_lblNumberOfProducts.anchor = GridBagConstraints.WEST;
		gbc_lblNumberOfProducts.fill = GridBagConstraints.VERTICAL;
		gbc_lblNumberOfProducts.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfProducts.gridx = 1;
		gbc_lblNumberOfProducts.gridy = 1;
		panel_10.add(lblNumberOfProducts, gbc_lblNumberOfProducts);
						
						
		lblNumberOfRepresentatives.setHorizontalAlignment(SwingConstants.LEFT);
		lblNumberOfRepresentatives.setFont(new Font("Arial", Font.PLAIN, 20));
		GridBagConstraints gbc_lblNumberOfRepresentatives = new GridBagConstraints();
		gbc_lblNumberOfRepresentatives.anchor = GridBagConstraints.WEST;
		gbc_lblNumberOfRepresentatives.fill = GridBagConstraints.VERTICAL;
		gbc_lblNumberOfRepresentatives.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfRepresentatives.gridx = 1;
		gbc_lblNumberOfRepresentatives.gridy = 2;
		panel_10.add(lblNumberOfRepresentatives, gbc_lblNumberOfRepresentatives);
				
				
		lblNumberOfClients.setHorizontalAlignment(SwingConstants.LEFT);
		lblNumberOfClients.setFont(new Font("Arial", Font.PLAIN, 20));
		GridBagConstraints gbc_lblNumberOfClients = new GridBagConstraints();
		gbc_lblNumberOfClients.fill = GridBagConstraints.BOTH;
		gbc_lblNumberOfClients.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfClients.gridx = 1;
		gbc_lblNumberOfClients.gridy = 3;
		panel_10.add(lblNumberOfClients, gbc_lblNumberOfClients);
										

		lblTotalNumberOfSales.setHorizontalAlignment(SwingConstants.LEFT);
		lblTotalNumberOfSales.setFont(new Font("Arial", Font.PLAIN, 20));
		GridBagConstraints gbc_lblTotalNumberOfSales = new GridBagConstraints();
		gbc_lblTotalNumberOfSales.anchor = GridBagConstraints.WEST;
		gbc_lblTotalNumberOfSales.fill = GridBagConstraints.VERTICAL;
		gbc_lblTotalNumberOfSales.insets = new Insets(0, 0, 5, 5);
		gbc_lblTotalNumberOfSales.gridx = 1;
		gbc_lblTotalNumberOfSales.gridy = 4;
		panel_10.add(lblTotalNumberOfSales, gbc_lblTotalNumberOfSales);
		
						
		lblNumberOfCategories.setHorizontalAlignment(SwingConstants.LEFT);
		lblNumberOfCategories.setFont(new Font("Arial", Font.PLAIN, 20));
		GridBagConstraints gbc_lblNumberOfCategories = new GridBagConstraints();
		gbc_lblNumberOfCategories.fill = GridBagConstraints.BOTH;
		gbc_lblNumberOfCategories.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfCategories.gridx = 1;
		gbc_lblNumberOfCategories.gridy = 5;
		panel_10.add(lblNumberOfCategories, gbc_lblNumberOfCategories);
								
								
		lblRepresentativeWithMostSales.setHorizontalAlignment(SwingConstants.LEFT);
		lblRepresentativeWithMostSales.setFont(new Font("Arial", Font.PLAIN, 20));
		GridBagConstraints gbc_lblRepresentativeWithMostSales = new GridBagConstraints();
		gbc_lblRepresentativeWithMostSales.insets = new Insets(0, 0, 5, 5);
		gbc_lblRepresentativeWithMostSales.fill = GridBagConstraints.BOTH;
		gbc_lblRepresentativeWithMostSales.gridx = 1;
		gbc_lblRepresentativeWithMostSales.gridy = 6;
		panel_10.add(lblRepresentativeWithMostSales, gbc_lblRepresentativeWithMostSales);
								
						
		lblMostSalesByCategory.setHorizontalAlignment(SwingConstants.LEFT);
		lblMostSalesByCategory.setFont(new Font("Arial", Font.PLAIN, 20));
		GridBagConstraints gbc_lblMostSalesByCategory = new GridBagConstraints();
		gbc_lblMostSalesByCategory.fill = GridBagConstraints.BOTH;
		gbc_lblMostSalesByCategory.insets = new Insets(0, 0, 5, 5);
		gbc_lblMostSalesByCategory.gridx = 1;
		gbc_lblMostSalesByCategory.gridy = 7;
		panel_10.add(lblMostSalesByCategory, gbc_lblMostSalesByCategory);
		
		
		lblMostSalesByProduct.setHorizontalAlignment(SwingConstants.LEFT);
		lblMostSalesByProduct.setFont(new Font("Arial", Font.PLAIN, 20));
		GridBagConstraints gbc_lblMostSalesByProduct = new GridBagConstraints();
		gbc_lblMostSalesByProduct.fill = GridBagConstraints.BOTH;
		gbc_lblMostSalesByProduct.insets = new Insets(0, 0, 5, 5);
		gbc_lblMostSalesByProduct.gridx = 1;
		gbc_lblMostSalesByProduct.gridy = 8;
		panel_10.add(lblMostSalesByProduct, gbc_lblMostSalesByProduct);
						
		
		lblTotalEarnings.setHorizontalAlignment(SwingConstants.LEFT);
		lblTotalEarnings.setFont(new Font("Arial", Font.PLAIN, 20));
		GridBagConstraints gbc_lblTotalEarnings = new GridBagConstraints();
		gbc_lblTotalEarnings.fill = GridBagConstraints.BOTH;
		gbc_lblTotalEarnings.insets = new Insets(0, 0, 0, 5);
		gbc_lblTotalEarnings.gridx = 1;
		gbc_lblTotalEarnings.gridy = 9;
		panel_10.add(lblTotalEarnings, gbc_lblTotalEarnings);
		
		JButton btnPieChart = new JButton("Pie chart");
		btnPieChart.setFont(new Font("Arial", Font.PLAIN, 26));
		GridBagConstraints gbc_btnPieChart = new GridBagConstraints();
		gbc_btnPieChart.insets = new Insets(0, 0, 0, 5);
		gbc_btnPieChart.gridx = 2;
		gbc_btnPieChart.gridy = 9;
		panel_10.add(btnPieChart, gbc_btnPieChart);
		
		updateTablesAndComboboxes();
				
		mnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				LoginWindow loginWin = new LoginWindow();
				loginWin.setVisible(true);
			}
		});
		
		mnManageAdmins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		
		mnChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ChangePasswordWindow changePassWin = new ChangePasswordWindow(thisAdmin.getId());
				changePassWin.setVisible(true);
				changePassWin.setTitle("Password - " + thisAdmin.getName() + "(" + thisAdmin.getUsername() + ")");
			}
		});
		
		mnManageAdmins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ManageAdminWindow addAdminWindow = new ManageAdminWindow();
				addAdminWindow.setVisible(true);
				addAdminWindow.setTitle("Add admin");
			}
		});
		
		btnPieChart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 PieChartWindow demo = new PieChartWindow("Sales by category");
				 demo.setVisible(true);
				
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
					
					if(checkBoxTweet.isSelected()) {
						adminController.addProduct(newProduct, true);
					}
					else {
						adminController.addProduct(newProduct, false);
					}
									
					JOptionPane.showMessageDialog(null, "Added [" + newProduct.getName() + ", x" + newProduct.getQuantity() + "]");
					
				} catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Incorrect input");
				} catch(SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error with database");
				} catch (NegativePriceException | NegativeQuantityException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				} 

				textFieldNewCategory.setText("");
				textFieldQuantity.setText("");
				textFieldProductName.setText("");
				textFieldPrice.setText("");
						
				updateTablesAndComboboxes();
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
						adminController.editFieldProduct(productToEdit, "name");
					}
					else if(comboBoxEdit.getSelectedItem().equals("category"))
					{
						productToEdit.setCategory(textFieldEdit.getText());
						adminController.editFieldProduct(productToEdit, "category");
					}
					else if(comboBoxEdit.getSelectedItem().equals("quantity"))
					{
						productToEdit.setQuantity(Integer.parseInt(textFieldEdit.getText()));
						adminController.editFieldProduct(productToEdit, "quantity");
					}
					else if(comboBoxEdit.getSelectedItem().equals("price"))
					{
						productToEdit.setPrice(Double.parseDouble(textFieldEdit.getText()));
						adminController.editFieldProduct(productToEdit, "price");
					}
					
				} catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Incorrect input");
				} catch(SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error with database");
				}
				
				updateTablesAndComboboxes();
				
			}
		});	

		btnDeleteClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int id = Integer.parseInt((String)comboBoxIdProductToDelete.getSelectedItem());
				
				Product productToDelete = new Product(id);
				
				try {
					adminController.deleteProduct(productToDelete);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error with database.");
					e.printStackTrace();
				}
				
				JOptionPane.showMessageDialog(null, "Product with [id = " + productToDelete.getId() + "] has been deleted");
				
				updateTablesAndComboboxes();
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
		
		btnAddRepresentative.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Representative represenatativeToAdd = new Representative.Builder().name(textFieldNameRep.getText())
																				  .username(textFieldUsernameRep.getText())
																				  .category(textFieldCategoryRep.getText())
																				  .numberOfSales(0)
																				  .profit(0)
																				  .build();
				
				try {
					adminController.checkUniqueRepresentativeUsername(represenatativeToAdd);
					adminController.addRepresentative(represenatativeToAdd);
					
					JOptionPane.showMessageDialog(null, "Added [" + represenatativeToAdd.getName() + " (" + represenatativeToAdd.getUsername() + "), "
							+ "category: " + represenatativeToAdd.getCategory() + ".");
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error with database.");
					e.printStackTrace();
				} catch (NotUniqueUsernameException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				
				updateTablesAndComboboxes();
			}
			
		});
		

		btnDeleteRepresentative.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int id = Integer.parseInt((String)comboBoxIdRepresentativeToDelete.getSelectedItem());
				
				Representative represenatativeToDelete = new Representative.Builder().id(id)
																					 .build();
				
				try {
					adminController.deleteRepresentative(represenatativeToDelete);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error with database.");
					e.printStackTrace();
				}
				
				JOptionPane.showMessageDialog(null, "Representative with [id = " + represenatativeToDelete.getId() + "] has been deleted");
				
				updateTablesAndComboboxes();
				
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
						
							adminController.editFieldRepresentative(representativeToEdit, "name");
					}
					else if(comboBoxEditRepresentative.getSelectedItem().equals("username"))
					{
						representativeToEdit.setUsername(textFieldEditRep.getText());
						adminController.editFieldRepresentative(representativeToEdit, "username");
					}
					else if(comboBoxEditRepresentative.getSelectedItem().equals("category"))
					{
						representativeToEdit.setCategory(textFieldEditRep.getText());
						adminController.editFieldRepresentative(representativeToEdit, "category");
					}

					
				} catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Incorrect input");
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error with database");
				}
				
				updateTablesAndComboboxes();
			}
		});
		
		btnFilterByDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Date selectedStartDate = dateChooserStartDate.getDate();
				Date selectedEndDate = dateChooserEndDate.getDate();
				
				
	            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
	            
	            String dateStart = dateFormat.format(selectedStartDate);   
	            String dateEnd = dateFormat.format(selectedEndDate);	
				
	            listAllSales = analizer.filterSalesByDate(dateStart, dateEnd, listAllSales);	         
				
				loadTableFiltered(listAllSales);
			}
		});
		
		btnFilterByRepresentative.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String username;
				
				if(comboBoxUsernames.getSelectedItem().equals("other")) {
					username = textFieldRepUsername.getText();
				}
				else {			
					username = (String)comboBoxUsernames.getSelectedItem();
				}
				
				listAllSales = analizer.filterSalesByCriteria(listAllSales, "username", username);
				
				loadTableFiltered(listAllSales);
			}
		});
		
		btnFilterByCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String category;
			
				if(comboBoxCategoryFilter.getSelectedItem().equals("other")) {
					category = textFieldCategoryFilter.getText();
				}
				else {			
					category = (String)comboBoxCategoryFilter.getSelectedItem();
				}
				
				listAllSales = analizer.filterSalesByCriteria(listAllSales, "category", category);
				
				loadTableFiltered(listAllSales);
			}
		});
		
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				updateTablesAndComboboxes();
			}
		});
		
	}
	
	public static void updateTablesAndComboboxes()
	{
		loadTablesAdmin();
		fillComboBoxProductsId(comboBoxIdProduct);
		fillComboBoxProductsId(comboBoxIdProductToDelete);
		fillComboBoxRepresentativeId(comboBoxIdRepresentative);
		fillComboBoxRepresentativeId(comboBoxIdRepresentativeToDelete);
		getAllCategories(comboBoxCategory);
		findCategoriesWithNoRepresentative(model);
		
		fillComboBoxFilters(comboBoxCategoryFilter, comboBoxUsernames);
		
		int numberOfCategories = categoriesWithRepresentative.size() + categoriesWithoutRepresentative.size();
		String mostSalesByCategory = analizer.getMostSalesByCriteria("category");
		String mostSalesByProduct = analizer.getMostSalesByCriteria("product");
		String mostSalesByRepresentative = analizer.getMostSalesByCriteria("representative_username");
		int numberOfClients = analizer.getNumberOfClients();
		String totalProfit = analizer.getTotalProfit();
		
		lblNumberOfProducts.setText("Number of products: " + numberOfProducts);
		lblNumberOfRepresentatives.setText("Number of representatives: " + numberOfRepresentatives);
		lblTotalNumberOfSales.setText("Total number of sales: " + numberOfSales);
		lblMostSalesByCategory.setText("Most sales by category: " + mostSalesByCategory);
		lblNumberOfCategories.setText("Number of categories: " + numberOfCategories);
		lblMostSalesByProduct.setText("Most sales by product: " + mostSalesByProduct);
		lblRepresentativeWithMostSales.setText("Representative with most sales: " + mostSalesByRepresentative);
		lblTotalEarnings.setText("Total profit: " + totalProfit + "lv.");
		lblNumberOfClients.setText("Number of clients: " + numberOfClients);
		
	}
	
	public static void loadTablesAdmin()
	{
		try(Connection con = DBConnection.getCon()){
			DBConnection dbConnection = new DBConnection();
			
			DefaultTableModel modelProducts = (DefaultTableModel)tableProducts.getModel(); 
			DefaultTableModel modelRepresentatives = (DefaultTableModel)tableRepresentatives.getModel(); 
			DefaultTableModel modelAllSales = (DefaultTableModel)tableAllSales.getModel();
			
			modelProducts.setRowCount(0);		
			modelRepresentatives.setRowCount(0);
			modelAllSales.setRowCount(0);
			listProducts.clear();
			listRepresentatives.clear();
			listAllSales.clear();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allproducts");
			
			numberOfProducts = 0;
	        while(rs.next())
	        {  	
	        	
	        	Product p = new Product.Builder().id(rs.getInt("id_prod"))
	        									 .name(rs.getString("name"))
	        									 .category(rs.getString("category"))
	        									 .quantity(rs.getInt("quantity"))
	        									 .price(rs.getDouble("price"))
	        									 .build();
	        	
	        	listProducts.add(p);
	        	numberOfProducts++;
	        }
	        
			Object[] row = new Object[5];
	        
	        for (int i = 0; i < listProducts.size(); i++) {
				row[0] = listProducts.get(i).getId();
				row[1] = listProducts.get(i).getName();
				row[2] = listProducts.get(i).getCategory();
				row[3] = listProducts.get(i).getQuantity();
				row[4] = listProducts.get(i).getPrice();
	
				modelProducts.addRow(row);
			}
	        
	        
			rs = dbConnection.getData("SELECT * FROM allrepresentatives");
			
			numberOfRepresentatives = 0;
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
	        	numberOfRepresentatives++;
	        }
	        
			Object[] row2 = new Object[6];
	        
	        for (int i = 0; i < listRepresentatives.size(); i++) {
				row2[0] = listRepresentatives.get(i).getId();
				row2[1] = listRepresentatives.get(i).getName();
				row2[2] = listRepresentatives.get(i).getUsername();
				row2[3] = listRepresentatives.get(i).getCategory();
				row2[4] = listRepresentatives.get(i).getNumberOfSales();
				row2[5] = listRepresentatives.get(i).getProfit();
	
				modelRepresentatives.addRow(row2);
			}   
	        
	        rs = dbConnection.getData("SELECT * FROM allsales");
			
	        numberOfSales = 0;
	        while(rs.next())
	        {  	
	        	
	        	Sale o = new Sale.Builder().id(rs.getInt("id_sale"))
						   								   .email(rs.getString("email"))
						   								   .representativeUsername(rs.getString("representative_username"))
						   								   .product(rs.getString("product"))
						   								   .category(rs.getString("category"))
						   								   .quantity(rs.getInt("quantity"))
						   								   .price(rs.getDouble("price"))
						   								   .date(rs.getDate("date"))
						   								   .build();	
	        	
	        	listAllSales.add(o);
	        	numberOfSales++;
	        }
	        
			Object[] row3 = new Object[8];
	        
	        for (int i = 0; i < listAllSales.size(); i++) {
				row3[0] = listAllSales.get(i).getId();
				row3[1] = listAllSales.get(i).getEmail();
				row3[2] = listAllSales.get(i).getRepresentativeUsername();
				row3[3] = listAllSales.get(i).getCategory();
				row3[4] = listAllSales.get(i).getProduct();
				row3[5] = listAllSales.get(i).getQuantity();
				row3[6] = listAllSales.get(i).getPrice();
				row3[7] = listAllSales.get(i).getDate();
	
				modelAllSales.addRow(row3);
			}   
	        
		}catch(SQLException e){
			e.printStackTrace();
		} catch (NegativePriceException | NegativeQuantityException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} 
	
	}
	
	public static void loadTableFiltered(ArrayList<Sale> filteredList)
	{
		DefaultTableModel modelAllSales = (DefaultTableModel)tableAllSales.getModel(); 
		
		modelAllSales.setRowCount(0);
		
		Object[] row = new Object[8];
        
        for (int i = 0; i < filteredList.size(); i++) {
			row[0] = filteredList.get(i).getId();
			row[1] = filteredList.get(i).getEmail();
			row[2] = filteredList.get(i).getRepresentativeUsername();
			row[3] = filteredList.get(i).getCategory();
			row[4] = filteredList.get(i).getProduct();
			row[5] = filteredList.get(i).getQuantity();
			row[6] = filteredList.get(i).getPrice();
			row[7] = filteredList.get(i).getDate();

			modelAllSales.addRow(row);
		}   
        
	}
	
	public static void getAllCategories(JComboBox<String>  comboBoxCategory)
	{
		try(Connection con = DBConnection.getCon()) {
			DBConnection dbConnection = new DBConnection();
			
			ResultSet rs = dbConnection.getData("SELECT * FROM allrepresentatives");
				
			String category;
			while(rs.next())
			{
				category = rs.getString("category");
				
				if(!allCategories.contains(category) && !category.equals("all"))
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
	
	public static void fillComboBoxFilters(JComboBox<String> comboBoxCategoriesFilter, JComboBox<String> comboBoxUsernames)
	{	
		comboBoxCategoriesFilter.removeAllItems();
		comboBoxCategoriesFilter.addItem("other");
		
		comboBoxUsernames.removeAllItems();
		comboBoxUsernames.addItem("other");
			
		for(String category : allCategories)
		{
			comboBoxCategoriesFilter.addItem(""+category);
		}
		
		for(Representative rep : listRepresentatives)
		{
			comboBoxUsernames.addItem(""+rep.getUsername());
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
		
		comboBoxCategory.addItem("new category");
		
		for(String category : allCategories)
		{
			comboBoxCategory.addItem(category);
		}
		
	}
	
	public static void findCategoriesWithNoRepresentative(DefaultListModel<String> model)
	{
		model.clear();
		categoriesWithoutRepresentative.clear();
		categoriesWithRepresentative.clear();
		
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

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}