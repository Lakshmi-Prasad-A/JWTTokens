package com.insignia.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insignia.entity.CustomerBasicDetailsEntity;

public interface CustomerBasicDetailsRepo extends JpaRepository<CustomerBasicDetailsEntity, Serializable> {

	public final static String FETCH_USER_DETAILS = "SELECT CONCAT(customer_id,application_id,tenant_id) AS userName,COALESCE(customer_password,'') AS PASSWORD,cbd.sequence_number,rol.role_name,rol.role_id,rol.permission1,rol.permission2, rol.permission3,rol.permission4,rol.role_approved_date, rol.role_revoked_date, rol.permission_change_date,rol.updated_permissions FROM customer_basic_details AS cbd INNER JOIN roles_and_permissions AS rol ON cbd.sequence_number=rol.sequence_number WHERE CONCAT(customer_id,application_id,tenant_id)=:userName or (customer_id=:userName and customer_password='')";
	

	@Query(value = FETCH_USER_DETAILS, nativeQuery = true)
	public List<Object[]> fetchUserDetails(@Param("userName") String userName);

}
