package com.noar.webapp.entity;

import java.util.Date;

import com.noar.dbist.annotation.Column;
import com.noar.dbist.annotation.GenerationRule;
import com.noar.dbist.annotation.Index;
import com.noar.dbist.annotation.PrimaryKey;
import com.noar.dbist.annotation.Table;

@Table(name = "users", idStrategy = GenerationRule.NONE, indexes = {
		@Index(name = "ix_user_1", columnList = "domain_id"), 
		@Index(name = "ix_user_2", columnList = "login"), 
		@Index(name = "ix_user_3", columnList = "email"),
		@Index(name = "ix_user_4", columnList = "name"), 
		@Index(name = "ix_user_5", columnList = "active_flag"), 
		@Index(name = "ix_user_6", columnList = "admin_flag") })
public class User {
	@PrimaryKey
	@Column(name = "id", nullable = false)
	private String id;

	@Column(name = "login", nullable = false, length = 25)
	private String login;

	@Column(name = "email")
	private String email;

	@Column(name = "encrypted_password", nullable = false, length = 80)
	private String encryptedPassword;

	@Column(name = "reset_password_token", length = 80)
	private String resetPasswordToken;

	@Column(name = "reset_password_sent_at")
	private Date resetPasswordSentAt;

	@Column(name = "remember_created_at")
	private Date rememberCreatedAt;

	@Column(name = "sign_in_count")
	private Integer signInCount;

	@Column(name = "current_sign_in_at")
	private Date currentSignInAt;

	@Column(name = "last_sign_in_at")
	private Date lastSignInAt;

	@Column(name = "current_sign_in_ip")
	private String currentSignInIp;

	@Column(name = "last_sign_in_ip")
	private String lastSignInIp;

	@Column(name = "name", nullable = false, length = 30)
	private String name;

	@Column(name = "dept")
	private String dept;

	@Column(name = "division")
	private String division;

	@Column(name = "locale")
	private String locale;

	@Column(name = "timezone", length = 64)
	private String timezone;

	@Column(name = "super_user")
	private Boolean superUser;

	@Column(name = "admin_flag")
	private Boolean adminFlag;

	@Column(name = "operator_flag")
	private Boolean operatorFlag;

	@Column(name = "active_flag")
	private Boolean activeFlag;

	@Column(name = "exclusive_role", length = 20)
	private String exclusiveRole;

	@Column(name = "account_type", length = 20)
	private String accountType;

	@Column(name = "password_expire_date", length = 20)
	private String passwordExpireDate;

	@Column(name = "account_expire_date", length = 20)
	private String accountExpireDate;

