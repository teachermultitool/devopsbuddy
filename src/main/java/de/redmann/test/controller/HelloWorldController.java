package de.redmann.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by redmann on 10.10.16.
 */
@Controller
public class HelloWorldController
{
	@RequestMapping ("/")
	public String sayHello()
	{
		return "index";
	}
}
