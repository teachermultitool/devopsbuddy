package de.redmann.test.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by redmann on 11.10.16.
 */
@Controller
public class CopyController
{
	@RequestMapping ("/about")
	public String about()
	{
		return "copy/about";
	}
}
