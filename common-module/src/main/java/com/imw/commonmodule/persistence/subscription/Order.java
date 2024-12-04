package com.imw.commonmodule.persistence.subscription;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.imw.commonmodule.enums.subscription.CurrencyType;
import com.imw.commonmodule.enums.subscription.OrderStatus;
import com.imw.commonmodule.persistence.Organization;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", nullable = false)
    private Organization organization;

    @JsonManagedReference
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plan_id", nullable = true)
    private Plans plan;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "billing_details_id", nullable = true)
    private BillingDetails billingDetails;


    @JsonManagedReference
    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER)
    private List<OrderAddOns> orderAddOns;

    @NotNull
    private Integer amount;

    private String razorpayOrderId;

    private String razorpayPaymentId;

    private Integer taxRate;

    private float billableAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date billingDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
