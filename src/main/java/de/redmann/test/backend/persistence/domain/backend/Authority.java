package de.redmann.test.backend.persistence.domain.backend;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by redmann on 17.10.16.
 */
public class Authority implements GrantedAuthority
{
	private final String authority;
	
	
	
	public Authority(String authority)
	{
		this.authority = authority;
	}
	
	
	
	@Override
	public String getAuthority()
	{
		return authority;
	}
}
