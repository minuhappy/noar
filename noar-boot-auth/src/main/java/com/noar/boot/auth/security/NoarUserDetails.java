package com.noar.boot.auth.security;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User Information for security
 * 
 * @author Minu.Kim
 */
@SuppressWarnings("serial")
public class NoarUserDetails implements UserDetails {

	private IUser userDetail;

	public NoarUserDetails(final IUser user) {
		super();
		this.userDetail = user;
	}

	@Override
	public String getPassword() {
		return this.userDetail.getPassword();
	}

	@Override
	public String getUsername() {
		return userDetail.getUsername();
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return null;
	}

	/**
	 * 계정 만료 상태 확인.
	 */
	@Override
	public boolean isAccountNonExpired() {
		return this.isNonExpireDate(this.userDetail.getAccountExpireDate());
	}

	/**
	 * 계정 잠금 상태 확인.
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	/**
	 * 비밀번호 만료 상태 확인.
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return this.isNonExpireDate(this.userDetail.getPasswordExpireDate());
	}

	@Override
	public boolean isEnabled() {
		return this.userDetail != null ? this.userDetail.getActiveFlag() : true;
	}

	@Override
	public boolean equals(Object object) {
		return object != null && object instanceof NoarUserDetails && ((NoarUserDetails) object).getUsername().equals(getUsername());
	}

	@Override
	public int hashCode() {
		return getUsername() == null ? 0 : getUsername().hashCode();
	}

	private boolean isNonExpireDate(String date) {
		return date == null || date.isEmpty() ? true : LocalDate.parse(date).isBefore(LocalDate.now());
	}

	public IUser getUser() {
		return this.userDetail;
	}
}