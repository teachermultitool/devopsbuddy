package de.redmann.test.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import de.redmann.test.backend.service.EmailService;
import de.redmann.test.backend.service.SmtpEmailService;

/**
 * Created by redmann on 12.10.16.
 */
@Configuration
@Profile ("prod")
@PropertySource ("file:///${user.home}/.devopsbuddy/application-prod.properties")
public class ProductionConfig
{
	@Value ("${stripe.prod.private.key}")
	private String stripeDevKey;
	
	
	
	@Bean
	public EmailService emailService()
	{
		return new SmtpEmailService();
	}
	
	
	
	@Bean
	public String stripeKey()
	{
		return stripeDevKey;
	}
}
