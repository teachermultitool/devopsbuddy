package de.redmann.test.backend.persistence.domain.backend;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import de.redmann.test.enums.PlansEnum;
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
public class Plan implements Serializable
{
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	
	private long	id;
	
	private String	name;
	
	
	
	public Plan(PlansEnum plansEnum)
	{
		this.id = plansEnum.getId();
		this.name = plansEnum.getPlanName();
	}
}
