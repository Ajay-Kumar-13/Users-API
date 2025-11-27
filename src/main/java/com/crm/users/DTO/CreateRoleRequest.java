package com.crm.users.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateRoleRequest {
    private UUID roleId;
    private String roleName;
    private List<UUID> authorities;
}
