package de.redmann.test.backend.service;

import java.util.Locale;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by redmann on 11.10.16.
 */
@Service
@Slf4j
public class I18NService
{
	private final MessageSource messageSource;
	
	
	
	@Autowired
	public I18NService(MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}
	
	
	
	public String getMessage(String messageId)
	{
		Locale locale = LocaleContextHolder.getLocale();
		return getMessage(messageId, locale);
	}
	
	
	
	public String getMessage(String messageId, Locale locale)
	{
		return messageSource.getMessage(messageId, null, locale);
	}
}
