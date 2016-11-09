package de.redmann.test.backend.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import com.stripe.model.Token;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 08.11.16.
 */
@Service
@Slf4j
public class StripeService
{
	private final String stripeKey;
	
	
	
	@Autowired
	public StripeService(String stripeKey)
	{
		this.stripeKey = stripeKey;
	}
	
	
	
	public String createCustomer(Map<String, Object> tokenParams, Map<String, Object> customerParams)
	{
		Stripe.apiKey = stripeKey;
		
		String stripeCustomerId = null;
		
		try
		{
			Token token = Token.create(tokenParams);
			customerParams.put("source", token.getId());
			Customer customer = Customer.create(customerParams);
			stripeCustomerId = customer.getId();
		}
		catch (APIConnectionException | InvalidRequestException | AuthenticationException | APIException | CardException e)
		{
			log.error("An exception while interacting with Stripe is occurred", e);
			throw new de.redmann.test.excepions.StripeException(e);
		}
		return stripeCustomerId;
	}
}
