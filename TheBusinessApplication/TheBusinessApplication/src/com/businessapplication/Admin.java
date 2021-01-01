package com.businessapplication;

import com.businessapplication.Admin.Builder;
import com.rolecontrollers.AdminController;

public class Admin extends AdminController{
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
	
}
