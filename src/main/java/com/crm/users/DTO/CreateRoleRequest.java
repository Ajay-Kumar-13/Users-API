package com.crm.users.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateRoleRequest {
    private UUID roleId;
    @NotNull
    private String roleName;
    @NotNull
    private List<UUID> authorities;
}
