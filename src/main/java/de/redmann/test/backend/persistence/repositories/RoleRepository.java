package de.redmann.test.backend.persistence.repositories;

import de.redmann.test.backend.persistence.domain.backend.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.redmann.test.backend.persistence.domain.backend.User;

/**
 * Created by redmann on 14.10.16.
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long>
{
}
