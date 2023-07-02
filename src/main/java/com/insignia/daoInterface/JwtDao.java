package com.insignia.daoInterface;


import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.insignia.entity.CustomerBasicDetailsEntity;
import com.insignia.entity.TokensEntity;
import com.insignia.model.AuthenticationRequest;
import com.insignia.model.AuthenticationResponse;

public interface JwtDao {
	

	public Optional<List<TokensEntity>> fetchTokendetails(Long seqNo) throws Exception;

	public void updateTokenDetails(AuthenticationRequest authRequest, AuthenticationResponse authRes);

	public void deleteTokenDetails(Long seqNo);

	public void createTokenDetails(AuthenticationRequest authRequest, AuthenticationResponse authRes);
	
	public CustomerBasicDetailsEntity findByUserName(String userName) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, ParseException;


}
