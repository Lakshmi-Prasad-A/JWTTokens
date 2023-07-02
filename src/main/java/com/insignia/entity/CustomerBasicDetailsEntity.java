package com.insignia.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Generated;
import javax.management.relation.Role;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_basic_details")
public class CustomerBasicDetailsEntity {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "sequence_number", nullable = false)
	private Long sequenceNumber;

	@Column(name = "application_id", nullable = false)
	private String applicationId;

	@Column(name = "tenant_id", nullable = false)
	private String tenantId;

	@Column(name = "customer_id", unique = true, nullable = false)
	private String customerId;

	@Column(name = "customer_password", nullable = false)
	private String customerPassword;

	@Column(name = "customer_email", unique = true, nullable = false)
	private String customerEmail;

	private String userName;

	private String roleName;

	@OneToOne(mappedBy = "CustomerBasicDetailsEntity", cascade = CascadeType.ALL)
	private TokensEntity tokenEntity;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "sequence_number")
	private List<RolesAndPermissions> rolesAndPermissions = new ArrayList<>();

	public CustomerBasicDetailsEntity(String applicationId, String tenantId, String customerId, String customerPassword,
			String customerEmail, String userName, TokensEntity tokenEntity,
			List<RolesAndPermissions> rolesAndPermissions) {
		super();
		this.applicationId = applicationId;
		this.tenantId = tenantId;
		this.customerId = customerId;
		this.customerPassword = customerPassword;
		this.customerEmail = customerEmail;
		this.userName = userName;
		this.tokenEntity = tokenEntity;
		this.rolesAndPermissions = rolesAndPermissions;
	}

}
