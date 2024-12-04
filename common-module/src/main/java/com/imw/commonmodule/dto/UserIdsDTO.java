package com.imw.commonmodule.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class UserIdsDTO {

    @NotEmpty
    private List<Long> userIds;
}
