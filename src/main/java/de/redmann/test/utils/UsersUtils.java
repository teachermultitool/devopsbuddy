package de.redmann.test.utils;

import de.redmann.test.backend.persistence.domain.backend.User;

/**
 * Created by redmann on 17.10.16.
 */
public class UsersUtils
{
	private UsersUtils()
	{
		throw new AssertionError("Non instantiable");
	};
	
	
	
	public static User createBasisUser()
	{
        final User user = new User();
        user.setUsername("basicUser");
        user.setPassword("secret");
        user.setEmail("me@example.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhoneNumber("123456789123");
        user.setCountry("DE");
        user.setEnabled(true);
        user.setDescription("A basic user");
        user.setProfileImageUrl("https://blabla.images.com/basicuser");
        return user;
	}
}
