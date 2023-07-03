package com.insignia.entity;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="tokens_table")
public class TokensEntity {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name="token_sequence_number")
	private Long tokenSequenceNumber;

	@Column(name="token_type", nullable = false)
	private String tokenType;

	@Column(name="token_details")
	private String tokenDetails;

	
	@Column(name="token_expires_at")
	private Date tokenExpiresAt;

	
	@Column(name="token_created_at")
	private Date tokenCreatedAt;

	
	@Column(name="token_revoked_at")
	private Date tokenRevokedAt;
	
	
	@Column(name="is_token_expired")
	private Boolean isTokenExpired;
	
	
	@Column(name="is_long_lived_token")
	private Boolean isLongLivedToken;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name="sequence_number")
	private CustomerBasicDetailsEntity CustomerBasicDetailsEntity;
	
	public TokensEntity( String tokenType, String tokenDetails, Date tokenExpiresAt,
			Date tokenCreatedAt, Date tokenRevokedAt, Boolean isTokenExpired, Boolean isLongLivedToken,
			com.insignia.entity.CustomerBasicDetailsEntity customerBasicDetailsEntity) {
		super();
		this.tokenType = tokenType;
		this.tokenDetails = tokenDetails;
		this.tokenExpiresAt = tokenExpiresAt;
		this.tokenCreatedAt = tokenCreatedAt;
		this.tokenRevokedAt = tokenRevokedAt;
		this.isTokenExpired = isTokenExpired;
		this.isLongLivedToken = isLongLivedToken;
		CustomerBasicDetailsEntity = customerBasicDetailsEntity;
	}

	public TokensEntity() {
		super();
	}
	
}
