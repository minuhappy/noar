package com.noar.laboratory.rest;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

@RestController
@RequestMapping("/sample")
public class SampleController {

	@Autowired
	Environment env;

	@GetMapping(value = "/execute")
	public boolean execute() throws Exception {

		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = new WebCredentials("emailAddress", "password");
		service.setCredentials(credentials);
		service.setUrl(new URI("<ews_url>"));
		service.autodiscoverUrl("<your_email_address>");

		EmailMessage msg = new EmailMessage(service);
		msg.setSubject("Hello world!");
		msg.setBody(MessageBody.getMessageBodyFromText("Sent using the EWS Java API."));
		msg.getToRecipients().add("someone@contoso.com");
		msg.send();

		return true;
	}
}
