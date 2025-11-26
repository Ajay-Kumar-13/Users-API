package com.crm.users.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CreateRoleResponse {
    private UUID roleId;
    private String roleName;
}
