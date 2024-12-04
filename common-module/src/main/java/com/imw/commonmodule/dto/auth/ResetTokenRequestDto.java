package com.imw.commonmodule.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetTokenRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String otp;
}
