package de.redmann.test;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.redmann.test.backend.persistence.domain.backend.Role;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.domain.backend.UserRole;
import de.redmann.test.backend.service.UserService;
import de.redmann.test.enums.PlansEnum;
import de.redmann.test.enums.RolesEnum;
import de.redmann.test.utils.UsersUtils;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class Main implements CommandLineRunner
{
	@Autowired
	private UserService userService;
	
	
	
	public static void main(String[] args)
	{
		SpringApplication.run(Main.class, args);
	}
	
	
	
	@Override
	public void run(String... args) throws Exception
	{
		Set<UserRole> userRoleSet = new HashSet<>();
		User basicUser = UsersUtils.createBasisUser("proUser", "proUser@devopsbuddy.com");
		
		userRoleSet.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));
		
		log.debug("Creating user with username: " + basicUser.getUsername());
		userService.createUser(basicUser, PlansEnum.PRO, userRoleSet);
		
		log.debug("User {} created", basicUser.getUsername());
	}
}
