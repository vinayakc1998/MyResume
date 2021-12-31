package com.app.controller;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpSession;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MyExceptionHandler {

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = NoResultException.class)
	public String exceptionHandlerNoResult(Model m) {
		m.addAttribute("noResult", "User doesn't exist, Please register yourself first or enter valid email!");
		return "/user/login";
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public String exceptionHandlerDataIntegrity(Model m) {
		m.addAttribute("message", "Form for this Aadhar Number or email is already submitted!");
		return "/error";
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(IllegalArgumentException.class)
	public String exceptionHandlerIllegal(Model m) {
		m.addAttribute("message", "Please Login again!");
		return "/error";
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(IllegalStateException.class)
	public String exceptionHandlerIllegalState(HttpSession hs) {
		hs.setAttribute("message", "Please apply for learning license first");
		return "/error";
	}

	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public String exceptionHandlerGeneric() {
		return "/error";
	}
}
