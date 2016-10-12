package de.redmann.test.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import de.redmann.test.web.domain.frontend.FeedbackPojo;

/**
 * Created by redmann on 12.10.16.
 */
public abstract class AbstractEmailService implements EmailService
{
	@Value ("${default.to.address}")
	private String defaultToAddress;
	
	
	
	protected SimpleMailMessage prepareSimpleMailMessageFromFeedbackPojo(final FeedbackPojo feedbackPojo)
	{
		final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setTo(defaultToAddress);
		simpleMailMessage.setFrom(feedbackPojo.getEmail());
		simpleMailMessage.setSubject(
				"[DevOps Buddy]: Feedback received from " + feedbackPojo.getFirstName() + " " + feedbackPojo.getLastName() + "!");
		simpleMailMessage.setText(feedbackPojo.getFeedback());
		return simpleMailMessage;
	}
	
	
	
	@Override
	public void sendFeedbackEmail(FeedbackPojo feedbackPojo)
	{
		sendGenericEmailMessage(prepareSimpleMailMessageFromFeedbackPojo(feedbackPojo));
	}
}
