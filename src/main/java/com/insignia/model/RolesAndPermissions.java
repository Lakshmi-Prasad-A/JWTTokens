package com.insignia.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="roles_and_permissions")
public class RolesAndPermissions {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id

	@Column(name="role_id")
	private Integer roleId;
	
	@Column(name="role_name")
	private String roleName;
	
	
	private Boolean permission1;
	
	private Boolean permission2;
	
	private Boolean permission3;
	
	private Boolean permission4;
	
	@Column(name="role_approved_date")
	private Date roleApprovedDate;
	
	
	@Column(name="role_revoked_date")
	private Date roleRevokedDate;
	
	
	@Column(name="permission_change_date")
	private Date permissionChangeDate;
	
	
	@Column(name="updated_permissions")
	private String updatedPermissions;
	
	
	@Column(name="sequence_number")
	private Long sequence_number;


	public RolesAndPermissions(String roleName) {
		super();
		this.roleName = roleName;
	}
	
	//@JoinColumn(name="sequence_number")
	//@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	//private CustomerBasicDetailsEntity CustomerBasicDetailsEntity;
	
	
}
