package com.insignia.serviceInterface;

import com.insignia.model.AuthenticationRequest;
import com.insignia.model.AuthenticationResponse;
import com.insignia.model.CustomerBasicDetailsEntity;

public interface IJwtService {

	void updateTokenDetails(AuthenticationRequest authRequest,AuthenticationResponse authRes);

	void deleteTokenDetails(Long seqNo);

	public void createTokenDetails(AuthenticationRequest authRequest, AuthenticationResponse authRes);

	public AuthenticationResponse fetchExistTokenIfPresent(AuthenticationResponse authRes, AuthenticationRequest authReq) throws Exception;
	
	public CustomerBasicDetailsEntity findByUserIdPassword(AuthenticationRequest authRequest);

}
