package de.redmann.test.backend.persistence.domain.backend;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.Length;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Created by redmann on 13.10.16.
 */
@Entity
@NoArgsConstructor
@Data
@EqualsAndHashCode (of = "id")
public class User implements Serializable
{
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private long			id;
	
	private String			username;
	
	private String			password;
	private String			email;
	
	private String			firstName;
	private String			lastName;
	private String			phoneNumber;
	
	@Length (max = 500)
	private String			description;
	
	private String			country;
	
	private String			profileImageUrl;
	
	private String			stripeCustimerId;
	
	private boolean			enabled;
	
	@OneToMany (mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<UserRole>	userRoles	= new HashSet<>();
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "plan_id")
	private Plan			plan;
}
