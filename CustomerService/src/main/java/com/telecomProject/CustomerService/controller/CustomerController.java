package com.telecomProject.CustomerService.controller;

import com.telecomProject.CustomerService.dto.UpdateBalanceRequest;
import com.telecomProject.CustomerService.entity.Customer;
import com.telecomProject.CustomerService.dto.CustomerRequestDTO;
import com.telecomProject.CustomerService.entity.Plan;
import com.telecomProject.CustomerService.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/test")
    public Customer test() {
        LOGGER.info("test() method called");
        return new Customer();
    }

    @PostMapping("/add")
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody CustomerRequestDTO customerRequestDTO) {
        LOGGER.info("addCustomer() method called with phone number: {}", customerRequestDTO.getPhoneNumber());
        Customer customer = customerService.addCustomer(customerRequestDTO);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/update/balance/")
    public ResponseEntity<Boolean> updateBalance(@RequestBody UpdateBalanceRequest updateBalanceRequest) {
        LOGGER.info("updateBalance() method called for phone number: {}", updateBalanceRequest.getPhoneNumber());
        return ResponseEntity.ok(customerService.updateBalance(updateBalanceRequest));
    }

    @GetMapping("/get/phone/{phoneNumber}")
    public ResponseEntity<Customer> getCustomerByPhoneNumber(@PathVariable String phoneNumber) {
        LOGGER.info("getCustomerByPhoneNumber() method called for phone number: {}", phoneNumber);
        return ResponseEntity.ok(customerService.getCustomerByPhoneNumber(phoneNumber));
    }

    @GetMapping("/get/id/{uuid}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String uuid) {
        LOGGER.info("getCustomerById() method called for customer ID: {}", uuid);
        return ResponseEntity.ok(customerService.getCustomerById(uuid));
    }

    @GetMapping("/plan/{uuid}")
    public ResponseEntity<Plan> getPlanByCustId(@PathVariable String uuid) {
        LOGGER.info("getPlanByCustId() method called for customer ID: {}", uuid);
        return ResponseEntity.ok(customerService.getPlanByCustId(uuid).get());
    }

    @GetMapping("/getAllCustomers")
    public ResponseEntity<Map<String,String>> getAllCustomers() {
        LOGGER.info("getAllCustomers() method called");
        Map<String,String> customerSummary = new HashMap<>();
        for(Customer customer : customerService.getAllCustomers()) {
            customerSummary.put(customer.getFirstName(), customer.toString());
        }
        return ResponseEntity.ok(customerSummary);
    }
}

    /**
     * Need to complete it as soon as possible as easrly as possible
     * AKANKSHA is waiting and I am at stake now
     *
     * As of now the cache is like each cust add it into cache byt key as phno
     * but didnt add what if multiple cust are added at once saveall or multip plan
     * even getall
     * need to look there too (advanced) for me
     */