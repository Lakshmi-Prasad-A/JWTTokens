package com.insignia.userdetailsservice;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.insignia.daoInterface.JwtDao;
import com.insignia.entity.CustomerBasicDetailsEntity;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private JwtDao jwtDao;

	@Value("${errorCodes.403}")
	private String UNAUTHORIZED;

	private CustomerBasicDetailsEntity userDetails = null;

	@Override
	public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

		try {
			userDetails = jwtDao.findByUserName(s);

			if (userDetails != null && userDetails.getUserName() != null && userDetails.getCustomerPassword() != null) {

				return new User(userDetails.getUserName(), userDetails.getCustomerPassword(),
						new ArrayList<GrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority(
								userDetails.getRolesAndPermissions().get(0).getRoleName()))));

			}

		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException | UsernameNotFoundException | ParseException e) {

			throw new UsernameNotFoundException(UNAUTHORIZED, e);
		}
		return null;
	}

	public CustomerBasicDetailsEntity getCustomerBasicDetailsEntity() {
		return userDetails;
	}

}
