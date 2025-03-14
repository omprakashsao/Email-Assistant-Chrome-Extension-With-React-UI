package com.email.om.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.email.om.bean.EmailRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class EmailServiceImpl implements EmailService {
	

	
    @Value("${gemini.api.url}")
	private String url;
	
	
    @Value("${gemini.api.key}")
	private String key;

	@Override
	public String getResponse(EmailRequest emailrequest) {
		
		//build promt	
		String prompt = buildPrompt(emailrequest);
		
		//craft request
		Map<String, Object> body = Map.of(
                "contents", new Object[] {
                       Map.of("parts", new Object[]{
                               Map.of("text", prompt)
                       })
                }
        );
		
		
		//send request and get response
		
		WebClient client = WebClient.create();
		
		String response = client.post()
		      .uri(url+key)
		      .contentType(MediaType.APPLICATION_JSON)
		      .body(BodyInserters.fromValue(body))
		      .retrieve()
		      .bodyToMono(String.class)
		      .block();
		//extract response and return
		
		return extractResponse(response);
		
		
		
	}

	private String extractResponse(String response) {
		

		ObjectMapper mapper = new ObjectMapper();
		 
		try {
			
			JsonNode node = mapper.readTree(response);
		return	node.path("candidates")
			.get(0)
			.path("content")
			.path("parts")
			.get(0)
			.path("text")
			.asText();
		
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			return "Error processing request: " + e.getMessage();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			return "Error processing request: " + e.getMessage();
		}
		
		
	}

	private String buildPrompt(EmailRequest emailrequest) {
	    StringBuilder prompt = new StringBuilder();
	    prompt.append("Generate a professional email reply for the following email content. Please don't generate a subject line ");
	    if(emailrequest.getTone() != null && !emailrequest.getTone().isEmpty()) {
	    	prompt.append("use a ").append(emailrequest.getTone()).append(" tone ");
	    }
	    
	    prompt.append("\n Original Email: ").append(emailrequest.getEmailContent());
	    
		return prompt.toString();
	}

}
