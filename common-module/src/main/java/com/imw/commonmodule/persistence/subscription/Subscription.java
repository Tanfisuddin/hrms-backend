package com.imw.commonmodule.persistence.subscription;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imw.commonmodule.persistence.Organization;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "subscription")
@Table(indexes = {
        @Index(name = "idx_subscription_start_date", columnList = "startDate"),
        @Index(name = "idx_subscription_end_date", columnList = "endDate")
})
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    private Integer memberCount;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "isActive", nullable = false, columnDefinition = "boolean default false")
    private Boolean isActive = true;
}
