package com.insignia.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insignia.daoInterface.JwtDao;
import com.insignia.entity.TokensEntity;
import com.insignia.model.AuthenticationRequest;
import com.insignia.model.AuthenticationResponse;
import com.insignia.repository.TokensEntityRepo;
import com.insignia.serviceInterface.IJwtService;
import com.insignia.util.Constants;

@Service
public class IJwtServiceImpl implements IJwtService {

	@Autowired
	private JwtDao jwtdao;

	public AuthenticationResponse fetchExistTokenIfPresent(AuthenticationResponse authRes,
			AuthenticationRequest authReq) throws Exception {

		Optional<List<TokensEntity>> tokenDetails = jwtdao.fetchTokendetails(authRes.getCustomerSeqNumber());

		if (tokenDetails.isPresent()) {
			Optional<TokensEntity> isLongLivedToken = tokenDetails.get().stream()
					.filter(p -> p.getIsLongLivedToken() && p.getTokenRevokedAt() == null).findFirst();

			if (isLongLivedToken.isPresent()) {

				longLivedToken(isLongLivedToken.get(), authRes);

				return authRes;

			} else {

				Optional<TokensEntity> activeJwtToken = tokenDetails.get().stream().filter(p -> !p.getIsTokenExpired()
						&& p.getTokenRevokedAt() == null && p.getTokenExpiresAt().compareTo(new Date()) == 1)
						.findFirst();

				activeOrExpiredToken(activeJwtToken, authRes, authReq);

				return authRes;

			}
		} else {
			createTokenDetails(authReq, authRes);
			authRes.setTokenStatus(Constants.statusNewToken);
			return authRes;
		}
	}

	private void activeOrExpiredToken(Optional<TokensEntity> activeJwtToken, AuthenticationResponse authRes,
			AuthenticationRequest authReq) {
		if (activeJwtToken.isPresent()) {
			authRes.setExpirationTime(activeJwtToken.get().getTokenExpiresAt());
			authRes.setType(activeJwtToken.get().getTokenType());
			authRes.setToken(activeJwtToken.get().getTokenDetails());
			authRes.setTokenStatus(Constants.statusTokenNotExpired);
		} else {
			deleteTokenDetails(authRes.getCustomerSeqNumber());
			createTokenDetails(authReq, authRes);
			authRes.setCustomerSeqNumber(authRes.getCustomerSeqNumber());
			authRes.setTokenStatus(TokensEntityRepo.createToken);
		}
	}

	private void longLivedToken(TokensEntity tokensEntity, AuthenticationResponse authRes) {
		authRes.setExpirationTime(tokensEntity.getTokenExpiresAt());
		authRes.setType(tokensEntity.getTokenType());
		authRes.setToken(tokensEntity.getTokenDetails());
		authRes.setTokenStatus(Constants.statusTokenLongLived);
	}

	public void updateTokenDetails(AuthenticationRequest authRequest, AuthenticationResponse authRes) {

		jwtdao.updateTokenDetails(authRequest, authRes);

	}

	public void deleteTokenDetails(Long seqNo) {
		jwtdao.deleteTokenDetails(seqNo);
	}

	public void createTokenDetails(AuthenticationRequest authRequest, AuthenticationResponse authRes) {
		jwtdao.createTokenDetails(authRequest, authRes);
	}

}
