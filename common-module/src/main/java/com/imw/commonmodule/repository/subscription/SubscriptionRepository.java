package com.imw.commonmodule.repository.subscription;

import com.imw.commonmodule.persistence.subscription.Subscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findFirstByOrganizationIdOrderByEndDateDesc(Long organizationId);

    @Query("SELECT s FROM subscription s WHERE s.organization.id=:organizationId And s.startDate <= :currentDate AND s.endDate >= :currentDate")
    Subscription findActiveSubscriptionByOrganizationId(@Param("organizationId") Long organizationId, @Param("currentDate") LocalDate currentDate);

    Page<Subscription> findAllByOrganizationIdOrderByIdDesc(Long organizationId, Pageable pageable);

    Page<Subscription> findAllByOrderByIdDesc( Pageable pageable);
}