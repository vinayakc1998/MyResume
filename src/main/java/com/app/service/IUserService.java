package com.app.service;

import org.springframework.mail.MailException;

import com.app.pojos.User;

public interface IUserService {
	//User authenticateUser(String email, String pwd);

	String registerUser(User transientUser) throws MailException, InterruptedException;

}
