package com.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.businessapplication.Admin;
import com.businessapplication.Representative;
import com.rolecontrollers.AdminController;

import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class AddAdminWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textFieldName;
	private JTextField textFieldUsername;
	
	private ArrayList<String> listAdminUsernames = new ArrayList<>();
	private AdminController adminController = new AdminController();
	

	/**
	 * Create the frame.
	 */
	public AddAdminWindow() {		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 384, 331);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Add admin", null, panel, null);
		panel.setLayout(null);
		
		textFieldName = new JTextField();
		textFieldName.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textFieldName.setColumns(10);
		textFieldName.setBounds(166, 23, 133, 26);
		panel.add(textFieldName);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setFont(new Font("Arial", Font.PLAIN, 22));
		lblName.setBounds(43, 23, 122, 30);
		panel.add(lblName);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textFieldUsername.setColumns(10);
		textFieldUsername.setBounds(166, 68, 133, 26);
		panel.add(textFieldUsername);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUsername.setFont(new Font("Arial", Font.PLAIN, 22));
		lblUsername.setBounds(30, 66, 133, 30);
		panel.add(lblUsername);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Arial", Font.PLAIN, 20));
		btnAdd.setBounds(102, 107, 133, 38);
		panel.add(btnAdd);
		
		JLabel lbldefaultPasswordIs = new JLabel("*default password is \"1234\"");
		lbldefaultPasswordIs.setHorizontalAlignment(SwingConstants.CENTER);
		lbldefaultPasswordIs.setFont(new Font("Arial", Font.PLAIN, 17));
		lbldefaultPasswordIs.setBounds(43, 158, 250, 30);
		panel.add(lbldefaultPasswordIs);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Remove admin", null, panel_1, null);
		panel_1.setLayout(null);
		
		JComboBox<String> comboBoxUsername = new JComboBox<String>();
		comboBoxUsername.setFont(new Font("Tahoma", Font.PLAIN, 17));
		comboBoxUsername.setBounds(175, 43, 139, 26);
		panel_1.add(comboBoxUsername);
		
		JLabel label_1 = new JLabel("Username:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setFont(new Font("Arial", Font.PLAIN, 22));
		label_1.setBounds(30, 39, 133, 30);
		panel_1.add(label_1);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Arial", Font.PLAIN, 20));
		btnDelete.setBounds(106, 104, 133, 38);
		panel_1.add(btnDelete);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Edit admin", null, panel_2, null);
		panel_2.setLayout(null);
		
		updateComboBoxFilters(comboBoxUsername);
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = textFieldName.getText();
				String username = textFieldUsername.getText();
				
				Admin adminToAdd = new Admin.Builder().id(132)
													  .name(name)
													  .username(username)
													  .password("1234")
													  .build();
				
				adminController.addAdmin(adminToAdd);
				
				JOptionPane.showMessageDialog(null, "Added [" + adminToAdd.getName() + " (" + adminToAdd.getUsername() + "]");
				
				updateComboBoxFilters(comboBoxUsername);
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = (String) comboBoxUsername.getSelectedItem();
				
				Admin adminToDelete = new Admin.Builder().username(username)
																   .build();
				
				adminController.deleteAdmin(adminToDelete);
				
				JOptionPane.showMessageDialog(null, "Deleted  [" + adminToDelete.getUsername() + "]");
				
				updateComboBoxFilters(comboBoxUsername);
			}
		});
	}
	
	public void updateComboBoxFilters(JComboBox<String> comboBoxUsernames){
		
		comboBoxUsernames.removeAllItems();
		
		listAdminUsernames = adminController.getListOfAdminUsernames();
		
		for(String username : listAdminUsernames)
		{
			comboBoxUsernames.addItem(""+username);
		}
	}
}
