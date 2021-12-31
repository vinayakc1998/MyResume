package com.app.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.app.pojos.LearningLicense;
import com.app.pojos.LearningStatus;
import com.app.pojos.PermanentLicense;
import com.app.pojos.PermanentStatus;
import com.app.repository.LearningRepository;
import com.app.repository.PermanentRepository;
import com.app.service.IEmailSenderService;
import com.app.service.ILearningService;
import com.app.service.IPermanentService;
import com.app.service.ISmsSender;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private LearningRepository learningRepo;

	@Autowired
	private PermanentRepository permanentRepo;

	@Autowired
	private ILearningService learningService;

	@Autowired
	private IPermanentService permanentService;

	@Autowired
	private IEmailSenderService emailSenderService;

	@Autowired
	private ISmsSender smsSender;

	public AdminController() {
		System.out.println("in ctor of " + getClass().getName());
	}

	@GetMapping("/ladmin")
	public ModelAndView showLearningList(Model map, Authentication auth) {
		if (auth.getAuthorities().toString().equalsIgnoreCase("[ADMIN]")) {
			map.addAttribute("learning_list", learningRepo.findAll());
			return new ModelAndView("/admin/ladmin");
		}
		return new ModelAndView("redirect:/user/login");
	}

	@GetMapping("/padmin")
	public ModelAndView showPermanentList(Model map, Authentication auth) {
		if (auth.getAuthorities().toString().equalsIgnoreCase("[ADMIN]")) {
			map.addAttribute("permanent_list", permanentRepo.findAll());
			return new ModelAndView("/admin/padmin");
		}
		return new ModelAndView("redirect:/user/login");
	}

	@GetMapping("/ledit")
	public ModelAndView ShowEditLearningtable(@RequestParam int vid, Model map, HttpServletRequest request) {
		map.addAttribute("user_details", learningService.findById(vid));
		return new ModelAndView("/admin/ledit");
	}

	@GetMapping("/ldelete")
	public String deleteLearningtable(@RequestParam int vid) {
		learningService.deleteLearningLicenseById(vid);
		return "redirect:/admin/ladmin";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/ledit")
	public ModelAndView editLearningTable(@RequestParam MultiValueMap map, HttpServletRequest request, HttpSession hs)
			throws MailException, InterruptedException {
		String s;

		s = map.getFirst("vid").toString();
		int id = Integer.parseInt(s);

		s = map.getFirst("learningStatus").toString();
		LearningStatus status = LearningStatus.valueOf(s);

		LearningLicense learnerLicense = learningService.findById(id).get();
		learnerLicense.setLearningStatus(status);
		if (status == LearningStatus.WRITTENTESTPASSED || status == LearningStatus.COMPLETED) {
			learnerLicense.setWrittenTestFlag("Y");
		}
		learningService.updateLisence(learnerLicense);

		if (learnerLicense.getLearningStatus() == LearningStatus.WRITTENSLOTISSUED) {
			emailSenderService.sendSimpleEmail(learnerLicense.getEmail(),
					"Dear " + learnerLicense.getFirstName() + " " + learnerLicense.getLastName() + ",\n\n"
							+ "Your Learning License Test will be held on " + learnerLicense.getAppointmentDate()
							+ " at " + learnerLicense.getAppointmentTime() + "\n"
							+"Question Paper will reach you via mail, 5 minutes prior your exam time."
							+ "Wish you the Best of Luck for the test process.\n"
							+ "In case you have any query, you can connect us at rtocdacproject2021@gmail.com\n" + "\n"
							+ "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Learning Test");
			smsSender.sendSms(learnerLicense.getMobileNo(),
					"Your Learning License Test will be held on " + learnerLicense.getAppointmentDate() + " at "
							+ learnerLicense.getAppointmentTime());

		}
		if (learnerLicense.getLearningStatus() == LearningStatus.WRITTENTESTPASSED) {
			emailSenderService.sendSimpleEmail(learnerLicense.getEmail(), "Dear " + learnerLicense.getFirstName() + " "
					+ learnerLicense.getLastName() + ",\n\n"
					+ "Congratulations, You have successfully cleared the Written Exam For License.\n"
					+ "You can get your license from the nearest RTO office.\n"
					+ "Learning License is valid for next 6 months only.So, apply for permanent license within the due date.\n"
					+ "In case you have any query, you can connect us at rtocdacproject2021@gmail.com\n" + "\n"
					+ "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services", "E-RTO Learning Test");
			smsSender.sendSms(learnerLicense.getMobileNo(),
					"Congratulations, You have successfully cleared the Written Exam For License."
							+ "Please collect it from the nearest RTO.");
		}

		if (learnerLicense.getLearningStatus() == LearningStatus.WRITTENTESTFAILED) {
			emailSenderService.sendSimpleEmail(learnerLicense.getEmail(),
					"Dear " + learnerLicense.getFirstName() + " " + learnerLicense.getLastName() + ",\n\n"
							+ "We are Sorry, but you just failed the Written Exam for License\n"
							+ "Your application form is cancelled. Please apply again.\n\n" + "Warm Regards,\n"
							+ "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Learning Test");
			smsSender.sendSms(learnerLicense.getMobileNo(),
					"Sorry, But you have failed the written examination for Learner's license."
							+ "Your application form is cancelled. Please apply again.");
		}
		if (learnerLicense.getLearningStatus() == LearningStatus.REJECTED) {
			emailSenderService.sendSimpleEmail(learnerLicense.getEmail(), "Dear " + learnerLicense.getFirstName() + " "
					+ learnerLicense.getLastName() + ",\n\n"
					+ "Your learner license application form is rejected,Please fill the form again carefully.\n"
					+ "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services", "E-RTO Driving Test");
		}
		return new ModelAndView("redirect:/admin/ladmin");
	}

	@GetMapping("/pedit")
	public ModelAndView ShowEditPermanentTable(@RequestParam int vid, Model map, HttpServletRequest request) {
		map.addAttribute("permanent_user_details", permanentService.findById(vid));
		return new ModelAndView("/admin/pedit");
	}

	@GetMapping("/pdelete")
	public String deletePermanenttable(@RequestParam int vid) {
		permanentService.deletePermanentLicenseById(vid);
		return "redirect:/admin/padmin";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PostMapping("/pedit")
	public ModelAndView editPermanentTable(@RequestParam MultiValueMap map, HttpServletRequest request, HttpSession hs)
			throws MailException, InterruptedException {
		String s;

		s = map.getFirst("vid").toString();
		int id = Integer.parseInt(s);

		s = map.getFirst("permanentStatus").toString();
		PermanentStatus status = PermanentStatus.valueOf(s);

		PermanentLicense permanentLicense = permanentService.findById(id).get();
		permanentLicense.setPermanentStatus(status);
		if (status == PermanentStatus.DRIVINGPASS || status == PermanentStatus.COMPLETED) {
			permanentLicense.setWrittenTestFlag("Y");
		}
		permanentService.updateLisence(permanentLicense);

		if (permanentLicense.getPermanentStatus() == PermanentStatus.DRIVINGSLOTISSUED) {
			emailSenderService.sendSimpleEmail(permanentLicense.getEmail(),
					"Dear " + permanentLicense.getFirstName() + " " + permanentLicense.getLastName() + ",\n\n"
							+ "Your Driving License Test will be held on " + permanentLicense.getAppointmentDate()
							+ " at " + permanentLicense.getAppointmentTime() + "\n"
							+ "Test Centre : MH-12, RTO Office Pune\n"
							+ "\t38, Dr. Ambedkar Raod, Near Sangam Bridge, Pune - 411001\n\n"
							+ "Wish you the Best of Luck for the test process.\n"
							+ "In case you have any query, you can connect us at rtocdacproject2021@gmail.com\n" + "\n"
							+ "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Driving Test");
			smsSender.sendSms(permanentLicense.getMobileNo(),
					"Your Driving License Test" + "will be held on " + permanentLicense.getAppointmentDate() + " at "
							+ permanentLicense.getAppointmentTime() + "\n"
							+ " Test Centre : MH-12, RTO Office Pune - t38, Dr. Ambedkar Raod, Near"
							+ "Sangam Bridge, Pune - 411001");
		}
		if (permanentLicense.getPermanentStatus() == PermanentStatus.DRIVINGPASS) {
			emailSenderService.sendSimpleEmail(permanentLicense.getEmail(),
					"Dear " + permanentLicense.getFirstName() + " " + permanentLicense.getLastName() + ",\n\n"
							+ "Congratulations, You have successfully passed the Driving Test For License.\n"
							+ "In case you have any query, you can connect us at rtocdacproject2021@gmail.com\n" + "\n"
							+ "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Driving Test");
			smsSender.sendSms(permanentLicense.getMobileNo(),
					"Congratulations, You have successfully cleared the Driving Test For Permanent License.");
		}
		if (permanentLicense.getPermanentStatus() == PermanentStatus.COMPLETED) {
			emailSenderService.sendSimpleEmail(permanentLicense.getEmail(),
					"Dear " + permanentLicense.getFirstName() + " " + permanentLicense.getLastName() + ",\n\n"
							+ "Your Permanent Driving License is out for delievery, and will reach you in some days.\n"
							+ "Track Your License from here: "
							+ "In case you have any query, you can connect us at rtocdacproject2021@gmail.com\n" + "\n"
							+ "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services",
					"E-RTO Permanent Driving License");
			smsSender.sendSms(permanentLicense.getMobileNo(),
					"Your permanent license is out for delievery. Track Your Order : xxxxxxxx");
		}

		if (permanentLicense.getPermanentStatus() == PermanentStatus.DRIVINGFAIL) {
			emailSenderService.sendSimpleEmail(permanentLicense.getEmail(), "Dear " + permanentLicense.getFirstName()
					+ " " + permanentLicense.getLastName() + ",\n\n"
					+ "We are Sorry, but you just failed the Written Exam for License\n"
					+ "Your permanent license application form is cancelled,Please fill again to apply for re-test.\n"
					+ "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services", "E-RTO Driving Test");
			smsSender.sendSms(permanentLicense.getMobileNo(),
					"Sorry, But you have failed the written examination for Learner's license."
							+ "Your permanent license application form is cancelled. Please apply again.");
		}

		if (permanentLicense.getPermanentStatus() == PermanentStatus.REJECTED) {
			emailSenderService.sendSimpleEmail(permanentLicense.getEmail(), "Dear " + permanentLicense.getFirstName()
					+ " " + permanentLicense.getLastName() + ",\n\n"
					+ "Your permanent license application form is cancelled,Please fill the form again carefully.\n"
					+ "Warm Regards,\n" + "E-RTO Group,\n" + "CDAC Services", "E-RTO Driving Test");
		}
		return new ModelAndView("redirect:/admin/padmin");
	}
}