package de.redmann.test.backend.persistence.domain.backend;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
public class User implements Serializable, UserDetails
{
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private long					id;
	
	@Column (unique = true)
	private String					username;
	
	private String					password;
	@Column (unique = true)
	private String					email;
	
	private String					firstName;
	private String					lastName;
	private String					phoneNumber;
	
	@Length (max = 500)
	private String					description;
	
	private String					country;
	
	private String					profileImageUrl;
	
	private String					stripeCustomerId;
	
	private boolean					enabled;
	
	@OneToMany (mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<UserRole>			userRoles			= new HashSet<>();
	
	@OneToMany (mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<PasswordResetToken>	passwordResetTokens	= new HashSet<>();
	
	@ManyToOne (fetch = FetchType.EAGER)
	@JoinColumn (name = "plan_id")
	private Plan					plan;
	
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		final Set<GrantedAuthority> authorities = new HashSet<>();
		userRoles.forEach(userRole -> authorities.add(new Authority(userRole.getRole().getName())));
		return authorities;
	}
	
	
	
	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}
	
	
	
	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}
	
	
	
	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}
}
