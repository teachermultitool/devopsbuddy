package de.redmann.test.web.domain.frontend;

import lombok.Data;
import lombok.ToString;

/**
 * Created by redmann on 07.11.16.
 */
@Data
@ToString (exclude = { "cardNumber", "cardCode", "cardMonth", "cardYear" })
public class ProAccountPayload extends BasicAccountPayload
{
	private String	cardNumber;
	private String	cardCode;
	private String	cardMonth;
	private String	cardYear;
}
