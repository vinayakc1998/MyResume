package com.app.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.pojos.LearningLicense;
import com.app.pojos.LearningStatus;
import com.app.pojos.PermanentLicense;
import com.app.pojos.User;
import com.app.repository.LearningRepository;
import com.app.service.ILearningService;
import com.app.service.IPermanentService;

@Controller
@RequestMapping("/license")
public class LicenseController {

	@Autowired
	private IPermanentService permanentService;

	@Autowired
	private ILearningService learningService;
	
	public LicenseController() {
		System.out.println(getClass().getName());
	}

	@GetMapping("/learning")
	public ModelAndView ShowLearningForm(Model modelMap, LearningLicense ll,HttpSession hs) {
		int userId = (int) hs.getAttribute("userId");
		modelMap.addAttribute("luser_details",learningService.findByUserId(userId));
		return new ModelAndView("/license/learningForm");
	}

	@PostMapping("/learning")
	public ModelAndView fillLearningForm(@Valid LearningLicense learnerLicense, BindingResult res, RedirectAttributes flashMap,
			Model modelMap, HttpSession hs) throws MailException, InterruptedException{
		modelMap.addAttribute(hs.getAttribute("user_details"));
		int userId= (int) hs.getAttribute("userId");
		learnerLicense.setUser((User) hs.getAttribute("user_details"));
		LearningLicense applicant = learningService.registerForLearning(learnerLicense, userId);
		hs.setAttribute("applicantId",applicant.getApplicantId());
		return new ModelAndView("redirect:/upload/upload-files");
	}

	@GetMapping("/permanent")
	public ModelAndView ShowPermanentForm(Model modelMap, PermanentLicense pl,HttpSession hs) {
		
		int userId = (int) hs.getAttribute("userId");
		LearningLicense ll =learningService.findByUserId(userId);
		if(ll==null) {
			modelMap.addAttribute("message", "Please apply for Learning License first!");
			return new ModelAndView("/error");
		} if(ll.getLearningStatus()==LearningStatus.COMPLETED) {
			modelMap.addAttribute("puser_details",learningService.findByUserId(userId));
			System.out.println("in permanent form ");
			return new ModelAndView("/license/permanentForm");
		} else {
			modelMap.addAttribute("message", "Please wait for Learning License completion first!");
			return new ModelAndView("/error");
		}
	}

	@PostMapping("/permanent")
	public ModelAndView fillPermanentForm(@Valid PermanentLicense permanentLicense, BindingResult res, RedirectAttributes flashMap,
			Model modelMap, HttpSession hs) throws MailException, InterruptedException {
		if (res.hasErrors()) {
			return new ModelAndView("/license/permanentForm");
		}
		int userId = (int) hs.getAttribute("userId");
		permanentLicense.setUser((User) hs.getAttribute("puser_details"));
		LearningLicense ll = learningService.findByUserId(userId);
		if (ll.getLearningStatus() == LearningStatus.COMPLETED) {
			PermanentLicense applicant = permanentService.registerForPermanent(permanentLicense, userId);
			ll.setLearningStatus(LearningStatus.APPLIEDFORPERMANENT);
			learningService.updateLisence(ll);
			hs.setAttribute("applicantId", applicant.getApplicantId());
			return new ModelAndView("/license/documents");
		} else {
			modelMap.addAttribute("message", "Please wait for Learning License completion first!");
			return new ModelAndView("/error");
		}
		
	}

	
}
