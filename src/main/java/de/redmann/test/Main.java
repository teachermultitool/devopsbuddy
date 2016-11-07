package de.redmann.test;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.redmann.test.backend.persistence.domain.backend.Role;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.domain.backend.UserRole;
import de.redmann.test.backend.service.PlanService;
import de.redmann.test.backend.service.UserService;
import de.redmann.test.enums.PlansEnum;
import de.redmann.test.enums.RolesEnum;
import de.redmann.test.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class Main implements CommandLineRunner
{
	private final UserService	userService;
	
	private final PlanService	planService;
	
	@Value ("${webmaster.username}")
	private String				webmasterUsername;
	@Value ("${webmaster.password}")
	private String				webmasterPassword;
	@Value ("${webmaster.email}")
	private String				webmasterEmail;
	
	
	
	@Autowired
	public Main(PlanService planService, UserService userService)
	{
		this.planService = planService;
		this.userService = userService;
	}
	
	
	
	public static void main(String[] args)
	{
		SpringApplication.run(Main.class, args);
	}
	
	
	
	@Override
	public void run(String... args) throws Exception
	{
		log.info("Creating Basix and Pro plans in the database...");
		planService.createPlan(PlansEnum.BASIC.getId());
		planService.createPlan(PlansEnum.PRO.getId());
		
		Set<UserRole> userRoleSet = new HashSet<>();
		User basicUser = UserUtils.createBasisUser(webmasterUsername, webmasterEmail);
		basicUser.setPassword(webmasterPassword);
		userRoleSet.add(new UserRole(basicUser, new Role(RolesEnum.ADMIN)));
		
		log.debug("Creating user with username: " + basicUser.getUsername());
		userService.createUser(basicUser, PlansEnum.PRO, userRoleSet);
		
		log.debug("User {} created", basicUser.getUsername());
	}
}
