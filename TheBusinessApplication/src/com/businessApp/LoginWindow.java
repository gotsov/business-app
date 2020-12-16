package com.businessApp;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class LoginWindow extends JFrame {
	
	static DBConnection cDB = new DBConnection();
	static Connection con = cDB.createConnection();

	private JPanel contentPane;
	private JTextField userNameTxtField;
	private JPasswordField passwordField;
	private JLabel lblLoginAs;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JPanel panel = new JPanel();
					LoginWindow frame = new LoginWindow();
					frame.setVisible(true);
					
					frame.getContentPane().add(panel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 479, 293);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Username:");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setFont(new Font("Arial", Font.PLAIN, 26));
		lblName.setBounds(54, 77, 139, 40);
		contentPane.add(lblName);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 26));
		lblPassword.setBounds(54, 130, 139, 40);
		contentPane.add(lblPassword);
		
		userNameTxtField = new JTextField();
		userNameTxtField.setText("ivanov1");
		userNameTxtField.setFont(new Font("Tahoma", Font.PLAIN, 26));
		userNameTxtField.setBounds(195, 77, 193, 35);
		contentPane.add(userNameTxtField);
		userNameTxtField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 26));
		passwordField.setBounds(195, 130, 193, 35);
		contentPane.add(passwordField);
		
		lblLoginAs = new JLabel("Login as:");
		lblLoginAs.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLoginAs.setFont(new Font("Arial", Font.PLAIN, 26));
		lblLoginAs.setBounds(54, 24, 139, 40);
		contentPane.add(lblLoginAs);
		
		JComboBox rolesComboBox = new JComboBox();
		rolesComboBox.setFont(new Font("Tahoma", Font.PLAIN, 26));
		rolesComboBox.setModel(new DefaultComboBoxModel(new String[] {"admin", "representative"}));
		rolesComboBox.setBounds(195, 24, 193, 35);
		contentPane.add(rolesComboBox);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Arial", Font.PLAIN, 26));
		btnLogin.setBounds(152, 190, 145, 43);
		contentPane.add(btnLogin);
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
						
				if(rolesComboBox.getSelectedItem().equals("admin"))
				{
					
        			AdminWindow adminWin = new AdminWindow();
					adminWin.setVisible(true);
					//setVisible(false);
					
				}
				else if(rolesComboBox.getSelectedItem().equals("representative"))
				{
					try {
						Statement stmt = con.createStatement();
				        ResultSet rs = stmt.executeQuery("SELECT * FROM allrepresentatives");
				        String username, password, category;
				        while(rs.next())
				        {
				        	username = rs.getString("username");
				        	
				        	if(username.equals(userNameTxtField.getText()))
				        	{
				        		password = rs.getString("password");
				        		category = rs.getString("category");
				        		String passToCheck = new String(passwordField.getPassword());
				        		if(password.equals(passToCheck))
				        		{
									RepresentativeWindow repWin = new RepresentativeWindow(username, category);
									repWin.setVisible(true);
									repWin.setTitle("Sales Representative - " + username + "/" + category);
									//setVisible(false);
				        		}
				        	}
				        }
				        
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					
				}
				
			}
		});
		
		
		
	}
}
