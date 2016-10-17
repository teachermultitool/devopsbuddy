package de.redmann.test.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import de.redmann.test.backend.persistence.domain.backend.User;
import de.redmann.test.backend.persistence.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by redmann on 17.10.16.
 */
@Service
@Slf4j
public class UserSecurityService implements UserDetailsService
{
	@Autowired
	private UserRepository userRepository;
	
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		final User user = userRepository.findByUsername(username);
		if (null == user)
		{
			log.warn("Username {} not found", username);
			throw new UsernameNotFoundException("Username " + username + " not found");
		}
		return user;
	}
}
