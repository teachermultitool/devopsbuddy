package de.redmann.test.integration;

import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.redmann.test.Main;
import de.redmann.test.backend.persistence.domain.backend.Plan;
import de.redmann.test.backend.persistence.domain.backend.Role;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.domain.backend.UserRole;
import de.redmann.test.enums.PlansEnum;
import de.redmann.test.enums.RolesEnum;

/**
 * Created by redmann on 14.10.16.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@SpringBootTest (classes = Main.class)
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest
{
	
	@Rule
	public TestName testName = new TestName();
	
	
	
	@Before
	public void init()
	{
		Assert.assertNotNull(planRepository);
		Assert.assertNotNull(roleRepository);
		Assert.assertNotNull(userRepository);
	}
	
	
	
	@Test
	public void testCreateNewPlan() throws Exception
	{
		Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
		planRepository.save(basicPlan);
		Plan retrievedPlan = planRepository.findOne(PlansEnum.BASIC.getId());
		Assert.assertNotNull(retrievedPlan);
	}
	
	
	
	@Test
	public void testCreateNewRole() throws Exception
	{
		Role basicRole = createBasicRole(RolesEnum.BASIC);
		roleRepository.save(basicRole);
		Role retrievedRole = roleRepository.findOne(RolesEnum.BASIC.getId());
		Assert.assertNotNull(retrievedRole);
	}
	
	
	
	@Test
	public void testCreateNewUser() throws Exception
	{
		User basicUser = createUser(testName);
		
		basicUser = userRepository.save(basicUser);
		User newlyCreatedUser = userRepository.findOne(basicUser.getId());
		Assert.assertNotNull(newlyCreatedUser);
		Assert.assertTrue(newlyCreatedUser.getId() != 0);
		Assert.assertNotNull(newlyCreatedUser.getPlan());
		Assert.assertNotNull(newlyCreatedUser.getPlan().getId());
		Set<UserRole> userRoles = newlyCreatedUser.getUserRoles();
		for (UserRole role : userRoles)
		{
			Assert.assertNotNull(role.getRole());
			Assert.assertNotNull(role.getRole().getId());
		}
		
	}
	
	
	
	@Test
	public void testDeleteUser()
	{
		User basicUser = createUser(testName);
		userRepository.delete(basicUser.getId());
	}
	
	
	
	@Test
	public void testGetUserByEmail()
	{
		User basicUser = createUser(testName);
		User byEmail = userRepository.findByEmail(basicUser.getEmail());
		
		Assert.assertNotNull(byEmail);
		Assert.assertNotNull(byEmail.getId());
	}
	
	
	
	@Test
	public void testUpdateUserPassword()
	{
		User user = createUser(testName);
		String newPassword = UUID.randomUUID().toString();
		
		userRepository.updateUserPassword(user.getId(), newPassword);
		
		user = userRepository.findOne(user.getId());
		
		Assert.assertEquals(newPassword, user.getPassword());
	}
}
