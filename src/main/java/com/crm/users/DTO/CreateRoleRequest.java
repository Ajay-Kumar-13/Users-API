package com.crm.users.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateRoleRequest {
    private UUID role_id;
    private String role_name;
    private List<UUID> authorities;
}
