package de.redmann.test.web.domain.frontend;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by redmann on 12.10.16.
 */
@Data
public class FeedbackPojo implements Serializable
{
	private static final long	seialVersionUID	= 1L;
	
	private String				email;
	private String				firstName;
	private String				lastName;
	private String				feedback;
}
