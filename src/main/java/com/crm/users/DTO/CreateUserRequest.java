package com.crm.users.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserRequest {
    @NotNull
    private String username;
    @NotNull
    private UUID roleId;
}
