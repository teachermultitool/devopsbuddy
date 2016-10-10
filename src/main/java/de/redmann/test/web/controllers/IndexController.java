package de.redmann.test.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by redmann on 10.10.16.
 */
@Controller
public class IndexController
{
	@RequestMapping ("/")
	public String home()
	{
		return "index";
	}
}
