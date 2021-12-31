package com.app.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.pojos.PermanentLicense;
import com.app.pojos.User;
import com.app.repository.PermanentRepository;
import com.app.repository.UserRepository;
import com.app.service.IEmailSenderService;
import com.app.service.IPermanentService;
import com.app.service.ISmsSender;

@Service
@Transactional
public class PermanentServiceImpl implements IPermanentService {

	@Autowired
	private IEmailSenderService emailSenderService;

	@Autowired
	private ISmsSender smsSender;

	@Autowired
	private PermanentRepository permanentRepo;

	@Autowired
	private UserRepository userRepo;

	@Override
	public String applyForPermanent(PermanentLicense pl) throws MailException, InterruptedException {
		if (permanentRepo.save(pl) == pl) {
			emailSenderService.sendSimpleEmail(pl.getEmail(), "Dear " + pl.getFirstName() + " " + pl.getLastName()
					+ ",\n\n"
					+ "Congratulations! You have successfully filled the Permanent license application form.\n\n"
					+ "Your Applicant ID is : " + pl.getApplicantId() + ".\nPlease note this for further reference.\n"
					+ "Login to the system using Registered email ID and Password.\n\n"
					+ "You can check your application status from the E-RTO Portal.\n"
					+ "Mail Regarding test schedule will reach you within 48 hours.\n"
					+ "Wish you the Best of Luck for the driving test process.\n"
					+ "In case you have any query, you can connect with us at rtocdacproject2021@gmail.com\n" + "\n"
					+ "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services", "E-RTO Permanent License Application");

			smsSender.sendSms(pl.getMobileNo(), "Congratulations, Your permanent license application form is submitted!");
			return "Registered Successfully!!";
		}
		emailSenderService.sendSimpleEmail(pl.getEmail(), " Failed!! Try Again!!", "Permanent License Application");
		return "Not Registered!!";
	}

	@Override
	public PermanentLicense registerForPermanent(PermanentLicense pl, Integer userId)
			throws MailException, InterruptedException {
		Optional<User> user = userRepo.findById(userId);
		pl.setUser(user.get());
		applyForPermanent(pl);
		return pl;
	}

	@Override
	public Optional<PermanentLicense> findById(Integer applicantId) {

		return permanentRepo.findById(applicantId);
	}

	@Override
	public PermanentLicense findByUserId(Integer userId) {
		User user = userRepo.findById(userId).get();
		PermanentLicense pl = permanentRepo.findByUserId(user);
		return pl;
	}
	
	@Override
	public void deletePermanentLicenseById(Integer applicantId) {
		permanentRepo.deleteById(applicantId);

	}

	@Override
	public boolean updateLisence(PermanentLicense pl) {
		try {
			permanentRepo.save(pl);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
