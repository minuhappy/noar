package com.noar.boot.auth.security;

public interface IUserService {
	public IUser getUser(String username);

	public IUser getUserByToken(String token);
}
