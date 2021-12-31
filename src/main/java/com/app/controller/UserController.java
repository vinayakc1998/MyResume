package com.app.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.pojos.LearningLicense;
import com.app.pojos.LearningStatus;
import com.app.pojos.PermanentLicense;
import com.app.pojos.PermanentStatus;
import com.app.pojos.Role;
import com.app.pojos.User;
import com.app.repository.UserRepository;
import com.app.service.ILearningService;
import com.app.service.IPermanentService;
import com.app.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private IUserService userService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ILearningService learningService;

	@Autowired
	private IPermanentService permanentService;

	public UserController() {
		System.out.println("in ctor of " + getClass().getName());
	}

	@GetMapping("/login")
	public ModelAndView showLoginForm() {
		return new ModelAndView("/user/login");
	}

	@GetMapping("/register")
	public ModelAndView showRegForm(Model modelMap, User u) {
		return new ModelAndView("/user/register");
	}

	@PostMapping("/register")
	public ModelAndView processRegForm(@Valid User u, BindingResult res, RedirectAttributes flashMap, Model modelMap)
			throws MailException, InterruptedException {
		if (res.hasErrors()) {
			return new ModelAndView("/user/register");
		}
		u.setPassword(passwordEncoder.encode(u.getPassword()));
		flashMap.addFlashAttribute("message", userService.registerUser(u));
		return new ModelAndView("redirect:/user/login");

	}

	@GetMapping("/process-login")
	public ModelAndView processUserLogin(Authentication auth, HttpSession hs) {
		String email = auth.getName();
		String role = auth.getAuthorities().toString();
		if (role.equalsIgnoreCase("[ADMIN]")) {
			return new ModelAndView("redirect:/admin/ladmin");
		}
		User user = userRepo.findByEmail(email);
		hs.setAttribute("user_details", user);
		hs.setAttribute("message", user.getFirstName() + " Logged in succesfully");
		hs.setAttribute("userId", user.getUserId());
		return new ModelAndView("redirect:/user/page");
	}

	@GetMapping("/page")
	public ModelAndView showUserPage() {
		return new ModelAndView("/user/page");
	}

	@GetMapping("/success")
	public ModelAndView showSuccessPage() {
		return new ModelAndView("/user/success");
	}

	@GetMapping("/mocktest")
	public ModelAndView showMocktest() {
		return new ModelAndView("/user/mocktest");
	}

	@GetMapping("/status")
	public ModelAndView showStatusPage(HttpSession hs, Model model) {

		int userId = (int) hs.getAttribute("userId");
		System.out.println("userId " + userId);
		LearningLicense ll = learningService.findByUserId(userId);
		if (ll == null) {
			model.addAttribute("message", "Please apply for Learning License first!");
			return new ModelAndView("/error");
		}
		LearningStatus s1 = ll.getLearningStatus();
		model.addAttribute("learningBooked2", "LEARNER LICENSE FORM SUBMITTED ");
		model.addAttribute("learningBooked3",
				"Your Learning License Application Form is under Process, Confirmation regarding test will reach you within 48 hours.");
		if (s1 == LearningStatus.BOOKED)
			return new ModelAndView("/user/status");

		model.addAttribute("writtenTestSlot2", "LEARNER LICENSE TEST SLOT ISSUED ");
		model.addAttribute("writtenTestSlot3", "Your schedule has been confirmed.Test Date : " + ll.getAppointmentDate()
				+ " Test Time : " + ll.getAppointmentTime() + ". Question Paper will reach you by mail. Best of luck");
		if (s1 == LearningStatus.WRITTENSLOTISSUED)
			return new ModelAndView("/user/status");
		
		if (ll.getWrittenTestFlag().equalsIgnoreCase("Y")) {
			model.addAttribute("writtenTestStatus2", "LEARNER LICENSE WRITTEN TEST PASSED ");
			model.addAttribute("writtenTestStatus3",
					"Congratulations, You have cleared the written test for Learning license. Please collect your license from the nearest RTO office");
			if (s1 == LearningStatus.WRITTENTESTPASSED)
				return new ModelAndView("/user/status");
		} else {
			model.addAttribute("writtenTestStatus2", "LEARNER LICENSE WRITTEN TEST FAILED ");
			model.addAttribute("writtenTestStatus3",
					"Sorry, but you have failed in clearing the written test for learning license. Your application form is cancelled. Please apply again.");
			if (s1 == LearningStatus.WRITTENTESTFAILED)
				return new ModelAndView("/user/status");
		}

		model.addAttribute("learningCompleted2", "LEARNER LICENSE ALLOTED ");
		model.addAttribute("learningCompleted3",
				"Your learning license has been issued successfully.The validity for learner's license is 6 months only. So, apply within the due date");
		if (s1 == LearningStatus.COMPLETED)
			return new ModelAndView("/user/status");

		if (s1 == LearningStatus.APPLIEDFORPERMANENT) {
			PermanentLicense pl = permanentService.findByUserId(userId);
			PermanentStatus s2 = null;
			if (pl != null)
				s2 = pl.getPermanentStatus();

			model.addAttribute("permanentBooked1", "PERMANENT LICENSE FORM SUBMITTED ");
			model.addAttribute("permanentBooked2",
					"Your Permanent License Application Form is under Process, Confirmation regarding test will reach you within 48 hours.");
			if (s2 == PermanentStatus.BOOKED)
				return new ModelAndView("/user/status");

			model.addAttribute("drivingTestSlot1", "DRIVING TEST SLOT ISSUED ");
			model.addAttribute("drivingTestSlot2",
					"You Have Been Alloted a schedule for Driving Test.Your schedule has been confirmed, Test Date : "
							+ pl.getAppointmentDate() + " Test Time : " + pl.getAppointmentTime()
							+ ". Please bring documents along with you.");
			if (s2 == PermanentStatus.DRIVINGSLOTISSUED)
				return new ModelAndView("/user/status");

			if (pl.getWrittenTestFlag().equalsIgnoreCase("Y")) {
				model.addAttribute("drivingTestStatus1", "DRIVING TEST PASSED ");
				model.addAttribute("drivingTestStatus2",
						"Congratulations, You have cleared the driving test, Mail regarding the issue of license will reach you soon.");
				if (s2 == PermanentStatus.DRIVINGPASS)
					return new ModelAndView("/user/status");
			} else {
				model.addAttribute("drivingTestStatus1", "DRIVING TEST FAILED ");
				model.addAttribute("drivingTestStatus2",
						"Sorry, but you have failed in clearing the driving test, Please apply again.");
				if (s2 == PermanentStatus.DRIVINGFAIL)
					return new ModelAndView("/user/status");
			}

			model.addAttribute("permanentCompleted1", "DRIVING PERMANENT LICENSE ISSUED ");
			model.addAttribute("permanentCompleted2", "Your Permanent license has been issued successfully.");
			if (s2 == PermanentStatus.COMPLETED)
				return new ModelAndView("/user/status");
		}
		return new ModelAndView("/user/status");
	}

}
