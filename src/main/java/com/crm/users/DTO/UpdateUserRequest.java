package com.crm.users.DTO;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UpdateUserRequest {
    private String username;
    private String email;
    private UUID roleId;
    private List<OverrideAuthority> authorities;
    private boolean accountActive;
}
