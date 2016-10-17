package de.redmann.test.backend.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.redmann.test.backend.persistence.domain.backend.Plan;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.domain.backend.UserRole;
import de.redmann.test.backend.persistence.repositories.PlanRepository;
import de.redmann.test.backend.persistence.repositories.RoleRepository;
import de.redmann.test.backend.persistence.repositories.UserRepository;
import de.redmann.test.enums.PlansEnum;

/**
 * Created by redmann on 17.10.16.
 */
@Service
@Transactional (readOnly = true)
public class UserService
{
	@Autowired
	private RoleRepository	roleRepository;
	@Autowired
	private PlanRepository	planRepository;
	@Autowired
	private UserRepository	userRepository;
	
	
	
	@Transactional
	public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoleSet)
	{
		Plan plan = planRepository.findOne(plansEnum.getId());
		if (plan == null)
		{
			plan = planRepository.save(new Plan(plansEnum));
		}
		
		user.setPlan(plan);
		
		for (UserRole userRole : userRoleSet)
		{
			roleRepository.save(userRole.getRole());
		}
		
		user.getUserRoles().addAll(userRoleSet);
		user = userRepository.save(user);
		
		return user;
	}
}
