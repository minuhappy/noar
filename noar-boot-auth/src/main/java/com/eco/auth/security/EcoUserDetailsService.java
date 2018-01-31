package com.eco.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.noar.util.BeanUtil;

/**
 * User Information provider service for security
 * 
 * @author Minu.Kim
 */
@Service
public class EcoUserDetailsService implements UserDetailsService {
	@Autowired
	Environment env;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (username == null || username.isEmpty()) {
			throw new UsernameNotFoundException("Empty username is not allowed!");
		}

		IUser user = BeanUtil.get(IUserService.class).getUser(username);

		if (user == null) {
			throw new UsernameNotFoundException("User [" + username + "] is not exist.");
		}

		return new EcoUserDetails(user);
	}
}