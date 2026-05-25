package com.crm.users.DTO;

import java.util.List;

import lombok.Data;

@Data
public class UpdateRoleRequest {
    private String roleName;
    private String roleDesc;
    private List<OverrideAuthority> authorities;
}
