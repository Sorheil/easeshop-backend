package com.nexora.easeshop.repositories;

import com.nexora.easeshop.models.Customer;
import com.nexora.easeshop.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
