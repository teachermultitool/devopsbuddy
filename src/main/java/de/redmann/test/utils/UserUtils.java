package de.redmann.test.utils;

import javax.servlet.http.HttpServletRequest;

import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.web.controllers.ForgotMyPasswordController;
import de.redmann.test.web.domain.frontend.BasicAccountPayload;

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
	
	
	
	public static <T extends BasicAccountPayload> User fromWebUserToDomainUser(T frontendPayload)
	{
		User user = new User();

		user.setUsername(frontendPayload.getUsername());
		user.setPassword(frontendPayload.getPassword());
		user.setFirstName(frontendPayload.getFirstName());
		user.setLastName(frontendPayload.getLastName());
		user.setEmail(frontendPayload.getEmail());
		user.setPhoneNumber(frontendPayload.getPhoneNumber());
		user.setCountry(frontendPayload.getCountry());
		user.setEnabled(true);
		user.setDescription(frontendPayload.getDescription());

		return user;
	}
}
