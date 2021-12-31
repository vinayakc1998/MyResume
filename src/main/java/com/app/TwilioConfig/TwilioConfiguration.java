package com.app.TwilioConfig;

import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfiguration {
	
	public final String accountSid = "AC1662adb0958305695d0667dc63f4f33d";
	public final String authToken = "11cf502717dc269b882d6648a1c4ad70";
	public final String trialNumber = "+18506798147";
	
	public String getAccountSid() {
		return accountSid;
	}

	public String getAuthToken() {
		return authToken;
	}
	
	public String getTrialNumber() {
		return trialNumber;
	}
}
