package com.insignia.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.insignia.entity.RolesAndPermissions;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token;
	private String type;
	private Date expirationTime;
	private String errorCode;
	private String errorMessage;
	@JsonIgnore
	private String tokenStatus;
	@JsonIgnore
	private Long customerSeqNumber;
	
	private RolesAndPermissions rolesAndPermissions;

	public String getTokenStatus() {
		return tokenStatus;
	}

	public void setTokenStatus(String tokenStatus) {
		this.tokenStatus = tokenStatus;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Long getCustomerSeqNumber() {
		return customerSeqNumber;
	}

	public void setCustomerSeqNumber(Long customerSeqNumber) {
		this.customerSeqNumber = customerSeqNumber;
	}

	public RolesAndPermissions getRolesAndPermissions() {
		return rolesAndPermissions;
	}

	public void setRolesAndPermissions(RolesAndPermissions rolesAndPermissions) {
		this.rolesAndPermissions = rolesAndPermissions;
	}
	public AuthenticationResponse(String token, String type, Date expirationTime,
			RolesAndPermissions rolesAndPermissions) {
		super();
		this.token = token;
		this.type = type;
		this.expirationTime = expirationTime;
		this.rolesAndPermissions = rolesAndPermissions;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public AuthenticationResponse(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public AuthenticationResponse() {
	}


	@Override
	public String toString() {
		return "AuthenticationResponse [token=" + token + ", type=" + type + ", expirationTime=" + expirationTime
				+ ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + "]";
	}

}
