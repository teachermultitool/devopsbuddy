package de.redmann.test.web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import de.redmann.test.backend.persistence.domain.backend.Plan;
import de.redmann.test.backend.persistence.domain.backend.Role;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.domain.backend.UserRole;
import de.redmann.test.backend.service.PlanService;
import de.redmann.test.backend.service.S3Service;
import de.redmann.test.backend.service.UserService;
import de.redmann.test.enums.PlansEnum;
import de.redmann.test.enums.RolesEnum;
import de.redmann.test.utils.UserUtils;
import de.redmann.test.web.domain.frontend.BasicAccountPayload;
import de.redmann.test.web.domain.frontend.ProAccountPayload;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 07.11.16.
 */
@Controller
@Slf4j
public class SignupController
{
	public static final String	SIGNUP_URL_MAPPING		= "/signup";
	
	public static final String	PAYLOAD_MODEL_KEY_NAME	= "payload";
	
	public static final String	SUBSCRIPTION_VIEW_NAME	= "registration/signup";
	
	public static final String	DUPLICATE_USERNAME_KEY	= "duplicateUsername";
	
	public static final String	DUPLICATE_EMAIL_KEY		= "duplicateEmail";
	
	public static final String	SIGNED_UP_MESSAGE_KEY	= "signedUp";
	
	public static final String	ERROR_MESSAGE_KEY		= "message";
	
	private final PlanService	planService;
	private final UserService	userService;
	private final S3Service		s3Service;
	
	
	
	@Autowired
	public SignupController(PlanService planService, UserService userService, S3Service s3Service)
	{
		this.planService = planService;
		this.userService = userService;
		this.s3Service = s3Service;
	}
	
	
	
	@RequestMapping (value = SIGNUP_URL_MAPPING, method = RequestMethod.GET)
	public String signupGet(@RequestParam ("planId") int planId, ModelMap model)
	{
		if (planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId())
		{
			throw new IllegalStateException("Plan id is not valid");
		}
		model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());
		return SUBSCRIPTION_VIEW_NAME;
	}
	
	
	
	@RequestMapping (value = SIGNUP_URL_MAPPING, method = RequestMethod.POST)
	public String signupPost(@RequestParam (value = "planId", required = true) int planId,
			@RequestParam (name = "file", required = false) MultipartFile file,
			@ModelAttribute (PAYLOAD_MODEL_KEY_NAME) @Valid ProAccountPayload payload, ModelMap model) throws IOException
	{
		if (planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId())
		{
			model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
			model.addAttribute(ERROR_MESSAGE_KEY, "Plan id does not exist");
			return SUBSCRIPTION_VIEW_NAME;
		}
		
		this.checkForDuplicates(payload, model);
		
		boolean duplicates = false;
		final ArrayList<String> errorMessages = new ArrayList<>();
		
		if (model.containsKey(DUPLICATE_USERNAME_KEY))
		{
			log.warn("The username already esists. Displaying error to the user");
			model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
			errorMessages.add("Username already exist");
			duplicates = true;
		}
		
		if (model.containsKey(DUPLICATE_EMAIL_KEY))
		{
			log.warn("The email already esists. Displaying error to the user");
			model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
			errorMessages.add("Email already exist");
			duplicates = true;
		}
		
		if (duplicates)
		{
			model.addAttribute(ERROR_MESSAGE_KEY, errorMessages);
			return SUBSCRIPTION_VIEW_NAME;
		}
		
		log.debug("Transforming user payload into User domain object");
		User user = UserUtils.fromWebUserToDomainUser(payload);
		
		if (file != null && !file.isEmpty())
		{
			String profileImageUrl = s3Service.storeProfileImage(file, payload.getUsername());
			if (profileImageUrl != null)
			{
				user.setProfileImageUrl(profileImageUrl);
			}
			else
			{
				log.warn(
						"There was a problem uploading the profile image to s3. The user's profile wiil be created without the image");
			}
		}
		
		log.debug("Retrieving plan from the database");
		Plan selectedPlan = planService.findPlanById(planId);
		
		if (selectedPlan == null)
		{
			log.error("The plan {} could not be found, Throwing exception.", planId);
			model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
			model.addAttribute(ERROR_MESSAGE_KEY, "Plan id not found");
			return SUBSCRIPTION_VIEW_NAME;
		}
		
		user.setPlan(selectedPlan);
		
		User registeredUser = null;
		
		HashSet<UserRole> roles = new HashSet<>();
		
		if (planId == PlansEnum.BASIC.getId())
		{
			roles.add(new UserRole(user, new Role(RolesEnum.BASIC)));
			registeredUser = userService.createUser(user, PlansEnum.BASIC, roles);
		}
		else
		{
			
			roles.add(new UserRole(user, new Role(RolesEnum.PRO)));
			registeredUser = userService.createUser(user, PlansEnum.PRO, roles);
			log.debug(payload.toString());
		}
		
		Authentication auth = new UsernamePasswordAuthenticationToken(registeredUser, null, registeredUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		log.info("User created successfully");
		
		model.addAttribute(SIGNED_UP_MESSAGE_KEY, "true");
		return SUBSCRIPTION_VIEW_NAME;
	}
	
	
	
	private void checkForDuplicates(BasicAccountPayload payload, ModelMap model)
	{
		if (userService.findByUserName(payload.getUsername()) != null)
		{
			model.addAttribute(DUPLICATE_USERNAME_KEY, true);
		}
		if (userService.findByUserEmail(payload.getEmail()) != null)
		{
			model.addAttribute(DUPLICATE_EMAIL_KEY, true);
		}
	}
}
