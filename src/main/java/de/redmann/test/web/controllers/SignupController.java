package de.redmann.test.web.controllers;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.validation.Valid;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import de.redmann.test.backend.persistence.domain.backend.Plan;
import de.redmann.test.backend.persistence.domain.backend.Role;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.domain.backend.UserRole;
import de.redmann.test.backend.service.PlanService;
import de.redmann.test.backend.service.S3Service;
import de.redmann.test.backend.service.StripeService;
import de.redmann.test.backend.service.UserService;
import de.redmann.test.enums.PlansEnum;
import de.redmann.test.enums.RolesEnum;
import de.redmann.test.excepions.S3Exception;
import de.redmann.test.excepions.StripeException;
import de.redmann.test.utils.StripeUtils;
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

    public static final String	GENERIC_ERROR_VIEW_NAME	= "error/genericError";
	
	private final PlanService	planService;
	private final UserService	userService;
	private final S3Service		s3Service;
	private final StripeService	stripeService;
	
	
	
	@Autowired
	public SignupController(PlanService planService, UserService userService, S3Service s3Service, StripeService stripeService)
	{
		this.planService = planService;
		this.userService = userService;
		this.s3Service = s3Service;
		this.stripeService = stripeService;
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
			
			if (StringUtils.isEmpty(payload.getCardCode()) || StringUtils.isEmpty(payload.getCardNumber())
					|| StringUtils.isEmpty(payload.getCardMonth()) || StringUtils.isEmpty(payload.getCardYear()))
			{
				log.error("One or more credit card fields is null or empty. Returning to the user");
				model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
				model.addAttribute(ERROR_MESSAGE_KEY, "One ofr more credit card details is null or empty.");
				return SUBSCRIPTION_VIEW_NAME;
			}
			
			Map<String, Object> stringTokenParams = StripeUtils.extractTokenParamsFromSignupPayload(payload);
			Map<String, Object> customerParams = new HashMap<>();
			
			customerParams.put("description", "DevOps Buddy customer, Username: " + payload.getUsername());
			customerParams.put("email", payload.getEmail());
			customerParams.put("plan", selectedPlan.getId());
			log.info("Subscribing the customer to plan: " + selectedPlan.getName());
			String stripeCustomerId = stripeService.createCustomer(stringTokenParams, customerParams);
			log.info("Username: " + payload.getUsername() + " has been subscribed to Stripe");
			user.setStripeCustomerId(stripeCustomerId);
			
			registeredUser = userService.createUser(user, PlansEnum.PRO, roles);
			log.debug(payload.toString());
		}
		
		Authentication auth = new UsernamePasswordAuthenticationToken(registeredUser, null, registeredUser.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		log.info("User created successfully");
		
		model.addAttribute(SIGNED_UP_MESSAGE_KEY, "true");
		return SUBSCRIPTION_VIEW_NAME;
	}
	
	
	
	@ExceptionHandler ({ StripeException.class, S3Exception.class })
	public ModelAndView signupException(HttpServletRequest request, Exception exception)
	{
		log.error("Request {} raised exception {}", request.getRequestURL(), exception);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("exception", exception);
		mav.addObject("url", request.getRequestURL());
		mav.addObject("timestamp", LocalDate.now(Clock.systemUTC()));
		mav.setViewName(GENERIC_ERROR_VIEW_NAME);
		
		return mav;
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
