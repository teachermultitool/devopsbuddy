package de.redmann.test.backend.service;

import org.springframework.mail.SimpleMailMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 12.10.16.
 */
@Slf4j
public class MockEmailService extends AbstractEmailService
{
	@Override
	public void sendGenericEmailMessage(SimpleMailMessage simpleMailMessage)
	{
		log.debug("Simulating an email service...");
		log.info(simpleMailMessage.toString());
		log.debug("Email sent.");
	}
}
