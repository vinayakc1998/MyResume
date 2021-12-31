package com.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.app.pojos.Role;
import com.app.pojos.User;
import com.app.repository.UserRepository;
import com.app.service.IEmailSenderService;
import com.app.service.IUserService;

@Service(value = "userService")
@Transactional
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IEmailSenderService emailSenderService;

	@Autowired
	private UserRepository userRepo;


	@Override
	public String registerUser(User transientUser) throws MailException, InterruptedException {
		transientUser.setRole(Role.CITIZEN);
		if (userRepo.save(transientUser) == transientUser) {
			emailSenderService.sendSimpleEmail(transientUser.getEmail(),
					"Dear " + transientUser.getFirstName() + " " + transientUser.getLastName() + ",\n"
							+ "Congratulations! You have successfully registered on the E-RTO Portal.\n"
							+ "Follow the instructions given below to fill the License Application Form.\n\n"
							+ "1. Connect to the internet and click on the link http://localhost:8080/rto/user/login.\n"
							+ "2. Login to the system using the following credentials.\n"
							+ "\tRegistered email ID and Password." + "\n"
							+ "3. Upload your aadhar card, signature and passport size photographs in .jpeg/.png.\n"
							+ "\tNote: Maximum size for uploading the photograph is 500KB.\n\n"
							+ "Once you have filled the form, you can check your status from the E-RTO Portal.\n"
							+ "\nThank you for registering on E-RTO.\n"
							+ "Wish you the Best of Luck for the test process.\n"
							+ "In case you have any query, you can connect with us at rtocdacproject2021@gmail.com\n"
							+ "\n" + "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Registration");
			return "Registered Successfully!!";
		}
		emailSenderService.sendSimpleEmail(transientUser.getEmail(), "Registration Failed!! Try Again!!",
				"Registration Failed!!");
		return "Not Registered!!";
	}

}
