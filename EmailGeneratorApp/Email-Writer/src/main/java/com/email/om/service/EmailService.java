package com.email.om.service;

import org.springframework.stereotype.Service;

import com.email.om.bean.EmailRequest;


public interface EmailService {

	public String getResponse(EmailRequest emailrequest);
}
