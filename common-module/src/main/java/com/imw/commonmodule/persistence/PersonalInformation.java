package com.imw.commonmodule.persistence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PersonalInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Father name is required")
    @Size(max = 20)
    private String fatherName ;

    @NotBlank(message = "Nationality is required")
    @Size(max = 20)
    private String nationality ;

    @NotBlank(message = "Marital status is required")
    @Size(max = 20)
    private String maritalStatus ;

    private String spouseName ;

    @NotBlank(message = "Blood group is required")
    @Size(max = 20)
    private String bloodGroup ;

    @NotNull(message = "Birth date is required")
    @Temporal(TemporalType.DATE)
    private Date birthDate ;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
