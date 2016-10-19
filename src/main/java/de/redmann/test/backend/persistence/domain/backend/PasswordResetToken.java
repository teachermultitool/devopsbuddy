package de.redmann.test.backend.persistence.domain.backend;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import de.redmann.test.backend.persistence.converters.LocalDateTimeAttributeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 19.10.16.
 */
@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode (of = "id")
@Slf4j
public class PasswordResetToken implements Serializable
{
	private static final int	DEFAULT_TOKEN_LENGTH_IN_MINUTES	= 120;
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private long				id;
	
	@Column (unique = true)
	private String				token;
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "user_id")
	private User				user;
	
	@Column (name = "expiry_date")
	@Convert (converter = LocalDateTimeAttributeConverter.class)
	private LocalDateTime		expiryDate;
	
	
	
	public PasswordResetToken(@NonNull String token, @NonNull User user, @NonNull LocalDateTime creationDateTime,
			@NonNull int expirationInMinutes)
	{
		this.token = token;
		this.user = user;
		
		if (expirationInMinutes <= 0)
		{
			log.warn("The token expiration in minutes is zero or less. Assigning the defaul value{}",
					DEFAULT_TOKEN_LENGTH_IN_MINUTES);
			expirationInMinutes = DEFAULT_TOKEN_LENGTH_IN_MINUTES;
		}
		this.expiryDate = creationDateTime.plusMinutes(expirationInMinutes);
	}
}
