package com.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.app.helper.FileUploadHelper;

@Controller
@RequestMapping("/upload")
public class FileUploadController {

	@Autowired
	private FileUploadHelper fileUploadHelper;

	@GetMapping("/upload-files")
	public ModelAndView showUploadForm(HttpSession hs) {
		System.out.println("in show upload form");
		return new ModelAndView("/upload/upload-files");
	}

	@PostMapping("/upload-files")
	public ModelAndView fileUpload(@RequestParam("aadharImage") MultipartFile file1,
			@RequestParam("passportImage") MultipartFile file2, @RequestParam("signature") MultipartFile file3,
			HttpSession hs) {
		System.out.println("File Upload Controller");
		
		fileUploadHelper.uploadFile(file1);
		fileUploadHelper.uploadFile(file2);
		fileUploadHelper.uploadFile(file3);
		
		return new ModelAndView("redirect:/user/success");
	}

}
