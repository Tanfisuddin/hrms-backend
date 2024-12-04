package com.imw.commonmodule.dto.auth;

import com.imw.commonmodule.persistence.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAuthResponseDto {
    User user;
    String token;
}
