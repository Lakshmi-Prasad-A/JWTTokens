package com.insignia.serviceInterface;

import com.insignia.model.AuthenticationRequest;
import com.insignia.model.AuthenticationResponse;

public interface IJwtService {

	void updateTokenDetails(AuthenticationRequest authRequest,AuthenticationResponse authRes);

	void deleteTokenDetails(Long seqNo);

	public void createTokenDetails(AuthenticationRequest authRequest, AuthenticationResponse authRes);

	public AuthenticationResponse fetchExistTokenIfPresent(AuthenticationResponse authRes, AuthenticationRequest authReq) throws Exception;

}
