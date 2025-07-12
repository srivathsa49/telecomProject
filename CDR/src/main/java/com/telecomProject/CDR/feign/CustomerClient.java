package com.telecomProject.CDR.feign;

import com.telecomProject.CDR.model.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "customer-service", url = "http://localhost:8083/customer")
public interface CustomerClient {
    @GetMapping("/getAllCustomers")
    public ResponseEntity<Map<String,String>> getAllCustomers();

    @GetMapping("/get/phone/{phoneNumber}")
    ResponseEntity<Customer> getCustomerByPhoneNumber(@PathVariable String phoneNumber);

    @GetMapping("/get/id/{uuid}")
    ResponseEntity<Customer> getCustomerById(@PathVariable String uuid);
}
