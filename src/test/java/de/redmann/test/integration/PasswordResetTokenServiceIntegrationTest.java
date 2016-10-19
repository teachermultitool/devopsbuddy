package de.redmann.test.integration;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.redmann.test.Main;
import de.redmann.test.backend.persistence.domain.backend.PasswordResetToken;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.service.PasswordResetTokenService;

import java.util.UUID;

/**
 * Created by redmann on 19.10.16.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@SpringBootTest (classes = Main.class)
public class PasswordResetTokenServiceIntegrationTest extends AbstractServiceIntegrationTest
{
	@Autowired
	private PasswordResetTokenService	passwordResetTokenService;
	
	@Rule
	public TestName						testName	= new TestName();
	
	
	
	@Test
	public void testCreateNewTokenForUserEmail()
	{
		User user = createUser(testName);
		
		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(user.getEmail());
		
		Assert.assertNotNull(passwordResetToken);
		Assert.assertNotNull(passwordResetToken.getToken());
	}
	
	
	
	@Test
	public void testFindByToken()
	{
		User user = createUser(testName);
		
		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(user.getEmail());
		
		Assert.assertNotNull(passwordResetToken);
		Assert.assertNotNull(passwordResetToken.getToken());
		
		PasswordResetToken token = passwordResetTokenService.findByToken(passwordResetToken.getToken());
		Assert.assertNotNull(token);
	}
}
