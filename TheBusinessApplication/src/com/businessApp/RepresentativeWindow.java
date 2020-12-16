package com.businessApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;

public class RepresentativeWindow extends JFrame {
	
	static DBConnection cDB = new DBConnection();
	static Connection con = cDB.createConnection();
	private JTable tableCatalog;
	private String category;
	private String username;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					RepresentativeWindow frame = new RepresentativeWindow();
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
	public RepresentativeWindow(String username, String category) {
		this.category = category;
		this.username = username;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 847, 501);
		
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
				"id", "name", "e-mail", "bought product", "quantity", "price (lv.)", "date"
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
		
		JLabel lblSales = new JLabel("Your catalog (" + category + "):");
		lblSales.setFont(new Font("Arial", Font.PLAIN, 22));
		lblSales.setBounds(53, 13, 381, 23);
		getContentPane().add(lblSales);
		
		JButton btnNewOrder = new JButton("Add new order");
		
		btnNewOrder.setFont(new Font("Arial", Font.PLAIN, 20));
		btnNewOrder.setBounds(52, 240, 180, 39);
		getContentPane().add(btnNewOrder);
		
		JButton btnRefreshTable = new JButton("Refresh table");
		btnRefreshTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//resetTable();
				showCatalog();
			}
		});
		btnRefreshTable.setFont(new Font("Arial", Font.PLAIN, 20));
		btnRefreshTable.setBounds(622, 7, 163, 36);
		getContentPane().add(btnRefreshTable);
		
		btnNewOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewOrderWindow newOrder = new NewOrderWindow(username, category);
				newOrder.setVisible(true);
				
			}
		});

		
		mnLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		showCatalog();
	}
		
	public void showCatalog()
	{
		try
		{
			DefaultTableModel model = (DefaultTableModel)tableCatalog.getModel(); 
			
			if(model.getRowCount() > 0)
			{
				model.setRowCount(0);
			}
			
			ArrayList<Order> catalog = new ArrayList<>();
			Statement stmt = con.createStatement();
	        ResultSet rs = stmt.executeQuery("SELECT * FROM allsales WHERE category = '" + this.category + "'");
	        while(rs.next())
	        {  	
	        	Order o = new Order(rs.getInt("id_sale"), rs.getString("name"), rs.getString("email"), rs.getString("product"), rs.getInt("quantity"), rs.getDouble("price"), rs.getDate("date"));
	        	
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
	        
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
		
	}
}
