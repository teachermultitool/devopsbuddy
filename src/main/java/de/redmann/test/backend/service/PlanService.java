package de.redmann.test.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.redmann.test.backend.persistence.domain.backend.Plan;
import de.redmann.test.backend.persistence.repositories.PlanRepository;
import de.redmann.test.enums.PlansEnum;

/**
 * Created by redmann on 07.11.16.
 */
@Service
@Transactional (readOnly = true)
public class PlanService
{
	private PlanRepository planRepository;
	
	
	
	@Autowired
	public PlanService(PlanRepository planRepository)
	{
		this.planRepository = planRepository;
	}
	
	
	
	public Plan findPlanById(long planId)
	{
		return planRepository.findOne(planId);
	}
	
	
	
	@Transactional
	public Plan createPlan(long planId)
	{
		Plan plan = null;
		if (planId == 1)
		{
			plan = planRepository.save(new Plan(PlansEnum.BASIC));
		}
		else if (planId == 2)
		{
			plan = planRepository.save(new Plan(PlansEnum.PRO));
		}
		else
		{
			throw new IllegalStateException("Plan id " + planId + " not recognized.");
		}
		return plan;
	}
	
}
