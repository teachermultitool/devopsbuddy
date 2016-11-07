package de.redmann.test.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.redmann.test.enums.PlansEnum;
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
}
