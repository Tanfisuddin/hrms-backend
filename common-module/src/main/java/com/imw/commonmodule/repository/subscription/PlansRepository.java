package com.imw.commonmodule.repository.subscription;

import com.imw.commonmodule.persistence.subscription.Plans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlansRepository extends JpaRepository<Plans, Long> {
    List<Plans> findAllByOrganizationIsNullAndOrderIsNull();
    Plans getByOrganizationIdAndOrderId(Long organizationId, Long orderId);
    boolean existsByOrganizationId(Long organizationId);
}