	public User() {
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * @param login the login to set
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the encryptedPassword
	 */
	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	/**
	 * @param encryptedPassword the encryptedPassword to set
	 */
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	/**
	 * @return the resetPasswordToken
	 */
	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	/**
	 * @param resetPasswordToken the resetPasswordToken to set
	 */
	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	/**
	 * @return the resetPasswordSentAt
	 */
	public Date getResetPasswordSentAt() {
		return resetPasswordSentAt;
	}

	/**
	 * @param resetPasswordSentAt the resetPasswordSentAt to set
	 */
	public void setResetPasswordSentAt(Date resetPasswordSentAt) {
		this.resetPasswordSentAt = resetPasswordSentAt;
	}

	/**
	 * @return the rememberCreatedAt
	 */
	public Date getRememberCreatedAt() {
		return rememberCreatedAt;
	}

	/**
	 * @param rememberCreatedAt the rememberCreatedAt to set
	 */
	public void setRememberCreatedAt(Date rememberCreatedAt) {
		this.rememberCreatedAt = rememberCreatedAt;
	}

	/**
	 * @return the signInCount
	 */
	public Integer getSignInCount() {
		return signInCount;
	}

	/**
	 * @param signInCount the signInCount to set
	 */
	public void setSignInCount(Integer signInCount) {
		this.signInCount = signInCount;
	}

	/**
	 * @return the currentSignInAt
	 */
	public Date getCurrentSignInAt() {
		return currentSignInAt;
	}

	/**
	 * @param currentSignInAt the currentSignInAt to set
	 */
	public void setCurrentSignInAt(Date currentSignInAt) {
		this.currentSignInAt = currentSignInAt;
	}

	/**
	 * @return the lastSignInAt
	 */
	public Date getLastSignInAt() {
		return lastSignInAt;
	}

	/**
	 * @param lastSignInAt the lastSignInAt to set
	 */
	public void setLastSignInAt(Date lastSignInAt) {
		this.lastSignInAt = lastSignInAt;
	}

	/**
	 * @return the currentSignInIp
	 */
	public String getCurrentSignInIp() {
		return currentSignInIp;
	}

	/**
	 * @param currentSignInIp the currentSignInIp to set
	 */
	public void setCurrentSignInIp(String currentSignInIp) {
		this.currentSignInIp = currentSignInIp;
	}

	/**
	 * @return the lastSignInIp
	 */
	public String getLastSignInIp() {
		return lastSignInIp;
	}

	/**
	 * @param lastSignInIp the lastSignInIp to set
	 */
	public void setLastSignInIp(String lastSignInIp) {
		this.lastSignInIp = lastSignInIp;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the dept
	 */
	public String getDept() {
		return dept;
	}

	/**
	 * @param dept the dept to set
	 */
	public void setDept(String dept) {
		this.dept = dept;
	}

	/**
	 * @return the division
	 */
	public String getDivision() {
		return division;
	}

	/**
	 * @param division the division to set
	 */
	public void setDivision(String division) {
		this.division = division;
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return the timezone
	 */
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @param timezone the timezone to set
	 */
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @return the adminFlag
	 */
	public Boolean getAdminFlag() {
		return adminFlag;
	}

	/**
	 * @param adminFlag the adminFlag to set
	 */
	public void setAdminFlag(Boolean adminFlag) {
		this.adminFlag = adminFlag;
	}

	/**
	 * @return the operatorFlag
	 */
	public Boolean getOperatorFlag() {
		return operatorFlag;
	}

	/**
	 * @param operatorFlag the operatorFlag to set
	 */
	public void setOperatorFlag(Boolean operatorFlag) {
		this.operatorFlag = operatorFlag;
	}

	/**
	 * @return the activeFlag
	 */
	public Boolean getActiveFlag() {
		return activeFlag;
	}

	/**
	 * @param activeFlag the activeFlag to set
	 */
	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

	/**
	 * @return the superUser
	 */
	public Boolean getSuperUser() {
		return superUser;
	}

	/**
	 * @param superUser the superUser to set
	 */
	public void setSuperUser(Boolean superUser) {
		this.superUser = superUser;
	}

	/**
	 * @return the exclusiveRole
	 */
	public String getExclusiveRole() {
		return exclusiveRole;
	}

	/**
	 * @param exclusiveRole the exclusiveRole to set
	 */
	public void setExclusiveRole(String exclusiveRole) {
		this.exclusiveRole = exclusiveRole;
	}

	/**
	 * Get Account Type (ex.user,token,json...)
	 * 
	 * @return
	 */
	public String getAccountType() {
		return accountType;
	}

	/**
	 * Set Account Type (ex.user,token,json...)
	 * 
	 * @param accountType
	 */
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	/**
	 * Get Password Expire Date
	 * 
	 * @return
	 */
	public String getPasswordExpireDate() {
		return passwordExpireDate;
	}

	/**
	 * Set Password Expire Date
	 * 
	 * @param passwordExpireDate
	 */
	public void setPasswordExpireDate(String passwordExpireDate) {
		this.passwordExpireDate = passwordExpireDate;
	}

	/**
	 * Get Account Expire Date
	 * 
	 * @return
	 */
	public String getAccountExpireDate() {
		return accountExpireDate;
	}

	/**
	 * Set Account Expire Date
	 * 
	 * @param accountExpireDate
	 */
	public void setAccountExpireDate(String accountExpireDate) {
		this.accountExpireDate = accountExpireDate;
	}
}