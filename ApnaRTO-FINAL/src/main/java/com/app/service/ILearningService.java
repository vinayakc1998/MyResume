package com.app.service;

import java.util.Optional;

import org.springframework.mail.MailException;

import com.app.pojos.LearningLicense;

public interface ILearningService {

	String applyForLearning(LearningLicense ll) throws MailException, InterruptedException;

	LearningLicense registerForLearning(LearningLicense ll, Integer userId) throws MailException, InterruptedException;

	Optional<LearningLicense> findById(Integer applicantId);
	
	LearningLicense findByUserId(Integer userId);
	
	void deleteLearningLicenseById(Integer applicantId);

	boolean updateLisence(LearningLicense ll);
}
