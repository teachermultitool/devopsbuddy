package de.redmann.test.enums;

/**
 * Created by redmann on 17.10.16.
 */
public enum RolesEnum
{
	//@formatter:off
    BASIC(1, "ROLE_BASIC"),
    PRO(2, "ROLE_PRO"),
    ADMIN(3, "ROLE_ADMIN");
    //@formatter:on
	
	private final long		id;
	
	private final String	roleName;
	
	
	
	RolesEnum(long id, String roleName)
	{
		this.id = id;
		this.roleName = roleName;
	}
	
	
	
	public long getId()
	{
		return id;
	}
	
	
	
	public String getRoleName()
	{
		return roleName;
	}
}
