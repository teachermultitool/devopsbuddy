package de.redmann.test.utils;

import java.util.HashMap;
import java.util.Map;

import de.redmann.test.web.domain.frontend.ProAccountPayload;

/**
 * Created by redmann on 08.11.16.
 */
public class StripeUtils
{
	
	public static String	STRIPE_CARD_NUMBER_KEY	= "number";
	public static String	STRIPE_EXP_MONTH_KEY	= "exp_month";
	public static String	STRIPE_EXP_YEAR_KEY		= "exp_year";
	public static String	STRIPE_CVC_KEY			= "cvc";
	public static String	STRIPE_CARD_KEY			= "card";
	
	
	
	private StripeUtils()
	{
		throw new AssertionError("non instantiable");
	}
	
	
	
	public static Map<String, Object> extractTokenParamsFromSignupPayload(ProAccountPayload payload)
	{
		HashMap<String, Object> tokenParams = new HashMap<>();
		HashMap<String, Object> cardParams = new HashMap<>();
		cardParams.put(STRIPE_CARD_NUMBER_KEY, payload.getCardNumber());
		cardParams.put(STRIPE_EXP_MONTH_KEY, Integer.valueOf(payload.getCardMonth()));
		cardParams.put(STRIPE_EXP_YEAR_KEY, Integer.valueOf(payload.getCardYear()));
		cardParams.put(STRIPE_CVC_KEY, payload.getCardCode());
		tokenParams.put(STRIPE_CARD_KEY, cardParams);
		return tokenParams;
	}
}
