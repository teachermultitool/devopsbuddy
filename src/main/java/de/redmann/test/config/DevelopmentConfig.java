package de.redmann.test.config;

import org.apache.catalina.servlets.WebdavServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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
	@Value ("${stripe.test.private.key}")
	private String stripeDevKey;
	
	
	
	@Bean
	public EmailService emailService()
	{
		return new MockEmailService();
	}
	
	
	
	@Bean
	public ServletRegistrationBean h2ConsoleServletRegistration()
	{
		ServletRegistrationBean bean = new ServletRegistrationBean(new WebdavServlet());
		bean.addUrlMappings("/console/**");
		return bean;
	}
	
	
	
	@Bean
	public String stripeKey()
	{
		return stripeDevKey;
	}
}
