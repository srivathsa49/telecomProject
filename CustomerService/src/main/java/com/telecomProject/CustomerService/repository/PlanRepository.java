package com.telecomProject.CustomerService.repository;

import com.telecomProject.CustomerService.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    @Query("SELECT p FROM Plan p WHERE p.planName = ?1")
    Plan findByPlanName(String plan);
}
