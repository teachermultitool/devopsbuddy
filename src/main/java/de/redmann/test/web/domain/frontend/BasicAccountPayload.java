package de.redmann.test.web.domain.frontend;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;

/**
 * Created by redmann on 04.11.16.
 */
@Data
@EqualsAndHashCode (of = "username")
public class BasicAccountPayload implements Serializable
{
	
	private static final long	serialVersionUID	= 8434282796814143436L;
	
	@NotNull
    @Email
	private String				email;
	@NotNull
	private String				username;
	@NotNull
	private String				password;
	@NotNull
	private String				confirmPassword;
	@NotNull
	private String				firstName;
	@NotNull
	private String				lastName;
	
	private String				description;
	@NotNull
	private String				phoneNumber;
	@NotNull
	private String				country;
	
}
