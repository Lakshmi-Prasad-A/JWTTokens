package com.insignia.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.insignia.daoImpl.JwtDaoImpl;
import com.insignia.model.AuthenticationRequest;
import com.insignia.model.AuthenticationResponse;
import com.insignia.model.CustomerBasicDetailsEntity;
import com.insignia.model.RolesAndPermissions;
import com.insignia.model.TokensEntity;
import com.insignia.repository.CustomerBasicDetailsRepo;
import com.insignia.repository.TokensEntityRepo;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
@ActiveProfiles("test")
public class JwtDaoimlTest {

	@Autowired
	private TokensEntityRepo tokenRepo;

	@Autowired
	private CustomerBasicDetailsRepo customerRepo;

	@Mock
	private TokensEntityRepo mockTokenRepo;

	@InjectMocks
	private JwtDaoImpl daoImpl;
	
	Long sequenceNumber = null;
	AuthenticationResponse authRes = new AuthenticationResponse();
	AuthenticationRequest authReq = new AuthenticationRequest();
	
	@BeforeEach
	public void dataInitializationInH2DataBase() {
		
		authReq.setApplicationId("112");
		authReq.setPassword("/OAWLNI28El94OvpIzoZnQ==");
		authReq.setRememberMeSelected(false);
		authReq.setTenantId("254");
		authReq.setUserId("125");
		authReq.setExpirationTime(30);

		authRes.setCustomerSeqNumber(12l);
		authRes.setToken("25452");
		authRes.setType("JWT");

		TokensEntity tokenEntity = new TokensEntity("JWT", "haha", new Date(),new Date(), new Date(), false, false, new CustomerBasicDetailsEntity());

		RolesAndPermissions rolesAndPermissions = new RolesAndPermissions();
		rolesAndPermissions.setPermission1(false);
		rolesAndPermissions.setRoleName("admin");
		CustomerBasicDetailsEntity customerBasicDetailsEntity = new CustomerBasicDetailsEntity("125", "5252", "184",
				"/OAWLNI28El94OvpIzoZnQ==", "insignia@gmail.com", "Nagu", tokenEntity,
				new ArrayList<>(Arrays.asList(rolesAndPermissions)));

		CustomerBasicDetailsEntity basicDetails = customerRepo.save(customerBasicDetailsEntity);
		tokenEntity.setCustomerBasicDetailsEntity(basicDetails);
		tokenRepo.save(tokenEntity);
	}

	@AfterEach
	public void destoryData() {
		customerRepo.deleteAll();
	}
	
	@Test
	public void testFindByUserName() throws Exception {

		CustomerBasicDetailsEntity expectedCustomeObj = new CustomerBasicDetailsEntity();
		expectedCustomeObj.setUserName("1841255252");
		//expectedCustomeObj.setRoleName("admin");
		expectedCustomeObj.setCustomerPassword("tech");

		CustomerBasicDetailsEntity actualCustomeObj = new JwtDaoImpl(tokenRepo, customerRepo)
				.findByUserName("1841255252"); 

		assertThat(actualCustomeObj).usingRecursiveComparison().ignoringFields("sequenceNumber").ignoringFields("rolesAndPermissions")
				.isEqualTo(expectedCustomeObj);
	}

	@Test
	public void testFetchTokendetails() throws Exception {

		TokensEntityRepo tokenMockObj = mock(TokensEntityRepo.class);
		customerRepo.findAll().stream().limit(1).forEach(d -> {
			sequenceNumber = d.getSequenceNumber();
		});
		Object[] tokenData = { false, false, "2019-10-25 10:10:10", "25452", "2019-10-25 10:10:10",
				"2019-10-25 10:10:10", "JWT" };

		List<Object[]> listOftokensData = new ArrayList<>();
		listOftokensData.add(tokenData);
		when(tokenMockObj.fetQueryForTOken(sequenceNumber)).thenReturn(listOftokensData);

		Optional<List<TokensEntity>> actualTokenObj = new JwtDaoImpl(tokenMockObj, customerRepo)
				.fetchTokendetails(sequenceNumber);

		assertNotNull(actualTokenObj);
	}

	@Test
	public void testUpdateTokenDetails() {

		TokensEntity fetchTokenObj = customerRepo.findAll().stream().limit(1)
				.map(CustomerBasicDetailsEntity::getTokenEntity).findFirst().get();

		authReq.setRememberMeSelected(true);
		authRes.setCustomerSeqNumber(fetchTokenObj.getCustomerBasicDetailsEntity().getSequenceNumber());
		new JwtDaoImpl(tokenRepo, customerRepo).updateTokenDetails(authReq, authRes);

		assertEquals("Update", authRes.getTokenStatus());

	}

	@Test
	public void testDeleteTokenDetails() {
		TokensEntity fetchTokenObj = tokenRepo.findAll().stream().limit(1).findFirst().get();

		Long sequnceNum = fetchTokenObj.getCustomerBasicDetailsEntity().getSequenceNumber();
		new JwtDaoImpl(tokenRepo, customerRepo).deleteTokenDetails(sequnceNum);

		Optional<TokensEntity> deletedTokenObj = tokenRepo.findAll().stream().filter(t->t.getTokenSequenceNumber()==fetchTokenObj.getTokenSequenceNumber()).limit(1).findFirst();

		assertThat(deletedTokenObj).isEmpty();
	}
	
	@Test
	public void testCreateTokenDetails() {
		TokensEntity fetchTokenObj = tokenRepo.findAll().stream().limit(1).findFirst().get();
		authReq.setRememberMeSelected(true);
		authRes.setCustomerSeqNumber(fetchTokenObj.getCustomerBasicDetailsEntity().getSequenceNumber());
		new JwtDaoImpl(tokenRepo, customerRepo).createTokenDetails(authReq, authRes);
		int actualSize=2;
		int expectedSize = tokenRepo.findAll().size();
		
		assertEquals(expectedSize, actualSize);
		
	}
	
}
