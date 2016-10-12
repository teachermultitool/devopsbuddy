package de.redmann.test.backend.service;

import org.springframework.mail.SimpleMailMessage;

import de.redmann.test.web.domain.frontend.FeedbackPojo;

/**
 * Created by redmann on 12.10.16.
 */
public interface EmailService
{
	void sendFeedbackEmail(FeedbackPojo feedbackPojo);
	
	
	
	void sendGenericEmailMessage(SimpleMailMessage simpleMailMessage);
	
}
