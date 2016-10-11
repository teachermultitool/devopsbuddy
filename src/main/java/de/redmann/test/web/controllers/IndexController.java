package de.redmann.test.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

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
