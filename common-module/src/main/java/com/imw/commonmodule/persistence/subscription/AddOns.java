package com.imw.commonmodule.persistence.subscription;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.imw.commonmodule.enums.subscription.AddOnsType;
import com.imw.commonmodule.enums.subscription.CurrencyType;
import com.imw.commonmodule.enums.subscription.DurationType;
import com.imw.commonmodule.persistence.Organization;
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
public class AddOns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AddOnsType addOnsType;

    @NotNull
    private Integer duration;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DurationType durationType;

    @NotNull
    private Integer amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

}