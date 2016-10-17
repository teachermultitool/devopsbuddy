package de.redmann.test.integration;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.redmann.test.Main;
import de.redmann.test.backend.persistence.domain.backend.Plan;
import de.redmann.test.backend.persistence.domain.backend.Role;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.domain.backend.UserRole;
import de.redmann.test.backend.persistence.repositories.PlanRepository;
import de.redmann.test.backend.persistence.repositories.RoleRepository;
import de.redmann.test.backend.persistence.repositories.UserRepository;

/**
 * Created by redmann on 14.10.16.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@SpringBootTest (classes = Main.class)
public class RepositoryIntegrationTest
{
	
	@Autowired
	private PlanRepository		planRepository;
	
	@Autowired
	private RoleRepository		roleRepository;
	
	@Autowired
	private UserRepository		userRepository;
	
	private static final Long	BASIC_PLAN_ID	= 1L;
	private static final Long	BASIC_ROLE_ID	= 1L;
	
	
	
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
		Plan basicPlan = createBasicPlan();
		planRepository.save(basicPlan);
		Plan retrievedPlan = planRepository.findOne(BASIC_PLAN_ID);
		Assert.assertNotNull(retrievedPlan);
	}
	
	
	
	@Test
	public void testCreateNewRole() throws Exception
	{
		Role basicRole = createBasicRole();
		roleRepository.save(basicRole);
		Role retrievedRole = roleRepository.findOne(BASIC_ROLE_ID);
		Assert.assertNotNull(retrievedRole);
	}
	
	
	
	@Test
	public void testCreateNewUser() throws Exception
	{
		Plan basicPlan = createBasicPlan();
		planRepository.save(basicPlan);
		
		User basicUser = createBasicUser();
		basicUser.setPlan(basicPlan);
		
		Role basicRole = createBasicRole();
		Set<UserRole> userRoleSet = new HashSet<>();
		UserRole userRole = new UserRole();
		userRole.setUser(basicUser);
		userRole.setRole(basicRole);
		userRoleSet.add(userRole);
		
		basicUser.getUserRoles().addAll(userRoleSet);
		
		for (UserRole ur : userRoleSet)
		{
			roleRepository.save(ur.getRole());
		}
		
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
	
	
	
	private Plan createBasicPlan()
	{
		final Plan plan = new Plan();
		plan.setId(BASIC_PLAN_ID);
		plan.setName("Basic");
		return plan;
	}
	
	
	
	private Role createBasicRole()
	{
		final Role role = new Role();
		role.setId(BASIC_ROLE_ID);
		role.setName("Role");
		return role;
	}
	
	
	
	private User createBasicUser()
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
