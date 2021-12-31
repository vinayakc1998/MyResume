package com.app.service.impl;

import java.util.ArrayList;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.app.pojos.LearningLicense;
import com.app.pojos.PermanentLicense;
import com.app.repository.LearningRepository;
import com.app.repository.PermanentRepository;
import com.app.service.IEmailSenderService;

@Component
public class ScheduledTasks{
	
	@Autowired
	private IEmailSenderService emailSenderService;
	
	@Autowired
	private LearningRepository learningRepo;
	
	@Autowired
	private PermanentRepository permanentRepo;

	@Scheduled(cron = "0 55 23 * * * ")
	public void reminderForLearning() throws MailException, InterruptedException {
		ArrayList<LearningLicense> list = learningRepo.findAllApplicants();
		ListIterator<LearningLicense> i =list.listIterator();
		while(i.hasNext()) {
			LearningLicense l = i.next();
			emailSenderService.sendSimpleEmail(l.getEmail(),
					"Dear " + l.getFirstName() + " " + l.getLastName() + ",\n\n"
							+"This is a reminder that your Learning License Written test is on "+l.getAppointmentDate() 
							+"at " + l.getAppointmentTime()
						+"You will get your exam link in a mail which will reach you 5 minutes prior your test time.\n\n"
							+ "You can check your application status from the E-RTO Portal.\n"
							+ "Wish you the Best of Luck for the test process.\n"
							+ "In case you have any query, you can connect with us at rtocdacproject2021@gmail.com\n"
							+ "\n" + "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Learning License Application");
		}
	}
	@Scheduled(cron = "0 55 23 * * * ")
	public void reminderForPermanent() throws MailException, InterruptedException {
		ArrayList<PermanentLicense> list = permanentRepo.findAllApplicants();
		ListIterator<PermanentLicense> i =list.listIterator();
		while(i.hasNext()) {
			PermanentLicense p = i.next();
			emailSenderService.sendSimpleEmail(p.getEmail(),
					"Dear " + p.getFirstName() + " " + p.getLastName() + ",\n\n"
							+"This is a reminder that your permanent License Driving test is on "+p.getAppointmentDate() 
							+"at " + p.getAppointmentTime()
						+"Please bring your Aadhar Card, Electricity Bill and a Passport Size Photo, else you will not be allowed to give the test"
							+ "You can check your application status from the E-RTO Portal.\n"
							+ "Wish you the Best of Luck for the test process.\n"
							+ "In case you have any query, you can connect with us at rtocdacproject2021@gmail.com\n"
							+ "\n" + "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Learning License Application");
		}
	}
	
	@Scheduled(cron = "0 55 10 * * * ")
	public void examFor11() throws MailException, InterruptedException {
		ArrayList<LearningLicense> list = learningRepo.findAllApplicantsWithTime11();
		ListIterator<LearningLicense> i =list.listIterator();
		while(i.hasNext()) {
			LearningLicense l = i.next();
			emailSenderService.sendSimpleEmail(l.getEmail(),
					"Dear " + l.getFirstName() + " " + l.getLastName() + ",\n\n"
							+"Its Time for your exam"
							+"Please submit your form till 11:25, else it will be submitted automatically\n\n"
								+"Please click on the link and fill your email Id, Name and Aadhar Number."+"\n"
								+ "https://docs.google.com/forms/d/1WRGDIFW-exjQdvcVTdLkD4Sk7Hl9uCv3nJmO-PRzKRI/edit?ts=60534007&gxids=7628\n"
						+"Best of luck for your test process"+ "In case you have any query, you can connect with us at rtocdacproject2021@gmail.com\n"
							+ "\n" + "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Learning License Application");
		}
	}
	
	@Scheduled(cron = "0 25 13 * * * ")
	public void examFor130() throws MailException, InterruptedException {
		ArrayList<LearningLicense> list = learningRepo.findAllApplicantsWithTime130();
		ListIterator<LearningLicense> i =list.listIterator();
		while(i.hasNext()) {
			LearningLicense l = i.next();
			emailSenderService.sendSimpleEmail(l.getEmail(),
					"Dear " + l.getFirstName() + " " + l.getLastName() + ",\n\n"
							+"Its Time for your exam"
						+"Please submit your form till 1:55, else it will be submitted automatically\n\n"
							+"Please click on the link and fill your email Id, Name and Aadhar Number.\n"
							+ "https://docs.google.com/forms/d/1WRGDIFW-exjQdvcVTdLkD4Sk7Hl9uCv3nJmO-PRzKRI/edit?ts=60534007&gxids=7628\n"
							+ "Wish you the Best of Luck for the test.\n"
							+ "In case you have any query, you can connect with us at rtocdacproject2021@gmail.com\n"
							+ "\n" + "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Learning License Application");
		}
	}
	
	@Scheduled(cron = "0 25 15 * * * ")
	public void examFor330() throws MailException, InterruptedException {
		ArrayList<LearningLicense> list = learningRepo.findAllApplicantsWithTime330();
		ListIterator<LearningLicense> i =list.listIterator();
		while(i.hasNext()) {
			LearningLicense l = i.next();
			emailSenderService.sendSimpleEmail(l.getEmail(),
					"Dear " + l.getFirstName() + " " + l.getLastName() + ",\n\n"
							+"Please submit your form till 3:55, else it will be submitted automatically\n\n"
							+"Please click on the link and fill your email Id, Name and Aadhar Number.\n"
							+ "https://docs.google.com/forms/d/1WRGDIFW-exjQdvcVTdLkD4Sk7Hl9uCv3nJmO-PRzKRI/edit?ts=60534007&gxids=7628\n"
							+ "Wish you the Best of Luck for the test.\n"
							+ "In case you have any query, you can connect with us at rtocdacproject2021@gmail.com\n"
							+ "\n" + "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Learning License Application");
		}
	}
	
}