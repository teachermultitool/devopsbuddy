package de.redmann.test.web.controllers;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.redmann.test.backend.persistence.domain.backend.PasswordResetToken;
import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.service.EmailService;
import de.redmann.test.backend.service.I18NService;
import de.redmann.test.backend.service.PasswordResetTokenService;
import de.redmann.test.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 20.10.16.
 */
@Controller
@Slf4j
public class ForgotMyPasswordController
{
	public static final String			EMAIL_ADDRESS_VIEW_NAME				= "forgotmypassword/emailForm";
	public static final String			FORGOT_PASSWORD_URL_MAPPING			= "/forgotmypassword";
	public static final String			MAIL_SENT_KEY						= "mailSent";
	public static final String			CHANGE_PASSWORD_PATH				= "/changeuserpassword";
	public static final String			EMAIL_MESSAGE_TEXT_PROPERTY_NAME	= "forgotmypassword.email.text";
	
	@Autowired
	private I18NService					i18NService;
	
	@Autowired
	private EmailService				emailService;
	
	@Value ("${webmaster.email}")
	private String						webMasterEmail;
	
	@Autowired
	private PasswordResetTokenService	passwordResetTokenService;
	
	
	
	@RequestMapping (value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.GET)
	public String forgotPasswordGet()
	{
		return EMAIL_ADDRESS_VIEW_NAME;
	}
	
	
	
	@RequestMapping (value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.POST)
	public String forgotPasswordPost(HttpServletRequest request, @RequestParam ("email") String email, ModelMap model)
	{
		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);
		
		if (passwordResetToken == null)
		{
			log.warn("Couldm't find a password reset token for email {}", email);
		}
		else
		{
			User user = passwordResetToken.getUser();
			String token = passwordResetToken.getToken();
			String passwordResetUrl = UserUtils.createPasswordResetUrl(request, user.getId(), token);
			log.info("Reset password URL " + passwordResetUrl);
			
			String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME, request.getLocale());
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(user.getEmail());
			mailMessage.setSubject("\"Devopsbuddy]: How to reset your password\"");
			mailMessage.setText(emailText + "\r\n" + passwordResetUrl);
			mailMessage.setFrom(webMasterEmail);
			
			emailService.sendGenericEmailMessage(mailMessage);
		}
		
		model.addAttribute(MAIL_SENT_KEY, "true");
		
		return EMAIL_ADDRESS_VIEW_NAME;
	}
}
