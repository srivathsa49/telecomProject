package com.telecomProject.CustomerService.controller;

import com.telecomProject.CustomerService.entity.Customer;
import com.telecomProject.CustomerService.entity.Plan;
import com.telecomProject.CustomerService.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/plan")
public class PlanController {
    private final CustomerService customerService;
    public PlanController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/getPlan/{planName}")
    public ResponseEntity<Plan> getPlan(@PathVariable String planName) {
        return ResponseEntity.ok(customerService.getPlan(planName));
    }
    @GetMapping("/getAllPlans")
    public ResponseEntity<Map<String,String>> getAllPlans() {
        Map<String,String> planSummary = new HashMap<>();
        for(Plan plan : customerService.getAllPlans()){
            planSummary.put(plan.getPlanName(), plan.getDetails());
        }
        return ResponseEntity.ok(planSummary);
    }

    @PostMapping("/addPlan")
    public ResponseEntity<Plan> addPlan(@RequestBody Plan plan) {
        return ResponseEntity.ok(customerService.addPlan(plan));
    }

    @GetMapping("/get/customers/{id}")
    public ResponseEntity<List<Customer>> getCustomersByPlanId(@PathVariable int id) {
        return ResponseEntity.ok(customerService.getCustomersByPlanId(id));
    }
}