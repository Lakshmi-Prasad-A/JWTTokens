package com.insignia.controller;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.insignia.customExceptions.InvalidInputParametersException;
import com.insignia.entity.CustomerBasicDetailsEntity;
import com.insignia.entity.RolesAndPermissions;
import com.insignia.model.AuthenticationRequest;
import com.insignia.model.AuthenticationResponse;
import com.insignia.serviceInterface.IJwtService;
import com.insignia.stringValidator.StringValidation;
import com.insignia.userdetailsservice.CustomUserDetailsService;
import com.insignia.util.Constants;
import com.insignia.util.JwtUtil;

@RestController
public class JwtController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private IJwtService service;

	@Value("${errorCodes.500}")
	private String internalServerError;

	@Value("${errorCodes.403}")
	private String UNAUTHORIZED;

	@Value("${errorCodes.406}")
	private String decryptionError;

	@Value("${errorCodes.407}")
	private String unexpectedError;

	@Autowired
	public RestTemplate restTemplate;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		AuthenticationResponse authResp = new AuthenticationResponse();
		UserDetails userDetails = null;
		String customeUserName = null;
		try {

			if (authenticationRequest.getIsToValidatePassword() && authenticationRequest.getEmailId() != null) {
				userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmailId());
				authenticationRequest
						.setPassword(userDetailsService.getCustomerBasicDetailsEntity().getCustomerPassword());
				authenticationRequest.setExpirationTime(60);
				customeUserName = authenticationRequest.getEmailId();
			} else {
				StringValidation.ValidateUserId(authenticationRequest.getUserId(), Constants.userIdlength);

				StringValidation.ValidateApplicationId(authenticationRequest.getApplicationId(),
						Constants.applicationIdlength);
				StringValidation.ValidateTenantId(authenticationRequest.getTenantId(), Constants.tenantIdlength);
				StringValidation.ValidatePassword(authenticationRequest.getPassword(),Constants.passwordlength );
				
				userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getCustomeUserName());
				customeUserName = authenticationRequest.getCustomeUserName();
			}

			CustomerBasicDetailsEntity customerBasicData = userDetailsService.getCustomerBasicDetailsEntity();
			if (!authenticationRequest.getIsToValidatePassword() && customerBasicData != null
					&& !userDetails.getPassword().equalsIgnoreCase(authenticationRequest.getPassword())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new AuthenticationResponse(Constants.errorCode403, UNAUTHORIZED));
			}
			if (customerBasicData != null && customerBasicData.getSequenceNumber() != null) {
				authResp.setCustomerSeqNumber(customerBasicData.getSequenceNumber());
				service.fetchExistTokenIfPresent(authResp, authenticationRequest);
			}

			if (authResp != null && authResp.getTokenStatus() != null
					&& authResp.getTokenStatus().equalsIgnoreCase(Constants.errorCode403)) {

				return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new AuthenticationResponse(Constants.errorCode403, UNAUTHORIZED));
			} else if (authResp != null && authResp.getTokenStatus() != null
					&& (authResp.getTokenStatus().equalsIgnoreCase(Constants.statusTokenLongLived)
							|| authResp.getTokenStatus().equalsIgnoreCase(Constants.statusTokenNotExpired))) {
				authResp.setRolesAndPermissions(customerBasicData.getRolesAndPermissions().get(0));
				return ResponseEntity.ok(authResp);
			}

			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(customeUserName, authenticationRequest.getPassword()));

			final String jwt = jwtTokenUtil.generateToken(userDetails, authenticationRequest.getExpirationTime());
			authResp.setToken(jwt);
			service.updateTokenDetails(authenticationRequest, authResp);
			RolesAndPermissions rolesAndPermissions = null;
			if (customerBasicData != null && !customerBasicData.getRolesAndPermissions().isEmpty()
					&& customerBasicData.getRolesAndPermissions().get(0) != null) {
				rolesAndPermissions = customerBasicData.getRolesAndPermissions().get(0);
			}

			return ResponseEntity.ok(new AuthenticationResponse(jwt, Constants.tokenType,
					jwtTokenUtil.extractExpiration(jwt), rolesAndPermissions));

		} catch (InvalidInputParametersException ex) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new AuthenticationResponse(ex.getErrorCode(), ex.getStrMsg()));

		} catch (BadCredentialsException e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new AuthenticationResponse(Constants.errorCode403, UNAUTHORIZED));

		} catch (Exception e) {
			if (e.getCause() != null && (e.getCause().equals(InvalidKeyException.class)
					|| e.getCause().equals(NoSuchAlgorithmException.class)
					|| e.getCause().equals(NoSuchPaddingException.class)
					|| e.getCause().equals(IllegalBlockSizeException.class)
					|| e.getCause().equals(BadPaddingException.class))) {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new AuthenticationResponse(Constants.errorCode406, decryptionError));
			}
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new AuthenticationResponse(Constants.errorCode407, unexpectedError));
		}

	}

}
