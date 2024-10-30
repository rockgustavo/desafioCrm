package com.rockgustavo.desafiocrm.model.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rockgustavo.desafiocrm.model.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByEmail(String email);

}
