package com.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.databaseconnection.DBConnection;
import com.emailconnection.EmailDriver;
import com.login.Hasher;

import twitter4j.TwitterException;

import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.awt.event.ActionEvent;

public class LoginWindow extends JFrame {

	private JPanel contentPane;
	private JTextField userNameTxtField;
	private JPasswordField passwordField;

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
		setBounds(100, 100, 460, 238);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Username:");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setFont(new Font("Arial", Font.PLAIN, 26));
		lblName.setBounds(57, 13, 139, 40);
		contentPane.add(lblName);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPassword.setFont(new Font("Arial", Font.PLAIN, 26));
		lblPassword.setBounds(57, 66, 139, 40);
		contentPane.add(lblPassword);
		
		userNameTxtField = new JTextField();
		userNameTxtField.setText("admin1");
		userNameTxtField.setFont(new Font("Tahoma", Font.PLAIN, 26));
		userNameTxtField.setBounds(198, 13, 193, 35);
		contentPane.add(userNameTxtField);
		userNameTxtField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 26));
		passwordField.setBounds(198, 66, 193, 35);
		contentPane.add(passwordField);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("Arial", Font.PLAIN, 26));
		btnLogin.setBounds(137, 119, 145, 43);
		contentPane.add(btnLogin);
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
		
				loginCheckWithHash();	
				//loginCheck();
			}	
		});
			
	}
	
	public void loginCheck()
	{
		try(Connection con = DBConnection.getCon()) {
			
			ResultSet rs = DBConnection.getData("SELECT * FROM users");
			
	        String username, name, password, usertype, category;
	        int id;
	        
	        String passwordToCheck = new String(passwordField.getPassword());
	        
	        while(rs.next())
	        {
	        	username = rs.getString("username");
	        	
	        	if(username.equals(userNameTxtField.getText())) {
	        		id = rs.getInt("id_user");
	        		usertype = rs.getString("usertype");
	        		name = rs.getString("name");
	        		category = rs.getString("category");
	        		password = rs.getString("password");
	        		
	        		if(password.equals(passwordToCheck))
	        		{
	        			if(usertype.equals("representative"))
	        			{
	        				RepresentativeWindow repWin = new RepresentativeWindow(id, name, username, password, category);
							repWin.setVisible(true);
							repWin.setTitle("Sales Representative - " + name + " (" + username + ") / " + category);
							//setVisible(false);
	        			}
	        			else
	        			{
		        			AdminWindow adminWin = new AdminWindow(id, name, username, password);
							adminWin.setVisible(true);
							adminWin.setTitle("Admin - " + name + " (" + username + ")");
	        			}
						
	        		}
	        	}
	        	
	        }
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void loginCheckWithHash()
	{
		try (Connection con = DBConnection.getCon()){
			
			ResultSet rs = DBConnection.getData("SELECT * FROM users");
			
	        String username, name, password, usertype, category;
	        int id;
	        
	        String passwordEnteredByUser = new String(passwordField.getPassword());
	        
	        while(rs.next())
	        {
	        	username = rs.getString("username");
	        	
	        	if(username.equals(userNameTxtField.getText())) {
	        		
	        		String[] passwordFiled = rs.getString("password").split(" ");
	        		
	     	        String saltedHashStringDB = passwordFiled[0];
	     	        String saltStringDB = passwordFiled[1];	       
	     	        
	     	        System.out.println("passwordEnteredByUser = " + passwordEnteredByUser);
	     	        System.out.println("saltStringDB = " + saltStringDB);
	     	        
	     	        String saltedHashBytesToCheck = Hasher.getSaltedHash(passwordEnteredByUser, saltStringDB);
	     	        
	     			System.out.println("saltedHashBytesToCheck = " + saltedHashBytesToCheck);
	     			System.out.println("saltedHash(in db)= " + saltedHashStringDB);
	     	        	
	        		if(saltedHashStringDB.equals(saltedHashBytesToCheck))
	        		{
	        			System.out.println("im after passwordToCheck.equals(saltedHashToCheck)");
	        			
	        			id = rs.getInt("id_user");
		        		usertype = rs.getString("usertype");
		        		name = rs.getString("name");
		        		category = rs.getString("category");
		        		
	        			if(usertype.equals("representative"))
	        			{
	        				RepresentativeWindow repWin = new RepresentativeWindow(id, name, username, saltedHashStringDB, category);
							repWin.setVisible(true);
							repWin.setTitle("Sales Representative - " + name + " (" + username + ") / " + category);
							setVisible(false);
	        			}
	        			else
	        			{
		        			AdminWindow adminWin = new AdminWindow(id, name, username, saltedHashStringDB);
							adminWin.setVisible(true);
							adminWin.setTitle("Admin - " + name + " (" + username + ")");
	        			}
						
	        		}
	        	}
	        	
	        }
	        
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
