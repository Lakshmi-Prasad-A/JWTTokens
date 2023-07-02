package com.insignia.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserModelClass {
	private String applicationId;
	private String tenantId;
	private String emailId;
	private String customerId;
	
}
