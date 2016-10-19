package de.redmann.test.backend.persistence.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.redmann.test.backend.persistence.domain.backend.PasswordResetToken;

/**
 * Created by redmann on 19.10.16.
 */
@Repository
public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long>
{
	PasswordResetToken findByToken(String token);
	
	
	
	@Query ("select ptr from PasswordResetToken ptr inner join ptr.user u where ptr.user.id = ?1")
	Set<PasswordResetToken> findAllByUserId(long userId);
}
