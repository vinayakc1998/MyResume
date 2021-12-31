package com.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexPageController {

	
	public IndexPageController() {
		System.out.println(getClass().getName());
	}

	@GetMapping("/")
	public ModelAndView provideHomePage() {
		return new ModelAndView("/index");
	}
	
}
