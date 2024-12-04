package com.imw.commonmodule.repository.subscription;

import com.imw.commonmodule.persistence.subscription.OrderAddOns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderAddOnsRepository extends JpaRepository<OrderAddOns, Long> {
//    void deleteAllBy
}
