package com.imw.commonmodule.persistence.subscription;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imw.commonmodule.enums.subscription.CurrencyType;
import com.imw.commonmodule.enums.subscription.DurationType;
import com.imw.commonmodule.persistence.Organization;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Plans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organisation_id", nullable = true)
    private Organization organization;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Integer duration;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DurationType durationType;

    @NotNull
    private Integer memberCount;

    @NotNull
    private Integer amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = true)
    private Order order;
}
