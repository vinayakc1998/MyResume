package com.app.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.pojos.LearningLicense;
import com.app.pojos.User;
import com.app.repository.LearningRepository;
import com.app.repository.UserRepository;
import com.app.service.IEmailSenderService;
import com.app.service.ILearningService;
import com.app.service.ISmsSender;

@Service
@Transactional
public class LearningServiceImpl implements ILearningService {

	@Autowired
	private IEmailSenderService emailSenderService;

	@Autowired
	private ISmsSender smsSender;

	@Autowired
	private LearningRepository learningRepo;

	@Autowired
	private UserRepository userRepo;

	@Override
	public String applyForLearning(LearningLicense ll) throws MailException, InterruptedException {

		if (learningRepo.save(ll) == ll) {
			emailSenderService.sendSimpleEmail(ll.getEmail(),
					"Dear " + ll.getFirstName() + " " + ll.getLastName() + ",\n\n"
							+ "Congratulations! You have successfully filled the Learning license application form.\n\n"
							+ "Your Applicant ID is : " + ll.getApplicantId()
							+ ".\nPlease note this for further reference.\n"
							+ "Login to the system using Registered email ID and Password.\n\n"
							+ "You can check your application status from the E-RTO Portal.\n"
							+ "Mail Regarding test schedule will reach you within 48 hours.\n"
							+ "Wish you the Best of Luck for the test process.\n"
							+ "In case you have any query, you can connect with us at rtocdacproject2021@gmail.com\n"
							+ "\n" + "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Learning License Application");

			smsSender.sendSms(ll.getMobileNo(), "Congratulations, Your learner license form is submitted!");
			System.out.println("message sent");

			return "Registered Successfully!!";

		}
		emailSenderService.sendSimpleEmail(ll.getEmail(), " Failed!! Try Again!!", "Learning License Application");
		return "Not Registered!!";

	}

	@Override
	public LearningLicense registerForLearning(LearningLicense ll, Integer userId)
			throws MailException, InterruptedException {
		Optional<User> user = userRepo.findById(userId);
		ll.setUser(user.get());
		applyForLearning(ll);
		return ll;
	}

	@Override
	public Optional<LearningLicense> findById(Integer applicantId) {

		return learningRepo.findById(applicantId);
	}

	@Override
	public LearningLicense findByUserId(Integer userId) {
		User user = userRepo.findById(userId).get();
		LearningLicense ll = learningRepo.findByUserId(user);
		return ll;
	}
	@Override
	public void deleteLearningLicenseById(Integer applicantId) {
		learningRepo.deleteById(applicantId);

	}

	@Override
	public boolean updateLisence(LearningLicense ll) {
		try {
			learningRepo.save(ll);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
}
