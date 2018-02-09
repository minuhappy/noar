package com.noar.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "user", indexes = { 
		@Index(name = "index_user_on_name", columnList = "name"),
		@Index(name = "index_user_on_eng_name", columnList = "eng_name") })
public class User {

	@Id
	@Column(name = "id", nullable = false, length = 255)
	private String id;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "name", length = 50)
	private String name;

	@Column(name = "eng_name", length = 500)
	private String engName;

	@Column(name = "mobile", length = 50)
	private String mobile;

	@Column(name = "email", length = 500)
	private String email;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEngName() {
		return engName;
	}

	public void setEngName(String engName) {
		this.engName = engName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User() {
	}

	public User(String id) {
		this.id = id;
	}
	
	public User(User user) {
		this.id = user.getId();
		this.password = user.getPassword();
		this.name = user.getName();
		this.engName = user.getEngName();
		this.email = user.getEmail();
		this.mobile = user.getMobile();
	}
}