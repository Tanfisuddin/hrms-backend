package com.imw.commonmodule.repository.subscription;

import com.imw.commonmodule.enums.subscription.OrderStatus;
import com.imw.commonmodule.persistence.subscription.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findOrderByOrganizationIdAndStatusNot(Long organizationId, OrderStatus status);
    Order findOrderByRazorpayOrderId(String razorpayOrderId);

    Page<Order> findAllByOrganizationIdAndStatusOrderByIdDesc(Long organizationId, OrderStatus status, Pageable pageable);
    Page<Order> findAllByStatusOrderByIdDesc(OrderStatus status, Pageable pageable);
}
