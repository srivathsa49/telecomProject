package com.telecomProject.CustomerService.service;

import com.telecomProject.CustomerService.dto.UpdateBalanceRequest;
import com.telecomProject.CustomerService.entity.Customer;
import com.telecomProject.CustomerService.dto.CustomerRequestDTO;
import com.telecomProject.CustomerService.entity.Plan;
import com.telecomProject.CustomerService.repository.CustomerRepository;
import com.telecomProject.CustomerService.repository.PlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "CustomerCache")
public class CustomerService {
    private static final String CUSTOMER_CACHE_NAME = "CustomerCache";
    private static final String PLAN_CACHE_NAME = "planCache";
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private KafkaTemplate<String, String> customerInfoKafkaTemplate;
    @Autowired
    private KafkaTemplate<String, String> planKafkaTemplate;


    //need to implement both intead of inmemory now using redis so 31 to 33 not used
//    public Map<String,Object> CustInMemoryCache = new java.util.concurrent.ConcurrentHashMap<>();
//
//    public Map<Integer,Object> PlanInMemoryCache = new java.util.concurrent.ConcurrentHashMap<>();

    @CachePut(key = "#customerRequestDTO.phoneNumber")
    public Customer addCustomer(CustomerRequestDTO customerRequestDTO) {
        LOGGER.info("Inside METHOD addCustomer");
        Customer customer = Customer.builder()
                .firstName(customerRequestDTO.getFirstName())
                .lastName(customerRequestDTO.getLastName())
                .address(customerRequestDTO.getAddress())
                .email(customerRequestDTO.getEmail())
                .phoneNumber(customerRequestDTO.getPhoneNumber())
                .planStartTime(customerRequestDTO.getPlanStartTime())
                .planEndTime(customerRequestDTO.getPlanEndTime())
                .build();
        customer.setPlan(planRepository.findByPlanName(customerRequestDTO.getPlanName()));
        customer.setBalance(customer.getPlan().getPrice());
        try{
            Customer savedCustomer = customerRepository.save(customer);
            LOGGER.info("Customer saved with id: {}", savedCustomer.getId());
            sendCustomer(savedCustomer);
            return savedCustomer;
        }
        catch (Exception e){
            return null;
        }
    }

    @Cacheable(key = "#phoneNumber")
    public Customer getCustomerByPhoneNumber(String phoneNumber) {
        LOGGER.info("Inside METHOD getCustomerByPhoneNumber");
        return customerRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    public Customer getCustomerById(String uuid) {
        LOGGER.info("Inside METHOD getCustomerById");
        return customerRepository.findById(uuid).orElse(null);
    }
    @Cacheable(value = "planCache", key = "#planName")
    public Plan getPlan(String planName) {
        LOGGER.info("Inside METHOD getPlan");
        return planRepository.findByPlanName(planName);
    }

    public void sendPlan(Plan plan) {
        LOGGER.debug("Inside METHOD sendPlan");
        planKafkaTemplate.send("plans.topic", String.valueOf(plan.getId()),"new plan added");
    }

    public void sendCustomer(Customer savedCustomer) {
        LOGGER.debug("Inside METHOD sendCustomer");
        customerInfoKafkaTemplate.send("cust.updated.topic", savedCustomer.getId(), "new Customer added");
    }

    public Optional<Plan> getPlanByCustId(String uuid) {
        LOGGER.info("Inside METHOD getPlanByCustId");
        Optional<Customer> customer = customerRepository.findById(uuid);
        return planRepository.findById(customer.get().getPlan().getId());
    }

    public List<Plan> getAllPlans() {
        LOGGER.info("Inside METHOD getAllPlans");
        return planRepository.findAll();
    }

    @CachePut(key = "#plan.planName")
    public Plan addPlan(Plan plan) {
        LOGGER.info("Inside METHOD addPlan");
        Plan savedPlan = planRepository.save(plan);
        sendPlan(savedPlan);
        return savedPlan;
    }

    public List<Customer> getCustomersByPlanId(int id) {
        LOGGER.info("Inside METHOD getCustomersByPlanId");
        return customerRepository.findCustomersByPlanId(id);
    }

    @CacheEvict( key = "#updateBalanceRequest.phoneNumber")
    public boolean updateBalance(UpdateBalanceRequest updateBalanceRequest) {
        LOGGER.info("Inside METHOD updateBalance");
        String phoneNumber = updateBalanceRequest.getPhoneNumber();
        int balance = updateBalanceRequest.getBalance();
        int modifiedRows = customerRepository.updateBalance(phoneNumber, balance);
        if(modifiedRows > 0){
            sendCustomer(getCustomerByPhoneNumber(phoneNumber));
            return true;
        }
        else{
            return false;
        }
    }

    public List<Customer> getAllCustomers() {
        LOGGER.info("Inside METHOD getAllCustomers");
        return customerRepository.findAll();
    }
}
