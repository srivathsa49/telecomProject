package com.telecomProject.CustomerService.repository;

import com.telecomProject.CustomerService.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findCustomersByPlanId(int id);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.balance = ?2 WHERE c.phoneNumber = ?1")
    int updateBalance(String phoneNumber, int balance);
}
