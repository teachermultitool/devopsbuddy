package de.redmann.test.backend.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.redmann.test.backend.persistence.domain.backend.PasswordResetToken;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.repositories.PasswordResetTokenRepository;
import de.redmann.test.backend.persistence.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 19.10.16.
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class PasswordResetTokenService
{
	@Autowired
	private UserRepository					userRepository;
	
	@Autowired
	private PasswordResetTokenRepository	passwordResetTokenRepository;
	
	@Value ("${token.expiration.length.minutes}")
	private int								experationTimeInMinutes;
	
	
	
	public PasswordResetToken findByToken(String token)
	{
		return passwordResetTokenRepository.findByToken(token);
	}
	
	
	
	@Transactional
	public PasswordResetToken createPasswordResetTokenForEmail(String email)
	{
		PasswordResetToken passwordResetToken = null;
		User user = userRepository.findByEmail(email);
		if (user != null)
		{
			String token = UUID.randomUUID().toString();
			LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
			passwordResetToken = new PasswordResetToken(token, user, now, experationTimeInMinutes);
			passwordResetToken = passwordResetTokenRepository.save(passwordResetToken);
			log.debug("Successfully created token {} for user {}", token, user.getUsername());
		}
		else
		{
			log.warn("We couldn't find a user for the given email {}", email);
		}
		return passwordResetToken;
	}
}
