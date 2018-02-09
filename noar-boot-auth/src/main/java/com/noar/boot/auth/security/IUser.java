package com.noar.boot.auth.security;

public interface IUser {
	public String getUsername();

	public String getPassword();

	public String getAccountExpireDate();

	public String getPasswordExpireDate();

	public boolean getActiveFlag();
}
