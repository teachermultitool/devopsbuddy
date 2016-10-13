package de.redmann.test.backend.persistence.domain.backend;

import java.io.Serializable;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by redmann on 13.10.16.
 */
@Entity
@Table (name = "user_role")
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class UserRole implements Serializable
{
	@Id
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "user_id")
	private User	user;
	
	@Id
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "role_id")
	private Role	role;
}
