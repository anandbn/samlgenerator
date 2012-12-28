package com.saml.controller;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.opensaml.xml.io.MarshallingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.saml.utils.SAML2AssertionGenerator;



@Controller
public class JSONController {

	@RequestMapping(value="/saml" ,method = RequestMethod.GET)
	public @ResponseBody Map<String,String> getAssertion(@RequestParam("username") String username,
														 WebRequest webRequest) {
		String recipient = webRequest.getParameter("recipient");
		String issuer = webRequest.getParameter("issuer");
		String orgId = webRequest.getParameter("orgId");
		String portalId = webRequest.getParameter("portalId");
		String siteUrl = webRequest.getParameter("siteUrl");
        if(recipient == null){
        	recipient = "https://login.salesforce.com/services/oauth2/token";
        }
        if(issuer==null){
        	issuer = "http://www.chatterio.com";
        }

		Map<String,String> retMap = new HashMap<String,String>();
		try {
			String samlResponse = SAML2AssertionGenerator.getSamlAssertion(username,issuer,recipient,orgId,portalId,siteUrl);
			retMap.put("assertion",samlResponse);
			retMap.put("status","success");
			retMap.put("issuer",issuer);
			retMap.put("recipient", recipient);
			retMap.put("statusMsg","Generated SAML Assertion successfully !!");
		} catch (MarshallingException e) {
			retMap.put("status","error");
			retMap.put("statuMsg",e.getMessage());
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			retMap.put("status","error");
			retMap.put("statuMsg",e.getMessage());
			e.printStackTrace();
		} catch (TransformerException e) {
			retMap.put("status","error");
			retMap.put("statuMsg",e.getMessage());
			e.printStackTrace();
		}
		retMap.put("username", username);
		return retMap;
	}
	
	
	@RequestMapping(value="/saml" ,method = RequestMethod.OPTIONS)
	public ResponseEntity<String> handleOptions() {
		
        System.out.println("==============GOT OPTIONS REQUEST ===============");
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.TEXT_PLAIN);
	    responseHeaders.add("access-control-allow-methods", "HEAD, POST, GET, PUT, PATCH, DELETE");
	    responseHeaders.add("access-control-max-age", "86400");
	    responseHeaders.add("access-control-allow-credentials", "true");
	    responseHeaders.add("access-control-allow-origin", "*");
	    return new ResponseEntity<String>("Ok", responseHeaders, HttpStatus.OK);
	}
}