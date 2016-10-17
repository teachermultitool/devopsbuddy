package de.redmann.test.enums;

/**
 * Created by redmann on 17.10.16.
 */
public enum PlansEnum
{
	//@formatter:off
    BASIC(1, "Basix"),
    PRO(2, "Pro");
    //@formatter:on
	
	private final long		id;
	
	private final String	planName;
	
	
	
	PlansEnum(long id, String planName)
	{
		this.id = id;
		this.planName = planName;
	}
	
	
	
	public long getId()
	{
		return id;
	}
	
	
	
	public String getPlanName()
	{
		return planName;
	}
}
