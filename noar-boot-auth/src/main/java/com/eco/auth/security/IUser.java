package com.eco.auth.security;

public interface IUser {
	public String getUsername();

	public String getPassword();

	public String getAccountExpireDate();

	public String getPasswordExpireDate();

	public boolean getActiveFlag();
}
