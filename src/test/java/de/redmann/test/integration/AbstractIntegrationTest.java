package de.redmann.test.integration;

import java.util.HashSet;
import java.util.Set;

import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import de.redmann.test.backend.persistence.domain.backend.Plan;
import de.redmann.test.backend.persistence.domain.backend.Role;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.domain.backend.UserRole;
import de.redmann.test.backend.persistence.repositories.PlanRepository;
import de.redmann.test.backend.persistence.repositories.RoleRepository;
import de.redmann.test.backend.persistence.repositories.UserRepository;
import de.redmann.test.enums.PlansEnum;
import de.redmann.test.enums.RolesEnum;
import de.redmann.test.utils.UserUtils;

/**
 * Created by redmann on 19.10.16.
 */
public class AbstractIntegrationTest
{
	
	@Autowired
	protected PlanRepository	planRepository;
	
	@Autowired
	protected RoleRepository	roleRepository;
	
	@Autowired
	protected UserRepository	userRepository;
	
	
	
	protected User createUser(String username, String email)
	{
		Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
		planRepository.save(basicPlan);
		
		User basicUser = UserUtils.createBasisUser(username, email);
		basicUser.setPlan(basicPlan);
		
		Role basicRole = createBasicRole(RolesEnum.BASIC);
		roleRepository.save(basicRole);
		
		Set<UserRole> userRoleSet = new HashSet<>();
		UserRole userRole = new UserRole(basicUser, basicRole);
		userRoleSet.add(userRole);
		
		basicUser.getUserRoles().addAll(userRoleSet);
		return userRepository.save(basicUser);
	}
	
	
	
	protected User createUser(TestName testName)
	{
		return createUser(testName.getMethodName(), testName.getMethodName() + "@devopsbuddy.com");
	}
	
	
	
	protected Plan createBasicPlan(PlansEnum plansEnum)
	{
		return new Plan(plansEnum);
	}
	
	
	
	protected Role createBasicRole(RolesEnum rolesEnum)
	{
		return new Role(rolesEnum);
	}
}
