package de.redmann.test.backend.persistence.domain.backend;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import de.redmann.test.enums.RolesEnum;
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
public class Role implements Serializable
{
	@Id
	private long			id;
	
	private String			name;
	
	@OneToMany (mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<UserRole>	userRoles	= new HashSet<>();
	
	
	
	public Role(RolesEnum rolesEnum)
	{
		this.id = rolesEnum.getId();
		this.name = rolesEnum.getRoleName();
	}
}
