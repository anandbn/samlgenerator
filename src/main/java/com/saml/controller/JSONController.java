package com.saml.controller;

import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.opensaml.xml.io.MarshallingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.saml.utils.SAML2AssertionGenerator;



@Controller
public class JSONController {

	@RequestMapping(value="/saml" ,method = RequestMethod.GET)
	public @ResponseBody Map<String,String> getAssertion(@RequestParam("username") String username,
														 @RequestParam("issuer") String issuer,
														 @RequestParam("recipient") String recipient) {
		Map<String,String> retMap = new HashMap<String,String>();
		try {
			String samlResponse = SAML2AssertionGenerator.getSamlAssertion(username,issuer,recipient);
			retMap.put("assertion",samlResponse);
			retMap.put("status","success");
			retMap.put("statusMsg","Generated SAML Assertion successfully !!!");
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
	
}