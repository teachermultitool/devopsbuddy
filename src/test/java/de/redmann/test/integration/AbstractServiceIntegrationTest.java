package de.redmann.test.integration;

import java.util.HashSet;
import java.util.Set;

import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import de.redmann.test.backend.persistence.domain.backend.Role;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.domain.backend.UserRole;
import de.redmann.test.backend.service.UserService;
import de.redmann.test.enums.PlansEnum;
import de.redmann.test.enums.RolesEnum;
import de.redmann.test.utils.UsersUtils;

/**
 * Created by redmann on 19.10.16.
 */
public class AbstractServiceIntegrationTest
{
	
	@Autowired
	private UserService userService;
	
	
	
	protected User createUser(TestName testName)
	{
		String username = testName.getMethodName();
		String email = testName.getMethodName() + "@devopsbuddy.com";
		Set<UserRole> userRoleSet = new HashSet<>();
		User basicUser = UsersUtils.createBasisUser(username, email);
		
		userRoleSet.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));
		
		return userService.createUser(basicUser, PlansEnum.BASIC, userRoleSet);
	}
}
