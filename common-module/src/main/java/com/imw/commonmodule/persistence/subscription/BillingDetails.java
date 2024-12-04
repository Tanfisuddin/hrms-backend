package com.imw.commonmodule.persistence.subscription;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BillingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String number;

    @NotBlank
    private String email;

    @NotBlank
    private String companyName;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String zipCode;

    @NotBlank
    private String state;

    @NotNull
    @NotBlank
    private String country;
}
