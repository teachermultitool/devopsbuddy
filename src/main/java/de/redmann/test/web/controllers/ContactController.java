package de.redmann.test.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.redmann.test.web.domain.frontend.FeedbackPojo;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 12.10.16.
 */
@Controller
@Slf4j
public class ContactController
{
	private static final String	CONTACT_US_VIEW_NAME	= "contact/contact";
	private static final String	FEEDBACK_MODEL_KEY		= "feedback";
	
	
	
	@RequestMapping (value = "/contact", method = RequestMethod.GET)
	public String contactGet(ModelMap model)
	{
		FeedbackPojo feedbackPojo = new FeedbackPojo();
		model.addAttribute(ContactController.FEEDBACK_MODEL_KEY, feedbackPojo);
		return ContactController.CONTACT_US_VIEW_NAME;
	}
	
	
	
	@RequestMapping (value = "/contact", method = RequestMethod.POST)
	public String contactPost(@ModelAttribute (FEEDBACK_MODEL_KEY) FeedbackPojo feedbackPojo)
	{
		log.debug("Feedback POJO content: " + feedbackPojo.toString());
		return ContactController.CONTACT_US_VIEW_NAME;
	}
}
