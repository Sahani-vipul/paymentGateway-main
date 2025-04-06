package com.example.PaymentGetway.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PaymentGetway.modal.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    OrderEntity findByOrderId(String orderId);
}
