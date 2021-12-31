package com.app.service;

import java.util.Optional;

import org.springframework.mail.MailException;

import com.app.pojos.PermanentLicense;

public interface IPermanentService {

	String applyForPermanent(PermanentLicense pl) throws MailException, InterruptedException;
	
	PermanentLicense registerForPermanent(PermanentLicense pl, Integer userId) throws MailException, InterruptedException;

	Optional<PermanentLicense> findById(Integer applicantId);
	
	PermanentLicense findByUserId(Integer userId);
	
	void deletePermanentLicenseById(Integer applicantId);

	
	boolean updateLisence(PermanentLicense pl);

	}