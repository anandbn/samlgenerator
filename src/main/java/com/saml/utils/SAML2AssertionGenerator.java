package com.saml.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.codec.binary.Base64;
import org.opensaml.xml.io.MarshallingException;

import axiom.saml.idp.IdentityProvider;
import axiom.saml.idp.IdpConfiguration;
import axiom.saml.idp.SamlUserIdLocation;
import axiom.saml.idp.SamlVersion;
import axiom.saml.idp.UserType;
import axiom.saml.idp.XmlObjectSerializer;
public class SAML2AssertionGenerator {
	private static URL keyStoreFile=null;
	
	static{
		keyStoreFile = SAML2AssertionGenerator.class.getClassLoader().getResource("AxiomIdpExample.keystore");
		System.out.println("Loaded keystore file from :"+keyStoreFile);
	}

	public static String getSamlAssertion(String username,String issuer,String recipient,String orgId,String portalId,String siteUrl) throws MarshallingException, TransformerFactoryConfigurationError, TransformerException{
		// TODO Auto-generated method stub
        IdentityProvider idp = new IdentityProvider(getIdpConfig(username,issuer,recipient,orgId,portalId,siteUrl));
        String samlAssertion = XmlObjectSerializer.xmlObjectToString(idp.generateSamlResponse());
        String base64Str = new String(Base64.encodeBase64(samlAssertion.getBytes()));
        System.out.println("==============Assertion BASE64-START ===============");
        System.out.println(base64Str);
        System.out.println("==============Assertion BASE64-END   ===============");
        return base64Str;
	}
 	public static IdpConfiguration getIdpConfig(String username,String issuer,String recipient,String orgId,String portalId,String siteUrl) {
    	IdpConfiguration idpConfig = new IdpConfiguration();
    	idpConfig.setKeystoreFile(keyStoreFile);
        idpConfig.setSamlUserIdLocation(SamlUserIdLocation.SUBJECT);
        idpConfig.setKeystoreAlias("axiom");
        idpConfig.setSamlVersion(SamlVersion._2_0);
        idpConfig.setUserType(UserType.STANDARD);
        idpConfig.setKeystorePassword("123456".toCharArray());
        idpConfig.setKeystoreAliasPassword("123456".toCharArray());
        idpConfig.setAudience("https://saml.salesforce.com");
        idpConfig.setUserId(username);
        idpConfig.setSsoStartPage("http://axiomsso.herokuapp.com/RequestSamlResponse.action");
        idpConfig.setIssuer(issuer);
        idpConfig.setRecipient(recipient);
        idpConfig.setLogoutURL("");
        if(orgId!=null && !orgId.equals(""))
        	idpConfig.setOrgId(orgId);
        if(portalId!=null && !portalId.equals(""))
        	idpConfig.setPortalId(portalId);
        if(siteUrl!=null && !siteUrl.equals(""))
        	idpConfig.setSiteURL(siteUrl);
        
        return idpConfig;
    }
    

}
