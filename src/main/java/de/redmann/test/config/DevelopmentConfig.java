package de.redmann.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import de.redmann.test.backend.service.EmailService;
import de.redmann.test.backend.service.MockEmailService;

/**
 * Created by redmann on 12.10.16.
 */
@Configuration
@Profile ("dev")
@PropertySource ("file:///${user.home}/.devopsbuddy/application-dev.properties")
public class DevelopmentConfig
{
	@Bean
	public EmailService emailService()
	{
		return new MockEmailService();
	}
}
