package com.imw.commonmodule.persistence.subscription;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.imw.commonmodule.enums.subscription.AddOnsType;
import com.imw.commonmodule.enums.subscription.CurrencyType;
import com.imw.commonmodule.enums.subscription.DurationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class OrderAddOns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer memberCount = 0;

    private Integer totalAmount = 0;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // add on details.
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private AddOnsType addOnsType;

    private Integer amount;

}
