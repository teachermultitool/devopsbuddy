package de.redmann.test.unit;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.redmann.test.integration.StripeIntegrationTest;
import de.redmann.test.utils.StripeUtils;
import de.redmann.test.web.domain.frontend.ProAccountPayload;

/**
 * Created by redmann on 08.11.16.
 */
public class StripeUtilsUnitTest {
    @Test
    public void createStripeTokenParamsFromUserPayload() {
        ProAccountPayload payload = new ProAccountPayload();
        payload.setCardCode(StripeIntegrationTest.TEST_CC_CVC_NBR);
        payload.setCardNumber(StripeIntegrationTest.TEST_CC_NUMBER);
        payload.setCardMonth("" + StripeIntegrationTest.TEST_CC_EXP_MONTH);
        String cardYear = String.valueOf(LocalDate.now(Clock.systemUTC()).getYear() + 1);
        payload.setCardYear(cardYear);

        Map<String, Object> tokenParams = StripeUtils.extractTokenParamsFromSignupPayload(payload);
        Map<String, Object> cardParams = (Map<String, Object>) tokenParams.get(StripeUtils.STRIPE_CARD_KEY);
        Assert.assertEquals(StripeIntegrationTest.TEST_CC_CVC_NBR, cardParams.get(StripeUtils.STRIPE_CVC_KEY));
        Assert.assertEquals(StripeIntegrationTest.TEST_CC_NUMBER, cardParams.get(StripeUtils.STRIPE_CARD_NUMBER_KEY));
        Assert.assertEquals(StripeIntegrationTest.TEST_CC_EXP_MONTH, cardParams.get(StripeUtils.STRIPE_EXP_MONTH_KEY));
        Assert.assertEquals(Integer.valueOf(cardYear), cardParams.get(StripeUtils.STRIPE_EXP_YEAR_KEY));
    }
}
