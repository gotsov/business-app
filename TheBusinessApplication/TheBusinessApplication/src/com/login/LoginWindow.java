package com.login;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.databaseconnection.DBConnection;
import com.emailconnection.EmailDriver;
import com.exceptions.InvalidUserException;

import twitter4j.TwitterException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
		setTitle("TheBusinessApp - Login");
		setResizable(false);
		
		LoginController loginController = new LoginController();
		
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
	
				try {
					loginController.checkLogin(passwordField, userNameTxtField);
					setVisible(false);
				} catch (SQLException e) {
					JOptionPane.showMessageDialog(null, "Error with database");
					e.printStackTrace();
				} catch (InvalidUserException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}	
			}	
		});
			
	}
	
}
