package com.login;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.businessapplication.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class ChangePasswordWindow extends JFrame {

	private JPanel contentPane;
	private JPasswordField passwordField;
	private JPasswordField passwordFieldRepeat;
	
	private UserController userController = new UserController();

	/**
	 * Create the frame.
	 */
	public ChangePasswordWindow(int id) {
		setResizable(false);
		
		User thisUser = new User(id);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 466, 226);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblEnterNewPassword = new JLabel("Enter new password:");
		lblEnterNewPassword.setBounds(12, 13, 219, 38);
		lblEnterNewPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEnterNewPassword.setFont(new Font("Arial", Font.PLAIN, 20));
		contentPane.add(lblEnterNewPassword);
		
		JLabel lblRepeatPassword = new JLabel("Repeat password:");
		lblRepeatPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRepeatPassword.setFont(new Font("Arial", Font.PLAIN, 20));
		lblRepeatPassword.setBounds(12, 64, 219, 38);
		contentPane.add(lblRepeatPassword);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordField.setBounds(243, 19, 163, 28);
		contentPane.add(passwordField);
		
		passwordFieldRepeat = new JPasswordField();
		passwordFieldRepeat.setFont(new Font("Tahoma", Font.PLAIN, 16));
		passwordFieldRepeat.setBounds(243, 70, 163, 28);
		contentPane.add(passwordFieldRepeat);
		
		JButton btnSafeChanges = new JButton("Safe changes");
		btnSafeChanges.setFont(new Font("Arial", Font.PLAIN, 22));
		btnSafeChanges.setBounds(130, 120, 184, 46);
		contentPane.add(btnSafeChanges);
		
		
		btnSafeChanges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				String newPassword = new String(passwordField.getPassword());
				String newPasswordRepeat = new String( passwordFieldRepeat.getPassword());
				
				if(newPassword.equals(newPasswordRepeat)){
					try {
						userController.changePassword(thisUser, newPassword);
					} catch (SQLException e) {
						JOptionPane.showMessageDialog(null, "Error with database");
						e.printStackTrace();
					}
					
					JOptionPane.showMessageDialog(null, "Passwords changed successfully!");
					setVisible(false);
				} else {
					JOptionPane.showMessageDialog(null, "Passwords in thr fields don't match");
				}
				
			}
		});
	}
}
