package com.insignia.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.insignia.model.TokensEntity;

public interface TokensEntityRepo extends JpaRepository<TokensEntity, Serializable> {

	public final static String fetQueryForTOken = "SELECT is_long_lived_token,is_token_expired,token_created_at,token_details,token_expires_at,token_revoked_at,token_type from tokens_table where sequence_number=:sequence_number and token_type='JWT'";

	public final static String updateToken = "UPDATE tokens_table SET is_long_lived_token=:is_long_lived_token,token_expires_at=:token_expires_at, is_token_expired=:is_token_expired,token_created_at=:token_created_at,token_details=:token_details,token_revoked_at=:token_revoked_at WHERE sequence_number=:customer_sequence_number and token_type='JWT'";

	public final static String deleteToken = "delete from tokens_table where sequence_number=:customer_sequence_number and token_type='JWT'";

	public final static String createToken = "Insert into tokens_table(sequence_number,token_type,token_details,token_expires_at,token_created_at,token_revoked_at,is_token_expired,is_long_lived_token) values(:customer_sequence_number,:token_type,:token_details,:token_expires_at,:token_created_at,:token_revoked_at,:is_token_expired,:is_long_lived_token)";

	@Modifying
	@Query(value = deleteToken, nativeQuery = true)
	public void deleteToken(@Param("customer_sequence_number") Long customerSequenceNumber);

	@Modifying
	@Query(value = createToken, nativeQuery = true)
	public void createToken(@Param("customer_sequence_number") Long customerSequenceNumber,
			@Param("token_type") String tokenType, @Param("token_details") String tokenDetails,
			@Param("token_expires_at") Date tokenExpiresAt, @Param("token_created_at") Date tokenCreatedAt,
			@Param("token_revoked_at") Date tokenRevokedAt, @Param("is_token_expired") Boolean is_token_expired,
			@Param("is_long_lived_token") Boolean isLongLivedToken);

	@Query(value = fetQueryForTOken, nativeQuery = true)
	public List<Object[]> fetQueryForTOken(@Param("sequence_number") Long sequenceNumber);

	@Modifying
	@Query(value = updateToken, nativeQuery = true)
	public void updateToken(@Param("is_long_lived_token") Boolean isLongLivedToken,
			@Param("token_expires_at") Date tokenExpiresAt, @Param("is_token_expired") Boolean isTokenExpired,
			@Param("token_created_at") Date tokenCreatedAt, @Param("token_details") String tokenDetails,
			@Param("token_revoked_at") Date tokenRevokedAt,
			@Param("customer_sequence_number") Long customerSequenceNumber);
}
