package com.insignia.daoImpl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.insignia.daoInterface.JwtDao;
import com.insignia.decryption.PasswordDecoder;
import com.insignia.entity.CustomerBasicDetailsEntity;
import com.insignia.entity.RolesAndPermissions;
import com.insignia.entity.TokensEntity;
import com.insignia.model.AuthenticationRequest;
import com.insignia.model.AuthenticationResponse;
import com.insignia.repository.CustomerBasicDetailsRepo;
import com.insignia.repository.TokensEntityRepo;
import com.insignia.util.Constants;

@Repository
public class JwtDaoImpl implements JwtDao {

	@Autowired
	private TokensEntityRepo tokenRepo;

	@Autowired
	private CustomerBasicDetailsRepo customerRepo;

	public JwtDaoImpl(TokensEntityRepo tokenRepo, CustomerBasicDetailsRepo customerRepo) {
		super();
		this.tokenRepo = tokenRepo;
		this.customerRepo = customerRepo;
	}

	public CustomerBasicDetailsEntity findByUserName(String userName)
			throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, ParseException {
		
		List<Object[]> resultList = customerRepo.fetchUserDetails(userName);

		CustomerBasicDetailsEntity cbde = new CustomerBasicDetailsEntity();
		cbde.setUserName(null);
		cbde.setCustomerPassword(null);
		cbde.setSequenceNumber(null);

		if (!resultList.isEmpty()) {
			Object[] custObj = resultList.get(0);
			RolesAndPermissions rolesAndPermissions = new RolesAndPermissions();
			cbde.setUserName(custObj[0] != null ? String.valueOf(custObj[0]) : null);
			cbde.setCustomerPassword(
					custObj[1] != null ? (custObj[1]!="" ? PasswordDecoder.getDecryptedPassword(String.valueOf(custObj[1])):custObj[1].toString()) : null);

			cbde.setSequenceNumber(custObj[2] != null ? Long.valueOf(custObj[2].toString()) : null);
			rolesAndPermissions.setRoleName(custObj[3] != null ? String.valueOf(custObj[3]) : null);
			rolesAndPermissions.setRoleId(custObj[4] != null ? Integer.valueOf(custObj[4].toString()) : null);
			rolesAndPermissions.setPermission1(custObj[5] != null ? Boolean.valueOf(custObj[5].toString()) : null);
			rolesAndPermissions.setPermission2(custObj[6] != null ? Boolean.valueOf(custObj[6].toString()) : null);
			rolesAndPermissions.setPermission3(custObj[7] != null ? Boolean.valueOf(custObj[7].toString()) : null);
			rolesAndPermissions.setPermission4(custObj[8] != null ? Boolean.valueOf(custObj[8].toString()) : null);
			rolesAndPermissions.setRoleApprovedDate(
					custObj[9] != null ? new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(custObj[9].toString())
							: null);
			rolesAndPermissions.setRoleRevokedDate(
					custObj[10] != null ? new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(custObj[10].toString())
							: null);
			rolesAndPermissions.setPermissionChangeDate(
					custObj[11] != null ? new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(custObj[11].toString())
							: null);
			rolesAndPermissions.setUpdatedPermissions(custObj[12] != null ? String.valueOf(custObj[12]) : null);
			rolesAndPermissions.setSequence_number(custObj[2] != null ? Long.valueOf(custObj[2].toString()) : null);
			cbde.getRolesAndPermissions().add(rolesAndPermissions);
		}

		return cbde;
	}

	public Optional<List<TokensEntity>> fetchTokendetails(Long sequenceNumber) throws Exception {

		TokensEntity tke = new TokensEntity();
		List<TokensEntity> listOfEntity = new ArrayList<>();
		List<Object[]> tokenEntity = tokenRepo.fetQueryForTOken(sequenceNumber);
		if (!tokenEntity.isEmpty()) {
			Object[] tokenObj = tokenEntity.get(0);
			tke.setTokenSequenceNumber(sequenceNumber);
			tke.setIsLongLivedToken(tokenObj[0] != null ? Boolean.valueOf(tokenObj[0].toString()) : false);
			tke.setIsTokenExpired(tokenObj[1] != null ? Boolean.valueOf(tokenObj[1].toString()) : false);
			tke.setTokenCreatedAt(
					tokenObj[2] != null ? new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(tokenObj[2].toString())
							: null);
			tke.setTokenDetails(tokenObj[3] != null ? String.valueOf(tokenObj[3]) : null);
			tke.setTokenExpiresAt(
					tokenObj[4] != null ? new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(tokenObj[4].toString())
							: null);
			tke.setTokenRevokedAt(
					tokenObj[5] != null ? new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(tokenObj[5].toString())
							: null);
			tke.setTokenType(tokenObj[6] != null ? String.valueOf(tokenObj[6]) : null);
			listOfEntity.add(tke);
		}

		return Optional.ofNullable(listOfEntity.isEmpty() ? null : listOfEntity);
	}

	@Transactional
	public void updateTokenDetails(AuthenticationRequest authRequest, AuthenticationResponse authRes) {

		tokenRepo.updateToken(authRequest.isRememberMeSelected(),
				new Date(System.currentTimeMillis() + 1000 * 60 * authRequest.getExpirationTime()), false, new Date(),
				authRes.getToken(), null, authRes.getCustomerSeqNumber());

		authRes.setTokenStatus(Constants.statusUpdate);

	}

	@Transactional
	public void deleteTokenDetails(Long sequenceNumber) {
		tokenRepo.deleteToken(sequenceNumber);
	}

	@Transactional
	public void createTokenDetails(AuthenticationRequest authRequest, AuthenticationResponse authRes) {
		tokenRepo.createToken(authRes.getCustomerSeqNumber(), Constants.tokenType, Constants.tokenAlgoritham,
				new Date(System.currentTimeMillis() + 1000 * 60 * authRequest.getExpirationTime()), new Date(), null,
				false, authRequest.isRememberMeSelected());
		authRes.setCustomerSeqNumber(authRes.getCustomerSeqNumber());
		authRes.setTokenStatus(Constants.statusCreated);

	}

}