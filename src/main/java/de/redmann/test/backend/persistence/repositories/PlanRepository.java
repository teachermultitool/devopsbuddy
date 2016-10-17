package de.redmann.test.backend.persistence.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.redmann.test.backend.persistence.domain.backend.Plan;

/**
 * Created by redmann on 14.10.16.
 */
@Repository
public interface PlanRepository extends CrudRepository<Plan, Long>
{
}
