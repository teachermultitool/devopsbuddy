package de.redmann.test.unit;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import de.redmann.test.utils.UserUtils;
import de.redmann.test.web.controllers.ForgotMyPasswordController;

/**
 * Created by redmann on 20.10.16.
 */
public class UserUtilsUnitTest
{
	private MockHttpServletRequest mockHttpServletRequest;
	
	
	
	@Before
	public void init()
	{
		mockHttpServletRequest = new MockHttpServletRequest();
	}
	
	
	
	@Test
	public void testPasswordResetEmailUrlConstruction() throws Exception
	{
		mockHttpServletRequest.setServerPort(8080);
		
		String token = UUID.randomUUID().toString();
		long userId = 123456;
		
		String expectedUrl = "http://localhost:8080" + ForgotMyPasswordController.CHANGE_PASSWORD_PATH + "?id=" + userId
				+ "&token=" + token;
		
		String actualUrl = UserUtils.createPasswordResetUrl(mockHttpServletRequest, userId, token);
		
		Assert.assertEquals(expectedUrl, actualUrl);
	}
}
