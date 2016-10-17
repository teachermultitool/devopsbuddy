package de.redmann.test.integration;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.redmann.test.Main;
import de.redmann.test.backend.persistence.domain.backend.Role;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.domain.backend.UserRole;
import de.redmann.test.backend.service.UserService;
import de.redmann.test.enums.PlansEnum;
import de.redmann.test.enums.RolesEnum;
import de.redmann.test.utils.UsersUtils;

/**
 * Created by redmann on 17.10.16.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@SpringBootTest (classes = Main.class)
public class UserServiceIntegrationTest
{
	
	@Autowired
	private UserService userService;
	
	
	
	@Test
	public void testCreatUser()
	{
		Set<UserRole> userRoleSet = new HashSet<>();
		User basicUser = UsersUtils.createBasisUser();
		
		userRoleSet.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));
		
		User user = userService.createUser(basicUser, PlansEnum.BASIC, userRoleSet);
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
	}
}
