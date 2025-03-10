package com.email.om.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.email.om.bean.EmailRequest;
import com.email.om.service.EmailService;



@RestController
@RequestMapping("v1/api/email")
@CrossOrigin("*")
public class EmailGeneratorController {
	
	@Autowired
	private EmailService service;
	
	@PostMapping("/search")
	public ResponseEntity<String> postMethodName(@RequestBody EmailRequest emailrequest) {
		//TODO: process POST request
		String response = service.getResponse(emailrequest);
	    System.out.println("request is get..");
		
		return ResponseEntity.ok(response);
	}
	
	

}
