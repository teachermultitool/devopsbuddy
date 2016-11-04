package de.redmann.test.unit;

import java.util.UUID;

import de.redmann.test.backend.persistence.domain.backend.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import de.redmann.test.utils.UserUtils;
import de.redmann.test.web.controllers.ForgotMyPasswordController;
import de.redmann.test.web.domain.frontend.BasicAccountPayload;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

/**
 * Created by redmann on 20.10.16.
 */
public class UserUtilsUnitTest
{
	private MockHttpServletRequest	mockHttpServletRequest;
	
	private PodamFactory			podamFactory;
	
	
	
	@Before
	public void init()
	{
		mockHttpServletRequest = new MockHttpServletRequest();
		podamFactory = new PodamFactoryImpl();
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
	
	
	
	@Test
	public void mapWebUserToDomainUser()
	{
		BasicAccountPayload webUser = podamFactory.manufacturePojoWithFullData(BasicAccountPayload.class);
		
		webUser.setEmail("me@example.com");

        User user = UserUtils.fromWebUserToDomainUser(webUser);

        Assert.assertEquals(webUser.getUsername(), user.getUsername());
        Assert.assertEquals(webUser.getPassword(), user.getPassword());
        Assert.assertEquals(webUser.getFirstName(), user.getFirstName());
        Assert.assertEquals(webUser.getLastName(), user.getLastName());
        Assert.assertEquals(webUser.getEmail(), user.getEmail());
        Assert.assertEquals(webUser.getPhoneNumber(), user.getPhoneNumber());
        Assert.assertEquals(webUser.getCountry(), user.getCountry());
        Assert.assertEquals(webUser.getDescription(), user.getDescription());
	}
}
