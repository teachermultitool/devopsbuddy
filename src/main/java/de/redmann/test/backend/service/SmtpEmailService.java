package de.redmann.test.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 12.10.16.
 */
@Slf4j
public class SmtpEmailService extends AbstractEmailService
{
	@Autowired
	private MailSender mailSender;
	
	
	
	@Override
	public void sendGenericEmailMessage(SimpleMailMessage simpleMailMessage)
	{
		log.debug("Sending email for: " + simpleMailMessage.toString());
		mailSender.send(simpleMailMessage);
		log.debug("Email sent.");
	}
}
