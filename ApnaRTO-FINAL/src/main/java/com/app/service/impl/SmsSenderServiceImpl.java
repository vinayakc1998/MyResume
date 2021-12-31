package com.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.TwilioConfig.TwilioConfiguration;
import com.app.service.ISmsSender;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsSenderServiceImpl implements ISmsSender {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsSenderServiceImpl.class);

	private final TwilioConfiguration twilioConfiguration;

	@Autowired
	public SmsSenderServiceImpl(TwilioConfiguration twilioConfiguration) {
		this.twilioConfiguration = twilioConfiguration;
	}

	@Override
	public void sendSms(String mobileNo, String message) {
		PhoneNumber to = new PhoneNumber(mobileNo);
		LOGGER.info("send sms successfully!" +twilioConfiguration.getAccountSid());
		 @SuppressWarnings("unused")
		Message m = Message.creator(to,"MG61b857ee0755b3e23b466ec2c805391d",message).create();
	}

}
