package com.imw.commonmodule.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //Communication Address
    private String communicationAddress1;
    private String communicationAddress2;
    private String communicationAddress3;
    private String communicationCity;
    private String communicationState;
    private String communicationArea;
    private String communicationPincode;
    private String communicationCountry;
    private String communicationLandmark;

    //Permanent Address
    private Boolean permanentSameAsCommunication;
    private String permanentAddress1;
    private String permanentAddress2;
    private String permanentAddress3;
    private String permanentCity;
    private String permanentState;
    private String permanentArea;
    private String permanentPincode;
    private String permanentCountry;
    private String permanentLandmark;

}
