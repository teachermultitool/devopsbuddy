package de.redmann.test.utils;

import javax.servlet.http.HttpServletRequest;

import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.web.controllers.ForgotMyPasswordController;

/**
 * Created by redmann on 17.10.16.
 */
public class UserUtils
{
	private UserUtils()
	{
		throw new AssertionError("Non instantiable");
	};
	
	
	
	public static User createBasisUser(String username, String email)
	{
		final User user = new User();
		user.setUsername(username);
		user.setPassword("secret");
		user.setEmail(email);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		user.setPhoneNumber("123456789123");
		user.setCountry("DE");
		user.setEnabled(true);
		user.setDescription("A basic user");
		user.setProfileImageUrl("https://blabla.images.com/basicuser");
		return user;
	}
	
	
	
	public static String createPasswordResetUrl(HttpServletRequest httpServletRequest, long userId, String token)
	{
		//@formatter:off
		String passwordResetUrl =
                httpServletRequest.getScheme() +
                        "://" +
                        httpServletRequest.getServerName() +
                        ":"	+
                        httpServletRequest.getServerPort() +
                        httpServletRequest.getContextPath()	+
                        ForgotMyPasswordController.CHANGE_PASSWORD_PATH +
                        "?id=" +
                        userId +
                        "&token=" +
                        token;
        //@formatter:on
		return passwordResetUrl;
	}
}
