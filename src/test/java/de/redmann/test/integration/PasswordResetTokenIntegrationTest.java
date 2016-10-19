package de.redmann.test.integration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.redmann.test.Main;
import de.redmann.test.backend.persistence.domain.backend.PasswordResetToken;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.repositories.PasswordResetTokenRepository;

/**
 * Created by redmann on 19.10.16.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@SpringBootTest (classes = Main.class)
public class PasswordResetTokenIntegrationTest extends AbstractIntegrationTest
{
	
	@Value ("${token.expiration.length.minutes}")
	private int								experationTimeInMinutes;
	
	@Autowired
	protected PasswordResetTokenRepository	passwordResetTokenRepository;
	
	@Rule
	public TestName							testName	= new TestName();
	
	
	
	@Before
	public void init()
	{
		Assert.assertFalse(experationTimeInMinutes <= 0);
	}
	
	
	
	@Test
	public void testTokenExpirationLength()
	{
		User user = createUser(testName);
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		String token = UUID.randomUUID().toString();
		
		LocalDateTime expectedTime = now.plusMinutes(experationTimeInMinutes);
		
		PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
		
		LocalDateTime expiryDate = passwordResetToken.getExpiryDate();
		Assert.assertNotNull(expiryDate);
		Assert.assertEquals(expectedTime, expiryDate);
	}
	
	
	
	@Test
	public void testFindTokenByTokenValue()
	{
		User user = createUser(testName);
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		String token = UUID.randomUUID().toString();
		
		createPasswordResetToken(token, user, now);
		
		PasswordResetToken byToken = passwordResetTokenRepository.findByToken(token);
		
		Assert.assertNotNull(byToken);
		Assert.assertNotNull(byToken.getId());
		Assert.assertNotNull(byToken.getUser());
	}
	
	
	
	@Test
	public void testDeleteToken()
	{
		User user = createUser(testName);
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		String token = UUID.randomUUID().toString();
		
		PasswordResetToken byToken = createPasswordResetToken(token, user, now);
		
		long byTokenId = byToken.getId();
		passwordResetTokenRepository.delete(byTokenId);
		
		PasswordResetToken shouldNotExists = passwordResetTokenRepository.findByToken(token);
		
		Assert.assertNull(shouldNotExists);
	}
	
	
	
	@Test
	public void testCascadeDeleteFromUserEntity()
	{
		User user = createUser(testName);
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		String token = UUID.randomUUID().toString();
		
		PasswordResetToken byToken = createPasswordResetToken(token, user, now);
		byToken.getId();
		
		userRepository.delete(user.getId());
		Set<PasswordResetToken> shouldBeEmpty = passwordResetTokenRepository.findAllByUserId(user.getId());
		
		Assert.assertTrue(shouldBeEmpty.isEmpty());
	}
	
	
	
	@Test
	public void testMultipleTokensAreReturnedWhenQueringByUserId()
	{
		User user = createUser(testName);
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		
		String token1 = UUID.randomUUID().toString();
		String token2 = UUID.randomUUID().toString();
		String token3 = UUID.randomUUID().toString();
		
		Set<PasswordResetToken> tokens = new HashSet<>();
		tokens.add(createPasswordResetToken(token1, user, now));
		tokens.add(createPasswordResetToken(token2, user, now));
		tokens.add(createPasswordResetToken(token3, user, now));
		
		passwordResetTokenRepository.save(tokens);
		
		User foundUser = userRepository.findOne(user.getId());
		
		Set<PasswordResetToken> actualTokens = passwordResetTokenRepository.findAllByUserId(foundUser.getId());
		
		Assert.assertTrue(actualTokens.size() == tokens.size());
		
		List<String> tokensAsList = tokens.stream().map(PasswordResetToken::getToken).collect(Collectors.toList());
		List<String> actualTokensAsList = actualTokens.stream().map(PasswordResetToken::getToken).collect(Collectors.toList());
		
		for (String token : tokensAsList)
		{
			Assert.assertTrue(actualTokensAsList.contains(token));
		}
	}
	
	
	
	private PasswordResetToken createPasswordResetToken(String token, User user, LocalDateTime now)
	{
		PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, now, experationTimeInMinutes);
		passwordResetTokenRepository.save(passwordResetToken);
		Assert.assertNotNull(passwordResetToken.getId());
		return passwordResetToken;
	}
}
