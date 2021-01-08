package com.businessapplication;

import com.businessapplication.Admin.Builder;
import com.rolecontrollers.AdminController;

public class Admin{
	private int id;
	private String name;
	private String username;
	private String password;
	
	public Admin(Builder builder)
	{
		this.name = builder.name;
		this.id = builder.id;
		this.username = builder.username;
		this.password = builder.password;

	}
	
	public static class Builder
	{
		private String name;
		private int id;
		private String username;
		private String password;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
	
		public Builder id(int id) {
			this.id = id;
			return this;
		}	
		
		public Admin build() {
			return new Admin(this);
		}

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
