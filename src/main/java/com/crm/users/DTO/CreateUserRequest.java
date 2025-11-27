package com.crm.users.DTO;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserRequest {
    private String username;
    private UUID roleId;
}
