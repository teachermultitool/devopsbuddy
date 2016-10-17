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
import de.redmann.test.enums.PlansEnum;
import de.redmann.test.enums.RolesEnum;
import de.redmann.test.utils.UsersUtils;

/**
 * Created by redmann on 14.10.16.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@SpringBootTest (classes = Main.class)
public class RepositoryIntegrationTest
{
	
	@Autowired
	private PlanRepository	planRepository;
	
	@Autowired
	private RoleRepository	roleRepository;
	
	@Autowired
	private UserRepository	userRepository;
	
	
	
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
		User basicUser = createUser();
		
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
		User user = createUser();
		userRepository.delete(user.getId());
	}
	
	
	
	private User createUser()
	{
		Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
		planRepository.save(basicPlan);
		
		User basicUser = UsersUtils.createBasisUser();
		basicUser.setPlan(basicPlan);
		
		Role basicRole = createBasicRole(RolesEnum.BASIC);
		roleRepository.save(basicRole);
		
		Set<UserRole> userRoleSet = new HashSet<>();
		UserRole userRole = new UserRole(basicUser, basicRole);
		userRoleSet.add(userRole);
		
		basicUser.getUserRoles().addAll(userRoleSet);
		return userRepository.save(basicUser);
	}
	
	
	
	private Plan createBasicPlan(PlansEnum plansEnum)
	{
		return new Plan(plansEnum);
	}
	
	
	
	private Role createBasicRole(RolesEnum rolesEnum)
	{
		return new Role(rolesEnum);
	}
	
}
