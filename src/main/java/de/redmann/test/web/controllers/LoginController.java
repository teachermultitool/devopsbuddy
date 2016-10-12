package de.redmann.test.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by redmann on 12.10.16.
 */
@Controller
public class LoginController
{
	public static final String LOGIN_VIEW_NAME = "user/login";
	
	
	
	@RequestMapping ("/login")
	public String login()
	{
		return LOGIN_VIEW_NAME;
	}
}
