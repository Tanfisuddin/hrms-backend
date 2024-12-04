package com.imw.commonmodule.repository.subscription;

import com.imw.commonmodule.persistence.subscription.AddOns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddOnsRepository extends JpaRepository<AddOns, Long> {
}
