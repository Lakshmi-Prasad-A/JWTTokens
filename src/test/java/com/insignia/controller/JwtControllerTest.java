package com.insignia.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.insignia.daoInterface.JwtDao;
import com.insignia.entity.CustomerBasicDetailsEntity;
import com.insignia.entity.RolesAndPermissions;
import com.insignia.model.AuthenticationRequest;
import com.insignia.model.AuthenticationResponse;
import com.insignia.serviceInterface.IJwtService;
import com.insignia.userdetailsservice.CustomUserDetailsService;
import com.insignia.util.JwtUtil;

@ExtendWith(MockitoExtension.class)
public class JwtControllerTest {

	

	@InjectMocks
	private JwtController jwtController;

	@Mock
	private JwtDao jwtdao;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtUtil jwtTokenUtil;

	@Mock
	private CustomUserDetailsService userDetailsService;

	@Mock
	private UserDetailsService userDetails;

	@Mock
	private IJwtService service;
	
	@Value("${errorCodes.500}")
	private String internalServerError;

	@Value("${errorCodes.403}")
	private String UNAUTHORIZED;

	@Value("${errorCodes.400}")
	private String badRequest;
	
	AuthenticationResponse authResp = new AuthenticationResponse();
	AuthenticationRequest authenticationRequest = new AuthenticationRequest();
	CustomerBasicDetailsEntity customerDetails = new CustomerBasicDetailsEntity();
	ResponseEntity<?> res =null;
	List<RolesAndPermissions> rolsList=new ArrayList<>();
	RolesAndPermissions role=new RolesAndPermissions();
	
	public void dataInitilization() {
		authResp.setExpirationTime(new Date());
		authResp.setType("JWT");
		authResp.setToken("1252ddf52drf25");
		authResp.setTokenStatus("update");
		res = ResponseEntity.status(HttpStatus.OK).body(authResp);

		authenticationRequest.setApplicationId("112");
		authenticationRequest.setExpirationTime(30);
		authenticationRequest.setTenantId("1124");
		authenticationRequest.setUserId("2545");
		authenticationRequest.setPassword("5485");

		customerDetails.setUserName(authenticationRequest.getCustomeUserName());
		customerDetails.setCustomerPassword(authenticationRequest.getPassword());
		customerDetails.setSequenceNumber(authResp.getCustomerSeqNumber());
		
		role.setPermission1(true);
		role.setRoleId(1);
		rolsList.add(role);
		customerDetails.setRolesAndPermissions(rolsList);
	}

	@Test
	public void testCreateAuthenticationToken() throws Exception {
		dataInitilization();
		User user = new User(authenticationRequest.getCustomeUserName(), authenticationRequest.getPassword(),new ArrayList<GrantedAuthority>(new ArrayList<>()));
		when(userDetailsService.loadUserByUsername(authenticationRequest.getCustomeUserName())).thenReturn(user);
		when(userDetailsService.getCustomerBasicDetailsEntity()).thenReturn(customerDetails);
		ResponseEntity<?> createAuthenticationToken = jwtController.createAuthenticationToken(authenticationRequest);
		assertEquals(createAuthenticationToken.getStatusCode(), res.getStatusCode());
	}
	
	@Test
	public void testCreateAuthenticationTokenForBadRequest() throws Exception {
		dataInitilization();
		User user = new User(authenticationRequest.getCustomeUserName(), authenticationRequest.getPassword(),new ArrayList<GrantedAuthority>(new ArrayList<>()));
		authResp.setCustomerSeqNumber(1l);
		
		when(userDetailsService.loadUserByUsername(authenticationRequest.getCustomeUserName())).thenReturn(user);
		when(userDetailsService.getCustomerBasicDetailsEntity()).thenThrow(BadRequest.class);
		
		
		ResponseEntity<?> createAuthenticationToken = jwtController.createAuthenticationToken(authenticationRequest);
		assertEquals(HttpStatus.UNAUTHORIZED,createAuthenticationToken.getStatusCode());
	}
	
	@Test
	public void testCreateAuthenticationTokenForUnAuthorization() throws Exception {
		dataInitilization();
		customerDetails.setSequenceNumber(9l);
		User user = new User("raja", authenticationRequest.getPassword(),new ArrayList<GrantedAuthority>(new ArrayList<>()));
		when(userDetailsService.loadUserByUsername(authenticationRequest.getCustomeUserName())).thenReturn(user);
		when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getCustomeUserName(), authenticationRequest.getPassword()))).thenThrow(BadCredentialsException.class);
		when(userDetailsService.getCustomerBasicDetailsEntity()).thenReturn(customerDetails);
		ResponseEntity<?> createAuthenticationToken = jwtController.createAuthenticationToken(authenticationRequest);
		assertEquals(HttpStatus.UNAUTHORIZED,createAuthenticationToken.getStatusCode());
	}
	
	@Test
	public void testCreateAuthenticationTokenUserIdLength() throws Exception {
		dataInitilization();
		authenticationRequest.setApplicationId(null);
		ResponseEntity<?> createAuthenticationToken = jwtController.createAuthenticationToken(authenticationRequest);
		assertEquals(HttpStatus.BAD_REQUEST,createAuthenticationToken.getStatusCode());
	}
	
	
}
