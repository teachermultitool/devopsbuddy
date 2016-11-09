package de.redmann.test.integration;

import java.time.Clock;
import java.time.LocalDate;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.stripe.Stripe;
import com.stripe.model.Customer;

import de.redmann.test.Main;
import de.redmann.test.backend.service.StripeService;
import de.redmann.test.enums.PlansEnum;
import de.redmann.test.utils.StripeUtils;

/**
 * Created by redmann on 08.11.16.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@SpringBootTest (classes = Main.class)
public class StripeIntegrationTest
{
	public static final String	TEST_CC_NUMBER		= "4242424242424242";
	public static final int		TEST_CC_EXP_MONTH	= 1;
	public static final String	TEST_CC_CVC_NBR		= "314";
	@Autowired
	private StripeService		stripeService;
	
	@Autowired
	private String				stripeKey;
	
	
	
	@Before
	public void init()
	{
		Assert.assertNotNull(stripeKey);
		Stripe.apiKey = stripeKey;
	}
	
	
	
	@Test
	public void createStripeCustomer() throws Exception
	{
		HashMap<String, Object> tokenParams = new HashMap<>();
		HashMap<String, Object> cardParams = new HashMap<>();
		cardParams.put(StripeUtils.STRIPE_CARD_NUMBER_KEY, TEST_CC_NUMBER);
		cardParams.put(StripeUtils.STRIPE_EXP_MONTH_KEY, TEST_CC_EXP_MONTH);
		cardParams.put(StripeUtils.STRIPE_EXP_YEAR_KEY, LocalDate.now(Clock.systemUTC()).getYear() + 1);
		cardParams.put(StripeUtils.STRIPE_CVC_KEY, TEST_CC_CVC_NBR);
		tokenParams.put(StripeUtils.STRIPE_CARD_KEY, cardParams);
		
		HashMap<String, Object> customerParams = new HashMap<>();
		customerParams.put("description", "Customer for test@example.com");
		customerParams.put("plan", PlansEnum.PRO.getId());
		
		String stripeCustomerId = stripeService.createCustomer(tokenParams, customerParams);
		Assert.assertNotNull(stripeCustomerId);
		
		Customer customer = Customer.retrieve(stripeCustomerId);
		customer.delete();
	}
}
