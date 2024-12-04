package com.imw.commonmodule.repository.subscription;

import com.imw.commonmodule.persistence.subscription.BillingDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingDetailsRepository extends JpaRepository<BillingDetails, Long> {
}
