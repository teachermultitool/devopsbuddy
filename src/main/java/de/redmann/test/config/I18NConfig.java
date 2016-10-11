package de.redmann.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Created by redmann on 11.10.16.
 */
@Configuration
public class I18NConfig
{
	@Bean
	public ReloadableResourceBundleMessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        reloadableResourceBundleMessageSource.setBasename("classpath:i18n/messages");
        reloadableResourceBundleMessageSource.setCacheSeconds(1800);
        return reloadableResourceBundleMessageSource;
	}
}
