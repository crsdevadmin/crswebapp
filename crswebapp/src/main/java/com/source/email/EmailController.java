package com.source.email;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//Importing required classes
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



//Annotation
@RestController
//Class
public class EmailController {

	@Autowired private EmailService emailService;

	Logger logger = LoggerFactory.getLogger(EmailService.class);
			
			
	// Sending a simple Email
	@PostMapping("/sendMail")
	public String
	sendMail(@RequestBody EmailDetails details)
	{
		String status
			= emailService.sendSimpleMail(details);
		logger.debug("Hellow Iam a Logger");
		return status;
	}

	// Sending email with attachment
	@PostMapping("/sendMailWithAttachment")
	public String sendMailWithAttachment(
		@RequestBody EmailDetails details)
	{
		String status
			= emailService.sendMailWithAttachment(details);

		return status;
	}
}
