package com.crm.users.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateAuthorityRequest {
    private UUID authorityId;

    @NotNull
    private String authorityName;
}
